package io.ignice.c17n.data;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.data.r2dbc.mapping.OutboundRow;
import org.springframework.r2dbc.core.Parameter;

import java.util.Map;

import static java.util.Objects.requireNonNull;

/**
 * Canonical {@link Converter} :: {@link OutboundRow} -> {@link User} implementation.
 *
 * @see User
 * @see UserReadConverter
 */
@WritingConverter
public class UserWriteConverter implements Converter<User, OutboundRow> {

    @Override
    public OutboundRow convert(User user) {
        return new OutboundRow(Map.of(
                "id", Parameter.from(requireNonNull(user.getId())),
                "wallet", Parameter.from(requireNonNull(user.getWallet())),
                "created_at", Parameter.from(requireNonNull(user.getCreatedAt())),
                "updated_at", Parameter.from(requireNonNull(user.getUpdatedAt())),
                "version", Parameter.from(requireNonNull(user.getVersion()))
        ));
    }

}
