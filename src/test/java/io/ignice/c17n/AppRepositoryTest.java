package io.ignice.c17n;

import discord4j.common.util.Snowflake;
import io.ignice.c17n.data.User;
import io.r2dbc.h2.H2ConnectionFactory;
import io.r2dbc.spi.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Hooks;
import reactor.test.StepVerifier;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Slf4j
@SpringJUnitConfig
@ExtendWith(SpringExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class AppRepositoryTest {

    static {
        Hooks.onOperatorDebug();
        Hooks.onErrorDropped(throwable -> {
            throwable.printStackTrace();
            log.error("Error dropped: ", throwable);
        });
    }

    private String schemaSql;
    private String dropSql;

    // nulls where database where generate on INSERT (otherwise its UPDATE)
    private final List<User> fakeUsers = List.of(
            User.of(Snowflake.of(0L), 1L),
            User.of(Snowflake.of(1L), 2L),
            User.of(Snowflake.of(2L), 4L),
            User.of(Snowflake.of(3L), 8L),
            User.of(Snowflake.of(4L), 16L)
    );

    @Autowired
    private AppRepository repository;

    @Autowired
    private R2dbcEntityTemplate template;


    @Configuration
    @ComponentScan("io.ignice.c17n")
    @Import(io.ignice.c17n.Config.class)
    static class TestConfig extends AbstractR2dbcConfiguration {
        @Bean
        @Override
        public ConnectionFactory connectionFactory() {
            return H2ConnectionFactory.inMemory("database");
        }
    }

    @BeforeAll
    void beforeAll() throws IOException {
        schemaSql = Files.readString(Path.of(new ClassPathResource("schema.sql").getURI()));
        dropSql = Files.readString(Path.of(new ClassPathResource("drop.sql").getURI()));
    }


    // https://github.com/spring-projects/spring-data-r2dbc/blob/fe7308100a2d06401fa03eaf3722d5c0e3ad514b/src/main/asciidoc/reference/r2dbc-repositories.adoc
    @BeforeEach
    void setUp() throws IOException {
        // setup schema
        template.getDatabaseClient()
                .sql(schemaSql)
                .fetch()
                .rowsUpdated()
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete();

        // populate with test data
        repository.saveAll(fakeUsers)
                .as(StepVerifier::create)
                .expectNextCount(fakeUsers.size())
                .verifyComplete();
    }

    @AfterEach
    void tearDown() {
        template.getDatabaseClient()
                .sql(dropSql)
                .fetch()
                .rowsUpdated()
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    void usersCanBeFoundById() {
        StepVerifier.create(Flux.just(1L, 2L, 3L, 4L, 5L)
                        .flatMap(id -> repository.findById(id)))
                .expectNext(User.of(Snowflake.of(0L), 1L))
                .expectNext(User.of(Snowflake.of(1L), 2L))
                .expectNext(User.of(Snowflake.of(2L), 4L))
                .expectNext(User.of(Snowflake.of(3L), 8L))
                .expectNext(User.of(Snowflake.of(4L), 16L))
                .verifyComplete();
    }

    @Test
    void usersCanBeFoundBySnowflake() {
        StepVerifier.create(Flux.just(0L, 1L, 2L, 3L, 4L)
                        .map(Snowflake::of)
                        .flatMap(snowflake -> repository.findUserBySnowflake(snowflake)))
                .expectNext(User.of(Snowflake.of(0L), 1L))
                .expectNext(User.of(Snowflake.of(1L), 2L))
                .expectNext(User.of(Snowflake.of(2L), 4L))
                .expectNext(User.of(Snowflake.of(3L), 8L))
                .expectNext(User.of(Snowflake.of(4L), 16L))
                .verifyComplete();
    }

    @Test
    void usersCanBeFoundBySnowflakeLiteral() {
        StepVerifier.create(Flux.just(0L, 1L, 2L, 3L, 4L)
                        .flatMap(snowflakeLiteral -> repository.findUserBySnowflake(snowflakeLiteral)))
                .expectNext(User.of(Snowflake.of(0L), 1L))
                .expectNext(User.of(Snowflake.of(1L), 2L))
                .expectNext(User.of(Snowflake.of(2L), 4L))
                .expectNext(User.of(Snowflake.of(3L), 8L))
                .expectNext(User.of(Snowflake.of(4L), 16L))
                .verifyComplete();
    }

    @Test
    void insertedUsersCanBeFound() {
        StepVerifier.create(repository
                        .save(User.of(Snowflake.of(5L), 32L))
                        .thenMany(repository.findAll()))
                .expectNext(User.of(Snowflake.of(0L), 1L))
                .expectNext(User.of(Snowflake.of(1L), 2L))
                .expectNext(User.of(Snowflake.of(2L), 4L))
                .expectNext(User.of(Snowflake.of(3L), 8L))
                .expectNext(User.of(Snowflake.of(4L), 16L))
                .expectNext(User.of(Snowflake.of(5L), 32L))
                .verifyComplete();
    }

    @Test
    void insertedUsersCanBeUpdated() {
        StepVerifier.create(Flux.range(1, fakeUsers.size())
                        .flatMapSequential(id -> repository.findById((long) id))
                        .flatMap(user -> template.update(user.updateWallet(amount -> amount * 2)))
                        .thenMany(template.select(User.class).all()))
                .expectNext(User.of(Snowflake.of(0L), 2L))
                .expectNext(User.of(Snowflake.of(1L), 4L))
                .expectNext(User.of(Snowflake.of(2L), 8L))
                .expectNext(User.of(Snowflake.of(3L), 16L))
                .expectNext(User.of(Snowflake.of(4L), 32L))
                .verifyComplete();
    }
}