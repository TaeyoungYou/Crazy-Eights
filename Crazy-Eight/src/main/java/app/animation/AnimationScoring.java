package app.animation;

import javafx.animation.FadeTransition;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

/**
 * The AnimationScoring class provides utility methods for applying various
 * animation effects to JavaFX UI components, such as fade-in and fade-out
 * transitions and button hover animations.
 */
public class AnimationScoring {
    public void fadeInScoring(StackPane pane) {
        FadeTransition fadeIn = new FadeTransition(Duration.seconds(0.5), pane);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        fadeIn.play();
    }

    /**
     * Applies a fade-out animation to a specified pane and removes it from the root layout
     * upon the completion of the animation.
     *
     * @param root the parent StackPane from which the pane will be removed after the animation
     * @param pane the StackPane to which the fade-out effect will be applied
     */
    public void fadeOutScoring(StackPane root, StackPane pane) {
        FadeTransition fadeOut = new FadeTransition(Duration.seconds(0.5), pane);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);
        fadeOut.play();
        fadeOut.setOnFinished(e -> root.getChildren().remove(pane));
    }

    /**
     * Adds hover animations to a button-like label, creating a fade-out effect
     * when the mouse enters the button and a fade-in effect when the mouse exits.
     * The cursor also changes to a hand on hover and reverts to default when not hovering.
     *
     * @param button the Label component to apply the hover animation
     */
    public void buttonAnimation(Label button) {
        FadeTransition fadeOut = new FadeTransition(Duration.millis(200), button);
        FadeTransition fadeIn = new FadeTransition(Duration.millis(200), button);

        fadeOut.setToValue(0.7);
        fadeIn.setToValue(1.0);

        button.setOnMouseEntered(e -> {
            button.setCursor(Cursor.HAND);
            fadeOut.play();
        });
        button.setOnMouseExited(e -> {
            button.setCursor(Cursor.DEFAULT);
            fadeIn.play();
        });
    }
}
