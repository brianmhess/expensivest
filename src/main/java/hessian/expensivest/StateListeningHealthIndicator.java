package hessian.expensivest;

import com.datastax.dse.driver.api.core.DseSession;
import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.core.metadata.*;
import com.datastax.oss.driver.api.core.metadata.token.TokenRange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class StateListeningHealthIndicator implements HealthIndicator {
    private Health lastHealth = Health.unknown().build();

    @Autowired
    private DseSession session;
    @Autowired
    LastUpdatedStateListener lastUpdatedStateListener;
    @Autowired
    LastUpdatedSchemaListener lastUpdatedSchemaListener;

    @Value("${dse.localDc}")
    private String datacenter;

    @Value("${dse.keyspace}")
    private String keyspace;

    private List<String> findKeyspacesForDataCenter(String datacenter, Metadata metadata, TokenMap tokenMap) {
        List<String> keyspaces = new ArrayList<String>();
        Node oneNode = metadata.getNodes().values().stream().filter(n -> (0 == datacenter.compareTo(n.getDatacenter()))).findAny().orElse(null);
        if (null == oneNode)
            throw new IllegalArgumentException("No nodes found for the data center (" + datacenter + ")");
        for (CqlIdentifier ks : metadata.getKeyspaces().keySet()) {
            if (0 <= tokenMap.getTokenRanges(ks, oneNode).size()) {
                keyspaces.add(ks.asInternal());
            }
        }
        if (0 == keyspaces.size())
            throw new IllegalArgumentException("No keyspaces replicated to this data center (" + datacenter + ")");
        return keyspaces;
    }

    @Override
    public Health health() {
        if ((lastUpdatedStateListener.getLastUpdated() < lastUpdatedStateListener.getLastChecked())
                && (lastUpdatedSchemaListener.getLastUpdated() < lastUpdatedSchemaListener.getLastChecked()))
            return lastHealth;
        Metadata metadata = session.getMetadata();
        List<TokenRange> badTokenRanges = new ArrayList<TokenRange>();
        if (!metadata.getTokenMap().isPresent())
            return Health.unknown().build();
        TokenMap tokenMap = metadata.getTokenMap().get();
        if (null == keyspace) {            List<String> keyspaces = findKeyspacesForDataCenter(datacenter, metadata, tokenMap);
            if (0 == keyspaces.size())
                return Health.unknown().build();
            keyspace = keyspaces.get(0);
        }
        for (TokenRange tr : tokenMap.getTokenRanges()) {
            long numReplicas = tokenMap.getReplicas(keyspace, tr).size();
            long numReplicasUp = tokenMap.getReplicas(keyspace, tr)
                    .stream()
                    .filter(h -> (0 == h.getDatacenter().compareTo(datacenter)))
                    .filter(h -> (h.getState() == NodeState.UP))
                    .count();
            if (numReplicasUp > (numReplicas + 1)/2)
                badTokenRanges.add(tr);
        }
        List<Node> badHosts = metadata.getNodes().values()
                .stream()
                .filter(h -> (0 == h.getDatacenter().compareTo(datacenter)))
                .filter(h -> (h.getState() != NodeState.UP))
                .collect(Collectors.toList());

        Health.Builder hbuilder = (badTokenRanges.isEmpty()) ? Health.up() : Health.down();
        hbuilder.withDetail("BadTokenRanges", badTokenRanges);
        hbuilder.withDetail("DownHosts", badHosts);
        hbuilder.withDetail("NumTokenRanges", tokenMap.getTokenRanges().size());
        hbuilder.withDetail("NumHosts", metadata.getNodes().values().size());
        hbuilder.withDetail("DataCenter", datacenter);
        hbuilder.withDetail("Keyspace Checked", keyspace);

        lastHealth = hbuilder.build();
        return lastHealth;
    }
}
