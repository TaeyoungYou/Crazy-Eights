package app.controller;

import app.model.*;
import app.view.*;
import javafx.animation.*;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class SinglePlayGameController implements CardObserver, DeckObserver, LogObserver, ChatObserver {
    private Scene scene;
    private StackPane root;
    private BorderPane mainPane;

    private SinglePlayGameView mainView;
    private SettingView settingView;
    private ChooseEightView chooseEightView;

    private List<Player> players = new ArrayList<>();
    private final int playerNum = 4;
    private Deck deck;
    private DummyCard dummyCard;
    private Log log;
    private Chat chat;

    private int time = 0;
    private int turn = -1;
    private int playerRanPutTime = ThreadLocalRandom.current().nextInt(2, 10);
    private boolean playerDoChat = new Random().nextDouble() < 0.7;
    private int playerChatTime = ThreadLocalRandom.current().nextInt(1,playerRanPutTime);
    private boolean userDid = false;
    private boolean eightTime = false;

    public SinglePlayGameController(Scene _scene) {
        scene = _scene;
        root = new StackPane();
        mainPane = new BorderPane();
        mainView = new SinglePlayGameView(mainPane);
        settingView = new SettingView(root);
        chooseEightView = new ChooseEightView(root);
        deck = new Deck(this);
        dummyCard = new DummyCard(this);
        log = new Log(this);
        chat = new Chat(this);
    }

    public void startGame(){
        drawGamePage();
        deck.generateDeck();
        createPlayers();

        log.setLogs("Setting Game..." , State.System);
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
                    if(userDid) userDid = false;
                    turn = (turn+1)%playerNum;
                    time = 0;

                    updatePlayerTurn();
                    if(players.get(turn).isSelf()){
                        mainView.setTimerEffect();
                    } else {
                        mainView.delTimerEffect();
                    }
                    if(playerChatTime == 1) playerDoChat = false;

                    log.setLogs(String.format("Player %d turn", turn + 1), State.Log);
                }

                if(time < 11) mainView.setTimer(10 - time);
                if(!eightTime) time++;

                if(time % playerRanPutTime == 0 && !players.get(turn).isSelf()){
                    Timeline temp = playerPutCard(players.get(turn));
                    temp.setOnFinished(e -> {
                        time = 0;
                        playerRanPutTime = ThreadLocalRandom.current().nextInt(2, 10);
                        playerDoChat = new Random().nextDouble() < 0.7;
                        playerChatTime = ThreadLocalRandom.current().nextInt(1,playerRanPutTime);
                    });
                    temp.play();
                }

                if(playerDoChat && time % playerChatTime == 0 && playerRanPutTime > 4 && !players.get(turn).isSelf()){
                    chat.addMessage(CPU_Msg.getMessage(new Random().nextInt(CPU_Msg.getSize())), players.get(turn));
                }

                if(!userDid && !eightTime){
                    if(time == 11) {
                        log.setLogs("Time out!", State.Error);
                        if(players.get(turn).getHand().size() < 12) {
                            timeOut(players.get(turn));
                            userDid = false;
                            time++; // Hold the turn until the animation completes
                        }
                    }
                }
            })
        );
        gameLoop.setCycleCount(Timeline.INDEFINITE);

        return gameLoop;
    }
    private Timeline playerPutCard(Player player) {
        for(Card card: player.getHand()){
            if(dummyCard.getCard().getSuit() == card.getSuit() || dummyCard.getCard().getRank() == card.getRank()){
                return new Timeline(new KeyFrame(Duration.seconds(1), event -> {mainView.putCardAnimationWithPlayer().setOnFinished(e->{
                    putCardDummy(card, false);
                    player.removeCard(card);
                });}));
            }
        }
        return new Timeline(new KeyFrame(Duration.seconds(1),event -> mainView.getCardAnimationToPlayer().setOnFinished(e -> player.setCard(deck))));
    }
    private void timeOut(Player player) {
        Timeline timeoutAnimation = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            if (player.isSelf()) {
                mainView.getCardAnimationToUser().setOnFinished(e -> {
                    mainView.removeAnimationCard();
                    player.setCard(deck);
                });
            } else {
                mainView.getCardAnimationToPlayer().setOnFinished(e -> {
                    mainView.removeAnimationCard();
                    player.setCard(deck);
                });
            }
        }));
        timeoutAnimation.setOnFinished(e -> time=0);
        timeoutAnimation.play();
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
            putCardDummy(deck.drawCard(), false);
        }));
        timeline.setOnFinished(e -> log.setLogs("Start Game!", State.System));
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
                    mainView.getDeck().setDisable(false);
                });
            } else {
                log.setLogs("Already has "+player.getCardLeft()+" cards!", State.Error);
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
        mainView.getMessage().setOnAction(event -> {
            String msg = mainView.getMessage().getText();
            if(!msg.isEmpty()){
                chat.addMessage(msg);
                mainView.getMessage().clear();
            }

        });
    }

    private void initPage(){
        root.getChildren().add(mainPane);
        scene.setRoot(root);
    }

    private void putCardDummy(Card card, boolean isEight){
        dummyCard.setCard(card, isEight);
        dummyCard.setImage();
        mainView.setCardDummy(dummyCard);
    }
    @Override
    public void update(Card card) {
        if(turn != -1 && players.get(turn).isSelf()) {
            userDid = true;
            if(card.getSuit() < 4 || card.getRank() == 7){
                eightTime = true;
                log.setLogs("Crazy Eight Time!", State.System);
                chooseEightView.generate();

                chooseEightView.getSpace().setOnMouseClicked(event -> {
                    event.consume();
                    fadeOutPane();
                    putCardDummy(new Card(0,7), true);
                    log.setLogs("Change to Space!", State.System);
                });
                chooseEightView.getHeart().setOnMouseClicked(event -> {
                    event.consume();
                    fadeOutPane();
                    putCardDummy(new Card(1,7), true);
                    log.setLogs("Change to Heart!", State.System);
                });
                chooseEightView.getDiamond().setOnMouseClicked(event -> {
                    event.consume();
                    fadeOutPane();
                    putCardDummy(new Card(2,7), true);
                    log.setLogs("Change to Diamond!", State.System);
                });
                chooseEightView.getClub().setOnMouseClicked(event -> {
                    event.consume();
                    fadeOutPane();
                    putCardDummy(new Card(3,7), true);
                    log.setLogs("Change to Club!", State.System);
                });
            } else {
                time = 0;
            }
        }
        removeDeckEffects();
        removeCardEffects();
        log.setLogs(String.format(" - Put %s %d card", card.getImogeSuit(), card.getRank() + 1), State.Log);
    }

    private void fadeOutPane(){
        Animation fadeOutPane = chooseEightView.getFadeOutPaneAnimation();
        fadeOutPane.play();
        fadeOutPane.setOnFinished(e -> {
            chooseEightView.getPane().getChildren().remove(chooseEightView.getOverlay());
            eightTime = false;
            time = 0;
        });
    }

    @Override
    public void update() {
        if(turn != -1 && players.get(turn).isSelf()) {
            userDid = true;
            time = 0;
        }
        if(turn != -1) log.setLogs(" - Draw card", State.Log);
    }

    @Override
    public void updateLog(String message, State state) {
        mainView.addLog(message, state);
    }

    @Override
    public void updateChat(String message, Player player) {
        if(player == null){
            mainView.addMsgFromUser(message);
        } else {
            mainView.addMsgFromPlayer(message, player);
        }

    }
}
