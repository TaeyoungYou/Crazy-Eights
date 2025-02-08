package app.animation;

import javafx.animation.FadeTransition;
import javafx.scene.Cursor;
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
}
