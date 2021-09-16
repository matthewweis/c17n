package io.ignice.c17n;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import discord4j.core.DiscordClient;
import discord4j.core.DiscordClientBuilder;
import discord4j.voice.AudioProvider;
import io.ignice.c17n.util.SanityOps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.transaction.reactive.TransactionalOperator;

import java.util.Arrays;

public class Launcher {

    private static final Logger log = LoggerFactory.getLogger(Launcher.class);

    static {
        Thread.setDefaultUncaughtExceptionHandler((thread, throwable) ->
                log.error("An uncaught exception occurred on thread {} " +
                        "All application errors should be handled by a recovery strategy (application logic) " +
                        "or Hooks#onErrorXYZ (context logic) as a last resort. ", thread, throwable));
    }

    // precondition: no constant or interned string can equal args[0]
    private static String readAndTryEraseToken(String[] args) {
        SanityOps.requireNonNull(args, "args");
        SanityOps.checkSize(args, "args", 1);
        try {
            final char[] stackToken;
            try {
                char[] undecidable = String.copyValueOf(args[0].toCharArray()).toCharArray(); // (double) copy
                args[0] = null; // clear token from args
                stackToken = new char[undecidable.length];
                System.arraycopy(undecidable, 0, stackToken, 0, stackToken.length);
            } finally {
                System.gc();
                System.runFinalization();
            }
            final String result = String.copyValueOf(stackToken);
            Arrays.fill(stackToken, '\0');
            return result;
        } catch (Throwable throwable) {
            throw new RuntimeException("An app token must be passed to c17n.");
        } finally {
            System.gc();
            System.runFinalization();
        }
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            throw new IllegalArgumentException("The app token must be passed to c17n.");
        }
        final Client launcher = new Client();
        try (final ConfigurableApplicationContext config = new AnnotationConfigApplicationContext(Config.class)) {
            // STEPS MUST OCCUR IN ORDER
            final AudioPlayerManager audioPlayerManager = config.getBean(AudioPlayerManager.class);
            AudioSourceManagers.registerRemoteSources(audioPlayerManager); // STEP 1
            final AudioProvider audioProvider = config.getBean(AudioProvider.class);
            final DiscordClient client = DiscordClientBuilder.create(readAndTryEraseToken(args)).build(); // STEP 2
//            final AppRepository repository = config.getBean(AppRepository.class);
//            final TransactionalOperator txOperator = config.getBean(TransactionalOperator.class);
            final AudioLoadResultHandler audioLoadResultHandler = config.getBean(AudioLoadResultHandler.class);
//            final Gateway gateway = new Gateway(audioProvider, audioLoadResultHandler, audioPlayerManager, client, repository, txOperator);
            final Gateway gateway = new Gateway(audioProvider, audioLoadResultHandler, audioPlayerManager, client);
            launcher.run(gateway);
        } catch (Throwable throwable) {
            log.error("A fatal error occurred on the main thread.", throwable);
        }
    }

}
