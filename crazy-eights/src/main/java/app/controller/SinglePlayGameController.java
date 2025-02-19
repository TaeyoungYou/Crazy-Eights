package app.controller;

import app.model.Card;
import app.model.Deck;
import app.model.Player;
import app.view.*;
import javafx.animation.KeyFrame;
import javafx.animation.ParallelTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SinglePlayGameController {
    private Scene scene;
    private StackPane root;
    private BorderPane mainPane;

    private SinglePlayGameView mainView;
    private SettingView settingView;

    private final ObservableList<ImageView> cards = FXCollections.observableArrayList();
    private List<Player> players = new ArrayList<>();
    private final int playerNum = 4;
    private Deck deck;

    private int time = 0;
    private int turn = 0;

    public SinglePlayGameController(Scene _scene) {
        scene = _scene;
        root = new StackPane();
        mainPane = new BorderPane();
        mainView = new SinglePlayGameView(mainPane);
        settingView = new SettingView(root);
        deck = new Deck();
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
                        updatePlayerTurn(turn);
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
    private void updatePlayerTurn(int turn){
        for(Player player: players){
            player.setMyTurn(false);
            addCardEffects(player);
        }
        players.get(turn).setMyTurn(true);
        addCardEffects(players.get(turn));
    }

    private Timeline putStartDummyCard(){
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            mainView.setCardDummy(deck.drawCard());
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
                                addCardEffects(player);
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

        mainView.getDeck().setOnMouseClicked(event -> {
            if(player.getCardLeft() < 12){
                mainView.setGetCardAnimation(player.getCardLeft()).setOnFinished(e -> {
                    mainView.removeAnimationCard();
                    player.setCard(deck);
                    addCardEffects(player);
                });
            } else {
                System.out.println("Player has "+player.getCardLeft()+" cards!");
            }
        });

    }

    private void addCardEffects(Player player){
        // 유저 핸드의 카드 움직임과 핸드 이벤트들 (매 카드가 추가가 될때 마다 실행)
        for(ImageView card: mainView.getCurCards()){
            if(card.getOnMouseClicked() == null){
                card.setOnMouseEntered(ev -> {
                    mainView.setCardHoverScaleUp(card);
                });
            }
            if(card.getOnMouseExited() == null){
                card.setOnMouseExited(ev -> {
                    mainView.setCardHoverScaleDown(card);
                });
            }
            if(card.getOnMousePressed() == null){
                card.setOnMousePressed(ev -> {
                    mainView.setDragPressed(ev, card);
                });
            }
            if(card.getOnMouseDragged() == null){
                card.setOnMouseDragged(ev -> {
                    mainView.setDragDragged(ev, card);
                });
            }
            if(card.getOnMouseReleased() == null) {
                card.setOnMouseReleased(ev -> {
                    mainView.setDragReleased(ev, card, player, () -> addCardEffects(player));
                });
            }
        }
    }

    private void drawGamePage(){
        initPage();
        mainView.drawMainPage(playerNum);

        dummyListener();

        mainView.getBack().setOnMouseClicked(event -> mainView.setFadeOutSinglePlay(scene));
        mainView.getSetting().setOnMouseClicked(event -> settingView.generate());
        mainView.getVolume().setOnMouseClicked(event -> mainView.setMusicVolume());

    }

    private void initPage(){
        root.getChildren().add(mainPane);
        scene.setRoot(root);
    }

    private void dummyListener(){
        cards.addListener((ListChangeListener<? super ImageView>) change -> {
            while(change.next()){
                if(change.wasRemoved()){
                    mainView.getCardDummy().setImage(change.getRemoved().get(0).getImage());
                }
            }
        });
    }

}
