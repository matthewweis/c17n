package io.ignice.c17n;

import discord4j.common.util.Snowflake;
import io.ignice.c17n.data.User;
import io.ignice.c17n.util.SanityOps;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface AppRepository extends R2dbcRepository<User, Long> {

    Mono<User> findUserBySnowflake(long snowflake);

    default Mono<User> findUserBySnowflake(Snowflake snowflake) {
        SanityOps.requireNonNull(snowflake, "snowflake");
        return findUserBySnowflake(snowflake.asLong());
    }


}
