package app.animation;

import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

/**
 * The {@code AnimationCharacter} class provides various animation effects for UI elements in a JavaFX application.
 * It includes fade-in and fade-out transitions for panes and hover animations for character image views.
 */
public class AnimationCharacter {

    /**
     * Applies a fade-in effect to the specified StackPane.
     *
     * @param pane The StackPane to apply the fade-in effect to.
     */
    public void fadeInPane(StackPane pane){
        FadeTransition fadeIn = new FadeTransition(Duration.seconds(0.5), pane);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        fadeIn.play();
    }

    /**
     * Creates and returns a fade-out animation for the specified StackPane.
     *
     * @param pane The StackPane to apply the fade-out effect to.
     * @return A {@code FadeTransition} animation that fades the pane out.
     */
    public Animation fadeOutPane(StackPane pane){
        FadeTransition fadeOut = new FadeTransition(Duration.seconds(0.5), pane);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);
        return fadeOut;
    }

    /**
     * Adds a hover animation effect to the specified ImageView.
     * The image scales up when hovered over and returns to its original size when the mouse exits.
     *
     * @param card The ImageView representing the character card.
     */
    public void characterHoverAnimation(ImageView card){
        ScaleTransition cardUp = new ScaleTransition(Duration.millis(200), card);
        cardUp.setToX(1.1);
        cardUp.setToY(1.1);
        ScaleTransition cardDown = new ScaleTransition(Duration.millis(200), card);
        cardDown.setToX(1.0);
        cardDown.setToY(1.0);

        card.setOnMouseEntered(e -> {
            cardUp.playFromStart();
        });
        card.setOnMouseExited(e -> {
            cardDown.playFromStart();
        });
    }
}
