package io.ignice.c17n;

import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import reactor.core.publisher.Mono;

import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.function.Function;

import static reactor.core.publisher.Mono.just;
import static reactor.core.publisher.Mono.using;

public class AppLauncher {

    private static final Logger log = LoggerFactory.getLogger(AppLauncher.class);

    static {
        Thread.setDefaultUncaughtExceptionHandler((thread, throwable) ->
                log.error("An uncaught exception occurred on thread {} " +
                        "All application errors should be handled by a recovery strategy (application logic) " +
                        "or Hooks#onErrorXYZ (context logic) as a last resort. ", thread, throwable));
    }

    private final Gateway gateway;

    public AppLauncher(Gateway gateway) {
        this.gateway = gateway;
    }

    public static void main(String[] args) throws Exception {
        final AnnotationConfigApplicationContext appContext = lazyContext().call();
        final AppLauncher launcher = new AppLauncher(appContext.getBean(Gateway.class));
        launcher.run();
    }

    private static Callable<? extends AnnotationConfigApplicationContext> lazyContext() {
        return () -> new AnnotationConfigApplicationContext(AppConfig.class);
    }

    private static <T> Function<Mono<T>, Mono<T>> attachErrorHandler(@NonNull String info) {
        return mono -> mono.doOnError(report(info)).onErrorResume(throwable -> Mono.empty());
    }

    private static Consumer<? super Throwable> report(@NonNull String info) {
        return throwable -> log.error("A fatal {} error occurred.", info, throwable);
    }

    private void run() {
        using(lazyContext(), Mono::just, AbstractApplicationContext::close)
                .flatMap(context -> just(gateway).transform(attachErrorHandler("context")))
                .flatMap(gateway -> gateway.run().transform(attachErrorHandler("application")))
                .transform(attachErrorHandler("unknown"))
                .block();
    }

}
