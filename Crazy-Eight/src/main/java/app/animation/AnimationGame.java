package app.animation;

import app.controller.MenuController;
import app.controller.SinglePlayGameController;
import app.model.Player;
import javafx.animation.*;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.util.List;

/**
 * Handles various animations for the game, such as card interactions,
 * hover effects, and scene transitions.
 */
public class AnimationGame {
    private double mouseOffsetX, mouseOffsetY;
    private double originCardX, originCardY;

    private final double DELETE_Y = 400;
    private final double DELETE_X = 480;

    /**
     * Scales up the card when hovered.
     *
     * @param card The ImageView representing the card.
     */
    public void cardHoverEffectScaleUp(ImageView card) {
        ScaleTransition scaleUp = new ScaleTransition(Duration.millis(100), card);
        scaleUp.setToX(1.1);
        scaleUp.setToY(1.1);
        scaleUp.playFromStart();
    }

    /**
     * Scales down the card when hover ends.
     *
     * @param card The ImageView representing the card.
     */
    public void cardHoverEffectScaleDown(ImageView card) {
        ScaleTransition scaleDown = new ScaleTransition(Duration.millis(100), card);
        scaleDown.setToX(1.0);
        scaleDown.setToY(1.0);
        scaleDown.playFromStart();
    }

    /**
     * Initializes card dragging by setting offset and lowering opacity.
     *
     * @param event The MouseEvent that triggered the drag.
     * @param card The ImageView being dragged.
     */
    public void cardDragPressed(MouseEvent event, ImageView card) {
        mouseOffsetX = event.getSceneX() - card.getLayoutX();
        mouseOffsetY = event.getSceneY() - card.getLayoutY();
        originCardX = card.getLayoutX();
        originCardY = card.getLayoutY();
        card.setOpacity(0.8);
        card.toFront();
    }

    /**
     * Moves the card while being dragged within the allowed bounds.
     *
     * @param event The MouseEvent that moves the card.
     * @param card The ImageView being dragged.
     */
    public void cardDragDragged(MouseEvent event, ImageView card){
        double newX = event.getSceneX() - mouseOffsetX;
        double newY = event.getSceneY() - mouseOffsetY;
        if (newX >= 0 && newX <= 865) {
            card.setLayoutX(newX);
        }
        if (newY >= 0 && newY <= 788) {
            card.setLayoutY(newY);
        }
    }

    /**
     * Determines whether the card should fade out upon release or remain.
     *
     * @param event The MouseEvent that triggered the release.
     * @param card The ImageView being dragged.
     * @return A FadeTransition animation if the card should fade out, otherwise null.
     */
    public Animation cardDragReleased(MouseEvent event, ImageView card){
        card.setOpacity(1.0);
        if (card.getLayoutY() < DELETE_Y && card.getLayoutX() > DELETE_X) {
            FadeTransition fadeOut = new FadeTransition(Duration.millis(500), card);
            fadeOut.setToValue(0.0);
            return fadeOut;
        }
        return null;
    }

    /**
     * Moves the card back to its original position when dropped outside a valid area.
     *
     * @param card The ImageView being moved.
     * @param cardPlace The pane containing the card.
     */
    public void cardMoveBackEffect(ImageView card, Pane cardPlace) {
        TranslateTransition moveBack = new TranslateTransition(Duration.millis(200), card);
        moveBack.setToX(originCardX - card.getLayoutX());
        moveBack.setToY(originCardY - card.getLayoutY());
        moveBack.setOnFinished(e -> {
            card.setLayoutX(originCardX);
            card.setLayoutY(originCardY);
        });
        moveBack.play();
    }

    /**
     * Fades in all children of the given pane.
     *
     * @param pane The pane whose children should fade in.
     */
    public void fadeInSinglePlay(Pane pane){
        ParallelTransition parallelFadeIn = new ParallelTransition();
        addFadeIn(pane, parallelFadeIn);
        parallelFadeIn.play();
    }

    private void addFadeIn(Pane pane, ParallelTransition parallelFadeIn){
        for(Node node: pane.getChildren()){
            FadeTransition fadeIn = new FadeTransition(Duration.seconds(1.5), node);
            fadeIn.setFromValue(0.0);
            fadeIn.setToValue(1.0);
            parallelFadeIn.getChildren().add(fadeIn);
        }
    }

    /**
     * Fades out all children of the given pane and transitions back to the main menu.
     *
     * @param scene The current scene.
     * @param pane The pane to fade out.
     */
    public void fadeOutSinglePlay(Scene scene, Pane pane){
        ParallelTransition parallelFadeOut = new ParallelTransition();
        addFadeOut(pane, parallelFadeOut);
        parallelFadeOut.play();
        parallelFadeOut.setOnFinished(e -> {
            MenuController menuController = new MenuController(scene);
            menuController.drawMenu();
        });
    }

    private void addFadeOut(Pane pane, ParallelTransition parallelFadeOut){
        for(Node node: pane.getChildren()){
            FadeTransition fadeOut = new FadeTransition(Duration.seconds(1.5), node);
            fadeOut.setFromValue(1.0);
            fadeOut.setToValue(0.0);
            parallelFadeOut.getChildren().add(fadeOut);
        }
    }
}
