package app.view;

import app.animation.AnimationGame;
import app.model.Card;
import app.model.Music;
import app.model.Player;
import app.style.StyleGame;
import javafx.animation.Animation;
import javafx.collections.ObservableList;
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

import java.util.*;
import java.util.stream.Collectors;

/**
 * Represents the single-player game screen for Crazy Eights.
 * Handles UI components and game state management.
 */
public class SinglePlayGameView {
    private final BorderPane pane;
    private final StyleGame style;
    private final AnimationGame animation;

    private BorderPane gamePane;
    private VBox sidebar;
    private HBox buttonBar;
    private ScrollPane logScroll;
    private VBox log;
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

    private ImageView volume;
    private ImageView setting;
    private ImageView back;
    private ImageView deck;
    private ImageView cardDummy;

    private ImageView tmpCard;
    private ObservableList<ImageView> curCards;

    public SinglePlayGameView(BorderPane _pane) {
        pane = _pane;
        style = new StyleGame();
        animation = new AnimationGame();
    }

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

    private void sidebarConfig() {
        sidebar.setPrefSize(385,1080);
        sidebar.setPadding(new Insets(50, 20, 20, 20));
        sidebar.setAlignment(Pos.TOP_CENTER);
        sidebar.setSpacing(10);
        sidebar.setStyle(style.gameSidePaneStyle());

        buttonBar = new HBox();
        buttonBar.setSpacing(40);
        buttonBar.setAlignment(Pos.CENTER);
        volume = new ImageView(new Image(getClass().getResource("/button/volume-on.png").toExternalForm()));
        setting = new ImageView(new Image(getClass().getResource("/button/settings.png").toExternalForm()));
        back = new ImageView(new Image(getClass().getResource("/button/back.png").toExternalForm()));

        animation.buttonAnimation(setting);
        animation.buttonAnimation(volume);
        animation.buttonAnimation(back);
        buttonBar.getChildren().addAll(volume, setting, back);

        sidebar.getChildren().add(buttonBar);

        Region spacer = new Region();
        spacer.setPrefHeight(30);

        sidebar.getChildren().add(spacer);

        logScroll = new ScrollPane();
        logScroll.setStyle(style.sideScrollPane());
        log = new VBox();
        log.setPrefWidth(340);
        log.setPrefHeight(236);
        log.setStyle(style.sideChatBox());
        logScroll.setContent(log);
        logScroll.setFitToHeight(true);
        logScroll.setFitToWidth(true);
        sidebar.getChildren().add(logScroll);

        msgScroll = new ScrollPane();
        msgScroll.setStyle(style.sideScrollPane());
        chats = new VBox();
        chats.setPrefWidth(340);
        chats.setPrefHeight(574);
        chats.setStyle(style.sideChatBox());

        msgScroll.setContent(chats);
        msgScroll.setFitToHeight(true);
        msgScroll.setFitToWidth(true);
        sidebar.getChildren().add(msgScroll);

        message = new TextField();
        message.setPrefWidth(300);
        message.setPrefHeight(50);
        message.setStyle(style.sideMessageBox());

        sidebar.getChildren().add(message);
    }

    private void gamePlayerStatusConfig(int playerNum) {
        for(int i = 0; i < playerNum - 1; ++i) {
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

    private void scoreTimeContainerConfig(int playerNum) {
        scoreContainer = new VBox();
        scoreContainer.setSpacing(10);
        scoreContainer.setAlignment(Pos.CENTER);

        scoreTitle = new Label("Score");
        scoreTitle.setFont(Font.loadFont(style.getLilitaOneFont(), 40));
        scoreTitle.setStyle(style.sideLabelStyle());

        scoreBox = new VBox();
        scoreBox.setStyle(style.statusScoreBoxStyle());
        scoreBox.setPrefSize(250, 300);
        scoreBox.setAlignment(Pos.CENTER);
        scoreBox.setPadding(new Insets(10, 10, 10, 20));
        scoreBox.setSpacing(10);

//        players.sort(Comparator.reverseOrder());
        for(int i = 0; i < playerNum; ++i){
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

    public ImageView getBack(){
        return back;
    }
    public ImageView getSetting(){
        return setting;
    }
    public ImageView getVolume(){
        return volume;
    }
    public ImageView getCardDummy(){
        return cardDummy;
    }
    public ImageView getDeck(){
        return deck;
    }
    public ImageView getDummyCard(){
        return cardDummy;
    }
    public void setTimer(int time){
        timer.setText(time+"");
    }
    public void setTimerEffect(){
        DropShadow edgeGlow = new DropShadow();
        edgeGlow.setRadius(10);
        edgeGlow.setSpread(0.5);
        edgeGlow.setColor(Color.DARKGREEN);
        edgeGlow.setOffsetX(0);
        edgeGlow.setOffsetY(0);
        timer.setEffect(edgeGlow);
    }
    public void delTimerEffect(){
        timer.setEffect(null);
    }
    public void setCardDummy(Card card){
        cardDummy.setImage(new Image(getClass().getResource(card.getCardURL()).toExternalForm()));
    }
    public ObservableList<ImageView> getCurCards(){
        return curCards;
    }
    public void setFadeOutSinglePlay(Scene _scene) {
        animation.fadeOutSinglePlay(_scene, pane);
    }
    public void setCardHoverScaleUp(ImageView card){
        animation.cardHoverEffectScaleUp(card);
    }
    public void setCardHoverScaleDown(ImageView card){
        animation.cardHoverEffectScaleDown(card);
    }
    private int index;
    public void setDragPressed(MouseEvent event, ImageView card){
        index = cardPlace.getChildren().indexOf(card);
        animation.cardDragPressed(event, card);
    }
    public void setDragDragged(MouseEvent event, ImageView card){
        animation.cardDragDragged(event, card);
    }
    public void setDragReleased(MouseEvent event, ImageView card, Player player, Runnable effect){
        Animation removed = animation.cardDragReleased(event, card);
        if(removed != null){
            int index = curCards.indexOf(card);

            removed.setOnFinished(e -> {
                curCards.remove(card);
                cardPlace.getChildren().remove(card);
                player.removeCard(index);
                effect.run();
            });

            removed.play();
        }else{
            animation.cardMoveBackEffect(card, cardPlace);
            cardPlace.getChildren().remove(card);
            cardPlace.getChildren().add(index, card);
            effect.run();
        }
    }
    public void setMusicVolume() {
        if (Music.isVolumeOn()) {
            volume.setImage(new Image(getClass().getResource("/button/volume-off.png").toExternalForm()));
            Music.volumeOff();
        } else {
            volume.setImage(new Image(getClass().getResource("/button/volume-on.png").toExternalForm()));
            Music.volumeOn();
        }
    }
    public void setPlayerStatus(HBox newPlayerStatus, Label cardLeft, Player player) {
        gamePlayerStatus.getChildren().set(player.getId(), newPlayerStatus);
        animation.addLeftCardAnimation(cardLeft);   // 수정해야됨. 그 플레이어가 카드를 받을 때만 발동되어야함.
    }
    public void setScoreBox(HBox newScoreBox, Player player) {
        scoreBox.getChildren().set(player.getId(), newScoreBox);
    }


    /**
     * Creates and displays the deck and a placeholder for the current card.
     * Adds hover animation and click event to draw a new card.
     *
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
    public Animation setGetCardAnimation(int prevHandCount){
        tmpCard = animation.getCardAnimation(cardPlace);
        Animation cardAnimation = animation.getCardTranslateAnimation(tmpCard, prevHandCount);
        cardAnimation.play();
        return cardAnimation;
    }
    public void removeAnimationCard(){
        cardPlace.getChildren().remove(tmpCard);
    }

    // 오직 유저가 deck으로부터 카드를 하나 가져왔을 때 카드를 처음부터 넣고 화면에 출력
    public void setUserHand(ObservableList<ImageView> cards) {
        cardPlace.getChildren().removeIf(node -> node != deck && node != cardDummy);
        for(ImageView card: cards) {
            cardPlace.getChildren().add(card);
        }
        resettingPosCard();
        curCards = cards;
    }

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

    public Animation getCardAnimationToPlayer(){
        tmpCard = animation.getCardAnimation(cardPlace);
        Animation getCardTrans = animation.getCardTranslateToPlayersAnimation(tmpCard);
        getCardTrans.play();
        return getCardTrans;
    }
    public Animation getCardAnimationToUser(){
        tmpCard = animation.getCardAnimation(cardPlace);
        Animation getCardTrans = animation.getCardTranslateAnimation(tmpCard,curCards.size());
        getCardTrans.play();
        return getCardTrans;
    }
}
