package app.animation;

import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

/**
 * The AnimationMultiMenu class provides utilities for animating menus and buttons
 * within a JavaFX application. It allows for creating fade-in and fade-out animations
 * and handling mouse interactions to alter opacity effect dynamically.
 */
public class AnimationMultiMenu {
    public void fadeInMultiMenu(StackPane pane) {
        FadeTransition fadeIn = new FadeTransition(Duration.seconds(0.5), pane);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        fadeIn.play();
    }

    /**
     * Adjusts the opacity and sets up animations for fade-in and fade-out transitions when the mouse
     * enters or exits a given HBox. It applies a smoother user interaction effect by modifying the
     * opacity using fade transitions.
     *
     * @param pane The HBox component for which the mouse hover animations are to be applied.
     */
    public void mouseInOutMultiMenu(HBox pane) {
        pane.setOpacity(0.6);

        FadeTransition fadeIn = new FadeTransition(Duration.millis(300), pane);
        fadeIn.setToValue(1.0);

        FadeTransition fadeOut = new FadeTransition(Duration.millis(300), pane);
        fadeOut.setToValue(0.6);

        pane.setOnMouseEntered(new AnimationMultiMenu.MouseEnteredHandler(pane, fadeIn));
        pane.setOnMouseExited(new AnimationMultiMenu.MouseExitedHandler(fadeOut));
    }

    private static class MouseEnteredHandler implements javafx.event.EventHandler<javafx.scene.input.MouseEvent> {
        private final HBox pane;
        private final FadeTransition fadeIn;

        MouseEnteredHandler(HBox pane, FadeTransition fadeIn) {
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
     * Configures fade-in and fade-out animations for a button when hovered over by the mouse.
     * This method sets up visual feedback by modifying the button's opacity
     * through fade transitions on mouse enter and exit events.
     *
     * @param button The Label component on which the hover animations will be applied.
     */
    public void buttonAnimation(Label button) {
        FadeTransition fadeOut = new FadeTransition(Duration.millis(200), button);
        FadeTransition fadeIn = new FadeTransition(Duration.millis(200), button);

        fadeOut.setToValue(0.7);
        fadeIn.setToValue(1.0);

        button.setOnMouseEntered(new AnimationMultiMenu.ButtonMouseEnteredHandler(button, fadeOut));
        button.setOnMouseExited(new AnimationMultiMenu.ButtonMouseExitedHandler(button, fadeIn));
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

    /**
     * Plays a fade-out animation on the specified pane, transitioning its opacity
     * from fully visible to fully transparent over a duration of 0.5 seconds.
     * Once the fade-out animation has completed, the pane is removed from the root's
     * children by invoking the FadeOutFinishedHandler.
     *
     * @param root The parent StackPane that contains the pane to be faded out.
     * @param pane The StackPane to apply the fade-out animation and remove upon completion.
     */
    public void fadeOutMultiMenu(StackPane root, StackPane pane) {
        FadeTransition fadeOut = new FadeTransition(Duration.seconds(0.5), pane);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);
        fadeOut.play();
        fadeOut.setOnFinished(new AnimationMultiMenu.FadeOutFinishedHandler(root, pane));
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
     * Plays a fade-out animation on the specified {@code StackPane}, transitioning its
     * opacity from fully visible to fully transparent over a duration of 0.5 seconds.
     *
     * @param pane The {@code StackPane} to which the fade-out animation will be applied.
     * @return The {@code Animation} object representing the fade-out transition.
     */
    public Animation closeMultiMenu(StackPane pane) {
        FadeTransition fadeOut = new FadeTransition(Duration.seconds(0.5), pane);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);
        return fadeOut;
    }
}
