package hessian.expensivest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.config.AbstractCassandraConfiguration;

@Configuration
public class ExpvestConfiguration extends AbstractCassandraConfiguration {
    @Value("${dse.contactPoints})")
    public String contactPoints;

    @Value("${dse.port}")
    private int port;

    @Value("${dse.keyspace}")
    private String keyspace;

    public String getContactPoints() {
        return contactPoints;
    }

    public String getKeyspaceName() {
        return keyspace;
    }

    public int getPort() {
        return port;
    }
}
