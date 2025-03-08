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
import java.util.Arrays;
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
    private boolean playerDoChat = Math.random() < 0.7;
    private int playerChatTime = ThreadLocalRandom.current().nextInt(1,playerRanPutTime);

    private boolean userDid = false;
    private boolean cardTime = false;
    private boolean settingTime = false;

    private boolean DEBUG = true;

    private int stackGetCard = 1;

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

        for(Player player : players) System.out.println(player.getPersonality());

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
                        turn = (turn+1)%playerNum;
                        userDid = false;
                        cardTime = false;
                        time = 0;

                        if(DEBUG) System.out.println(String.format("Turn: %d, userDid: %b, cardTime: %b, Stack: %d, Time: %d",turn, userDid, cardTime, stackGetCard, time));

                        updatePlayerTurn();
//                        if(playerDoChat && players.get(turn).getHand().size() == 1) conversation("I have only one cards left!", true);
//                        else if(playerDoChat && players.get(turn).getHand().size() < 3) conversation("I have only two cards left!", true);
//                        if(playerDoChat && players.get(turn).getHand().size() > 8) conversation("I have too many cards! I need a new strategy", true);
//
//                        conversation(chat.getLastMessage(), false);

                        if(players.get(turn).isSelf()){
                            mainView.setTimerEffect();
                        } else {
                            mainView.delTimerEffect();
                        }

                        if(playerChatTime == 1) playerDoChat = false;

                        log.setLogs(String.format("Player %d turn", turn + 1), State.Log);
                    }

                    if(time < 11) mainView.setTimer(10 - time);
                    if(!cardTime) time++;
                    else {
                        if(DEBUG) System.out.println(String.format("Time stop: %d, player put time: %d",time, playerRanPutTime));
                    }

                    if(!cardTime && time % playerRanPutTime == 0 && !players.get(turn).isSelf()){
                        if(DEBUG) System.out.println(String.format("Turn %d doing", turn));
                        playerPutCard(players.get(turn));
                        time++;
                    }

                    if(!userDid){
                        if(time == 11) {
                            log.setLogs("Time out!", State.Error);
                            if(players.get(turn).getHand().size() < 12) {
                                timeOut(players.get(turn));
                                time++;
                            }
                        }
                    } else {
                        if(DEBUG) System.out.println(String.format("User did!, Stack %d", stackGetCard));
                        time = 0;
                        userDid = false;
                        cardTime = false;
                    }
                })
        );
        gameLoop.setCycleCount(Timeline.INDEFINITE);

        return gameLoop;
    }
    private void playerPutCard(Player player) {
        if(stackGetCard > 1) {
            if(dummyCard.getCard().getRank() == 1){
                for(Card card : player.getHand()) {
                    if(dummyCard.getCard().getRank() == card.getRank()) {
                        cardTime = true;
                        Animation putCard = mainView.putCardAnimationWithPlayer();
                        putCard.setOnFinished(e -> {
                            putCardDummy(card, false);
                            player.removeCard(card);
                            time = 0;
                            cardTime = false;
                            playerRanPutTime = ThreadLocalRandom.current().nextInt(2, 10);
                            playerDoChat = Math.random() < 0.7;
                            playerChatTime = ThreadLocalRandom.current().nextInt(1,playerRanPutTime);
                            if(DEBUG) System.out.println(String.format("Turn %d done, Stack %d", turn, stackGetCard));
                        });
                        putCard.play();
                        return;
                    }
                }
                cardTime = true;
                drawCards(player);
                return;
            }
        }

        for(Card card: player.getHand()){
            if(dummyCard.getCard().getSuit() == card.getSuit() || dummyCard.getCard().getRank() == card.getRank()){
                cardTime = true;
                Animation putCard = mainView.putCardAnimationWithPlayer();
                putCard.setOnFinished(e -> {
                    putCardDummy(card, false);
                    player.removeCard(card);
                    time = 0;
                    cardTime = false;
                    playerRanPutTime = ThreadLocalRandom.current().nextInt(2, 10);
                    playerDoChat = Math.random() < 0.7;
                    playerChatTime = ThreadLocalRandom.current().nextInt(1,playerRanPutTime);
                    if(DEBUG) System.out.println(String.format("Turn %d done, Stack %d", turn, stackGetCard));
                });
                putCard.play();
                return;
            }
        }
        cardTime = true;
        drawCards(player);
    }
    private void drawCards(Player player){
        if(DEBUG) System.out.println(String.format("Drawing cards"));
        if(player.getCardLeft() + stackGetCard > 12) stackGetCard = 12 - player.getCardLeft();

        Timeline drawMotion = new Timeline(new KeyFrame(Duration.seconds(1),event -> {
            Animation getCard = mainView.getCardAnimationToPlayer();
            getCard.setOnFinished(e->{
                player.setCard(deck);
            });
        }));
        drawMotion.setCycleCount(stackGetCard);
        drawMotion.setOnFinished(e -> {
            delaySecond(()->{
                if(playerDoChat) conversation("I have no playable cards and must draw a new one", true);
                stackGetCard = 1;
                time = 0;
                cardTime = false;
                playerRanPutTime = ThreadLocalRandom.current().nextInt(2, 10);
                playerDoChat = Math.random() < 0.7;
                playerChatTime = ThreadLocalRandom.current().nextInt(1,playerRanPutTime);
                if(DEBUG) System.out.println(String.format("Turn %d done, Stack %d", turn, stackGetCard));
            });
        });
        drawMotion.play();
    }


    private void timeOut(Player player) {
        if(player.getCardLeft() == 12) return;
        if(player.getCardLeft() + stackGetCard > 12) stackGetCard = 12 - player.getCardLeft();
        cardTime = true;
        Timeline timeoutAnimation = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            Animation getCards = mainView.getCardAnimationToUser();
            getCards.setOnFinished(e -> {
                player.setCard(deck);
            });
        }));
        timeoutAnimation.setOnFinished(e -> {
            delaySecond(()->{
                time=0;
                userDid = false;
                cardTime = false;
                stackGetCard = 1;
            });
        });
        timeoutAnimation.setCycleCount(stackGetCard);
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
        settingTime = true;
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            putCardDummy(deck.drawCard(), true);
        }));

        timeline.setOnFinished(e -> {
            settingTime = false;
            log.setLogs("Start Game!", State.System);
        });

        return timeline;
    }

    private ParallelTransition getSixCards(){
        settingTime = true;
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
            giveCard.setOnFinished(e->{
                delaySecond(()->settingTime=false);
            });
            giveCard.setCycleCount(6);
            pt.getChildren().add(giveCard);
        }
        return pt;
    }
    private void delaySecond(Runnable action){
        PauseTransition delay = new PauseTransition(Duration.seconds(1)); // 1초 딜레이
        delay.setOnFinished(ev -> action.run());
        delay.play();
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
            mainView.getDeck().setDisable(true);
            if(player.getCardLeft() == 12) return;
            if(player.getCardLeft() + stackGetCard > 12) stackGetCard = 12 - player.getCardLeft();
            cardTime = true;
            Timeline getCards = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
                Animation getAnimation = mainView.setGetCardAnimation(player.getCardLeft());
                getAnimation.setOnFinished(ev -> {
                    mainView.removeAnimationCard();
                    player.setCard(deck);
                    removeDeckEffects();
                    mainView.getDeck().setDisable(false);
                });
            }));
            getCards.setOnFinished(e -> {
                delaySecond(()->{
                    userDid = true;
                    stackGetCard = 1;
                });
            });
            getCards.setCycleCount(stackGetCard);
            getCards.play();
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
                    if(stackGetCard > 1){
                        if(dummy.getRank() == 1 && dummy.getRank() == card.getRank()){
                            mainView.setDragReleased(ev, cardImg, player, dummyCard, true);
                        }else{
                            mainView.setDragReleased(ev, cardImg, player, dummyCard, false);
                        }
                    } else {
                        if(dummy.getSuit() == card.getSuit() || dummy.getRank() == card.getRank()){
                            mainView.setDragReleased(ev, cardImg, player, dummyCard, true);
                        }else{
                            mainView.setDragReleased(ev, cardImg, player, dummyCard, false);
                        }
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

    private void putCardDummy(Card card, boolean skipObserver){
        dummyCard.setCard(card, skipObserver);
        dummyCard.setImage();
        mainView.setCardDummy(dummyCard);
    }
    public List<Player> emptyDeck(){
        log.setLogs("Deck is empty!", State.Error);
        return players;
    }
    public void resetDeck(){
        settingTime = true;
        log.setLogs("Resetting Deck", State.Error);
        putCardDummy(new Card(), true);
        putStartDummyCard().play();
    }

    @Override
    public void update(Card card) {
        if(settingTime) return;
        log.setLogs(String.format(" - Put %s %d card", card.getImogeSuit(), card.getRank() + 1), State.Log);
        if(DEBUG) System.out.println(String.format(" - Put %s %d card", card.getImogeSuit(), card.getRank() + 1));

        if(card.getRank() == 7) {
            whenCardEight();
        } else if (card.getRank() == 1) {
            whenCardTwo();
        } else if (players.get(turn).isSelf()) {
            time = 0;
            userDid = true;
            cardTime = false;
        }

        removeDeckEffects();
        removeCardEffects();
    }

    private void whenCardTwo(){
        if(stackGetCard == 1) stackGetCard+=1;
        else stackGetCard += 2;

        if(players.get(turn).isSelf()){
            userDid = true;
        } else {
            time = 0;
        }

        if(DEBUG) System.out.println("Current stack: " + stackGetCard);
    }

    private void whenCardEight(){
        if(players.get(turn).isSelf()){
            cardTime = true;
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
            int shape = players.get(turn).getMostShape();
            switch(shape){
                case 0 -> log.setLogs("Change to Space!", State.System);
                case 1 -> log.setLogs("Change to Heart!", State.System);
                case 2 -> log.setLogs("Change to Diamond!", State.System);
                case 3 -> log.setLogs("Change to Club!", State.System);
            }
            putCardDummy(new Card(shape, 7), true);
            time = 0;
        }
    }

    private void conversation(String msg, boolean sayself){
        Player player = players.get(new Random().nextInt(4));
        while(player.isSelf()){
            player = players.get(new Random().nextInt(4));
        }

        if (Arrays.stream(chat.getRecentMessage().split(" ")).toList().contains(msg.toLowerCase())) return;

        if(sayself){
            String context = chat.getRecentMessage();
            boolean shouldAskQuestion = Math.random() < 0.2;

            String prompt = "Here is the recent conversation:\n" + context +
                    "\nSay to " + msg +
                    (shouldAskQuestion ? " Also, ask a relevant question to keep the conversation going." : "");
            String responds = AI.generate(prompt, players.get(turn));

            chat.addMessage(responds, player);

            if(Math.random() < 0.2) conversation(responds, false);
        }else{
            double baseResponseChance = 0.5;
            if(Math.random() < baseResponseChance) {
                String context = chat.getRecentMessage();
                boolean shouldAskQuestion = Math.random() < 0.2;

                String prompt = "Here is the recent conversation:\n" + context +
                        "\nRespond to " + msg +
                        (shouldAskQuestion ? " Also, ask a relevant question to keep the conversation going." : "");
                String responds = AI.generate(prompt, player);

                chat.addMessage(responds, player);

                if(Math.random() < 0.3) conversation(responds, false);
            }
        }
    }

    private void fadeOutPane(){
        Animation fadeOutPane = chooseEightView.getFadeOutPaneAnimation();
        fadeOutPane.play();
        fadeOutPane.setOnFinished(e -> {
            chooseEightView.getPane().getChildren().remove(chooseEightView.getOverlay());
            cardTime = false;
            userDid = true;
        });
    }

    @Override
    public void update() {
        if(settingTime) return;

        log.setLogs(" - Draw card", State.Log);
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
