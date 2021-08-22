package io.ignice.c17n;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Launcher {

    private static final Logger log = LoggerFactory.getLogger(Launcher.class);

    static {
        Thread.setDefaultUncaughtExceptionHandler((thread, throwable) ->
                log.error("An uncaught exception occurred on thread {} " +
                        "All application errors should be handled by a recovery strategy (application logic) " +
                        "or Hooks#onErrorXYZ (context logic) as a last resort. ", thread, throwable));
    }

    public static void main(String[] args) {
        final Client launcher = new Client();
        try (final ConfigurableApplicationContext config = new AnnotationConfigApplicationContext(Config.class)) {
            final Gateway gateway = config.getBean(Gateway.class);
            launcher.run(gateway);
        } catch (Throwable throwable) {
            log.error("A fatal error occurred on the main thread.", throwable);
        }
    }

}
