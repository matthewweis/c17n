//package io.ignice.c17n.data;
//
//import io.ignice.c17n.util.SanityOps;
//import org.springframework.core.convert.converter.Converter;
//import org.springframework.data.convert.WritingConverter;
//import org.springframework.data.r2dbc.mapping.OutboundRow;
//import org.springframework.r2dbc.core.Parameter;
//
//import java.lang.reflect.RecordComponent;
//import java.time.LocalDateTime;
//import java.util.Map;
//import java.util.function.Function;
//
///**
// * Canonical {@link Converter} :: {@link OutboundRow} -> {@link User} implementation.
// *
// * @see User
// * @see UserReadConverter
// */
//@WritingConverter
//public class UserWriteConverter implements Converter<User, OutboundRow> {
//
//    @Override
//    public OutboundRow convert(User user) {
////        SanityOps.requireNonNull(user, "user");
////        final boolean isINSERT = user.id() == null; // otherwise, it's an UPDATE
////        final OutboundRow row = new OutboundRow();
////        writeIfPresent(row, "id", user.id());
////        writeIfPresent(row, "snowflake", user.snowflake());
////        writeIfPresent(row, "wallet", user.wallet());
////        writeIfPresent(row, "created_at", user.created_at());
////        writeIfPresent(row, "updated_at", user.updated_at());
////        writeIfPresent(row, "version", user.version());
////        return row;
//
//        return new OutboundRow(Map.of(
//                "id",           Parameter.fromOrEmpty(user.id(),         Long.class),
//                "snowflake",    Parameter.fromOrEmpty(user.snowflake(),  Long.class),
//                "wallet",       Parameter.fromOrEmpty(user.wallet(),     Long.class),
//                "created_at",   Parameter.fromOrEmpty(user.created_at(), LocalDateTime.class),
//                "updated_at",   Parameter.fromOrEmpty(user.updated_at(), LocalDateTime.class),
//                "version",      Parameter.fromOrEmpty(user.version(),    Long.class)));
//    }
//
//    private static void writeIfPresent(OutboundRow row, String key, Object value) {
//        if (value != null) {
//            row.put(key, Parameter.from(value));
//        }
//    }
//
//    private static <T> Parameter write(User user, Function<User, T> lens) {
////        SanityOps.requireNonNull(user, "user");
////        SanityOps.requireNonNull(lens, "lens");
//        return Parameter.from(lens.apply(user));
//    }
//
//}
