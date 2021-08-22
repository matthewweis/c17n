package io.ignice.c17n;

import io.ignice.c17n.data.User;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface AppRepository extends R2dbcRepository<User, Long> {
//    @Query("select id, wallet, created_at, updated_at, version from users u where u.id = :id")
//    Mono<User> findById(@NonNull Long id);
}
