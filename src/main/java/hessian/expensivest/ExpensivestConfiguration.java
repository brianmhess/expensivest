package hessian.expensivest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.config.AbstractCassandraConfiguration;
import org.springframework.data.cassandra.config.CassandraClusterFactoryBean;
import org.springframework.data.cassandra.core.mapping.BasicCassandraMappingContext;
import org.springframework.data.cassandra.core.mapping.CassandraMappingContext;

import javax.validation.constraints.NotNull;


@Configuration
public class ExpensivestConfiguration extends AbstractCassandraConfiguration {
    //@Value("${dse.contactPoints})")
    public String contactPoints = "127.0.0.1"; //"18.237.248.211";

    //@Value("${dse.port}")
    private int port = 9042;

    //@Value("${dse.keyspace}")
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

}
