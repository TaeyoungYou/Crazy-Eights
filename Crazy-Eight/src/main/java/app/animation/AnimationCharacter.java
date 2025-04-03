package app.animation;

import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.event.EventHandler;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;


/**
 * The AnimationCharacter class provides animation effects for JavaFX components such as StackPane
 * and ImageView. It includes fade-in, fade-out, and hover effects to enhance user interaction and visual appeal.
 */
public class AnimationCharacter {


    /**
     * Applies a fade-in animation to the specified StackPane.
     * The animation gradually increases the pane's opacity from 0 to 1 over a duration of 0.5 seconds.
     *
     * @param pane The StackPane to apply the fade-in effect to.
     */
    public void fadeInPane(StackPane pane){
        FadeTransition fadeIn = new FadeTransition(Duration.seconds(0.5), pane);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        fadeIn.play();
    }


    /**
     * Creates a fade-out animation for the specified StackPane.
     * The animation reduces the opacity of the pane from 1.0 to 0.0 over*/
    public Animation fadeOutPane(StackPane pane){
        FadeTransition fadeOut = new FadeTransition(Duration.seconds(0.5), pane);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);
        return fadeOut;
    }


    /**
     * Adds a hover animation effect to the specified ImageView.
     * When the cursor hovers over the ImageView, it scales up slightly,
     * giving a zoom-in effect. When the cursor exits, it scales back to its original size.
     *
     * @param card The ImageView to which the hover animation will be applied.
     */
    public void characterHoverAnimation(ImageView card) {
        ScaleTransition cardUp = new ScaleTransition(Duration.millis(200), card);
        cardUp.setToX(1.1);
        cardUp.setToY(1.1);
        ScaleTransition cardDown = new ScaleTransition(Duration.millis(200), card);
        cardDown.setToX(1.0);
        cardDown.setToY(1.0);

        card.setOnMouseEntered(new OnMouseEnteredHandler(cardUp));
        card.setOnMouseExited(new OnMouseExitedHandler(cardDown));
    }

    private static class OnMouseEnteredHandler implements EventHandler<MouseEvent> {
        private final ScaleTransition transition;

        public OnMouseEnteredHandler(ScaleTransition transition) {
            this.transition = transition;
        }

        @Override
        public void handle(MouseEvent e) {
            transition.playFromStart();
        }
    }

    private static class OnMouseExitedHandler implements EventHandler<MouseEvent> {
        private final ScaleTransition transition;

        public OnMouseExitedHandler(ScaleTransition transition) {
            this.transition = transition;
        }

        @Override
        public void handle(MouseEvent e) {
            transition.playFromStart();
        }
    }
}
