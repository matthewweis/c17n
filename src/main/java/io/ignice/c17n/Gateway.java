package io.ignice.c17n;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import discord4j.core.DiscordClient;
import discord4j.core.event.ReactiveEventAdapter;
import discord4j.core.event.domain.lifecycle.ReadyEvent;
import discord4j.core.event.domain.message.MessageBulkDeleteEvent;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.event.domain.message.MessageDeleteEvent;
import discord4j.core.event.domain.message.MessageUpdateEvent;
import discord4j.core.object.VoiceState;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.MessageChannel;
import discord4j.core.shard.GatewayBootstrap;
import discord4j.gateway.GatewayOptions;
import discord4j.voice.AudioProvider;
import io.ignice.c17n.data.User;
import io.ignice.c17n.util.SanityOps;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.function.Function;
import java.util.function.LongUnaryOperator;

import static io.ignice.c17n.util.ArrayOps.head;
import static io.ignice.c17n.util.ArrayOps.tail;
import static io.ignice.c17n.util.StringOps.splitWhitespace;
import static java.lang.String.format;
import static java.util.Objects.isNull;
import static reactor.core.publisher.Operators.addCap;

public class Gateway extends ReactiveEventAdapter {

    private static final Logger log = LoggerFactory.getLogger(Gateway.class);

    private final AudioProvider audioProvider;
    private final AudioLoadResultHandler audioLoadResultHandler;
    private final DiscordClient client;
//    private final AppRepository repo;
//    private final TransactionalOperator txOperator;
    private final AudioPlayerManager audioPlayerManager;

    public Gateway(AudioProvider audioProvider, AudioLoadResultHandler audioLoadResultHandler, AudioPlayerManager audioPlayerManager, DiscordClient client) {
//    public Gateway(AudioProvider audioProvider, AudioLoadResultHandler audioLoadResultHandler, AudioPlayerManager audioPlayerManager, DiscordClient client, AppRepository repo, TransactionalOperator transactionalOperator) {
        SanityOps.requireNonNull(audioProvider, "audioProvider");
        SanityOps.requireNonNull(audioLoadResultHandler, "audioLoadResultHandler");
        SanityOps.requireNonNull(audioPlayerManager, "audioPlayerManager");
        SanityOps.requireNonNull(client, "client");
//        SanityOps.requireNonNull(repo, "repo");
//        SanityOps.requireNonNull(transactionalOperator, "transactionalOperator");
        this.audioProvider = audioProvider;
        this.audioLoadResultHandler = audioLoadResultHandler;
        this.audioPlayerManager = audioPlayerManager;
        this.client = client;
//        this.repo = repo;
//        this.txOperator = transactionalOperator;
    }

    public Mono<Void> connect(Function<GatewayBootstrap<GatewayOptions>, GatewayBootstrap<GatewayOptions>> options) {
        return options.apply(client.gateway()).withGateway(client -> client.on(this));
    }

    @Override
    public Publisher<?> onReady(ReadyEvent event) {
        return Mono.just(event.getSelf().getUsername()).doOnNext(username -> log.info("Logged in as " + username));
    }

    @Override
    public Publisher<?> onMessageCreate(MessageCreateEvent event) {
        final Message message = event.getMessage();
        final String[] words = splitWhitespace(message.getContent());
        final String command = head(words, "help");
        final String[] args = tail(words);

//        event.getMessage().getAuthorAsMember().block().getId();
//        final Optional<discord4j.core.object.entity.User> author = message.getAuthor();
//        event.getMessage().getAuthor().get().getId()

        // todo rm: quick guard
//        log.info(Arrays.toString(words));
//        if (command.equals("bal") && args.length < 1) {
//            return defaultCmd();
//        } else if (command.equals("steal") && args.length < 2) {
//            return defaultCmd();
//        }

        log.info("got a msg {}" + Arrays.toString(words));
//        final Mono<MessageChannel> mono = message.getChannel();
        //
        final Publisher<?> result = switch (command) {
            case "bal" -> balCmd(message.getChannel(), head(args, "0"));
            case "list" -> listCmd(message.getChannel());
            case "ping" -> pingCmd(message.getChannel());
//            case "yt" -> ytCmd(event, head(args, "stop")); // start, stop, or URL
            case "join" -> ytCmd(event, head(args, "join")); // start, stop, or URL
            case "yt" -> ytCmd(event, head(args, "play")); // start, stop, or URL
            case "help" -> helpCmd(message.getChannel());
            case "steal" -> stealCmd(message.getChannel(), head(args, "0"), head(args, "1"));
            default -> defaultCmd();
        };
        return result;
//        return Mono.just(echo(message.getChannel(), "got msg %s", message.getContent())).then(Mono.from(result));
    }

    enum ArgMatcher {
        NEGATIVE, POSITIVE, ZERO
    }

    enum Commands {
        BAL("bal"),
        LIST("list"),
        PING("ping"),
        YT("yt"),
        HELP("help"),
        STEAL("steal", EnumSet.of(ArgMatcher.POSITIVE));

        private final String command;
        // enum set lets us have
        private final List<EnumSet<ArgMatcher>> args;

        Commands(String command, EnumSet<ArgMatcher> ... args) {
            this.command = command;
            this.args = List.of(args);
        }
    }

    private Flux<Message> listCmd(Mono<MessageChannel> mono) {
//        return repo.findAll()
//                .map(user -> format("%d --> %d", user.id(), user.wallet()))
//                .flatMap(message -> echo(mono, message));
        return Flux.empty();
    }

    private Mono<Message> balCmd(Mono<MessageChannel> mono, String key) {
//        return mono.flatMap(channel -> repo.findById(Long.getLong(key))
//                .map(User::wallet)
//                .flatMap(balance -> channel.createMessage(Objects.toString(balance, "NULL"))));
        return Mono.empty();
    }

    private Mono<Message> pingCmd(Mono<MessageChannel> mono) {
        return echo(mono, "pong!");
    }

    private Mono<?> ytCmd(MessageCreateEvent event, String key) {
        SanityOps.requireNonNull(event, "event");
        SanityOps.requireNonNull(key, "key");
//        log.info("Got YT command: key={} msg={}", event, key);
//        Mono.justOrEmpty(event.getMember())
//                .flatMap(Member::getVoiceState)
//                .flatMap(VoiceState::getChannel)
//                .flatMap(voiceChannel -> voiceChannel.join(spec -> spec.setProvider(audioProvider)))
//                .then();
        return switch (key.toLowerCase(Locale.ROOT)) {
            case "stop" -> echo(event.getMessage().getChannel(), "TODO implement stop xd"); // todo
            case "join" -> Mono.justOrEmpty(event.getMember())
                    .flatMap(Member::getVoiceState)
                    .flatMap(VoiceState::getChannel)
                    .flatMap(voiceChannel -> voiceChannel.join(spec -> spec.setProvider(audioProvider)))
                    .then();
            default -> Mono.justOrEmpty(event.getMessage().getContent())
                    .map(content -> Arrays.asList(content.split(" ")))
                    .doOnNext(command -> audioPlayerManager.loadItem(command.get(1), audioLoadResultHandler))
                    .then();
        };

//        return switch (key.toLowerCase(Locale.ROOT)) {
//            case "stop" -> echo(message.getChannel(), "TODO implement stop xd");
//            case "join" -> message.getAuthorAsMember()
//                    .flatMap(Member::getVoiceState)
//                    .flatMap(VoiceState::getChannel)
//                    .flatMap(voiceChannel -> voiceChannel.join(spec -> spec.setProvider(audioProvider)))
//                    .then(echo(message.getChannel(), "joined"));
//            default -> message.getChannel()
//                    .doOnNext(channel -> audioPlayerManager.loadItem(key, audioLoadResultHandler))
//                    .flatMap(channel -> channel.createMessage("playing " + key));
//        };
    }

    private Mono<Message> helpCmd(Mono<MessageChannel> mono) {
        return echo(mono, """
                bal
                steal""");
    }

    // todo remove for command handler
    private Mono<Message> stealCmd(Mono<MessageChannel> mono, String key, String amount) {
//        final Long n = Long.getLong(amount);
//        if (isNull(n)) {
//            return echo(mono, "amount must be supplied", n);
//        } else if (n <= 0) {
//            return echo(mono, "amount must be a positive number but got %d", n);
//        } else {
//            // todo need transactionalOperator.commit or something?
//            return mono
//                    .flatMap(channel -> repo.findById(Long.getLong(key))
//                            .flatMap(user -> repo.save(updateWallet(user, w -> addCap(w, n))))
//                            .as(txOperator::transactional))
//                    .then(echo(mono, "stealing %d from the bank", n));
//        }
        return Mono.empty();
    }

    private Mono<Object> defaultCmd() {
        return Mono.empty();
    }

    @Override
    public Publisher<?> onMessageDelete(MessageDeleteEvent event) {
        return super.onMessageDelete(event);
    }

    @Override
    public Publisher<?> onMessageUpdate(MessageUpdateEvent event) {
        return super.onMessageUpdate(event);
    }

    @Override
    public Publisher<?> onMessageBulkDelete(MessageBulkDeleteEvent event) {
        return super.onMessageBulkDelete(event);
    }

//    private static Mono<Message> echo(Mono<MessageChannel> mono, String formatted) {
//        return mono.flatMap(channel -> channel.createMessage(formatted));
//    }

    private static Mono<Message> echo(Mono<MessageChannel> mono, String formatted, Object ... args) {
        return mono.flatMap(channel -> channel.createMessage(String.format(formatted, args)));
    }

    private static User updateWallet(User user, LongUnaryOperator updater) {
        return user.updateWallet(updater);
    }

}