package app.animation;

import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class AnimationGame {
    private double mouseOffsetX, mouseOffsetY;
    private double originCardX, originCardY;
    private int index;

    public void cardAnimation(ImageView card, Pane cardPlace){
        cardHoverEffect(card);
        cardDragEffect(card, cardPlace);
    }

    private void cardHoverEffect(ImageView card){
        ScaleTransition scaleUp = new ScaleTransition(Duration.millis(200), card);
        scaleUp.setToX(1.1);
        scaleUp.setToY(1.1);

        ScaleTransition scaleDown = new ScaleTransition(Duration.millis(200), card);
        scaleDown.setToX(1.0);
        scaleDown.setToY(1.0);

        card.setOnMouseEntered(e -> {
            scaleUp.playFromStart();
        });
        card.setOnMouseExited(e -> {
            scaleDown.playFromStart();
        });
    }

    private void cardDragEffect(ImageView card, Pane cardPlace){
        card.setOnMousePressed(e -> {
            mouseOffsetX = e.getSceneX() - card.getLayoutX();
            mouseOffsetY = e.getSceneY() - card.getLayoutY();
            originCardX = card.getLayoutX();
            originCardY = card.getLayoutY();
            index = cardPlace.getChildren().indexOf(card);
            card.setOpacity(0.8);
            card.toFront();
        });
        card.setOnMouseDragged(e -> {
            double newX = e.getSceneX() - mouseOffsetX;
            double newY = e.getSceneY() - mouseOffsetY;
            if(newX >= 0 && newX <= 865){
                card.setLayoutX(newX);
            }
            if(newY >= 0 && newY <= 1080){
                card.setLayoutY(newY);
            }
        });
        card.setOnMouseReleased(e -> {
            card.setOpacity(1.0);
            cardMoveBackEffect(card, cardPlace);
            cardPlace.getChildren().remove(card);
            cardPlace.getChildren().add(index, card);
        });
    }

    private void cardMoveBackEffect(ImageView card, Pane cardPlace){
        TranslateTransition moveBack = new TranslateTransition(Duration.millis(200), card);
        moveBack.setFromX(0);
        moveBack.setToX(0);
        moveBack.setToX(originCardX - card.getLayoutX());
        moveBack.setToY(originCardY - card.getLayoutY());
        moveBack.setOnFinished(e->{
            card.setLayoutX(originCardX);
            card.setLayoutY(originCardY);
            card.setTranslateX(0);
            card.setTranslateY(0);
        });
        moveBack.play();
    }
}
