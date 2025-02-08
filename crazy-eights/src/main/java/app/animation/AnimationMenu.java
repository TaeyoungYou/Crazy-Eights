package app.animation;

import app.ui.Music;
import app.ui.SinglePlayGame;
import javafx.animation.*;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

/**
 * Handles various animations for the main menu, such as title scaling, button hover effects,
 * and menu transition animations.
 */
public class AnimationMenu {

    /**
     * Applies animations to the menu title and buttons.
     * The title scales up and down continuously, and buttons fade in and out on hover.
     *
     * @param title   The menu title label to animate.
     * @param buttons An array of button labels that will have hover effects.
     */
    public void menuAnimation(Label title, Label[] buttons) {
        ScaleTransition scaleUpAndDown = new ScaleTransition(Duration.seconds(2), title);
        scaleUpAndDown.setToX(1.1);
        scaleUpAndDown.setToY(1.1);
        scaleUpAndDown.setAutoReverse(true);
        scaleUpAndDown.setCycleCount(Animation.INDEFINITE);
        scaleUpAndDown.play();

        for (Label button : buttons) {
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

    /**
     * Applies an animated sequence to the main menu, including title fade-in, movement, and button animations.
     * The animation consists of:
     * - A pause before animations begin.
     * - A fade-in effect for the title.
     * - A translation effect that moves the title upward.
     * - A fade-in effect for menu buttons.
     * - A continuous scaling effect for the title.
     *
     * @param centerPane The VBox containing the menu elements.
     * @param nodes      The menu buttons and interactive elements to animate.
     * @param title      The menu title label to animate.
     */
    public void startMenuAnimation(VBox centerPane, Node[] nodes, Label title) {
        // 2 seconds pause
        PauseTransition pause = new PauseTransition();
        pause.setDuration(Duration.seconds(2));

        // Title Fade-In animation
        FadeTransition titleFadeIn = new FadeTransition();
        titleFadeIn.setDuration(Duration.seconds(4));
        titleFadeIn.setNode(centerPane.getChildren().get(0));
        titleFadeIn.setFromValue(0);
        titleFadeIn.setToValue(1);

        // Title movement to Up
        TranslateTransition titleMoveUp = new TranslateTransition();
        titleMoveUp.setNode(centerPane.getChildren().get(0));
        titleMoveUp.setDuration(Duration.seconds(2));
        titleMoveUp.setByY(-144);
        titleMoveUp.setOnFinished(event -> {
            centerPane.getChildren().addAll(nodes);
            centerPane.getChildren().get(0).setTranslateY(0);
            Music.play();
        });


        // Menu Fade-In animation
        ParallelTransition buttons_parallel = new ParallelTransition();
        for (int i = 0; i < nodes.length; i++) {
            Node node = nodes[i];
            if (node instanceof Label) {
                FadeTransition menuFadeIn = new FadeTransition(Duration.seconds(2), node);
                menuFadeIn.setFromValue(0);
                menuFadeIn.setToValue(1);
                buttons_parallel.getChildren().add(menuFadeIn);
            } else {
                HBox hBox = (HBox) node;
                for (int j = 0; j < hBox.getChildren().size(); ++j) {
                    FadeTransition menuFadeIn = new FadeTransition(Duration.seconds(3), hBox.getChildren().get(j));
                    menuFadeIn.setFromValue(0);
                    menuFadeIn.setToValue(1);
                    buttons_parallel.getChildren().add(menuFadeIn);
                }
            }
        }

        ScaleTransition scaleUpAndDown = new ScaleTransition(Duration.seconds(2), title);
        scaleUpAndDown.setToX(1.1);
        scaleUpAndDown.setToY(1.1);
        scaleUpAndDown.setAutoReverse(true);
        scaleUpAndDown.setCycleCount(Animation.INDEFINITE);
        scaleUpAndDown.play();

        buttons_parallel.getChildren().add(scaleUpAndDown);

        SequentialTransition sequence = new SequentialTransition(
                pause,
                titleFadeIn,
                titleMoveUp,
                buttons_parallel
        );

        sequence.play();
    }

    /**
     * Applies a hover effect to menu nodes by adjusting their fade animation.
     * If a node is an HBox, the effect is applied to all its child nodes as well.
     *
     * @param nodes The array of menu elements to apply the hover effect to.
     */
    public void menuHover(Node[] nodes) {
        for (Node node : nodes) {
            applyFadeAnimation(node);

            if (node instanceof HBox) {
                HBox hBox = (HBox) node;
                for (Node child : hBox.getChildren()) {
                    applyFadeAnimation(child);
                }
            }
        }
    }

    /**
     * Applies a fade animation effect to a label when hovered.
     * The label fades out when the mouse enters and fades back in when the mouse exits.
     *
     * @param node The UI element to apply the fade animation to (only applies if it's a Label).
     */
    private void applyFadeAnimation(Node node) {
        if (node instanceof Label) {
            Label button = (Label) node;
            FadeTransition fadeOut = new FadeTransition(Duration.millis(200), button);
            FadeTransition fadeIn = new FadeTransition(Duration.millis(200), button);

            fadeOut.setToValue(0.7);
            fadeIn.setToValue(1.0);

            button.setOnMouseEntered(event -> {
                fadeOut.playFromStart();
                button.setCursor(Cursor.HAND);
            });
            button.setOnMouseExited(event -> {
                fadeIn.playFromStart();
                button.setCursor(Cursor.DEFAULT);
            });
        }
    }

    public void fadeOutMainMenu(Scene scene, VBox pane) {
        ParallelTransition fadeOutParallel = new ParallelTransition();
        for(Node node : pane.getChildren()) {
            FadeTransition fadeOut = new FadeTransition(Duration.seconds(1.5), node);
            fadeOut.setFromValue(1.0);
            fadeOut.setToValue(0.0);
            fadeOutParallel.getChildren().add(fadeOut);
        }
        fadeOutParallel.play();
        fadeOutParallel.setOnFinished(event -> {
            SinglePlayGame game = new SinglePlayGame(scene);
            game.generate();
        });
    }

    public void fadeInMainMenu(VBox pane) {
        ParallelTransition fadeOutParallel = new ParallelTransition();
        for(Node node : pane.getChildren()) {
            FadeTransition fadeOut = new FadeTransition(Duration.seconds(1.5), node);
            fadeOut.setFromValue(0.0);
            fadeOut.setToValue(1.0);
            fadeOutParallel.getChildren().add(fadeOut);
        }
        fadeOutParallel.play();
    }
}
