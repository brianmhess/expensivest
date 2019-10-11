package hessian.expensivest;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.dse.DseCluster;
import com.datastax.driver.dse.DseSession;
import com.datastax.driver.mapping.MappingManager;
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
        File apolloCredentialsFile = null;
        try {
            apolloCredentialsFile = File.createTempFile("dsecscb", ".zip");
            FileOutputStream fos = new FileOutputStream(apolloCredentialsFile);
            InputStream credsInputStream = this.getClass().getResourceAsStream(apolloCredentials);
            StreamUtils.copy(credsInputStream, fos);
            credsInputStream.close();
            fos.close();
        }
        catch (IOException ioe) {
            throw new RuntimeException("Could not save cloud secure connect bundle to filesystem ("
                    + ((null == apolloCredentialsFile) ? "<null>" : apolloCredentialsFile.getAbsolutePath()) + ")");
        }

        DseCluster.Builder dseClusterBuilder = DseCluster.builder()
                .withCloudSecureConnectBundle(apolloCredentialsFile.getAbsolutePath())
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
