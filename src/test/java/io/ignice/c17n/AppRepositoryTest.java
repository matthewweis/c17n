package io.ignice.c17n;

import io.ignice.c17n.data.User;
import io.r2dbc.h2.H2ConnectionFactory;
import io.r2dbc.spi.ConnectionFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

//@Transactional
@SpringJUnitConfig
@ExtendWith(SpringExtension.class)
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AppRepositoryTest {

    @Autowired
    private AppRepository repository;

    @Configuration
    @ComponentScan("io.ignice.c17n")
    @Import(io.ignice.c17n.Config.class)
    static class Config extends AbstractR2dbcConfiguration {
        @Bean
        @Override
        public ConnectionFactory connectionFactory() {
            return H2ConnectionFactory.inMemory("database");
        }
    }

    @BeforeEach
    void setUp() {
        repository.saveAll(List.of(
            new User(0L, 0L, null, null, 0L),
            new User(1L, 1L, LocalDateTime.now(), LocalDateTime.now(), 0L),
            new User(2L, 4L, LocalDateTime.now(), LocalDateTime.now(), 0L),
            new User(3L, 8L, LocalDateTime.now(), LocalDateTime.now(), 0L),
            new User(4L, 16L, LocalDateTime.now(), LocalDateTime.now(), 0L)
        ));
    }

    @AfterEach
    void tearDown() {
        repository.deleteAll();
    }

    @Test
    void simpleWrite() {
        System.out.println("HERE!");
        System.out.println("HERE: " + Arrays.toString(repository.findAll().collectList().block().toArray()));
    }

}