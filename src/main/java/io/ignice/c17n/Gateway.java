package io.ignice.c17n;

import discord4j.core.DiscordClient;
import discord4j.core.event.ReactiveEventAdapter;
import discord4j.core.event.domain.lifecycle.ReadyEvent;
import discord4j.core.event.domain.message.MessageBulkDeleteEvent;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.event.domain.message.MessageDeleteEvent;
import discord4j.core.event.domain.message.MessageUpdateEvent;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.MessageChannel;
import discord4j.core.shard.GatewayBootstrap;
import discord4j.gateway.GatewayOptions;
import io.ignice.c17n.data.User;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.EnumSet;
import java.util.List;
import java.util.Objects;
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

    private final DiscordClient client;
    private final AppRepository repo;
    private final TransactionalOperator txOperator;

    public Gateway(DiscordClient client, AppRepository repo, TransactionalOperator transactionalOperator) {
        this.client = client;
        this.repo = repo;
        this.txOperator = transactionalOperator;
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

        final Mono<MessageChannel> mono = message.getChannel();
        //
        return switch (command) {
            case "bal" -> balCmd(mono, head(args, "0"));
            case "list" -> listCmd(mono);
            case "ping" -> pingCmd(mono);
            case "help" -> helpCmd(mono);
            case "steal" -> stealCmd(mono, head(args, "0"), head(args, "1"));
            default -> defaultCmd();
        };
    }

    enum ArgMatcher {
        NEGATIVE, POSITIVE, ZERO
    }

    enum Commands {
        BAL("bal"),
        LIST("list"),
        PING("ping"),
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
        return repo.findAll()
                .map(user -> format("%d --> %d", user.id(), user.wallet()))
                .flatMap(message -> echo(mono, message));
    }

    private Mono<Message> balCmd(Mono<MessageChannel> mono, String key) {
        return mono.flatMap(channel -> repo.findById(Long.getLong(key))
                .map(User::wallet)
                .flatMap(balance -> channel.createMessage(Objects.toString(balance, "NULL"))));
    }

    private Mono<Message> pingCmd(Mono<MessageChannel> mono) {
        return echo(mono, "pong");
    }

    private Mono<Message> helpCmd(Mono<MessageChannel> mono) {
        return echo(mono, """
                bal
                steal""");
    }

    // todo remove for command handler
    private Mono<Message> stealCmd(Mono<MessageChannel> mono, String key, String amount) {
        final Long n = Long.getLong(amount);
        if (isNull(n)) {
            return echo(mono, "amount must be supplied", n);
        } else if (n <= 0) {
            return echo(mono, "amount must be a positive number but got %d", n);
        } else {
            // todo need transactionalOperator.commit or something?
            return mono
                    .flatMap(channel -> repo.findById(Long.getLong(key))
                            .flatMap(user -> repo.save(updateWallet(user, w -> addCap(w, n))))
                            .as(txOperator::transactional))
                    .then(echo(mono, "stealing %d from the bank", n));
        }
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

    private static Mono<Message> echo(Mono<MessageChannel> mono, String formatted, Object ... args) {
        return mono.flatMap(channel -> channel.createMessage(String.format(formatted, args)));
    }

    private static User updateWallet(User user, LongUnaryOperator updater) {
        return user.updateWallet(updater);
    }

}