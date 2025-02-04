package app.animation;

import app.style.StyleGame;
import app.ui.Msg;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.util.List;

/**
 * Handles various animations for the game, such as card interactions,
 * hover effects, and transitions.
 */
public class AnimationGame {
    private final StyleGame style = new StyleGame();
    private double mouseOffsetX, mouseOffsetY;
    private double originCardX, originCardY;
    private int index;

    private final double DELETE_Y = 400;
    private final double DELETE_X = 500;

    /**
     * Applies animations to a card, including hover and drag effects.
     *
     * @param card      The ImageView representing the card.
     * @param cardPlace The AnchorPane where the card is displayed.
     * @param cards     The list of cards currently in play.
     */
    public void cardAnimation(ImageView card, AnchorPane cardPlace, List<ImageView> cards) {
        cardHoverEffect(card);
        cardDragEffect(card, cardPlace, cards);
    }

    /**
     * Applies a hover effect to a card by scaling it up when the mouse enters
     * and scaling it back down when the mouse exits.
     *
     * @param card The ImageView representing the card.
     */
    private void cardHoverEffect(ImageView card) {
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

    /**
     * Applies a drag-and-drop effect to a card, allowing it to be moved within the play area.
     * If the card is released in a designated delete area, it fades out and is removed.
     * Otherwise, it returns to its original position.
     *
     * @param card      The ImageView representing the card.
     * @param cardPlace The AnchorPane where the card is displayed.
     * @param cards     The list of cards currently in play.
     */
    private void cardDragEffect(ImageView card, AnchorPane cardPlace, List<ImageView> cards) {
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
            if (newX >= 0 && newX <= 865) {
                card.setLayoutX(newX);
            }
            if (newY >= 0 && newY <= 1080) {
                card.setLayoutY(newY);
            }
        });
        card.setOnMouseReleased(e -> {
            card.setOpacity(1.0);

            if (card.getLayoutY() < DELETE_Y && card.getLayoutX() > DELETE_X) {
                FadeTransition fadeOut = new FadeTransition(Duration.millis(500), card);
                fadeOut.setToValue(0.0);
                fadeOut.setOnFinished(event -> {
                    cards.remove(card);
                    cardPlace.getChildren().remove(card);
                    resettingPosCard(cardPlace, cards);
                });
                fadeOut.play();
            } else {
                cardMoveBackEffect(card, cardPlace);
                cardPlace.getChildren().remove(card);
                cardPlace.getChildren().add(index, card);
            }
        });
    }

    /**
     * Resets the positions of the player's cards, aligning them in a structured manner.
     * Ensures that the cards are evenly spaced within the play area.
     *
     * @param cardPlace The AnchorPane containing the player's cards.
     * @param cards     The list of ImageView objects representing the cards.
     */
    private void resettingPosCard(AnchorPane cardPlace, List<ImageView> cards) {
        int i = 0;

        for (Node node : cardPlace.getChildren()) {
            if (node instanceof ImageView && cards.contains((ImageView) node)) {
                ImageView card = (ImageView) node;
                card.setLayoutX(i * 75);
                card.setLayoutY(1080 - 200);
                i++;
            }
        }
    }

    /**
     * Animates the card moving back to its original position if it was dragged but not placed in a valid area.
     *
     * @param card      The ImageView representing the card.
     * @param cardPlace The Pane where the card is displayed.
     */
    private void cardMoveBackEffect(ImageView card, Pane cardPlace) {
        TranslateTransition moveBack = new TranslateTransition(Duration.millis(200), card);
        moveBack.setFromX(0);
        moveBack.setToX(0);
        moveBack.setToX(originCardX - card.getLayoutX());
        moveBack.setToY(originCardY - card.getLayoutY());
        moveBack.setOnFinished(e -> {
            card.setLayoutX(originCardX);
            card.setLayoutY(originCardY);
            card.setTranslateX(0);
            card.setTranslateY(0);
        });
        moveBack.play();
    }

    /**
     * Applies a hover animation effect to a button, scaling it down slightly when hovered
     * and restoring its original size when the mouse exits.
     *
     * @param button The ImageView representing the button.
     */
    public void buttonAnimation(ImageView button) {
        ScaleTransition mouseOn = new ScaleTransition(Duration.millis(200), button);
        mouseOn.setToX(0.85);
        mouseOn.setToY(0.85);
        ScaleTransition mouseOff = new ScaleTransition(Duration.millis(200), button);
        mouseOff.setToX(1.0);
        mouseOff.setToY(1.0);
        button.setCursor(Cursor.HAND);

        button.setOnMouseEntered(e -> {
            mouseOn.playFromStart();
            button.setOpacity(0.8);
        });
        button.setOnMouseExited(e -> {
            mouseOff.playFromStart();
            button.setOpacity(1.0);
        });
    }

    /**
     * Applies a hover animation effect to the deck, slightly scaling it down when hovered
     * and restoring its original size when the mouse exits.
     *
     * @param deck The ImageView representing the deck.
     */
    public void deckHoverAnimation(ImageView deck) {
        ScaleTransition deckUp = new ScaleTransition(Duration.millis(200), deck);
        deckUp.setToX(1.0);
        deckUp.setToY(1.0);
        ScaleTransition deckDown = new ScaleTransition(Duration.millis(200), deck);
        deckDown.setToX(0.95);
        deckDown.setToY(0.95);

        deck.setOnMouseEntered(event -> {
            deck.setCursor(Cursor.HAND);
            deck.setOpacity(0.8);
            deckDown.playFromStart();
        });
        deck.setOnMouseExited(event -> {
            deck.setCursor(Cursor.DEFAULT);
            deck.setOpacity(1);
            deckUp.playFromStart();
        });
    }

    /**
     * Animates a message label by scaling it up and down in a loop.
     * After a set number of cycles, the message toggles between a status message and an error message.
     *
     * @param msgLbl The Label displaying the message.
     * @param msg    The Msg object containing the message content and status.
     */
    public void msgAnimation(Label msgLbl, Msg msg) {
        ScaleTransition scaleUpAndDown = new ScaleTransition(Duration.seconds(2), msgLbl);
        scaleUpAndDown.setToX(1.05);
        scaleUpAndDown.setToY(1.05);
        scaleUpAndDown.setAutoReverse(true);
        scaleUpAndDown.setCycleCount(6);

        scaleUpAndDown.setOnFinished(e -> {
            if (Msg.status.status == msg.getMsg_status()) {
                msg.setMsg_status(Msg.status.error);
                msg.setMsg("This is a error message");
                msgLbl.setText(msg.getMsg());
                msgLbl.setStyle(style.errorMsgStyle());
                msgAnimation(msgLbl, msg);
            } else {
                msg.setMsg_status(Msg.status.status);
                msg.setMsg("This is a status message");
                msgLbl.setText(msg.getMsg());
                msgLbl.setStyle(style.statusMsgStyle());
                msgAnimation(msgLbl, msg);
            }
        });
        scaleUpAndDown.play();
    }
}
