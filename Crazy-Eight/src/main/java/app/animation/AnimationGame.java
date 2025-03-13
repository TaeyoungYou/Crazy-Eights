package app.animation;

import app.controller.MenuController;
import app.controller.SinglePlayGameController;
import app.model.Player;
import javafx.animation.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
 * hover effects, and transitions.
 */
public class AnimationGame {
    private double mouseOffsetX, mouseOffsetY;
    private double originCardX, originCardY;

    private final double DELETE_Y = 400;
    private final double DELETE_X = 480;

    /**
     * Applies a hover effect to a card by slightly scaling it up for a visual feedback.
     *
     * @param card The ImageView representing the card to which the hover effect should be applied.
     */
    public void cardHoverEffectScaleUp(ImageView card) {
        ScaleTransition scaleUp = new ScaleTransition(Duration.millis(100), card);
        scaleUp.setToX(1.1);
        scaleUp.setToY(1.1);
        scaleUp.playFromStart();
    }

    /**
     * Applies a hover effect to a card by scaling it down to its original size.
     *
     * @param card The ImageView representing the card to which the hover effect should be applied.
     */
    public void cardHoverEffectScaleDown(ImageView card) {
        ScaleTransition scaleDown = new ScaleTransition(Duration.millis(100), card);
        scaleDown.setToX(1.0);
        scaleDown.setToY(1.0);
        scaleDown.playFromStart();
    }

    /**
     * Handles the initial press action when a card is dragged.
     * Captures the mouse offset relative to the card's position and stores the original position of the card.
     * Slightly reduces the card's opacity and brings it to the front of the display.
     *
     * @param event The MouseEvent triggered when the card drag is initiated.
     * @param card  The ImageView representing the card being dragged.
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
     * Handles the action of dragging a card by updating its position relative to the current mouse location.
     * Ensures the card stays within the defined boundaries during the drag action.
     *
     * @param event The MouseEvent triggered during the dragging of the card.
     * @param card  The ImageView representing the card being dragged.
     */
    public void cardDragDragged(MouseEvent event, ImageView card) {
        double newX = event.getSceneX() - mouseOffsetX;
        double newY = event.getSceneY() - mouseOffsetY;
        if (newX >= 0 && newX <= 865) {
            card.setLayoutX(newX);
        }
        if (newY >= 0 && newY <= 1080 - 292) {
            card.setLayoutY(newY);
        }
    }

    /**
     * Handles the release action of a card after it has been dragged.
     * Restores the opacity of the card and optionally fades it out if released in a specified delete zone.
     *
     * @param event The MouseEvent generated when the card is released after dragging.
     * @param card  The ImageView representing the card that was being dragged.
     * @return An Animation object if the card is to be faded out, or null if no animation is applied.
     */
    public Animation cardDragReleased(MouseEvent event, ImageView card) {
        card.setOpacity(1.0);

        if (card.getLayoutY() < DELETE_Y && card.getLayoutX() > DELETE_X) {
            FadeTransition fadeOut = new FadeTransition(Duration.millis(500), card);
            fadeOut.setToValue(0.0);
            return fadeOut;
        }
        return null;
    }

    /**
     * Animates the card moving back to its original position if it was dragged but not placed in a valid area.
     *
     * @param card      The ImageView representing the card.
     * @param cardPlace The Pane where the card is displayed.
     */
    public void cardMoveBackEffect(ImageView card, Pane cardPlace) {
        TranslateTransition moveBack = new TranslateTransition(Duration.millis(200), card);
        moveBack.setFromX(0);
        moveBack.setToX(0);
        moveBack.setToX(originCardX - card.getLayoutX());
        moveBack.setToY(originCardY - card.getLayoutY());
        moveBack.setOnFinished(new MoveBackEventHandler(card));
        moveBack.play();
    }

    private class MoveBackEventHandler implements EventHandler<ActionEvent> {
        private final ImageView card;

        public MoveBackEventHandler(ImageView card) {
            this.card = card;
        }

        @Override
        public void handle(ActionEvent e) {
            card.setLayoutX(originCardX);
            card.setLayoutY(originCardY);
            card.setTranslateX(0);
            card.setTranslateY(0);
        }
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

        button.setOnMouseEntered(new MouseEnteredEventHandler(mouseOn, button));
        button.setOnMouseExited(new MouseExitedEventHandler(mouseOff, button));
    }

    private class MouseEnteredEventHandler implements EventHandler<MouseEvent> {
        private final ScaleTransition mouseOn;
        private final ImageView button;

        public MouseEnteredEventHandler(ScaleTransition mouseOn, ImageView button) {
            this.mouseOn = mouseOn;
            this.button = button;
        }

        @Override
        public void handle(MouseEvent e) {
            mouseOn.playFromStart();
            button.setOpacity(0.8);
        }
    }

    private class MouseExitedEventHandler implements EventHandler<MouseEvent> {
        private final ScaleTransition mouseOff;
        private final ImageView button;

        public MouseExitedEventHandler(ScaleTransition mouseOff, ImageView button) {
            this.mouseOff = mouseOff;
            this.button = button;
        }

        @Override
        public void handle(MouseEvent e) {
            mouseOff.playFromStart();
            button.setOpacity(1.0);
        }
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

        deck.setOnMouseEntered(new DeckMouseEnteredEventHandler(deck, deckDown));
        deck.setOnMouseExited(new DeckMouseExitedEventHandler(deck, deckUp));
    }

    private class DeckMouseEnteredEventHandler implements EventHandler<MouseEvent> {
        private final ImageView deck;
        private final ScaleTransition deckDown;

        public DeckMouseEnteredEventHandler(ImageView deck, ScaleTransition deckDown) {
            this.deck = deck;
            this.deckDown = deckDown;
        }

        @Override
        public void handle(MouseEvent event) {
            deck.setCursor(Cursor.HAND);
            deck.setOpacity(0.8);
            deckDown.playFromStart();
        }
    }

    private class DeckMouseExitedEventHandler implements EventHandler<MouseEvent> {
        private final ImageView deck;
        private final ScaleTransition deckUp;

        public DeckMouseExitedEventHandler(ImageView deck, ScaleTransition deckUp) {
            this.deck = deck;
            this.deckUp = deckUp;
        }

        @Override
        public void handle(MouseEvent event) {
            deck.setCursor(Cursor.DEFAULT);
            deck.setOpacity(1);
            deckUp.playFromStart();
        }
    }

    /**
     * Creates and returns an ImageView representing a card animation. The card's image
     * is set to a card back with specific dimensions, opacity, and position. The card
     * is added to the provided AnchorPane.
     *
     * @param deckPlace The AnchorPane where the card animation will be added.
     * @return An ImageView object representing the animated card.
     */
    public ImageView getCardAnimation(AnchorPane deckPlace) {
        ImageView card = new ImageView(new Image(getClass().getResource("/card/Card-Back.png").toExternalForm()));
        card.setFitWidth(220);
        card.setPreserveRatio(true);
        card.setOpacity(0.5);
        card.setLayoutX(300);
        card.setLayoutY(200);
        deckPlace.getChildren().add(card);

        return card;
    }

    /**
     * Creates and returns a translate animation for an ImageView representing a card.
     * The animation moves the card horizontally based on the specified count and vertically
     * to predefined coordinates.
     *
     * @param card  The ImageView representing the card to be translated.
     * @param count The index used to determine the horizontal translation distance for the card.
     * @return An Animation object representing the translation movement of the card.
     */
    public Animation getCardTranslateAnimation(ImageView card, int count) {
        TranslateTransition moveToHand = new TranslateTransition(Duration.millis(500), card);
        moveToHand.setToX((count - 4) * 75);
        moveToHand.setToY(1080 - 300 - 200);
        moveToHand.play();

        return moveToHand;
    }

    /**
     * Animates a card with combined translate and fade-in effects to create a smooth movement
     * and appearance effect, often used for transitioning a card to the player's view.
     *
     * @param card The ImageView representing the card to be animated.
     * @return An Animation object combining the translate and fade-in effects.
     */
    public Animation putCardTranslateWithPlayerAnimation(ImageView card) {
        TranslateTransition moveToDummy = new TranslateTransition(Duration.millis(500), card);
        moveToDummy.setFromX(-500);
        moveToDummy.setToX(300);

        FadeTransition fadeIn = new FadeTransition(Duration.millis(500), card);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);

        ParallelTransition pt = new ParallelTransition(moveToDummy, fadeIn);

        pt.play();

        return pt;
    }

    /**
     * Creates and plays a combined translate and fade-out animation for a card.
     * The card is translated to a specified position and fades out during the transition.
     *
     * @param card The ImageView representing the card to be animated.
     * @return An Animation object that combines the translate and fade-out effects.
     */
    public Animation getCardTranslateToPlayersAnimation(ImageView card) {
        TranslateTransition moveToPlayer = new TranslateTransition(Duration.millis(500), card);
        moveToPlayer.setToX(-500);

        FadeTransition fadeOut = new FadeTransition(Duration.millis(500), card);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);

        ParallelTransition pt = new ParallelTransition(moveToPlayer, fadeOut);

        pt.play();

        return pt;
    }

    /**
     * Adds a scaling animation effect to the specified label.
     * The label briefly scales up and then returns to its original size,
     * creating a visual feedback.
     *
     * @param left The Label to which the scaling animation effect is applied.
     */
    public void addLeftCardAnimation(Label left) {
        ScaleTransition scale = new ScaleTransition(Duration.millis(200), left);
        scale.setToX(1.2);
        scale.setToY(1.2);
        scale.setAutoReverse(true);
        scale.setCycleCount(2);
        scale.play();
    }


    /**
     * Plays a fade-in animation effect for all child nodes within the specified pane.
     * Each child node gradually becomes visible as part of a parallel transition.
     *
     * @param pane The Pane containing the nodes to apply the fade-in animation.
     */
    public void fadeInSinglePlay(Pane pane) {
        ParallelTransition parallelFadeIn = new ParallelTransition();
        addFadeIn(pane, parallelFadeIn);
        parallelFadeIn.play();
    }

    /**
     * Plays a fade-in animation effect for all child nodes within the specified pane.
     * Each child node gradually becomes visible as part of a parallel transition.
     *
     * @param pane           The Pane containing the nodes to apply the fade-in animation.
     * @param parallelFadeIn The ParallelTransition to which the fade-in animations for each node are added.
     */
    private void addFadeIn(Pane pane, ParallelTransition parallelFadeIn) {
        for (Node node : pane.getChildren()) {
            FadeTransition fadeIn = new FadeTransition(Duration.seconds(1.5), node);
            fadeIn.setFromValue(0.0);
            fadeIn.setToValue(1.0);
            parallelFadeIn.getChildren().add(fadeIn);
        }
    }

    /**
     * Plays a fade-out animation for all child nodes within the specified pane
     * and transitions to the main menu view upon the animation's completion.
     *
     * @param scene The current Scene where the animation and transition will take place.
     * @param pane  The Pane containing the nodes to which the fade-out animation will be applied.
     */
    public void fadeOutSinglePlay(Scene scene, Pane pane) {
        ParallelTransition parallelFadeOut = new ParallelTransition();
        addFadeOut(pane, parallelFadeOut);
        parallelFadeOut.play();
        parallelFadeOut.setOnFinished(new FadeOutSinglePlayEventHandler(scene));
    }

    private class FadeOutSinglePlayEventHandler implements EventHandler<ActionEvent> {
        private final Scene scene;

        public FadeOutSinglePlayEventHandler(Scene scene) {
            this.scene = scene;
        }

        @Override
        public void handle(ActionEvent e) {
            MenuController menuController = new MenuController(scene);
            menuController.drawMenu();
        }
    }

    /**
     * Adds a fade-out animation effect to all child nodes within the specified pane.
     * Each child node will gradually fade out to transparency as part of a parallel transition.
     *
     * @param pane            The Pane containing the nodes to apply the fade-out animation.
     * @param parallelFadeOut The ParallelTransition to which the fade-out animations for each node are added.
     */
    private void addFadeOut(Pane pane, ParallelTransition parallelFadeOut) {
        for (Node node : pane.getChildren()) {
            FadeTransition fadeOut = new FadeTransition(Duration.seconds(1.5), node);
            fadeOut.setFromValue(1.0);
            fadeOut.setToValue(0.0);
            parallelFadeOut.getChildren().add(fadeOut);
        }
    }

    /**
     * Plays a fade-out animation for all child nodes within the specified BorderPane,
     * and upon completion, either starts a new game or resets the game with the provided players.
     *
     * @param scene   The current Scene where the animation and game reset will take place.
     * @param pane    The BorderPane containing the nodes to which the fade-out animation will be applied.
     * @param players The list of players participating in the game. Can be null to start a new game.
     */
    public void resetFadeOutGame(Scene scene, BorderPane pane, List<Player> players) {
        ParallelTransition fadeOutParallel = new ParallelTransition();
        for (Node node : pane.getChildren()) {
            FadeTransition fadeOut = new FadeTransition(Duration.seconds(1.5), node);
            fadeOut.setFromValue(1.0);
            fadeOut.setToValue(0.0);
            fadeOutParallel.getChildren().add(fadeOut);
        }
        fadeOutParallel.play();
        fadeOutParallel.setOnFinished(new ResetFadeOutGameEventHandler(scene, players));
    }

    private class ResetFadeOutGameEventHandler implements EventHandler<ActionEvent> {
        private final Scene scene;
        private final List<Player> players;

        public ResetFadeOutGameEventHandler(Scene scene, List<Player> players) {
            this.scene = scene;
            this.players = players;
        }

        @Override
        public void handle(ActionEvent event) {
            SinglePlayGameController singlePlayGameController = new SinglePlayGameController(scene);
            if (players == null) {
                singlePlayGameController.selectCharacter(new StartGameRunnable(singlePlayGameController));
            } else {
                singlePlayGameController.delaySecond(new ResetGameRunnable(singlePlayGameController, players));
            }
        }
    }

    private class StartGameRunnable implements Runnable {
        private final SinglePlayGameController singlePlayGameController;

        public StartGameRunnable(SinglePlayGameController singlePlayGameController) {
            this.singlePlayGameController = singlePlayGameController;
        }

        @Override
        public void run() {
            singlePlayGameController.startGame();
        }
    }

    private class ResetGameRunnable implements Runnable {
        private final SinglePlayGameController singlePlayGameController;
        private final List<Player> players;

        public ResetGameRunnable(SinglePlayGameController singlePlayGameController, List<Player> players) {
            this.singlePlayGameController = singlePlayGameController;
            this.players = players;
        }

        @Override
        public void run() {
            singlePlayGameController.resetGame(players);
        }
    }

}
