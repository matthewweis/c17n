package io.ignice.c17n;

import io.r2dbc.h2.H2ConnectionFactory;
import io.r2dbc.spi.ConnectionFactory;
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

import java.util.Arrays;

//@Transactional
@SpringJUnitConfig
@ExtendWith(SpringExtension.class)
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AppRepositoryTest {

    @Autowired
    private AppRepository repository;

    @Configuration
    @Import(io.ignice.c17n.Config.class)
    @ComponentScan("io.ignice.c17n")
    static class Config extends AbstractR2dbcConfiguration {
        @Bean
        @Override
        public ConnectionFactory connectionFactory() {
            return H2ConnectionFactory.inMemory("database");
        }
    }

    @BeforeEach
    void setUp() {

    }

    @Test
    void simpleWrite() {
        System.out.println(Arrays.toString(repository.findAll().collectList().block().toArray()));
    }

}