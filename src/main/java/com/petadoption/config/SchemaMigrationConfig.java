package com.petadoption.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class SchemaMigrationConfig {

    @Bean
    CommandLineRunner migratePetSchema(JdbcTemplate jdbcTemplate) {
        return args -> {
            Integer columnCount = jdbcTemplate.queryForObject(
                    "SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS " +
                            "WHERE TABLE_NAME = 'PET' AND COLUMN_NAME = 'AGE_MONTHS'",
                    Integer.class
            );

            if (columnCount != null && columnCount == 0) {
                jdbcTemplate.execute("ALTER TABLE PET ADD COLUMN AGE_MONTHS INT DEFAULT 0");
                jdbcTemplate.execute("UPDATE PET SET AGE_MONTHS = 0 WHERE AGE_MONTHS IS NULL");
            }
        };
    }
}
