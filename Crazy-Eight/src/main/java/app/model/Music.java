package app.model;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

/**
 * The Music class is responsible for managing background music in the application.
 * It implements a singleton pattern to ensure that only one instance of the Music class is created and used.
 * The class allows playing music, adjusting volume, and retrieving the current volume level.
 */
public class Music {
    private static Music music = null;
    private static MediaPlayer mediaPlayer;
    private static double volume = 0.5;

    /**
     * Private constructor for the Music class.
     * This constructor is part of the singleton implementation,
     * ensuring that the class cannot be instantiated externally.
     */
    private Music(){}

    /**
     * Initializes the media player for background music, ensuring that the media player is created only once.
     * This method sets up the music file, configures it to play in a loop, and applies the current volume level.
     * If the media player is already initialized, the method exits without performing any actions.
     *
     * Behavior:
     * - Creates a new instance of Media and MediaPlayer if not already initialized.
     * - Loads the music file located at "/sound/music.mp3".
     * - Sets up the media player to repeat the music indefinitely.
     * - Applies the current volume level as specified by the volume variable.
     */
    private static void initMusic(){
        if(mediaPlayer != null) return;

        Media media = new Media(Music.class.getResource("/sound/music.mp3").toExternalForm());
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        mediaPlayer.setVolume(volume);
    }

    /**
     * Plays the background music by initializing the media player if it is not already
     * created and starting the playback. This method ensures that the media player is
     * properly configured before starting the music.
     *
     * Behavior:
     * - Calls the initMusic() method to initialize the media player if it is not initialized.
     * - Starts playback of the music using the media player.
     */
    public static void play(){
        initMusic();
        mediaPlayer.play();
    }

    /**
     * Adjusts the volume for the background music being played by the media player.
     * The method sets the internal volume value and applies it to the media player's current volume.
     *
     * @param value the desired volume level, represented as a double. The value should
     *              typically range between 0.0 (mute) and 1.0 (maximum volume).
     */
    public static void setVolume(double value){
        volume = value;
        mediaPlayer.setVolume(value);
    }

    /**
     * Retrieves the current volume level of the background music.
     * The volume is represented as a double value, typically ranging
     * between 0.0 (mute) and 1.0 (maximum volume).
     *
     * @return the current volume level as a double
     */
    public static double getVolume(){
        return volume;
    }

}
