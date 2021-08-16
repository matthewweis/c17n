package io.ignice.c17n.data;

import io.r2dbc.spi.Row;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

import java.time.LocalDateTime;

import static java.util.Objects.requireNonNull;

/**
 * Canonical {@link Converter} :: {@link Row} -> {@link User} implementation.
 *
 * @see User
 * @see UserWriteConverter
 */
@ReadingConverter
public class UserReadConverter implements Converter<Row, User> {

    @Override
    public User convert(Row row) {
        return new User(
                requireNonNull(row.get("id", Long.class)),
                requireNonNull(row.get("wallet", Long.class)),
                requireNonNull(row.get("created_at", LocalDateTime.class)),
                requireNonNull(row.get("updated_at", LocalDateTime.class)),
                requireNonNull(row.get("version", Long.class))
        );
    }

}
