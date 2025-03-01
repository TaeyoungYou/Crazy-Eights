package app.controller;

import app.model.*;
import app.view.*;
import javafx.animation.KeyFrame;
import javafx.animation.ParallelTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SinglePlayGameController implements CardObserver {
    private Scene scene;
    private StackPane root;
    private BorderPane mainPane;

    private SinglePlayGameView mainView;
    private SettingView settingView;

    private List<Player> players = new ArrayList<>();
    private final int playerNum = 4;
    private Deck deck;
    private DummyCard dummyCard;

    private int time = 0;
    private int turn = 0;

    public SinglePlayGameController(Scene _scene) {
        scene = _scene;
        root = new StackPane();
        mainPane = new BorderPane();
        mainView = new SinglePlayGameView(mainPane);
        settingView = new SettingView(root);
        deck = new Deck();
        dummyCard = new DummyCard(this);
    }

    public void startGame(){
        drawGamePage();
        deck.generateDeck();
        createPlayers();

        SequentialTransition sequence = new SequentialTransition();

        sequence.getChildren().add(getSixCards());

        sequence.getChildren().add(putStartDummyCard());

        sequence.getChildren().add(gameLoop());

        sequence.play();
    }
    private Timeline gameLoop(){
        Timeline gameLoop = new Timeline(
                new KeyFrame(Duration.seconds(1),event -> {
                    if(time % 11 == 0){
                        updatePlayerTurn();
                        if(players.get(turn).isSelf()){
                            mainView.setTimerEffect();
                        } else {
                            mainView.delTimerEffect();
                        }

                        turn = (turn+1)%playerNum;
                        time = 0;
                    }
                    mainView.setTimer(10 - time);
                    time++;
            })
        );
        gameLoop.setCycleCount(Timeline.INDEFINITE);

        return gameLoop;
    }
    private void updatePlayerTurn(){
        for(Player player: players){
            player.setMyTurn(false);
            removeCardEffects();
            removeDeckEffects();
        }
        players.get(turn).setMyTurn(true);
        if(players.get(turn).isSelf()) {
            addCardEffects(players.get(turn));
            addDeckEffects(players.get(turn));
        }
    }

    private Timeline putStartDummyCard(){
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            dummyCard.setCard(deck.drawCard());
            dummyCard.setImage();
            mainView.setCardDummy(dummyCard);
        }));
        return timeline;
    }

    private ParallelTransition getSixCards(){
        ParallelTransition pt = new ParallelTransition();
        for(Player player : players){
            Timeline giveCard;
            if(player.isSelf()){
                giveCard = new Timeline(
                        new KeyFrame(Duration.seconds(1), event -> {
                            mainView.getCardAnimationToUser().setOnFinished(ev -> {
                                mainView.removeAnimationCard();
                                player.setCard(deck);
                            });
                        })
                );
            }else{
                giveCard = new Timeline(
                        new KeyFrame(Duration.seconds(1), event -> {
                            mainView.getCardAnimationToPlayer().setOnFinished(ev -> {
                                mainView.removeAnimationCard();
                                player.setCard(deck);
                            });
                        })
                );
            }
            giveCard.setCycleCount(6);
            pt.getChildren().add(giveCard);
        }
        return pt;
    }

    private void createPlayers(){
        Random random = new Random();
        for(int i = 0; i < playerNum-1; i++){
            Player player = new Player(i);
            players.add(player);
            new PlayerStatusView(player, mainView);
            new PlayerScoreView(player, mainView);
            int intUser = random.nextInt(7) + 1;
            player.setIcon(String.format("/avatar/User-0%d.png",intUser));
        }
        createUser();
    }

    private void createUser(){
        Player player = new Player(playerNum-1);
        player.setSelf();
        players.add(player);
        new PlayerScoreView(player, mainView);
        new PlayerHandView(player, mainView);
        player.setIcon("/avatar/User-02.png");
    }
    private void addDeckEffects(Player player){
        mainView.getDeck().setOnMouseClicked(event -> {
            if(player.getCardLeft() < 12){
                mainView.setGetCardAnimation(player.getCardLeft()).setOnFinished(e -> {
                    mainView.removeAnimationCard();
                    player.setCard(deck);
                    removeDeckEffects();
                });
            } else {
                System.out.println("Player has "+player.getCardLeft()+" cards!");
            }
        });
    }
    private void removeDeckEffects(){
        if(mainView.getDeck().getOnMouseClicked() != null){
            mainView.getDeck().setOnMouseClicked(null);
        }
    }

    private void addCardEffects(Player player){
        // 유저 핸드의 카드 움직임과 핸드 이벤트들 (매 카드가 추가가 될때 마다 실행)
        for(Pair<Card, ImageView> info : mainView.getCurCardInfo()){
            Card card = info.getKey();
            ImageView cardImg = info.getValue();
            if(cardImg.getOnMouseEntered() == null){
                cardImg.setOnMouseEntered(ev -> {
                    mainView.setCardHoverScaleUp(cardImg);
                });
            }
            if(cardImg.getOnMouseExited() == null){
                cardImg.setOnMouseExited(ev -> {
                    mainView.setCardHoverScaleDown(cardImg);
                });
            }
            if(cardImg.getOnMousePressed() == null){
                cardImg.setOnMousePressed(ev -> {
                    mainView.setDragPressed(ev, cardImg);
                });
            }
            if(cardImg.getOnMouseDragged() == null){
                cardImg.setOnMouseDragged(ev -> {
                    mainView.setDragDragged(ev, cardImg);
                });
            }
            if(cardImg.getOnMouseReleased() == null) {
                cardImg.setOnMouseReleased(ev -> {
                    Card dummy = dummyCard.getCard();
                    if(dummy.getSuit() == card.getSuit() || dummy.getRank() == card.getRank()){
                        mainView.setDragReleased(ev, cardImg, player, dummyCard, true);
                    }else{
                        mainView.setDragReleased(ev, cardImg, player, dummyCard, false);
                    }
                });

            }
        }
    }
    private void removeCardEffects(){
        for(ImageView card: mainView.getCurCards()){
            if(card.getOnMouseEntered() != null){
                card.setOnMouseEntered(null);
            }
            if(card.getOnMouseExited() != null){
                card.setOnMouseExited(null);
            }
            if(card.getOnMousePressed() != null){
                card.setOnMousePressed(null);
            }
            if(card.getOnMouseDragged() != null){
                card.setOnMouseDragged(null);
            }
            if(card.getOnMouseReleased() != null){
                card.setOnMouseReleased(null);
            }
        }
    }

    private void drawGamePage(){
        initPage();
        mainView.drawMainPage(playerNum);

        mainView.getBack().setOnMouseClicked(event -> mainView.setFadeOutSinglePlay(scene));
        mainView.getSetting().setOnMouseClicked(event -> settingView.generate());
        mainView.getVolume().setOnMouseClicked(event -> mainView.setMusicVolume());

    }

    private void initPage(){
        root.getChildren().add(mainPane);
        scene.setRoot(root);
    }

    @Override
    public void update(Card card) {
        System.out.println("더미 카드 놓아짐 감지: "+card.getSuit()+" "+card.getRank());
        removeDeckEffects();
        removeCardEffects();
    }
}
