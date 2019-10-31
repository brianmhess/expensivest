package hessian.expensivest;

import com.datastax.dse.driver.api.core.DseSession;
import com.datastax.dse.driver.api.core.DseSessionBuilder;
import hessian.expensivest.mapper.ExpenseDao;
import hessian.expensivest.mapper.ExpenseMapper;
import hessian.expensivest.mapper.ExpenseMapperBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;


@Configuration
public class ExpensivestDseConfiguration {
    @Value("${dse.keyspace}")
    private String keyspace;

    @Value("${dse.table}")
    private String table;

    @Value("${apollo.credentials}")
    private String apolloCredentials;

    @Value("${dse.username}")
    private String username;

    @Value("${dse.password}")
    private String password;

    public String getKeyspace() {
        return this.keyspace;
    }

    public String getTable() {
        return this.table;
    }

    public String getApolloCredentials() {
        return this.apolloCredentials;
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    @Bean
    public DseSession dseSession(LastUpdatedStateListener lastUpdatedStateListener, LastUpdatedSchemaListener lastUpdateSchemaListener) {
        DseSessionBuilder dseSessionBuilder = DseSession.builder()
                .withCloudSecureConnectBundle(this.getClass().getResourceAsStream(this.apolloCredentials))
                .withAuthCredentials(this.username, this.password);
        dseSessionBuilder.withNodeStateListener(lastUpdatedStateListener);
        dseSessionBuilder.withSchemaChangeListener(lastUpdateSchemaListener);

        return dseSessionBuilder.build();
    }

    @Bean
    public LastUpdatedStateListener lastUpdatedStateListener() {
        return new LastUpdatedStateListener();
    }

    @Bean
    public LastUpdatedSchemaListener lastUpdatedSchemaListener() {
        return new LastUpdatedSchemaListener();
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
