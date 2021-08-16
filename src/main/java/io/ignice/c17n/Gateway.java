package io.ignice.c17n;

import discord4j.core.DiscordClient;
import discord4j.core.event.domain.lifecycle.ReadyEvent;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class Gateway {

    private static final Logger log = LoggerFactory.getLogger(Gateway.class);

    private final DiscordClient discordClient;

    public Gateway(DiscordClient discordClient) {
        this.discordClient = discordClient;
    }

    public Mono<Void> run() {
        return discordClient
                .gateway()
                .withGateway(client -> {
                    final Flux<ReadyEvent> onReady = client.on(ReadyEvent.class)
                            .doOnNext(event -> log.info("Logged in as " + event.getSelf().getUsername()));

                    final Flux<Message> onMessage = client.on(MessageCreateEvent.class)
                            .flatMap(event -> {
                                final var message = event.getMessage();
                                return message.getContent().equals("!ping")
                                        ? message.getChannel().flatMap(channel -> channel.createMessage("pong!"))
                                        : Mono.empty();
                            });

                    return Flux.merge(onReady, onMessage).takeUntilOther(client.onDisconnect());
                });
    }
}
