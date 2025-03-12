package app.animation;

import javafx.animation.FadeTransition;
import javafx.scene.Cursor;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class AnimationScoring {
    public void fadeInScoring(StackPane pane){
        FadeTransition fadeIn = new FadeTransition(Duration.seconds(0.5), pane);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        fadeIn.play();
    }
    public void fadeOutScoring(StackPane root, StackPane pane){
        FadeTransition fadeOut = new FadeTransition(Duration.seconds(0.5), pane);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);
        fadeOut.play();
        fadeOut.setOnFinished(e -> root.getChildren().remove(pane));
    }
}
