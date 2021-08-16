package io.ignice.c17n;

import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import reactor.core.publisher.Mono;

import java.util.concurrent.Callable;
import java.util.function.Consumer;

import static reactor.core.publisher.Mono.fromSupplier;
import static reactor.core.publisher.Mono.using;

public class AppLauncher {


    private static final Logger log = LoggerFactory.getLogger(AppLauncher.class);

    static {
        Thread.setDefaultUncaughtExceptionHandler((thread, throwable) ->
                log.error("An uncaught exception occurred on thread {} " +
                        "All application errors should be handled by a recovery strategy (application logic) " +
                        "or Hooks#onErrorXYZ (context logic) as a last resort. ", thread, throwable));
    }

    public static void main(String[] args) {
        using(lazyContext(), Mono::just, AbstractApplicationContext::close)
                .flatMap(context -> fromSupplier(() -> context.getBean(Gateway.class)).doOnError(report("context"))
                .flatMap(gateway -> fromSupplier(gateway::run).doOnError(report("application")))
                .doOnError(throwable -> log.error("An unspecified error occurred.", throwable)))
                .block();
    }

    private static Callable<? extends AnnotationConfigApplicationContext> lazyContext() {
        return () -> new AnnotationConfigApplicationContext(AppConfig.class);
    }

    private static Consumer<? super Throwable> report(@NonNull String info) {
        return throwable -> log.error("A fatal {} error occurred.", info, throwable);
    }

}
