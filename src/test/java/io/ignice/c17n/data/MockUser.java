package io.ignice.c17n.data;

import java.time.LocalDateTime;

public final class MockUser extends User {
    /**
     * Exposes {@link User}'s protected constructor to the outside world for testing.
     *
     * @param id
     * @param snowflake
     * @param wallet
     * @param created_at
     * @param updated_at
     * @param version
     */
    public MockUser(Long id, Long snowflake, Long wallet, LocalDateTime created_at, LocalDateTime updated_at, Long version) {
        super(id, snowflake, wallet, created_at, updated_at, version);
    }
}
