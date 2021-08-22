package io.ignice.c17n;

import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

import java.util.function.Consumer;
import java.util.function.Function;

import static java.util.function.Function.identity;
import static reactor.core.publisher.Mono.just;

public class Client {

    private static final Logger log = LoggerFactory.getLogger(Client.class);

    private static <T> Function<Mono<T>, Mono<T>> attachErrorHandler(@NonNull String info) {
        return mono -> mono.doOnError(report(info)).onErrorResume(throwable -> Mono.empty());
    }

    private static Consumer<? super Throwable> report(@NonNull String info) {
        return throwable -> log.error("A fatal {} error occurred.", info, throwable);
    }

    public void run(Gateway gateway) {
        Mono.just(gateway)
                .flatMap(sandbox -> just(sandbox).transform(attachErrorHandler("context")))
                .flatMap(sandbox -> sandbox.connect(identity()).transform(attachErrorHandler("application")))
                .transform(attachErrorHandler("unknown"))
                .block();
    }

}
