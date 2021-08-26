package io.ignice.c17n.data;

import discord4j.common.util.Snowflake;
import io.ignice.c17n.util.SanityOps;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.annotation.*;
import org.springframework.data.relational.core.mapping.Table;

import java.beans.ConstructorProperties;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.function.LongUnaryOperator;

import static io.ignice.c17n.util.SanityOps.requireNonNull;

/**
 * Discord account DTO.
 * <br>
 * Note that some fields are labeled as <b>bookkeeping fields</b>.
 * Bookkeeping fields exist to fulfil special requirements and are ignored by
 * {@link User#equals(Object)} and {@link User#hashCode()}.
 *
 * @see UserReadConverter
 * @see UserWriteConverter
 * @see <a href="https://github.com/spring-projects/spring-data-r2dbc/blob/fe7308100a2d06401fa03eaf3722d5c0e3ad514b/src/main/asciidoc/reference/mapping.adoc#mapping-annotation-overview">r2dbc mapping annotation overview</a>
 */
@Table("users")
public class User {

    @Id
    private final Long id;
    private final Long snowflake;
    private final Long wallet;

    @CreatedDate
    private final LocalDateTime created_at;

    @LastModifiedDate
    private final LocalDateTime updated_at;

    @Version
    private final Long version;

    /**
     */
    @PersistenceConstructor
    @ConstructorProperties({"id","snowflake","wallet","created_at","updated_at","version"})
    protected User(Long id,
         Long snowflake,
         Long wallet,
         LocalDateTime created_at,
         LocalDateTime updated_at,
         Long version) {
        this.id = id;
        this.snowflake = snowflake;
        this.wallet = wallet;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.version = version;
    }

    public User withId(Long id) {
        return new User(id, this.snowflake, this.wallet, this.created_at, this.updated_at, this.version);
    }

    public User withSnowflake(Long snowflake) {
        return new User(this.id, snowflake, this.wallet, this.created_at, this.updated_at, this.version);
    }

    public User withWallet(Long wallet) {
        return new User(this.id, this.snowflake, wallet, this.created_at, this.updated_at, this.version);
    }

    public User withCreated_at(LocalDateTime created_at) {
        return new User(this.id, this.snowflake, this.wallet, created_at, this.updated_at, this.version);
    }

    public User withUpdated_at(LocalDateTime updated_at) {
        return new User(this.id, this.snowflake, this.wallet, this.created_at, updated_at, this.version);
    }

    public User withVersion(Long version) {
        return new User(this.id, this.snowflake, this.wallet, this.created_at, this.updated_at, version);
    }

    public User updateWallet(LongUnaryOperator walletUpdater) {
        requireNonNull(walletUpdater, "walletUpdater");
        final long newAmount = walletUpdater.andThen(Math::absExact).applyAsLong(this.wallet);
        return new User(this.id, this.snowflake, newAmount, this.created_at, this.updated_at, this.version);
    }

    public static User of(Snowflake snowflake, long wallet) {
        SanityOps.requireNonNull(snowflake, "snowflake");
        SanityOps.requireNonNegative(wallet, "wallet");
        final LocalDateTime now = LocalDateTime.now(); // todo gen server side

        // ID should be null when inserting (will be created by database).
        // https://docs.spring.io/spring-data/r2dbc/docs/current/reference/html/#r2dbc.entityoperations.save-insert
        return new User(null, snowflake.asLong(), wallet, null, null, null);
    }

    /**
     * The Entity's ID in the table.
     * Assigned to an account by SQL auto increment.
     * <br>
     * When 0L (or (Long) null), then the entity is assumed to be new.
     *
     * @return the entity's table id
     * @see <a href="https://docs.spring.io/spring-data/r2dbc/docs/current/reference/html/#is-new-state-detection">r2dbc is-new-state-detection</a>
     */
    public Long id() {
        return id;
    }

    /**
     * 64-bit UUID corresponding to the {@link User}'s Discord account.
     * <p>
     * Note: Discord represents Snowflake UUIDs as uint64. Special care should be taken to ensure the persistence layer
     * does not leak its internal type representation into or beyond the business logic.
     * </p>
     *
     * @see Snowflake#of(discord4j.discordjson.Id)
     * @see Long#toUnsignedString(long)
     * @see <a href="https://discord.com/developers/docs/reference#snowflakes">discord snowflakes</a>
     */
    public Snowflake snowflake() {
        return Snowflake.of(snowflake);
    }

    public Long wallet() {
        return wallet;
    }

    public LocalDateTime created_at() {
        return created_at;
    }

    public LocalDateTime updated_at() {
        return updated_at;
    }

    /**
     * A <b>bookkeeping field</b> used to implement concurrency-friendly optimistic locks on persistence layer.
     * <br>
     * When 0L (or (Long) null), then the entity is assumed to be new.
     *
     * @return the entity's table id
     * @see Version
     * @see OptimisticLockingFailureException
     * @see <a href="https://docs.spring.io/spring-data/r2dbc/docs/current/reference/html/#is-new-state-detection">r2dbc is new state detection</a>
     * @see <a href="https://docs.spring.io/spring-data/r2dbc/docs/current/reference/html/#r2dbc.optimistic-locking">r2dbc optimistic locking</a>
     */
    public Long version() {
        return version;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(snowflake, user.snowflake) && Objects.equals(wallet, user.wallet);
    }

    @Override
    public int hashCode() {
        return Objects.hash(snowflake, wallet);
    }

    @Override
    public String toString() {
        return "User[" +
                "id=" + id + ", " +
                "snowflake=" + snowflake + ", " +
                "wallet=" + wallet + ", " +
                "created_at=" + created_at + ", " +
                "updated_at=" + updated_at + ", " +
                "version=" + version + ']';
    }

}
