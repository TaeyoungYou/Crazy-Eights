package app.animation;

import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.scene.Cursor;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class AnimationCharacter {
    public void fadeInPane(StackPane pane){
        FadeTransition fadeIn = new FadeTransition(Duration.seconds(0.5), pane);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        fadeIn.play();
    }
    public Animation fadeOutPane(StackPane pane){
        FadeTransition fadeOut = new FadeTransition(Duration.seconds(0.5), pane);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);
        return fadeOut;
    }
    public void characterHoverAnimation(ImageView card){
        ScaleTransition cardUp = new ScaleTransition(Duration.millis(200), card);
        cardUp.setToX(1.1);
        cardUp.setToY(1.1);
        ScaleTransition cardDown = new ScaleTransition(Duration.millis(200), card);
        cardDown.setToX(1.0);
        cardDown.setToY(1.0);

        card.setOnMouseEntered(e -> {
            cardUp.playFromStart();
        });
        card.setOnMouseExited(e -> {
            cardDown.playFromStart();
        });
    }
}
