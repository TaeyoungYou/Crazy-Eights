package app.animation;

import javafx.animation.*;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class AnimationMenu {

    public void menuAnimation(VBox centerPane, Node[] nodes, Label title){
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
        });


        // Menu Fade-In animation
        ParallelTransition buttons_parallel = new ParallelTransition();
        for(int i=0; i<nodes.length; i++){
            Node node = nodes[i];
            if(node instanceof Label) {
                FadeTransition menuFadeIn = new FadeTransition(Duration.seconds(2), node);
                menuFadeIn.setFromValue(0);
                menuFadeIn.setToValue(1);
                buttons_parallel.getChildren().add(menuFadeIn);
            } else {
                HBox hBox = (HBox)node;
                for(int j=0; j<hBox.getChildren().size(); ++j){
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

    public void menuHover(Node[] nodes) {
        for (Node node : nodes) {
            applyScaleAnimation(node);

            if (node instanceof HBox) {
                HBox hBox = (HBox)node;
                for (Node child : hBox.getChildren()) {
                    applyScaleAnimation(child);
                }
            }
        }
    }

    private void applyScaleAnimation(Node node) {
        if (node instanceof Label) {
            Label button = (Label)node;
            FadeTransition fadeOut = new FadeTransition(Duration.millis(200), button);
            FadeTransition fadeIn = new FadeTransition(Duration.millis(200), button);

            fadeOut.setToValue(0.7);
            fadeIn.setToValue(1.0);

            button.setOnMouseEntered(event -> {
                fadeOut.playFromStart();
                button.setCursor(Cursor.HAND);
            });
            button.setOnMouseExited(event -> fadeIn.playFromStart());
        }
    }

}
