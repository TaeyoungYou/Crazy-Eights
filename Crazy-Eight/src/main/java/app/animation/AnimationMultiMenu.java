package app.animation;

import javafx.animation.FadeTransition;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class AnimationMultiMenu {
    public void fadeInMultiMenu(StackPane pane) {
        FadeTransition fadeIn = new FadeTransition(Duration.seconds(0.5), pane);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        fadeIn.play();
    }

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
}
