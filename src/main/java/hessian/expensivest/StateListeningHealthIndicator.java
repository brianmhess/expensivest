package hessian.expensivest;

import com.datastax.driver.core.*;
import com.datastax.driver.dse.DseSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class StateListeningHealthIndicator implements HealthIndicator {
    private DseSession session;
    private LastUpdatedStateListener hostListener = new LastUpdatedStateListener();
    private LastUpdatedSchemaListener schemaListener = new LastUpdatedSchemaListener();
    private Health lastHealth = Health.unknown().build();

    @Value("${dse.localDc}")
    private String datacenter;

    @Value("${dse.keyspace}")
    private String keyspace;

    public StateListeningHealthIndicator(DseSession session) {
        this.session = session;
        this.session.getCluster().register(hostListener);
        this.session.getCluster().register(schemaListener);
    }

    @Override
    public Health health() {
        if (null == datacenter)
            return Health.unknown().withDetail("Error", "No datacenter defined").build();
        if ((hostListener.getLastUpdated() < hostListener.getLastChecked())
                && (schemaListener.getLastUpdated() < schemaListener.getLastChecked()))
            return lastHealth;
        Metadata metadata = session.getCluster().getMetadata();
        List<TokenRange> badTokenRanges = new ArrayList<TokenRange>();
        String keyspaceName = (null == keyspace) ? findKeyspace() : keyspace;
        if (null == keyspaceName)
            return Health.unknown().build();
        for (TokenRange tr : metadata.getTokenRanges()) {
            long numReplicas = metadata.getReplicas(keyspaceName, tr)
                    .stream().filter(h -> (0 == h.getDatacenter().compareTo(datacenter))).count();
            long minReplicas = (numReplicas + 1) / 2;
            if (minReplicas > metadata.getReplicas(keyspace, tr)
                    .stream()
                    .filter(h -> (0 == h.getDatacenter().compareTo(datacenter)))
                    .filter(Host::isUp).count())
                badTokenRanges.add(tr);
        }
        List<Host> badHosts = metadata.getAllHosts()
                .stream()
                .filter(h -> (0 == h.getDatacenter().compareTo(datacenter)))
                .filter(h -> !h.isUp())
                .collect(Collectors.toList());

        Health.Builder hbuilder = (badTokenRanges.isEmpty()) ? Health.up() : Health.down();
        hbuilder.withDetail("BadTokenRanges", badTokenRanges);
        hbuilder.withDetail("DownHosts", badHosts);
        hbuilder.withDetail("NumTokenRanges", metadata.getTokenRanges().size());
        hbuilder.withDetail("NumHosts", metadata.getAllHosts().size());
        hbuilder.withDetail("KeyspaceUsedForCheck", keyspaceName);

        lastHealth = hbuilder.build();
        return lastHealth;
    }

    public String findKeyspace() {
        Metadata metadata = session.getCluster().getMetadata();
        Optional<Host> someHost = metadata.getAllHosts().stream()
                .filter(h -> (0 == h.getDatacenter().compareTo(datacenter))).findAny();
        if (!someHost.isPresent())
            return null;
        Host host = someHost.get();
        for (KeyspaceMetadata km : metadata.getKeyspaces()) {
            if (!km.getReplication().get("class").contains("NetworkTopologyStrategy")
                    && !km.getReplication().get("class").contains("SimpleStrategy")) {
                continue;
            }
            if (!metadata.getTokenRanges(km.getName(), host).isEmpty()) {
                return km.getName();
            }
        }

        return null;
    }

    public class LastUpdatedStateListener implements Host.StateListener {
        private long lastUpdated = System.currentTimeMillis();
        private long lastChecked = -1;

        @Override
        public void onAdd(Host host) {
            this.lastUpdated = System.currentTimeMillis();
        }

        @Override
        public void onUp(Host host) {
            this.lastUpdated = System.currentTimeMillis();
        }

        @Override
        public void onDown(Host host) {
            this.lastUpdated = System.currentTimeMillis();
        }

        @Override
        public void onRemove(Host host) {
            this.lastUpdated = System.currentTimeMillis();
        }

        @Override
        public void onRegister(Cluster cluster) {

        }

        @Override
        public void onUnregister(Cluster cluster) {

        }

        public long getLastUpdated() {
            return this.lastUpdated;
        }

        public long getLastChecked() {
            long retval = this.lastChecked;
            this.lastChecked = System.currentTimeMillis();
            return retval;
        }
    }

    public class LastUpdatedSchemaListener implements SchemaChangeListener {
        private long lastUpdated = System.currentTimeMillis();
        private long lastChecked = -1;

        public long getLastUpdated() {
            return this.lastUpdated;
        }

        public long getLastChecked() {
            long retval = this.lastChecked;
            this.lastChecked = System.currentTimeMillis();
            return retval;
        }

        @Override
        public void onKeyspaceAdded(KeyspaceMetadata keyspaceMetadata) {
            this.lastUpdated = System.currentTimeMillis();
        }

        @Override
        public void onKeyspaceRemoved(KeyspaceMetadata keyspaceMetadata) {
            this.lastUpdated = System.currentTimeMillis();
        }

        @Override
        public void onKeyspaceChanged(KeyspaceMetadata keyspaceMetadata, KeyspaceMetadata keyspaceMetadata1) {
            this.lastUpdated = System.currentTimeMillis();
        }

        @Override
        public void onTableAdded(TableMetadata tableMetadata) {

        }

        @Override
        public void onTableRemoved(TableMetadata tableMetadata) {

        }

        @Override
        public void onTableChanged(TableMetadata tableMetadata, TableMetadata tableMetadata1) {

        }

        @Override
        public void onUserTypeAdded(UserType userType) {

        }

        @Override
        public void onUserTypeRemoved(UserType userType) {

        }

        @Override
        public void onUserTypeChanged(UserType userType, UserType userType1) {

        }

        @Override
        public void onFunctionAdded(FunctionMetadata functionMetadata) {

        }

        @Override
        public void onFunctionRemoved(FunctionMetadata functionMetadata) {

        }

        @Override
        public void onFunctionChanged(FunctionMetadata functionMetadata, FunctionMetadata functionMetadata1) {

        }

        @Override
        public void onAggregateAdded(AggregateMetadata aggregateMetadata) {

        }

        @Override
        public void onAggregateRemoved(AggregateMetadata aggregateMetadata) {

        }

        @Override
        public void onAggregateChanged(AggregateMetadata aggregateMetadata, AggregateMetadata aggregateMetadata1) {

        }

        @Override
        public void onMaterializedViewAdded(MaterializedViewMetadata materializedViewMetadata) {

        }

        @Override
        public void onMaterializedViewRemoved(MaterializedViewMetadata materializedViewMetadata) {

        }

        @Override
        public void onMaterializedViewChanged(MaterializedViewMetadata materializedViewMetadata, MaterializedViewMetadata materializedViewMetadata1) {

        }

        @Override
        public void onRegister(Cluster cluster) {

        }

        @Override
        public void onUnregister(Cluster cluster) {

        }
    }
}
