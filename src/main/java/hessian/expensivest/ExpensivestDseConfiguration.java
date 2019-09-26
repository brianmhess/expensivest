package hessian.expensivest;

import com.datastax.dse.driver.api.core.DseSession;
import com.datastax.dse.driver.api.core.DseSessionBuilder;
import hessian.expensivest.mapper.ExpenseDao;
import hessian.expensivest.mapper.ExpenseMapper;
import hessian.expensivest.mapper.ExpenseMapperBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetSocketAddress;


@Configuration
public class ExpensivestDseConfiguration {
    @Value("${dse.contactPoints}")
    public String contactPoints;

    @Value("${dse.port}")
    private int port;

    @Value("${dse.keyspace}")
    private String keyspace;

    @Value("${dse.localDc}")
    private String localDatacenter;

    @Value("${dse.table}")
    private String table;

    public String getContactPoints() {
        return contactPoints;
    }

    public int getPort() {
        return port;
    }

    public String getKeyspace() {
        return keyspace;
    }

    public String getLocalDatacenter() {
        return localDatacenter;
    }

    public String getTable() {
        return table;
    }

    @Bean
    public DseSession dseSession() {
        DseSessionBuilder dseSessionBuilder = DseSession.builder().withLocalDatacenter(localDatacenter);
        for (String s : contactPoints.split(","))
                dseSessionBuilder.addContactPoint(InetSocketAddress.createUnresolved(s, port));
        return dseSessionBuilder.build();
    }

    @Bean
    public ExpenseMapper expenseMapper(DseSession dseSession) {
        return new ExpenseMapperBuilder(dseSession).build();
    }

    @Bean
    public ExpenseDao expenseDao(ExpenseMapper expenseMapper) {
        return expenseMapper.expenseDao(keyspace, table);
    }

}
