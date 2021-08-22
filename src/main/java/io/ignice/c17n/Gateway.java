package io.ignice.c17n;

import discord4j.core.DiscordClient;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.lifecycle.ReadyEvent;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import discord4j.core.shard.GatewayBootstrap;
import discord4j.gateway.GatewayOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.Function;

public class Gateway {

    private static final Logger log = LoggerFactory.getLogger(Gateway.class);

    private final DiscordClient client;
    private final AppRepository appRepository;

    public Gateway(DiscordClient client, AppRepository appRepository) {
        this.client = client;
        this.appRepository = appRepository;
    }

    public Mono<Void> connect(Function<GatewayBootstrap<GatewayOptions>, GatewayBootstrap<GatewayOptions>> options) {
        return options.apply(client.gateway())
                .withGateway(client -> {
                    final Flux<ReadyEvent> onReady = onReady(client);
                    final Flux<Message> onMessage = onMessage(client);
                    return Flux.merge(onReady, onMessage).takeUntilOther(client.onDisconnect());
                });
    }

    private Flux<ReadyEvent> onReady(GatewayDiscordClient client) {
        return client.on(ReadyEvent.class).doOnNext(event -> log.info("Logged in as " + event.getSelf().getUsername()));
    }

    private Flux<Message> onMessage(GatewayDiscordClient client) {
        return client.on(MessageCreateEvent.class)
                .flatMap(event -> {
                    final Message msg = event.getMessage();
                    return switch (msg.getContent()) {
                        case "list" -> appRepository.findAll()
                                .map(user -> user.getId() + " --> " + user.getWallet())
                                .flatMap(s -> msg.getChannel().flatMap(ch -> ch.createMessage(s)));
                        case "ping" -> msg.getChannel().flatMap(channel -> channel.createMessage("pong"));
                        default -> Mono.empty();
                    };
                });
    }

}
