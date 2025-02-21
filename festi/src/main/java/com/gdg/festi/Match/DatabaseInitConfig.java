package com.gdg.festi.Match;

import com.gdg.festi.Match.Service.DatabaseInit;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DatabaseInitConfig {

    private final DatabaseInit databaseInit;

    public DatabaseInitConfig(DatabaseInit databaseInit) {
        this.databaseInit = databaseInit;
    }

    @Bean
    public ApplicationRunner initializeDatabase() {
        return args -> databaseInit.init();
    }

}
