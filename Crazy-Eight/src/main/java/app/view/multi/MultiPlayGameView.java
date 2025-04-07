package app.view.multi;

import app.animation.multi.AnimationGame;
import app.model.multi.*;
import app.model.multi.Card;
import app.model.multi.DummyCard;
import app.style.StyleGame;
import javafx.animation.Animation;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * The SinglePlayGameView class represents the user interface for a single-player game mode.
 * It handles the layout, configuration, and animations of different game components and
 * offers methods for interaction and status updates.
 *
 * Fields:
 * - pane: The main layout container for the game.
 * - style: Contains styling information for the UI components.
 * - animation: Manages game-related animations.
 * - gamePane: Represents the center area where the game is displayed.
 * - sidebar: Represents the side panel for additional information and interactions.
 * - buttonBar: Contains buttons for game actions like restart and settings.
 * - logPane: Displays game logs.
 * - logScroll: A scroll pane for the log area.
 * - log: Stores the log messages.
 * - msgPane: Displays messages exchanged between players.
 * - msgScroll: A scroll pane for the message area.
 * - chats: Stores the chat messages.
 * - message: A text field for entering chat messages.
 * - gamePlayerStatus: Displays the status of the player.
 * - scoreTimeContainer: Contains elements for score and time display.
 * - scoreContainer: Holds score-related components.
 * - scoreBox: Displays the player's score.
 * - cardPlace: An area for placing cards in the UI.
 * - scoreTitle: Title label for the score section.
 * - timer: Displays the game timer.
 * - restart: A button or icon to restart the game.
 * - setting: A button or icon to access game settings.
 * - back: A button or icon to return to the previous menu.
 * - deck: Represents the card deck.
 * - cardDummy: Represents a placeholder for the current card in play.
 * - tmpCard: Temporarily holds a card object.
 * - curCards: A collection of the current cards held by the player.
 * - curCardInfo: A pairing of card objects and their corresponding UI representations.
 * - index: Tracks the current index for animations or card positioning.
 */
public class MultiPlayGameView {
    private final BorderPane pane;
    private final StyleGame style;
    private final AnimationGame animation;

    private BorderPane gamePane;
    private VBox sidebar;
    private HBox buttonBar;
    private BorderPane logPane;
    private ScrollPane logScroll;
    private VBox log;
    private BorderPane msgPane;
    private ScrollPane msgScroll;
    private VBox chats;
    private TextField message;
    private VBox gamePlayerStatus;
    private HBox scoreTimeContainer;
    private VBox scoreContainer;
    private VBox scoreBox;
    private AnchorPane cardPlace;

    private Label scoreTitle;
    private Label timer;

    private ImageView setting;
    private ImageView back;
    private ImageView deck;
    private ImageView cardDummy;

    private ImageView tmpCard;
    private ObservableList<ImageView> curCards;
    private List<Pair<Card, ImageView>> curCardInfo = new ArrayList<>();

    /**
     * Constructs a SinglePlayGameView instance.
     * Initializes the game view by setting up the main game pane and applying initial styles and animations.
     *
     * @param _pane the main game pane of type BorderPane where components are added and styled.
     */
    public MultiPlayGameView(BorderPane _pane) {
        pane = _pane;
        style = new StyleGame();
        animation = new AnimationGame();
    }

    /**
     * Draws the main page of the single-player game by configuring and arranging
     * various UI components such as the sidebar, game player status, and central card area.
     *
     * @param playerNum The number of players in the game, used to configure player-related UI elements.
     */
    public void drawMainPage(int playerNum){
        pane.setStyle(style.gameBorderPaneStyle());

        gamePane = new BorderPane();

        sidebar = new VBox();
        sidebarConfig();

        gamePlayerStatus = new VBox();
        gamePlayerStatusConfig(playerNum);

        gamePane.setLeft(gamePlayerStatus);

        cardPlace = new AnchorPane();
        cardPlace.setPrefSize(870, 1080);
        cardPlace.setPadding(new Insets(0, 0, 0, 0));

        createDeck();

        gamePane.setCenter(cardPlace);

        pane.setCenter(gamePane);
        pane.setRight(sidebar);

        animation.fadeInSinglePlay(pane);
    }

    /**
     * Configures the sidebar component by setting its size, layout, style, and containing UI elements.
     * This method organizes the sidebar into multiple sections, including button controls, a log view,
     * a message display section, and an input field for user messages.
     * <p>
     * The configuration includes:
     * - Sidebar layout properties such as size, padding, alignment, spacing, and style.
     * - Button controls (Restart, Settings, Back) with hover animation applied.
     * - A log pane and scrollable log section for displaying messages.
     * - A message pane and scrollable chat area for displaying user and system messages.
     * - A stylized text field for user message input.
     * - Dynamically adjusts scroll values to ensure new content is visible.
     * <p>
     * The styles for various components are applied using methods from the associated style object.
     * Animations such as button hover scaling are applied using methods from the animation object.
     */
    private void sidebarConfig() {
        sidebar.setPrefSize(385, 1080);
        sidebar.setPadding(new Insets(50, 20, 20, 20));
        sidebar.setAlignment(Pos.TOP_CENTER);
        sidebar.setSpacing(10);
        sidebar.setStyle(style.gameSidePaneStyle());

        buttonBar = new HBox();
        buttonBar.setSpacing(40);
        buttonBar.setAlignment(Pos.CENTER);
        setting = new ImageView(new Image(getClass().getResource("/button/settings.png").toExternalForm()));
        back = new ImageView(new Image(getClass().getResource("/button/back.png").toExternalForm()));

        animation.buttonAnimation(setting);
        animation.buttonAnimation(back);
        buttonBar.getChildren().addAll(setting, back);

        sidebar.getChildren().add(buttonBar);

        Region spacer = new Region();
        spacer.setPrefHeight(30);

        sidebar.getChildren().add(spacer);

        logPane = new BorderPane();
        logPane.setStyle(style.sideBorderPaneStyle());
        logPane.setPrefSize(340, 236);
        logPane.setPadding(new Insets(10, 10, 10, 10));

        logScroll = new ScrollPane();
        logScroll.setStyle(style.sideScrollPane());
        logScroll.setFitToWidth(true);
        logScroll.setFitToHeight(true);
        logScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        logScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        log = new VBox(5);
        log.setFillWidth(true);
        log.setPrefHeight(Region.USE_COMPUTED_SIZE);
        log.setStyle(style.sideVBox());

        logScroll.setContent(log);
        logPane.setCenter(logScroll);
        sidebar.getChildren().add(logPane);
        log.heightProperty().addListener(new LogHeightChangeListener());

        msgPane = new BorderPane();
        msgPane.setStyle(style.sideBorderPaneStyle());
        msgPane.setPrefSize(340, 574);
        msgPane.setPadding(new Insets(10, 10, 10, 10));

        msgScroll = new ScrollPane();
        msgScroll.setStyle(style.sideScrollPane());
        msgScroll.setFitToWidth(true);
        msgScroll.setFitToHeight(false); // Chat의 생략방지!!
        msgScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        msgScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        chats = new VBox(10);
        chats.setFillWidth(true);

        chats.setStyle(style.sideVBox());
        chats.heightProperty().addListener(new ChatHeightChangeListener());

        msgScroll.setContent(chats);
        msgPane.setCenter(msgScroll);
        sidebar.getChildren().add(msgPane);

        message = new TextField();
        message.setPrefWidth(300);
        message.setPrefHeight(50);
        if (Setting.isEnClicked()) message.setStyle(style.sideMessageBox());
        else {
            message.setStyle(style.sideMessageBoxKRVersion());
            message.setFont(Font.loadFont(style.getCookieRunFont(), 14));
        }


        sidebar.getChildren().add(message);
    }

    private class LogHeightChangeListener implements javafx.beans.value.ChangeListener<Number> {
        @Override
        public void changed(javafx.beans.value.ObservableValue<? extends Number> obs, Number oldVal, Number newVal) {
            logScroll.setVvalue(1.0);
        }
    }

    private class ChatHeightChangeListener implements javafx.beans.value.ChangeListener<Number> {
        @Override
        public void changed(javafx.beans.value.ObservableValue<? extends Number> obs, Number oldVal, Number newVal) {
            msgScroll.setVvalue(1.0);
        }
    }

    /**
     * Configures the layout and behavior of the game player status view by creating the necessary
     * containers for player information and scoreboard details. Each player, except the last one,
     * is assigned a placeholder that will later be populated with player-specific details. A container
     * for the scoreboard and timer is also added.
     *
     * @param playerNum the total number of players in the game, which determines the number of
     *                  placeholders and containers to initialize.
     */
    private void gamePlayerStatusConfig(int playerNum) {
        // 이유를 씨발 모르겠어요. 현재 유저는 status공간이 필요없고, 할당도 안하는데 왜 플레이어 수만큼 공간을 할당해야하는지
        // 근데 또 싱글플레이어에서는 현재플레이어-1 해도 잘만 되요 씨발
        // 일단 고쳐지긴 했으니 나둡니다 - 이거 찾는데 2틀이나 꼬박 밤을 샛어요 시발
        for(int i = 0; i < playerNum; ++i) {    
            HBox playerPlace = new HBox();
            gamePlayerStatus.getChildren().add(playerPlace);    // empty box, it will be initialized by PlayerStatusView, subview
        }

        scoreTimeContainer = new HBox();
        scoreTimeContainerConfig(playerNum);

        gamePlayerStatus.getChildren().add(scoreTimeContainer);

        gamePlayerStatus.setPrefSize(450, 1080);
        gamePlayerStatus.setAlignment(Pos.TOP_LEFT);
        gamePlayerStatus.setSpacing(10);
        gamePlayerStatus.setPadding(new Insets(30, 30, 30, 30));
    }

    /**
     * Configures the score and timer UI container for the single-player game view.
     * This method initializes and styles UI components, including a vertical box
     * for score display and a timer label, based on the number of players and
     * language settings.
     *
     * @param playerNum The number of players in the game, used to determine
     *                  the number of score boxes to initialize in the UI.
     */
    private void scoreTimeContainerConfig(int playerNum) {
        scoreContainer = new VBox();
        scoreContainer.setSpacing(10);
        scoreContainer.setAlignment(Pos.CENTER);

        if(Setting.isEnClicked()){
            scoreTitle = new Label("Score");
            scoreTitle.setFont(Font.loadFont(style.getLilitaOneFont(), 40));
            scoreTitle.setStyle(style.sideLabelStyle());
        } else {
            scoreTitle = new Label("점수");
            scoreTitle.setFont(Font.loadFont(style.getCookieRunFont(), 40));
            scoreTitle.setStyle(style.sideLabelStyle());
        }

        scoreBox = new VBox();
        scoreBox.setStyle(style.statusScoreBoxStyle());
        scoreBox.setPrefSize(250, 300);
        scoreBox.setAlignment(Pos.CENTER);
        scoreBox.setPadding(new Insets(10, 10, 10, 20));
        scoreBox.setSpacing(10);

        for(int i = 0; i < 4; ++i){
            HBox playerScoreBox = new HBox();
            scoreBox.getChildren().add(playerScoreBox);     // Empty Box, it will be initialized by PlayerScoreView, subView
        }

        scoreContainer.getChildren().addAll(scoreTitle, scoreBox);

        timer = new Label("10");
        timer.setFont(Font.loadFont(style.getLilitaOneFont(), 80));
        timer.setStyle(style.sideLabelStyle());

        scoreTimeContainer.getChildren().addAll(scoreContainer, timer);
        scoreTimeContainer.setAlignment(Pos.CENTER_LEFT);
        scoreTimeContainer.setSpacing(55);
        scoreTimeContainer.setPadding(new Insets(0, 0, 0, 0));
    }

    /**
     * Retrieves the ImageView representing the "Back" button or icon in the game view.
     *
     * @return the ImageView object associated with the "Back" button, used for navigating or returning to a previous screen.
     */
    public ImageView getBack(){
        return back;
    }
    /**
     * Retrieves the ImageView instance representing the settings icon or component
     * in the SinglePlayGameView. This element may be used to trigger or access
     * settings functionality within the single-player game interface.
     *
     * @return the ImageView instance associated with the settings component
     */
    public ImageView getSetting(){
        return setting;
    }

    /**
     * Retrieves the ImageView instance representing the dummy card in the SinglePlayGameView.
     * This element is used as a placeholder or visual representation for certain card-related actions.
     *
     * @return the ImageView instance associated with the dummy card.
     */
    public ImageView getCardDummy(){
        return cardDummy;
    }
    /**
     * Retrieves the ImageView instance representing the deck in the SinglePlayGameView.
     * This object is used to visually represent and interact with the card deck within the game interface.
     *
     * @return the ImageView object associated with the deck.
     */
    public ImageView getDeck(){
        return deck;
    }
    /**
     * Retrieves the TextField instance used for displaying or capturing messages in the game view.
     *
     * @return the TextField object associated with the message input or display functionality.
     */
    public TextField getMessage(){
        return message;
    }
    /**
     * Retrieves the ImageView instance representing a dummy card in the SinglePlayGameView.
     * This object serves as a placeholder or a visual representation of a card-related element.
     *
     * @return the ImageView instance associated with the dummy card.
     */
    public ImageView getDummyCard(){
        return cardDummy;
    }
    /**
     * Sets the timer by displaying the given time value in the timer UI component.
     *
     * @param time the time value to be displayed on the timer, expressed as an integer.
     */
    public void setTimer(int time){
        timer.setText(time+"");
    }
    /**
     * Applies a visual effect to the timer component in the game view.
     *
     * This method enhances the timer UI by adding a green glow effect using the
     * DropShadow class. The properties of the DropShadow effect include:
     *
     * - Radius: 10, which determines the size of the glow.
     * - Spread: 0.5, controlling the intensity of the glow.
     * - Color: DARKGREEN, specifying the color of the glow effect.
     * - OffsetX and OffsetY: Both set to 0, centering the glow around the timer.
     *
     * The effect is applied to the timer field, creating a distinctive visual
     * style to highlight its importance or current state.
     */
    public void setTimerEffect(){
        DropShadow edgeGlow = new DropShadow();
        edgeGlow.setRadius(10);
        edgeGlow.setSpread(0.5);
        edgeGlow.setColor(Color.DARKGREEN);
        edgeGlow.setOffsetX(0);
        edgeGlow.setOffsetY(0);
        timer.setEffect(edgeGlow);
    }
    /**
     * Removes any visual effect applied to the timer component.
     *
     * When invoked, this method sets the visual effect of the timer to null,
     * effectively clearing any previously applied styles or effects.
     */
    public void delTimerEffect(){
        timer.setEffect(null);
    }

    /**
     * Sets the dummy card in the game view by updating the image of the cardDummy instance
     * to match the image of the provided DummyCard object.
     *
     * @param card the DummyCard object whose image is used to set the image of the cardDummy.
     */
    public void setCardDummy(DummyCard card){
        cardDummy.setImage(card.getImage());
    }

    /**
     * Retrieves the current list of cards displayed in the single-player game view.
     * This list contains ImageView objects representing the visual components of the cards.
     *
     * @return an ObservableList of ImageView objects representing the current cards.
     */
    public ObservableList<ImageView> getCurCards(){
        return curCards;
    }
    /**
     * Retrieves the current card information in the game view.
     * The returned list contains pairs of cards and their corresponding ImageView objects.
     *
     * @return a list of pairs, where each pair consists of a Card and its associated ImageView.
     */
    public List<Pair<Card, ImageView>> getCurCardInfo(){
        return curCardInfo;
    }
    /**
     * Initiates a fade-out animation for the single-player game view and transitions
     * the application to the main menu upon the animation's completion.
     *
     * @param _scene The current Scene in which the fade-out animation is performed
     *               and the transition occurs.
     */
    public void setFadeOutGame(Scene _scene) {
        animation.fadeOutGame(_scene, pane);
    }
    /**
     * Applies a hover effect to the specified card by invoking the cardHoverEffectScaleUp
     * method from the animation object. This effect is used to visually scale up the card
     * slightly when hovered over.
     *
     * @param card The ImageView representing the card to which the hover effect should be applied.
     */
    public void setCardHoverScaleUp(ImageView card){
        animation.cardHoverEffectScaleUp(card);
    }
    /**
     * Applies a scale-down animation to the provided ImageView card when hovered.
     * This animation creates a visual effect that reduces the size of the card,
     * indicating a hover-out interaction.
     *
     * @param card the ImageView representing a card that the scale-down animation
     *             will be applied to upon hover.
     */
    public void setCardHoverScaleDown(ImageView card){
        animation.cardHoverEffectScaleDown(card);
    }
    private int index;
    /**
     * Handles the press event for dragging a card in the single-player game view.
     * Tracks the index of the card in the card container and invokes the drag press animation.
     *
     * @param event the MouseEvent that triggers the drag press action, typically a mouse press on the card.
     * @param card  the ImageView representing the card being dragged.
     */
    public void setDragPressed(MouseEvent event, ImageView card){
        index = cardPlace.getChildren().indexOf(card);
        animation.cardDragPressed(event, card);
    }
    /**
     * Handles the drag-and-dragged event for a card represented by an ImageView.
     *
     * @param event the MouseEvent that triggered the drag action
     * @param card  the ImageView representing the card being dragged
     */
    public void setDragDragged(MouseEvent event, ImageView card){
        animation.cardDragDragged(event, card);
    }

    /**
     * Handles the drag release event for a card in the game. If the release is valid and correct,
     * performs animations and updates the game state by removing the card from the current player's hand,
     * setting the dummy card's properties, and playing the animation. If the release is incorrect,
     * resets the card to its previous position.
     *
     * @param event     The MouseEvent triggered when the drag is released.
     * @param card      The ImageView representing the card being dragged.
     * @param player    The current player interacting with the card.
     * @param dummyCard The dummy card used to update the state of the game after a valid release.
     * @param correct   A boolean indicating whether the drag release is valid and correct.
     */
    public boolean setDragReleased(MouseEvent event, ImageView card, Player player, DummyCard dummyCard, boolean correct) {
        Animation removed = animation.cardDragReleased(event, card);
        if (removed != null && correct) {
            int index = curCards.indexOf(card);

            removed.setOnFinished(new DragReleaseHandler(card, player, dummyCard, index));

            removed.play();
            return true;
        } else {
            getBackAnimation(card);
            return false;
        }
    }

    private class DragReleaseHandler implements EventHandler<ActionEvent> {
        private final ImageView card;
        private final Player player;
        private final DummyCard dummyCard;
        private final int index;

        public DragReleaseHandler(ImageView card, Player player, DummyCard dummyCard, int index) {
            this.card = card;
            this.player = player;
            this.dummyCard = dummyCard;
            this.index = index;
        }

        @Override
        public void handle(ActionEvent e) {
            curCards.remove(card);
            dummyCard.setCard(curCardInfo.get(index).getKey(), false);
            dummyCard.setImage();
            curCardInfo.remove(index);
            cardPlace.getChildren().remove(card);
            player.removeCard(index);
            setCardDummy(dummyCard);
        }
    }
    /**
     * Executes an animation effect to move a specified card back to its previous position.
     *
     * @param card The ImageView object representing the card to be animated.
     */
    public void getBackAnimation(ImageView card){
        animation.cardMoveBackEffect(card, cardPlace);
        cardPlace.getChildren().remove(card);
        cardPlace.getChildren().add(index, card);
    }

    /**
     * Updates the player's status in the game and optionally adds a card animation.
     *
     * @param newPlayerStatus the new status component to be displayed for the player
     * @param cardLeft the label representing the number of cards left, used in the animation
     * @param player the player whose status is being updated
     * @param handleCard a boolean flag indicating whether to handle card animation
     */
    public void setPlayerStatus(HBox newPlayerStatus, Label cardLeft, Player player, boolean handleCard) {
        gamePlayerStatus.getChildren().set(player.getStatusId(), newPlayerStatus);
        if(handleCard){
            animation.addLeftCardAnimation(cardLeft);
        }
    }
    /**
     * Updates the score box for a specific player with a new HBox.
     *
     * @param newScoreBox the new HBox to replace the existing score box
     * @param player the player whose score box needs to be updated
     */
    public void setScoreBox(HBox newScoreBox, Player player) {
        scoreBox.getChildren().set(player.getScoreId(), newScoreBox);
    }

    /**
     * Creates a deck of cards and a placeholder for an empty card, represented by image views.
     * Initializes the deck and cardDummy image views with appropriate images and sizes.
     * Sets the position of the deck and the empty card placeholder on the screen.
     * Adds the deck and cardDummy images to the cardPlace container.
     * Applies a hover animation effect to the deck.
     */
    private void createDeck() {
        deck = new ImageView(new Image(getClass().getResource("/card/Card-Deck.png").toExternalForm()));
        cardDummy = new ImageView(new Image(getClass().getResource("/card/Card-Empty.png").toExternalForm()));
        deck.setFitWidth(250);
        deck.setPreserveRatio(true);
        cardDummy.setFitWidth(220);
        cardDummy.setPreserveRatio(true);

        deck.setLayoutX(300);
        deck.setLayoutY(180);
        cardDummy.setLayoutX(600);
        cardDummy.setLayoutY(200);

        cardPlace.getChildren().add(deck);
        cardPlace.getChildren().add(cardDummy);

        animation.deckHoverAnimation(deck);
    }
    /**
     * Configures and plays the card animation for a card being retrieved from the deck.
     *
     * @param prevHandCount The number of cards currently in hand before the animation begins.
     * @return The configured and played card animation object.
     */
    public Animation setGetCardAnimation(int prevHandCount){
        deck.setDisable(true);
        tmpCard = animation.getCardAnimation(cardPlace);
        Animation cardAnimation = animation.getCardTranslateAnimation(tmpCard, prevHandCount);
        cardAnimation.play();
        return cardAnimation;
    }
    /**
     * Removes the temporary animation card from the card place.
     * This method deletes the temporary card (tmpCard) from the list of children
     * associated with the cardPlace, effectively removing it from the displayed UI.
     */
    public void removeAnimationCard(){
        cardPlace.getChildren().remove(tmpCard);
    }

    /**
     * Sets the user's hand by adding the specified cards to the cardPlace while clearing existing cards
     * except for the deck and a dummy card. The provided cards are then displayed on the screen.
     *
     * @param cards the list of ImageView cards to be set in the user's hand
     */
    // 오직 유저가 deck으로부터 카드를 하나 가져왔을 때 카드를 처음부터 넣고 화면에 출력
    public void setUserHand(ObservableList<ImageView> cards) {
        cardPlace.getChildren().removeIf(node -> node != deck && node != cardDummy);
        for(ImageView card: cards) {
            cardPlace.getChildren().add(card);
        }
        resettingPosCard();
        curCards = cards;
    }
    /**
     * Updates the current user's card information with the provided list of cards.
     *
     * @param cards a list of Card objects representing the user's hand of cards
     */
    public void setUserHandInfo(List<Card> cards){
        curCardInfo.clear();
        for(int i=0; i<cards.size(); i++){
            curCardInfo.add(new Pair<>(cards.get(i), curCards.get(i)));
        }
    }

    /**
     * Resets the position of card elements in a visual layout.
     *
     * This method filters out specific nodes (`deck` and `cardDummy`) from the `cardPlace`
     * container, leaving only the relevant card elements to be repositioned. It adjusts
     * the layout properties (`layoutX` and `layoutY`) of these card nodes to arrange them
     * in a horizontal line, starting at the bottom of the screen and spaced 75 units apart.
     *
     * The vertical position is set at a fixed value (1080 - 300), and the horizontal
     * position is determined by the index of the card in the filtered node list,
     * multiplied by the spacing value.
     */
    public void resettingPosCard() {
        int i = 0;

        List<Node> filterNodes = cardPlace.getChildren().stream().filter(node -> node != deck && node!= cardDummy).collect(Collectors.toList());
        for(Node node: filterNodes){
            ImageView card = (ImageView) node;
            card.setLayoutX(i * 75);
            card.setLayoutY(1080 - 300);
            i++;
        }
    }

    /**
     * Creates and plays an animation for placing a card with the associated player's movement.
     * The method retrieves the card animation for a card's position, generates a translation
     * animation to simulate the card placement with the player's action, and plays the animation.
     *
     * @return The animation object representing the card placement with the player's movement.
     */
    public Animation putCardAnimationWithPlayer(){
        tmpCard = animation.getCardAnimation(cardPlace);
        Animation putCardTrans = animation.putCardTranslateWithPlayerAnimation(tmpCard);
        putCardTrans.play();
        return putCardTrans;
    }
    /**
     * Retrieves and plays the card animation for transferring a card to a player.
     *
     * @return an Animation object representing the card transfer animation to the player.
     */
    public Animation getCardAnimationToPlayer(){
        tmpCard = animation.getCardAnimation(cardPlace);
        Animation getCardTrans = animation.getCardTranslateToPlayersAnimation(tmpCard);
        getCardTrans.play();
        return getCardTrans;
    }
    /**
     * Retrieves and plays the animation for moving a card to the user.
     *
     * @return The animation object representing the card translation animation.
     */
    public Animation getCardAnimationToUser(){
        tmpCard = animation.getCardAnimation(cardPlace);
        Animation getCardTrans = animation.getCardTranslateAnimation(tmpCard, curCards.size());
        getCardTrans.play();
        return getCardTrans;
    }

    /**
     * Adds a log message with the specified content and styling based on the given state.
     *
     * @param message The log message to be displayed.
     * @param state   The state of the log message, determining its style.
     *                Possible values are State.System, State.Error, and State.Log.
     */
    public void addLog(String message, State state){
        Label msg = new Label(message);

        if(Setting.isEnClicked()) msg.setFont(Font.font("Comic Sans MS", 16));
        else msg.setFont(Font.loadFont(style.getCookieRunFont(), 16));

        if(state == State.System) msg.setStyle(style.systemLogStyle());
        if(state == State.Error) msg.setStyle(style.errorLogStyle());
        if(state == State.Log) msg.setStyle(style.sideLabelStyle());
        log.getChildren().add(msg);
    }

    /**
     * Adds a message from the user to the chatbox. The message is displayed
     * in a styled message box and aligned to the right within the chat pane.
     * The styling and font of the message box depend on the user's language
     * setting.
     *
     * @param message The text content of the message to be displayed in the chatbox.
     */
    public void addMsgFromUser(String message){
        HBox box = new HBox();

        Label msg = new Label(message);
        msg.setWrapText(true);
        msg.setMaxWidth(300);
        if(Setting.isEnClicked()){
            msg.setStyle(style.sideMessageBox());
        } else {
            msg.setStyle(style.sideMessageBoxKRVersion());
            msg.setFont(Font.loadFont(style.getCookieRunFont(), 14));
        }
        box.getChildren().add(msg);
        box.setAlignment(Pos.CENTER_RIGHT);
        chats.getChildren().add(box);
    }

    /**
     * Adds a message from a player to the chat interface as a styled message box with an associated player icon.
     *
     * @param message The message text to display in the chat.
     * @param player The player object representing the sender of the message. Contains details such as player icon.
     */
    public void addMsgFromPlayer(String message, Player player){
        HBox box = new HBox(5);

        ImageView icon = new ImageView(getClass().getResource(player.getIcon()).toExternalForm());
        icon.setFitWidth(60);
        icon.setFitHeight(60);

        Label msg = new Label(message);
        if(Setting.isEnClicked()){
            msg.setStyle(style.sideMessageBox());
        } else {
            msg.setStyle(style.sideMessageBoxKRVersion());
            msg.setFont(Font.loadFont(style.getCookieRunFont(), 14));
        }

        msg.setWrapText(true);
        msg.setMaxWidth(Double.MAX_VALUE);

        HBox.setHgrow(msg, Priority.ALWAYS);
        box.getChildren().addAll(icon, msg);
        box.setAlignment(Pos.CENTER_LEFT);
        chats.getChildren().add(box);
    }

    /**
     * Resets the game by invoking the fade-out animation and reinitializing necessary components.
     *
     * @param scene the current scene of the game which needs to be reset
     * @param pane the BorderPane layout in the game's UI that will be affected during reset
     * @param players the list of players involved in the game to reset their states
     */
    public void resetGame(Scene scene, BorderPane pane, List<Player> players){
        animation.resetFadeOutGame(scene, pane, players);
    }
    public void resetGameClient(Scene scene, BorderPane pane, List<Player> players){
        animation.resetFadeOutClient(scene, pane, players);
    }
}
