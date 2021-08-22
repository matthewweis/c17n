package io.ignice.c17n;

import discord4j.core.DiscordClient;
import discord4j.core.event.domain.lifecycle.ReadyEvent;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class Gateway {

    private static final Logger log = LoggerFactory.getLogger(Gateway.class);

    private final DiscordClient discordClient;

    private final AppRepository appRepository;

    public Gateway(DiscordClient discordClient, AppRepository appRepository) {
        this.discordClient = discordClient;
        this.appRepository = appRepository;
    }

    public Mono<Void> run() {
        return discordClient
                .gateway()
                .withGateway(client -> {
                    final Flux<ReadyEvent> onReady = client.on(ReadyEvent.class)
                            .doOnNext(event -> log.info("Logged in as " + event.getSelf().getUsername()));

                    final Flux<Message> onMessage = client.on(MessageCreateEvent.class)
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

                    return Flux.merge(onReady, onMessage).takeUntilOther(client.onDisconnect());
                });
    }
}
