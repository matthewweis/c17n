package io.ignice.c17n;

import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.ConnectionFactoryOptions;
import io.r2dbc.spi.Option;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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

import java.util.Collections;
import java.util.List;

@Configuration
@EnableR2dbcRepositories
@EnableTransactionManagement
@PropertySource("classpath:application.properties")
public class Config extends AbstractR2dbcConfiguration {

    @Value("${database.driver}")
    private String driver;

    @Value("${database.protocol}")
    private String protocol;

    @Value("${database.host}")
    private String host;

    @Value("${database.port}")
    private Integer port;

    @Value("${database.database}")
    private String database;

    @Value("${database.user}")
    private String user;

    @Value("${database.password}")
    private String password;

    @Value("${database.locale}")
    private String locale;

    @Override
    protected List<Object> getCustomConverters() {
//        return List.of(new UserWriteConverter(), new UserReadConverter());
        return Collections.emptyList();
    }

    @Bean
    @Override
    public ConnectionFactory connectionFactory() {
        return ConnectionFactories.find(ConnectionFactoryOptions.builder()
                .option(ConnectionFactoryOptions.DRIVER, driver)
//                .option(ConnectionFactoryOptions.PROTOCOL, protocol)
                .option(ConnectionFactoryOptions.HOST, host)
                .option(ConnectionFactoryOptions.PORT, port)
                .option(ConnectionFactoryOptions.DATABASE, database)
                .option(ConnectionFactoryOptions.USER, user)
                .option(ConnectionFactoryOptions.PASSWORD, password)
                .option(Option.valueOf("lock_timeout"), "10s")
                .option(Option.valueOf("statement_timeout"), "5m")
                .option(Option.valueOf("locale"), locale)
                .build());
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
