package app.animation;

import javafx.animation.FadeTransition;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

/**
 * The {@code AnimationSetting} class provides animations for UI components
 * related to settings, including fade transitions and hover effects.
 */
public class AnimationSetting {

    /**
     * Applies a fade-in and fade-out effect when the mouse enters and exits the given VBox.
     *
     * @param pane The VBox to apply the mouse hover effect to.
     */
    public void mouseInOutSetting(VBox pane) {
        pane.setOpacity(0.6);

        FadeTransition fadeIn = new FadeTransition(Duration.millis(300), pane);
        fadeIn.setToValue(1.0);

        FadeTransition fadeOut = new FadeTransition(Duration.millis(300), pane);
        fadeOut.setToValue(0.6);

        pane.setOnMouseEntered(e -> {
            pane.setCursor(Cursor.DEFAULT);
            fadeIn.playFromStart();
        });
        pane.setOnMouseExited(e -> {
            fadeOut.playFromStart();
        });
    }

    /**
     * Applies a fade-in animation to the specified StackPane.
     *
     * @param pane The StackPane to fade in.
     */
    public void fadeInSetting(StackPane pane) {
        FadeTransition fadeIn = new FadeTransition(Duration.seconds(0.5), pane);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        fadeIn.play();
    }

    /**
     * Applies a fade-out animation to the specified StackPane and removes it from the root.
     *
     * @param root The root StackPane containing the pane.
     * @param pane The StackPane to fade out and remove.
     */
    public void fadeOutSetting(StackPane root, StackPane pane) {
        FadeTransition fadeOut = new FadeTransition(Duration.seconds(0.5), pane);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);
        fadeOut.play();
        fadeOut.setOnFinished(e -> root.getChildren().remove(pane));
    }

    /**
     * Adds a hover animation effect to a button, making it fade slightly when hovered.
     *
     * @param button The Label representing the button.
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
