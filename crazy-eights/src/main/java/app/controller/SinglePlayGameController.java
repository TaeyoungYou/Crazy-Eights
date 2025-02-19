package app.controller;

import app.model.Player;
import app.view.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

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


    public SinglePlayGameController(Scene _scene) {
        scene = _scene;
        root = new StackPane();
        mainPane = new BorderPane();
        mainView = new SinglePlayGameView(mainPane);
        settingView = new SettingView(root);
    }

    public void startGame(){
        drawGamePage();

        createPlayers();
        shareCards();

    }

    private void shareCards(){

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
        player.setIcon("/avatar/User-01.png");

        // 유저가 카드를 한개 가져가기
        mainView.getDeck().setOnMouseClicked(event -> {
            if(player.getCardLeft() < 12){
                mainView.setGetCardAnimation(player.getCardLeft()).setOnFinished(e -> {
                    mainView.removeAnimationCard();
                    player.setCard();
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
            if(card.getOnMouseReleased() == null){
                card.setOnMouseEntered(ev -> {
                    mainView.setCardHoverScaleUp(card);
                });
                card.setOnMouseExited(ev -> {
                    mainView.setCardHoverScaleDown(card);
                });
                card.setOnMousePressed(ev -> {
                    mainView.setDragPressed(ev, card);
                });
                card.setOnMouseDragged(ev -> {
                    mainView.setDragDragged(ev, card);
                });
                card.setOnMouseReleased(ev -> {
                    mainView.setDragReleased(ev, card, player,()->addCardEffects(player));
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
