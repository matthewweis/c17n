package io.ignice.c17n;

import io.r2dbc.h2.H2ConnectionFactory;
import io.r2dbc.spi.ConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;

@Configuration
@Import(AppConfig.class) // by importing AppConfig, we can overwrite the ConnectionFactory
public class MockConfig extends AbstractR2dbcConfiguration {

    @Bean
    @Override
    public ConnectionFactory connectionFactory() {
        return H2ConnectionFactory.inMemory("database");
    }

}
