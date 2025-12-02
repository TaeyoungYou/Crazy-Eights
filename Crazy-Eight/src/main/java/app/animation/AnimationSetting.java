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

        pane.setOnMouseEntered(new MouseEnteredHandler(pane, fadeIn));
        pane.setOnMouseExited(new MouseExitedHandler(fadeOut));
    }

    private static class MouseEnteredHandler implements javafx.event.EventHandler<javafx.scene.input.MouseEvent> {
        private final VBox pane;
        private final FadeTransition fadeIn;

        MouseEnteredHandler(VBox pane, FadeTransition fadeIn) {
            this.pane = pane;
            this.fadeIn = fadeIn;
        }

        @Override
        public void handle(javafx.scene.input.MouseEvent event) {
            pane.setCursor(Cursor.DEFAULT);
            fadeIn.playFromStart();
        }
    }

    private static class MouseExitedHandler implements javafx.event.EventHandler<javafx.scene.input.MouseEvent> {
        private final FadeTransition fadeOut;

        MouseExitedHandler(FadeTransition fadeOut) {
            this.fadeOut = fadeOut;
        }

        @Override
        public void handle(javafx.scene.input.MouseEvent event) {
            fadeOut.playFromStart();
        }
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
        fadeOut.setOnFinished(new FadeOutFinishedHandler(root, pane));
    }

    private static class FadeOutFinishedHandler implements javafx.event.EventHandler<javafx.event.ActionEvent> {
        private final StackPane root;
        private final StackPane pane;

        FadeOutFinishedHandler(StackPane root, StackPane pane) {
            this.root = root;
            this.pane = pane;
        }

        @Override
        public void handle(javafx.event.ActionEvent event) {
            root.getChildren().remove(pane);
        }
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

        button.setOnMouseEntered(new ButtonMouseEnteredHandler(button, fadeOut));
        button.setOnMouseExited(new ButtonMouseExitedHandler(button, fadeIn));
    }

    private static class ButtonMouseEnteredHandler implements javafx.event.EventHandler<javafx.scene.input.MouseEvent> {
        private final Label button;
        private final FadeTransition fadeOut;

        ButtonMouseEnteredHandler(Label button, FadeTransition fadeOut) {
            this.button = button;
            this.fadeOut = fadeOut;
        }

        @Override
        public void handle(javafx.scene.input.MouseEvent event) {
            button.setCursor(Cursor.HAND);
            fadeOut.play();
        }
    }

    private static class ButtonMouseExitedHandler implements javafx.event.EventHandler<javafx.scene.input.MouseEvent> {
        private final Label button;
        private final FadeTransition fadeIn;

        ButtonMouseExitedHandler(Label button, FadeTransition fadeIn) {
            this.button = button;
            this.fadeIn = fadeIn;
        }

        @Override
        public void handle(javafx.scene.input.MouseEvent event) {
            button.setCursor(Cursor.DEFAULT);
            fadeIn.play();
        }
    }
}
