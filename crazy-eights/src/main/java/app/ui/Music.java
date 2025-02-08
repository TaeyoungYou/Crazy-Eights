package app.ui;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class Music {
    private final static Media media = new Media(Music.class.getResource("/sound/music.mp3").toExternalForm());
    private final static MediaPlayer mediaPlayer = new MediaPlayer(media);
    private static boolean volumeOn;

    public Music() {
        volumeOn = true;
    }

    public static void play(){
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        mediaPlayer.setVolume(0.5);

        mediaPlayer.play();
    }

    public static void volumeOn(){
        volumeOn = true;
        mediaPlayer.setVolume(0.5);
    }
    public static void volumeOff(){
        volumeOn = false;
        mediaPlayer.setVolume(0.0);
    }
    public static boolean isVolumeOn(){
        return volumeOn;
    }
}
