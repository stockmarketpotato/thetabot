package com.stockmarketpotato.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import javax.sql.DataSource;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;

@Configuration
public class DatabaseInitializer {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
    @Autowired
    private DataSource dataSource;

    @Bean
    public CommandLineRunner createTableIfNotExists() {
        return args -> {
            // Check if the table already exists
            if (!tableExists("persistent_logins")) {
                // Execute the SQL command to create the table
                JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
                jdbcTemplate.execute(
                    "CREATE TABLE PERSISTENT_LOGINS (" +
                        "series VARCHAR(64) PRIMARY KEY, " +
                        "username VARCHAR(64) NOT NULL, " +
                        "token VARCHAR(64) NOT NULL, " +
                        "last_used TIMESTAMP NOT NULL" +
                    ")"
                );
                logger.info("Table 'persistent_logins' created successfully.");
            } else {
            	logger.info("Table 'persistent_logins' already exists.");
            }
        };
    }

    private boolean tableExists(String tableName) {
        try {
            DatabaseMetaData metaData = dataSource.getConnection().getMetaData();
            ResultSet tables = metaData.getTables(null, null, tableName.toUpperCase(), null);
            return tables.next();
        } catch (Exception e) {
            throw new RuntimeException("Failed to check if table exists", e);
        }
    }
}
