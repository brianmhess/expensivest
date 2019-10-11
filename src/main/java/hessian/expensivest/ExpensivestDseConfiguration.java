package hessian.expensivest;

import com.datastax.dse.driver.api.core.DseSession;
import com.datastax.dse.driver.api.core.DseSessionBuilder;
import hessian.expensivest.mapper.ExpenseDao;
import hessian.expensivest.mapper.ExpenseMapper;
import hessian.expensivest.mapper.ExpenseMapperBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StreamUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;


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

    private File apolloCredentialsFile;

    public String getKeyspace() {
        return this.keyspace;
    }

    public String getTable() {
        return this.table;
    }

    public String getApolloCredentials() {
        return this.apolloCredentials;
    }

    public File getApolloCredentialsFile() {
        return this.apolloCredentialsFile;
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    @Bean
    public DseSession dseSession(LastUpdatedStateListener lastUpdatedStateListener, LastUpdatedSchemaListener lastUpdateSchemaListener) {
        try {
            this.apolloCredentialsFile = File.createTempFile("dsecscb", ".zip");
            FileOutputStream fos = new FileOutputStream(this.apolloCredentialsFile);
            InputStream credsInputStream = this.getClass().getResourceAsStream(this.apolloCredentials);
            StreamUtils.copy(credsInputStream, fos);
            credsInputStream.close();
            fos.close();
        }
        catch (IOException ioe) {
            throw new RuntimeException("Could not save cloud secure connect bundle to filesystem ("
                    + ((null == this.apolloCredentialsFile) ? " <null> " : this.apolloCredentialsFile.getAbsolutePath()) + ")");
        }

        DseSessionBuilder dseSessionBuilder = DseSession.builder()
                .withCloudSecureConnectBundle(this.apolloCredentialsFile.getAbsolutePath())
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
