package io.ignice.c17n.audio;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import io.ignice.c17n.util.SanityOps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Based on Discord4j tutorial:
 * https://docs.discord4j.com/music-bot-tutorial/#step-26
 */
public class LavaTrackScheduler implements AudioLoadResultHandler {

    private static final Logger log = LoggerFactory.getLogger(LavaTrackScheduler.class);

    private final AudioPlayer audioPlayer;

    public LavaTrackScheduler(AudioPlayer audioPlayer) {
        SanityOps.requireNonNull(audioPlayer, "audioPlayer");
        this.audioPlayer = audioPlayer;
    }

    @Override
    public void trackLoaded(AudioTrack audioTrack) {
        log.info("Lava player is playing the track {}", audioTrack);
        audioPlayer.playTrack(audioTrack);
    }

    @Override
    public void playlistLoaded(AudioPlaylist audioPlaylist) {
        log.info("Lava player found a playlist {}", audioPlaylist);
    }

    @Override
    public void noMatches() {
        log.info("Lava player found no audio to extract");
    }

    @Override
    public void loadFailed(FriendlyException e) {
        log.warn("Lava player could not parse audio source", e);
    }
}
