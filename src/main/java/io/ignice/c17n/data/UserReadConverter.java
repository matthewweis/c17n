//package io.ignice.c17n.data;
//
//import discord4j.common.util.Snowflake;
//import io.ignice.c17n.util.SanityOps;
//import io.r2dbc.spi.Row;
//import lombok.NonNull;
//import org.springframework.core.convert.converter.Converter;
//import org.springframework.data.convert.ReadingConverter;
//
//import java.time.LocalDateTime;
//
///**
// * Canonical {@link Converter} :: {@link Row} -> {@link User} implementation.
// *
// * @see User
// * @see UserWriteConverter
// */
//@ReadingConverter
//public class UserReadConverter implements Converter<Row, User> {
//
//    @Override
//    public User convert(Row row) {
////        SanityOps.requireNonNull(row, "row");
//        return new User(
//                read(row, "id",          Long.class),
//                read(row, "snowflake",   Long.class),
//                read(row, "wallet",      Long.class),
//                read(row, "createdAt",   LocalDateTime.class),
//                read(row, "updatedAt",   LocalDateTime.class),
//                read(row, "version",     Long.class)
//        );
//    }
//
////    @NonNull
//    private static <T> T read(Row row, String column, Class<T> type) {
////        SanityOps.requireNonNull(row, "row");
////        SanityOps.requireNonNull(column, "column");
////        SanityOps.requireNonNull(type, "type");
////        return SanityOps.requireNonNull(row.get(column, type), column);
//        return row.get(column, type);
//    }
//
//}
