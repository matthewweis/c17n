package io.ignice.c17n;

import io.ignice.c17n.data.User;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

public interface AppRepository extends R2dbcRepository<User, Long> {
    Mono<User> findById(Long id);
}
