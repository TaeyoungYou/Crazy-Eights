package app.animation;

import javafx.animation.FadeTransition;
import javafx.scene.Cursor;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class AnimationSetting {
    public void mouseInOutSetting(VBox pane){
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
    public void fadeInSetting(StackPane pane){
        FadeTransition fadeIn = new FadeTransition(Duration.seconds(0.5), pane);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        fadeIn.play();
    }
    public void fadeOutSetting(StackPane root, StackPane pane){
        FadeTransition fadeOut = new FadeTransition(Duration.seconds(0.5), pane);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);
        fadeOut.play();
        fadeOut.setOnFinished(e -> root.getChildren().remove(pane));
    }
}
