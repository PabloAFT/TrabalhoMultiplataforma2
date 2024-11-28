package com.trabalho.multi.trabalhomulti.connection;
import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.data.cassandra.core.CassandraTemplate;
import org.springframework.stereotype.Component;


@Component
public class ConnectDatabase {
   private static CqlSession session;
   @Autowired
    public static void connect() {
         // Create the CqlSession object:
         session = CqlSession.builder()
            .withCloudSecureConnectBundle(Paths.get(CassandraConfig.getSecureConnectPath()))
            .withAuthCredentials(CassandraConfig.getUsername(),CassandraConfig.getSecret())
            .withKeyspace(CqlIdentifier.fromCql(CassandraConfig.getKeyspaceName()))

            .build(); 
            CassandraOperations template = new CassandraTemplate(session);
            // Select the release_version from the system.local table:
            ResultSet rs = session.execute("select release_version from system.local");
            Row row = rs.one();
            //Print the results of the CQL query to the console:
            if (row != null) {
               System.out.println(row.getString("release_version"));
            } else {
               System.out.println("An error occurred.");
            }
            
         
            
        //  System.exit(0);
   }
   public static CqlSession getSession() {
       return session;
   }
}
