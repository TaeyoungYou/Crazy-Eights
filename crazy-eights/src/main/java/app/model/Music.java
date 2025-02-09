package app.model;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class Music {
    private static MediaPlayer mediaPlayer;
    private static boolean volumeOn = true;


    private static void initMusic(){
        if(mediaPlayer != null) return;

        Media media = new Media(Music.class.getResource("/sound/music.mp3").toExternalForm());
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        mediaPlayer.setVolume(volumeOn ? 0.5 : 0.0);
    }

    public static void play(){
        initMusic();
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
