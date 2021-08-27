package io.ignice.c17n;

import discord4j.core.DiscordClient;
import discord4j.core.DiscordClientBuilder;
import io.r2dbc.h2.H2ConnectionFactory;
import io.r2dbc.spi.ConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.r2dbc.connection.R2dbcTransactionManager;
import org.springframework.r2dbc.connection.init.CompositeDatabasePopulator;
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer;
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator;
import org.springframework.transaction.ReactiveTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.reactive.TransactionalOperator;

import java.util.List;

@Configuration
@EnableR2dbcRepositories
@EnableTransactionManagement
@PropertySource("classpath:application.properties")
public class Config extends AbstractR2dbcConfiguration {

    @Value("${app.token}")
    private String token;

    @Lazy
    @Bean("gateway")
    public Gateway gateway(DiscordClient client, AppRepository appRepository, TransactionalOperator txOperator) {
        return new Gateway(client, appRepository, txOperator);
    }

    @Lazy
    @Bean("discordClient")
    public DiscordClient discordClient() {
        return DiscordClientBuilder.create(token).build();
    }

    @Override
    protected List<Object> getCustomConverters() {
//        return List.of(new UserWriteConverter(), new UserReadConverter());
        return List.of();
    }

    @Bean
    @Override
    public ConnectionFactory connectionFactory() {
//        return new PostgresqlConnectionFactory(
//                PostgresqlConnectionConfiguration.builder()
//                        .host("localhost")
//                        .database("test")
//                        .username("user")
//                        .password("password")
//                        .build()
//        );
        return H2ConnectionFactory.inMemory("database");
    }

    @Bean
    public ReactiveTransactionManager transactionManager(ConnectionFactory connectionFactory) {
        return new R2dbcTransactionManager(connectionFactory);
    }

    @Bean
    public TransactionalOperator transactionalOperator(ReactiveTransactionManager transactionManager) {
        return TransactionalOperator.create(transactionManager);
    }

    @Bean
    public ConnectionFactoryInitializer initializer(ConnectionFactory connectionFactory) {
        final ConnectionFactoryInitializer initializer = new ConnectionFactoryInitializer();
        initializer.setConnectionFactory(connectionFactory);
        final CompositeDatabasePopulator populator = new CompositeDatabasePopulator();
        populator.addPopulators(new ResourceDatabasePopulator(new ClassPathResource("schema.sql")));
        initializer.setDatabasePopulator(populator);
        return initializer;
    }

}
