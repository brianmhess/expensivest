package hessian.expensivest;

import com.datastax.driver.dse.DseCluster;
import com.datastax.driver.dse.DseSession;
import com.datastax.driver.mapping.MappingManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class ExpensivestDseConfiguration {
    @Value("${dse.contactPoints}")
    public String contactPoints;

    @Value("${dse.port}")
    private int port = 9042;

    @Value("${dse.keyspace}")
    private String keyspace = "expensivest";

    public String getContactPoints() {
        return contactPoints;
    }

    public String getKeyspaceName() {
        return keyspace;
    }

    public int getPort() {
        return port;
    }

    @Bean
    public DseCluster dseCluster() {
        DseCluster.Builder dseClusterBuilder =
                DseCluster.builder()
                        .addContactPoints(contactPoints)
                        .withPort(port);
        return dseClusterBuilder.build();
    }

    @Bean
    public DseSession dseSession(DseCluster dseCluster) {

        return dseCluster.connect(keyspace);
    }


    @Bean
    public MappingManager mappingManager(DseSession dseSession) {

        return new MappingManager(dseSession);
    }
}
