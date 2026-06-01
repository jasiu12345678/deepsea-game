package pt.isec.pa.deepsea.ui.res;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.util.HashMap;
import java.util.Map;

/**
 * Plays game sounds from {@code resources/sounds}. Supports movement sounds,
 * effects that play to the end, and a global on/off switch.
 */

public class SoundManager {

    public static final String SHIP_MOVE = "ship_move.wav";
    public static final String DRONE_MOVE = "drone_move.wav";
    public static final String DRONE_HIT = "drone_hit.wav";
    public static final String DRONE_DESTROYED = "drone_destroyed.wav";
    public static final String DRONE_ANIMAL = "marineanimal.wav";

    private static final BooleanProperty enabled = new SimpleBooleanProperty(true);
    private static final Map<String, Media> mediaCache = new HashMap<>();

    private static MediaPlayer mp;
    private static MediaPlayer foreverMp;

    private SoundManager() {
    }

    public static BooleanProperty enabledProperty() {
        return enabled;
    }

    public static boolean isEnabled() {
        return enabled.get();
    }

    public static void setEnabled(boolean value) {
        enabled.set(value);
    }

    public static boolean playForever(String filename) {
        if (!enabled.get()) {
            return false;
        }
        try {
            Media music = getMedia(filename);
            if (music == null) {
                return false;
            }
            if (foreverMp != null) {
                foreverMp.setOnEndOfMedia(null);
                foreverMp.stop();
                foreverMp.dispose();
            }
            foreverMp = new MediaPlayer(music);
            foreverMp.setStartTime(Duration.ZERO);
            foreverMp.setStopTime(music.getDuration());
            foreverMp.setAutoPlay(true);
        } catch (Exception e) {
            System.err.println("Error playing sound: " + filename);
            return false;
        }
        return true;
    }

    public static boolean play(String filename) {
        if (!enabled.get()) {
            return false;
        }
        try {
            Media music = getMedia(filename);
            if (music == null) {
                return false;
            }
            stopCurrent();
            mp = new MediaPlayer(music);
            mp.setOnEndOfMedia(null);
            mp.setStartTime(Duration.ZERO);
            mp.setStopTime(music.getDuration());
            mp.setAutoPlay(true);
        } catch (Exception e) {
            System.err.println("Error playing sound file: " + filename);
            return false;
        }
        return true;
    }

    public static boolean playThen(String movementFile, String followUpFile) {
        if (!enabled.get()) {
            return false;
        }
        try {
            Media music = getMedia(movementFile);
            if (music == null) {
                return play(followUpFile);
            }
            stopCurrent();
            mp = new MediaPlayer(music);
            mp.setOnEndOfMedia(() -> play(followUpFile));
            mp.setStartTime(Duration.ZERO);
            mp.setStopTime(music.getDuration());
            mp.setAutoPlay(true);
        } catch (Exception e) {
            System.err.println("Error playing sound sequence: " + movementFile);
            return false;
        }
        return true;
    }

    private static Media getMedia(String filename) {
        if (mediaCache.containsKey(filename)) {
            return mediaCache.get(filename);
        }
        var url = SoundManager.class.getResource("/sounds/" + filename);
        if (url == null) {
            return null;
        }
        Media media = new Media(url.toExternalForm());
        mediaCache.put(filename, media);
        return media;
    }

    private static void stopCurrent() {
        if (mp != null) {
            mp.setOnEndOfMedia(null);
            if (mp.getStatus() == MediaPlayer.Status.PLAYING) {
                mp.stop();
            }
            mp.dispose();
            mp = null;
        }
    }
}