//package io.ignice.c17n;
//
//import discord4j.core.DiscordClient;
//import discord4j.core.DiscordClientBuilder;
//import io.ignice.c17n.data.UserReadConverter;
//import io.ignice.c17n.data.UserWriteConverter;
//import io.r2dbc.h2.H2ConnectionFactory;
//import io.r2dbc.spi.ConnectionFactory;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.PropertySource;
//import org.springframework.core.io.ClassPathResource;
//import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;
//import org.springframework.data.r2dbc.config.EnableR2dbcAuditing;
//import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
//import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
//import org.springframework.r2dbc.connection.R2dbcTransactionManager;
//import org.springframework.r2dbc.connection.init.CompositeDatabasePopulator;
//import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer;
//import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator;
//import org.springframework.transaction.ReactiveTransactionManager;
//import org.springframework.transaction.annotation.EnableTransactionManagement;
//
//import java.util.List;
//
//@Configuration
//@EnableR2dbcAuditing
//@EnableR2dbcRepositories
//@EnableTransactionManagement
//@PropertySource("classpath:application.properties")
//public class TestMockConfig extends AbstractR2dbcConfiguration {
//
//    @Value("${app.token}")
//    private String token;
//
//    @Bean("gateway")
//    public Gateway gateway(AppRepository appRepository) {
//        // create DiscordClient and pass to gateway
//        // (too many issues arise when treating it as a bean)
//        final DiscordClient discordClient = DiscordClientBuilder.create(token).build();
//        return new Gateway(discordClient, appRepository);
//    }
//
//    @Bean("appTemplate")
//    public R2dbcEntityTemplate appTemplate(ConnectionFactory connectionFactory) {
//        return new R2dbcEntityTemplate(connectionFactory);
//    }
//
//    @Override
//    protected List<Object> getCustomConverters() {
//        return List.of(new UserWriteConverter(), new UserReadConverter());
//    }
//
//    // todo test vs prod with application.properties
//    @Bean
//    @Override
//    public ConnectionFactory connectionFactory() {
////        return switch (token) {
////            case "" -> H2ConnectionFactory.inMemory("database");
////            default -> H2ConnectionFactory.inMemory("database");
////        };
//
////        return new PostgresqlConnectionFactory(
////                PostgresqlConnectionConfiguration.builder()
////                        .host("localhost")
////                        .database("test")
////                        .username("user")
////                        .password("password")
////                        .build()
////        );
//
//        return H2ConnectionFactory.inMemory("database");
//    }
//
//    @Bean
//    public ReactiveTransactionManager transactionManager(ConnectionFactory connectionFactory) {
//        return new R2dbcTransactionManager(connectionFactory);
//    }
//
//    @Bean
//    public ConnectionFactoryInitializer initializer(ConnectionFactory connectionFactory) {
//        final ConnectionFactoryInitializer initializer = new ConnectionFactoryInitializer();
//        initializer.setConnectionFactory(connectionFactory);
//        final CompositeDatabasePopulator populator = new CompositeDatabasePopulator();
//        populator.addPopulators(new ResourceDatabasePopulator(new ClassPathResource("schema.sql")));
//        populator.addPopulators(new ResourceDatabasePopulator(new ClassPathResource("data.sql")));
//        initializer.setDatabasePopulator(populator);
//        return initializer;
//    }
//
//
//}
