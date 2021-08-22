package io.ignice.c17n;

import io.r2dbc.h2.H2ConnectionFactory;
import io.r2dbc.spi.ConnectionFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.Arrays;

//@Transactional
@SpringJUnitConfig
@ExtendWith(SpringExtension.class)
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AppRepositoryTest {

//    @Autowired
//    private AppRepository appRepository;

    @Configuration
//    @EnableR2dbcRepositories
//    @EnableTransactionManagement
    @ComponentScan("io.ignice.c17n")
//    @PropertySource("classpath:application.properties")
    @Import(AppConfig.class)
    static class Config extends AbstractR2dbcConfiguration {

        @Bean
        @Override
        public ConnectionFactory connectionFactory() {
            return H2ConnectionFactory.inMemory("database");
        }
    }

    @Test
    void simpleWrite() {
//        System.out.println(Arrays.toString(appRepository.findAll().collectList().block().toArray()));
    }

}