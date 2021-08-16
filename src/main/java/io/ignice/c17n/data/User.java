package io.ignice.c17n.data;

import discord4j.common.util.Snowflake;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

/**
 * Discord account DTO.
 *
 * @see UserReadConverter
 * @see UserWriteConverter
 */
@Data
@Table
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class User {

    /**
     * 64-bit UUID corresponding to the {@link User}'s Discord account.
     * <p>
     * Note: Discord represents Snowflake UUIDs as uint64. Special care should be taken to ensure the persistence layer
     * does not leak its internal type representation into or beyond the business logic.
     * </p>
     *
     * @see Snowflake#of(discord4j.discordjson.Id)
     * @see Long#toUnsignedString(long)
     * @see <a href="https://discord.com/developers/docs/reference#snowflakes"></a>
     */
    @Id
    @Column("id")
    private Long id; // UUID id given by discord. Can be converted to type Snowflake.

    @Id
    @Column("wallet")
    private Long wallet;

    @CreatedDate
    @Column("created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column("updated_at")
    private LocalDateTime updatedAt;

    @Version
    @Column("version")
    private Long version;

}
