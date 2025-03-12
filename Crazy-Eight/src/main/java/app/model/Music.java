package app.model;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class Music {
    private static Music music = null;
    private static MediaPlayer mediaPlayer;
    private static double volume = 0.5;

    private Music(){}

    public static Music getInstance(){
        if(music == null){
            music = new Music();
        }
        return music;
    }

    private static void initMusic(){
        if(mediaPlayer != null) return;

        Media media = new Media(Music.class.getResource("/sound/music.mp3").toExternalForm());
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        mediaPlayer.setVolume(volume);
    }

    public static void play(){
        initMusic();
        mediaPlayer.play();
    }

    public static void setVolume(double value){
        volume = value;
        mediaPlayer.setVolume(value);
    }

    public static double getVolume(){
        return volume;
    }

}
