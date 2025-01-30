package app.animation;

import com.sun.javafx.fxml.ParseTraceElement;
import javafx.animation.*;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.sql.SQLOutput;

public class AnimationMenu {

    public void menuAnimation(VBox centerPane, Node[] nodes){
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
        titleMoveUp.setByY(-140);
        titleMoveUp.setOnFinished(event -> {
            centerPane.getChildren().addAll(nodes);
            centerPane.getChildren().get(0).setTranslateY(0);
        });


        // Menu Fade-In animation
        ParallelTransition buttons_parallel = new ParallelTransition();
        for(int i=0; i<nodes.length; i++){
            Node node = nodes[i];
            if(node instanceof Button) {
                FadeTransition menuFadeIn = new FadeTransition(Duration.seconds(3), node);
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

        // background color changing animation

        SequentialTransition sequence = new SequentialTransition(
                pause,
                titleFadeIn,
                titleMoveUp,
                buttons_parallel
        );

        sequence.play();
    }
}
