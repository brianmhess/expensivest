package hessian.expensivest;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.dse.DseCluster;
import com.datastax.driver.dse.DseSession;
import com.datastax.driver.mapping.MappingManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExpensivestDseConfiguration {
    @Value("${dse.keyspace}")
    private String keyspace;

    @Value("${dse.username}")
    private String username;

    @Value("${dse.password}")
    private String password;

    @Value("${apollo.credentials}")
    private String apolloCredentials;

    @Bean
    public Cluster cluster() {
        return dseCluster();
    }

    @Bean
    public DseCluster dseCluster() {
        DseCluster.Builder dseClusterBuilder = DseCluster.builder()
                .withCloudSecureConnectBundle(this.getClass().getResourceAsStream(apolloCredentials))
                .withCredentials(this.username, this.password);

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
