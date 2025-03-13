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

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class SinglePlayGameController implements CardObserver, DeckObserver, LogObserver, ChatObserver {
    private Scene scene;
    private StackPane root;
    private BorderPane mainPane;

    private SinglePlayGameView mainView;
    private SettingView settingView;
    private ChooseEightView chooseEightView;
    private CharacterChooseView characterChooseView;
    private ScoringView scoringView;

    private List<Player> players = new ArrayList<>();
    private final int playerNum = 4;
    private Deck deck;
    private DummyCard dummyCard;
    private Log log;
    private Chat chat;

    private int playerRanPutTime = ThreadLocalRandom.current().nextInt(2, 10);
    private boolean playerDoChat = Math.random() < 0.7;
    private int playerChatTime = ThreadLocalRandom.current().nextInt(1,playerRanPutTime);

    private String userCharacter;
    private GameStatusManager statusManager;

    private final boolean DEBUG = true;

    private int stackGetCard = 1;

    private Timeline game = null;

    private final int[] scoreTable = {5,3,1,0};


    public SinglePlayGameController(Scene _scene) {
        scene = _scene;
        root = new StackPane();
        mainPane = new BorderPane();
        mainView = new SinglePlayGameView(mainPane);
        settingView = new SettingView(root);
        chooseEightView = new ChooseEightView(root);
        characterChooseView = new CharacterChooseView(root);
        scoringView = new ScoringView(root);
        deck = new Deck(this);
        dummyCard = new DummyCard(this);
        log = new Log(this);
        chat = new Chat(this);
        statusManager = new GameStatusManager(playerNum);
    }

    public void selectCharacter(Runnable function){
        initPage();
        characterChooseView.generate();

        for(Pair<ImageView, String> pair: characterChooseView.getCharacters()){
            ImageView character = pair.getKey();
            character.setOnMouseClicked(e -> {
                e.consume();
                userCharacter = pair.getValue();
                Animation fadeOut = characterChooseView.getFadeOutPaneAnimation();
                fadeOut.play();

                fadeOut.setOnFinished(ev->{
                    root.getChildren().remove(characterChooseView.getOverlay());
                    function.run();
                });
            });
        }
    }

    public void startGame(){
        drawGamePage();

        deck.generateDeck();
        createPlayers();

        if(Setting.isEnClicked()) log.setLogs("Setting Game..." , State.System);
        else log.setLogs("게임 설정 중...", State.System);


        SequentialTransition sequence = new SequentialTransition();

        sequence.getChildren().add(getSixCards());

        sequence.getChildren().add(putStartDummyCard());

        sequence.play();

        sequence.setOnFinished(event -> {
            delaySecond(()->{
                gameLoop();
                game.play();
                disableButtons(false);
            });
        });

    }
    private void gameLoop(){
        Timeline gameLoop = new Timeline(
                new KeyFrame(Duration.seconds(1),event -> {
                    if(statusManager.getTime() % 11 == 0){
                        statusManager.nextTurn();
                        statusManager.resetPassTurn();
                        statusManager.resetUserDid();
                        statusManager.resetTime();
                        statusManager.resetFourTime();
                        statusManager.resetQueenTime();
                        if(DEBUG) System.out.printf("Turn changed : %d -> %d\n", statusManager.getTurn() - 1, statusManager.getTurn());
                        if(DEBUG) System.out.println("Current deck size: " + deck.deckSize());

                        updatePlayerTurn();

                        if(!players.get(statusManager.getTurn()).isSelf()){
                            if(Setting.isEnClicked()){
                                if(playerDoChat) chat.addMessage(CPU_Msg.getEnglishChatResponse(), players.get(statusManager.getTurn()));
                                if(players.get(statusManager.getTurn()).getCardLeft() > 9) chat.addMessage(CPU_Msg.getEnglishTooManyCards(), players.get(statusManager.getTurn()));
                                if(players.get(statusManager.getTurn()).getCardLeft() < 2) chat.addMessage(CPU_Msg.getEnglishFewCardsLeft(), players.get(statusManager.getTurn()));
                            }
                            else{
                                if(playerDoChat) chat.addMessage(CPU_Msg.getKoreanChatResponse(), players.get(statusManager.getTurn()));
                                if(players.get(statusManager.getTurn()).getCardLeft() > 9) chat.addMessage(CPU_Msg.getKoreanTooManyCards(), players.get(statusManager.getTurn()));
                                if(players.get(statusManager.getTurn()).getCardLeft() < 2) chat.addMessage(CPU_Msg.getKoreanFewCardsLeft(), players.get(statusManager.getTurn()));
                            }
                        }


                        setTurnEffect();

                        if(playerChatTime == 1) playerDoChat = false;

                        if(Setting.isEnClicked()) log.setLogs(String.format("Player %d turn", statusManager.getTurn() + 1), State.Log);
                        else log.setLogs(String.format("플레이어 %d 차례", statusManager.getTurn() + 1), State.Log);
                    }
                    if(DEBUG) System.out.printf(statusManager.toString());

                    if(statusManager.isFourTime() && statusManager.isQueenTime()){
                        Player player = players.get(statusManager.getTurn());
                        if(player.isSelf()){
                            if(statusManager.getTime() == 10 && !statusManager.isUserDid()){    // 이 userDid의 플래그는 다르게 애니메이션이 시작되기 전에 플래그를 바꿔줘야함
                                if(Setting.isEnClicked()) log.setLogs("Time out!", State.Error);
                                else log.setLogs("시간 초과!", State.Error);

                                userDrawCard(player);
                            }
                            // 유저는 3가지 상태가 있음.
                            // 1. 핸드의 카드가 release될때
                            // 2. 덱을 클릭을 했을때
                            // 3. time out이 되어 강제로 카드를 먹어야 할때
                        } else if(statusManager.getTime() == playerRanPutTime) {
                            // 플레이어는 항상 내가 낼 타임에 딱 한번 들어갈 수 있음
                            playerPutCard(player);
                            // 플레이어는 2가지 상태가 있음
                            // 1. 내 덱에 내보낼카드가 있을 때
                            // 2. 낼 카드가 없어 카드를 먹어야 할때
                        }
                        // 예외의 상태가 있음.
                        // 1. 전의 플레이어가 4를 냈을 때 여기가 아닌 다른 곳에서 턴이 바뀜
                        // 2....

                        // 시간은 10일때까지 흘러감
                        // 이 루프는 항상 infinity 흘러가기 때문에 유저나 플레이어가 cardTime이 false가 될때까지 끝나지 않음
                        mainView.setTimer(10 - statusManager.getTime());
                        if(statusManager.getTime() < 10){
                            statusManager.addTime();
                        }
                    }

                    // 이 플래그가 중요! 항상 carTime은 턴이 바뀔때 딱 한번 초기화를 시킴. 또한, 카드가 움직이고, 애니메이션이 끝날때 false로 끝남을 알림
                    // 이것이 false로 바뀔 때까지 그 플레이어의 턴은 끝나지 않음
                    if(statusManager.isPassTurn()){
                        if(players.get(statusManager.getTurn()).getCardLeft() == 0){
                            endGame();
                        }


                        statusManager.resetTime();

                        playerRanPutTime = ThreadLocalRandom.current().nextInt(2, 10);
                        playerDoChat = Math.random() < 0.7;
                        playerChatTime = ThreadLocalRandom.current().nextInt(1,playerRanPutTime);
                    }
                })
        );
        gameLoop.setCycleCount(Timeline.INDEFINITE);

        game = gameLoop;
    }
    private void endGame(){
        game.pause();
        if(DEBUG) System.out.println("Game is done..!");
        if(Setting.isEnClicked()) log.setLogs("Game Over!", State.System);
        else log.setLogs("게임 종료!", State.System);

        scoringView.generate(players, scoring());
        scoringView.buttonAnimation();
        scoringView.getContinueButton().setOnMouseClicked(e->{
            scoringView.fadeOutPane();
            mainView.resetGame(scene, mainPane, players);
            game.stop();
        });
        scoringView.getExitButton().setOnMouseClicked(e->{
            scoringView.fadeOutPane();
            mainView.setFadeOutSinglePlay(scene);
            game.stop();
        });
    }

    private Map<Player, Pair<Integer, Integer>> scoring() {
        // 1. 플레이어를 남은 카드 수 기준으로 정렬
        players.sort(Comparator.comparingInt(Player::getCardLeft));

        Map<Player, Pair<Integer, Integer>> scoreMap = new HashMap<>(); // {Player -> (순위, 점수)}
        int[] scoreTable = {5, 3, 1, 0};  // 1등, 2등, 3등, 4등 점수

        int rank = 1;  // 현재 순위
        int scoreIndex = 0;  // 점수 테이블 인덱스
        int countInRank = 0;  // 현재 등수 내에서 같은 카드 개수를 가진 사람 수
        boolean hasThirdPlace = false;  // 3등 존재 여부

        for (int i = 0; i < players.size(); ++i) {
            // 이전 플레이어와 카드 수가 다르면 순위 증가
            if (i > 0 && players.get(i).getCardLeft() > players.get(i - 1).getCardLeft()) {
                if (rank == 1) rank = 2;  // 1등은 1명만
                else if (rank == 2 && countInRank >= 3) rank = 3;  // 2등이 3명이면 그 다음은 3등
                else if (rank == 3 && countInRank >= 2) rank = 4;  // 3등이 2명이면 4등
                else rank++;

                if (rank == 3) hasThirdPlace = true; // 3등 등장 확인
                scoreIndex = Math.min(rank - 1, 3); // 점수 테이블 조정
                countInRank = 0;
            }

            countInRank++;  // 현재 등수에 몇 명 있는지 카운트
            int score = scoreTable[scoreIndex];  // 현재 등수에 맞는 점수 적용
            players.get(i).addScore(score);

            // 플레이어별 순위 및 점수 저장
            scoreMap.put(players.get(i), new Pair<>(rank, score));
        }

        // 3등이 없으면, 4등을 3등으로 승격
        if (!hasThirdPlace) {
            for (Map.Entry<Player, Pair<Integer, Integer>> entry : scoreMap.entrySet()) {
                if (entry.getValue().getKey() == 4) {
                    scoreMap.put(entry.getKey(), new Pair<>(3, scoreTable[2]));  // 3등 점수 (1점)으로 변경
                }
            }
        }

        players.sort(Comparator.comparingInt(p -> scoreMap.get(p).getKey()));

        return scoreMap;
    }


    private void playerPutCard(Player player) {
        // 이 상황은 stack에 카드가 쌓임 > 예상 상황 : 2
        if(stackGetCard > 1) {
            // 더미에 2인 상황
            if(dummyCard.getCard().getRank() == 1){
                for(Card card : player.getHand()) {
                    if(dummyCard.getCard().getRank() == card.getRank()) {
                        Animation putCard = mainView.putCardAnimationWithPlayer();
                        putCard.setOnFinished(e -> {
                            // 여기에 모든 초기화 코드 삭제. 꼭! putCardDummy에서 이어진 옵저버에서 초기화를 해줘야함 > cardTime
                            putCardDummy(card, false);
                            player.removeCard(card);
                        });
                        putCard.play();
                        return;
                    }
                }

                drawCards(player);
                return;
            }
        }

        // stack에 쌓여 있지 않는 상태
        for(Card card: player.getHand()){
            if(dummyCard.getCard().getSuit() == card.getSuit() || dummyCard.getCard().getRank() == card.getRank()){
                Animation putCard = mainView.putCardAnimationWithPlayer();
                putCard.setOnFinished(e -> {
                    // 여기도 옵저버에서 cardTime 초기화해줘야함!
                    putCardDummy(card, false);
                    player.removeCard(card);
                });
                putCard.play();
                return;
            }
        }

        drawCards(player);
    }

    private void drawCards(Player player){
        if(clamping(player)) return;

        final Timeline drawMotion = new Timeline(new KeyFrame(Duration.seconds(1),event -> {
            Animation getCard = mainView.getCardAnimationToPlayer();
            getCard.setOnFinished(e->{
                if(DEBUG) System.out.println("Drawing card");
                if(Setting.isEnClicked()) log.setLogs("Drawing card!", State.Log);
                else log.setLogs("카드 드로우!", State.Log);

                player.setCard(deck, false);
            });
            getCard.play();
        }));
        drawMotion.setCycleCount(stackGetCard);
        drawMotion.setOnFinished(e -> {
            if(Setting.isEnClicked()) chat.addMessage(CPU_Msg.getEnglishBadDraw(), player);
            else chat.addMessage(CPU_Msg.getKoreanBadDraw(), player);
            delaySecond(()->{
                // 여기선 옵저버가 call 경우가 없으니 여기서 초기화!
                stackGetCard = 1;
                statusManager.doPassTurn();
                statusManager.resetFourTime();  // 4일 경우, 여기도 들어오니 초기화
            });
        });

        drawMotion.play();
    }

    private void userDrawCard(Player player) {
        if(clamping(player)) return;

        statusManager.doUserDid();  // 플래그 바꿈! Time out에 다시는 들어가지 않음

        Timeline timeoutAnimation = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            Animation getCards = mainView.getCardAnimationToUser();
            getCards.setOnFinished(e -> {
                player.setCard(deck, false);
                if(DEBUG) System.out.println("Drawing card");
                if(Setting.isEnClicked()) log.setLogs("Drawing card!", State.Log);
                else log.setLogs("카드 드로우!", State.Log);
            });
            getCards.play();
        }));
        timeoutAnimation.setCycleCount(stackGetCard);
        timeoutAnimation.setOnFinished(e -> {
            delaySecond(()->{
                statusManager.doPassTurn();
                stackGetCard = 1;
                statusManager.resetFourTime();  // 4일 경우, 여기도 들어오니 초기화
            });
        });

        timeoutAnimation.play();
    }

    private void updatePlayerTurn(){
        for(Player player: players){
            player.setMyTurn(false);
            removeCardEffects();
            removeDeckEffects();
        }
        Player player = players.get(statusManager.getTurn());
        player.setMyTurn(true);
        if(statusManager.isFourTime() && player.isSelf()) {
            addCardEffects(player);
            addDeckEffects(player);
        }
    }

    private void addDeckEffects(Player player){
        mainView.getDeck().setOnMouseClicked(event -> {
            mainView.getDeck().setDisable(true);

            if(clamping(player)) return;

            statusManager.doUserDid();  // 플래그 바꿈! - Time out이 안됨

            Timeline getCards = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
                Animation getAnimation = mainView.setGetCardAnimation(player.getCardLeft());
                getAnimation.setOnFinished(ev -> {
                    mainView.removeAnimationCard();
                    player.setCard(deck, false);
                    removeDeckEffects();
                    mainView.getDeck().setDisable(false);
                });

                getAnimation.play();
            }));
            getCards.setOnFinished(e -> {
                delaySecond(()->{
                    statusManager.doPassTurn();
                    stackGetCard = 1;
                });
            });
            getCards.setCycleCount(stackGetCard);

            getCards.play();
        });
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
            // 유저가 카드를 놓았을때!
            if(cardImg.getOnMouseReleased() == null) {
                cardImg.setOnMouseReleased(ev -> {
                    Card dummy = dummyCard.getCard();
                    if(stackGetCard > 1){
                        // 카드가 2
                        if(dummy.getRank() == 1 && dummy.getRank() == card.getRank()){
                            statusManager.doUserDid();
                            mainView.setDragReleased(ev, cardImg, player, dummyCard, true);
                        }else{
                            mainView.setDragReleased(ev, cardImg, player, dummyCard, false);
                        }
                    } else {
                        if(dummy.getSuit() == card.getSuit() || dummy.getRank() == card.getRank()){
                            statusManager.doUserDid();
                            mainView.setDragReleased(ev, cardImg, player, dummyCard, true);
                        }else{
                            mainView.setDragReleased(ev, cardImg, player, dummyCard, false);
                        }
                    }
                });

            }
        }
    }

    private void putCardDummy(Card card, boolean skipObserver){
        dummyCard.setCard(card, skipObserver);
        dummyCard.setImage();
        mainView.setCardDummy(dummyCard);
    }

    @Override
    public void update(Card card) {
        if (DEBUG) System.out.println(String.format(" - Put %s %d card", card.getImogeSuit(), card.getRank() + 1));
        if(Setting.isEnClicked()) log.setLogs(String.format("Put %s %s card", card.getImogeSuit(), card.getRankString()), State.Log);
        else log.setLogs(String.format("카드 %s %s 놓음", card.getKoreanSuit(), card.getRankString()), State.Log);

        if (card.getRank() == 7) {
            whenCardEight();
        } else if(card.getRank() == 0) {
            whenCardAce();
        } else if (card.getRank() == 1) {
            whenCardTwo();
        } else if (card.getRank() == 3) {
            whenCardFour();
        } else if(card.getRank() == 11) {
            whenCardQueen();
        }else {
            statusManager.doPassTurn();
        }

        removeDeckEffects();
        removeCardEffects();
    }

    @Override
    public void updateAddToDeck(Card card) {
        deck.add(card);
    }

    private void whenCardAce(){
        statusManager.setReverseOrder();
        if(DEBUG) System.out.println("Turn is reversed..!");
        if(Setting.isEnClicked()) log.setLogs("Turn is reversed..!", State.System);
        else log.setLogs("순서가 바뀜..!", State.System);
        statusManager.doPassTurn();
    }

    private void whenCardQueen(){
        statusManager.setQueenTime();
        statusManager.nextTurn();
        updatePlayerTurn();
        setTurnEffect();

        if(DEBUG) System.out.printf("Skip player %d\n", statusManager.getTurn());
        if(Setting.isEnClicked()) log.setLogs("Skip next player!", State.System);
        else log.setLogs("다음 플레이어 스킵!", State.System);
        delayQueen(()->{
            statusManager.doPassTurn();
            statusManager.resetQueenTime();
        });
    }

    private void whenCardFour(){
        if(DEBUG) System.out.printf("Card Left %d\n",players.get(statusManager.getTurn()).getCardLeft());
        if(players.get(statusManager.getTurn()).getCardLeft() == 1){
            players.get(statusManager.getTurn()).removeCard(0);
            endGame();
            return;
        }
        statusManager.setFourTime();
        stackGetCard += 3;

        if(Setting.isEnClicked()) chat.addMessage(CPU_Msg.getEnglishAttack(), players.get(statusManager.getTurn()));
        else chat.addMessage(CPU_Msg.getKoreanAttack(), players.get(statusManager.getTurn()));

        statusManager.nextTurn();
        updatePlayerTurn();
        setTurnEffect();
        Player player = players.get(statusManager.getTurn());
        if(Setting.isEnClicked()) log.setLogs(String.format("Player %d gets 4 cards!", player.getScoreId()), State.System);
        else log.setLogs(String.format("플레이어 %d 4장 카드 드로우!", player.getScoreId()), State.System);
        if(player.isSelf()){
            userDrawCard(player);   // 여기서 cardTimeDid를 호출
            return;
        }
        drawCards(player);  // 여기서 cardTieDid를 호출
    }

    private void whenCardTwo(){
        if(stackGetCard == 1) stackGetCard+=1;
        else stackGetCard += 2;

        statusManager.doPassTurn();    // 여기가 마지막 초기화함!

        if(Setting.isEnClicked()) chat.addMessage(CPU_Msg.getEnglishAttack(), players.get(statusManager.getTurn()));
        else chat.addMessage(CPU_Msg.getKoreanAttack(), players.get(statusManager.getTurn()));

        if(DEBUG) System.out.println("Current stack: " + stackGetCard);
        if(Setting.isEnClicked()) log.setLogs(String.format("Current %d cards are stacked", stackGetCard), State.System);
        else log.setLogs(String.format("현재 %d개 카드 쌓임", stackGetCard), State.System);
    }

    private void whenCardEight(){
        if(players.get(statusManager.getTurn()).isSelf()){
            log.setLogs("Crazy Eight Time!", State.System);
            chooseEightView.generate();

            chooseEightView.getSpace().setOnMouseClicked(event -> {
                event.consume();
                putCardDummy(new Card(0,7), true);
                fadeOutPane();
                if(DEBUG) System.out.println("Change to Space!");
                if(Setting.isEnClicked()) log.setLogs("Change to Space!", State.System);
                else log.setLogs("스페이드로 바꿈!", State.System);
            });
            chooseEightView.getHeart().setOnMouseClicked(event -> {
                event.consume();
                putCardDummy(new Card(1,7), true);
                fadeOutPane();
                if(DEBUG) System.out.println("Change to Heart!!");
                if(Setting.isEnClicked()) log.setLogs("Change to Heart!", State.System);
                else log.setLogs("하트로 바꿈!", State.System);
            });
            chooseEightView.getDiamond().setOnMouseClicked(event -> {
                event.consume();
                putCardDummy(new Card(2,7), true);
                fadeOutPane();
                if(DEBUG) System.out.println("Change to Diamond!!");
                if(Setting.isEnClicked()) log.setLogs("Change to Diamond!", State.System);
                else log.setLogs("다이아몬드로 바꿈!", State.System);
            });
            chooseEightView.getClub().setOnMouseClicked(event -> {
                event.consume();
                putCardDummy(new Card(3,7), true);
                fadeOutPane();
                if(DEBUG) System.out.println("Change to Club!");
                if(Setting.isEnClicked()) log.setLogs("Change to Club!", State.System);
                else log.setLogs("크로버로 바꿈!", State.System);
            });
        } else {
            int shape = players.get(statusManager.getTurn()).getMostShape();
            switch(shape){
                case 0:
                    if(Setting.isEnClicked()) log.setLogs("Change to Space!", State.System);
                    else log.setLogs("스페이드로 바꿈!", State.System);
                    break;
                case 1:
                    if(Setting.isEnClicked()) log.setLogs("Change to Heart!", State.System);
                    else log.setLogs("하트로 바꿈!", State.System);
                    break;
                case 2:
                    if(Setting.isEnClicked()) log.setLogs("Change to Diamond!", State.System);
                    else log.setLogs("다이아몬드로 바꿈!", State.System);
                    break;
                case 3:
                    if(Setting.isEnClicked()) log.setLogs("Change to Club!", State.System);
                    else log.setLogs("크로버로 바꿈!", State.System);
            }
            if(DEBUG) System.out.println("Changed the shape!");
            putCardDummy(new Card(shape, 7), true);
            statusManager.doPassTurn();
        }
    }

    private void fadeOutPane(){
        Animation fadeOutPane = chooseEightView.getFadeOutPaneAnimation();
        fadeOutPane.play();
        fadeOutPane.setOnFinished(e -> {
            delaySecond(()->{
                chooseEightView.getPane().getChildren().remove(chooseEightView.getOverlay());
                statusManager.doPassTurn();
            });
        });
    }

    @Override
    public void update() {

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

    private Timeline putStartDummyCard(){
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            putCardDummy(deck.drawCard(), true);
        }));

        timeline.setOnFinished(e -> {
            delaySecond(()->{
                if(Setting.isEnClicked()) log.setLogs("Start Game!", State.System);
                else log.setLogs("게임 시작!", State.System);
                for(Player player: players) {
                    if(new Random().nextBoolean() && !player.isSelf()){
                        if(Setting.isEnClicked()) chat.addMessage(CPU_Msg.getEnglishGreeting(), player);
                        else chat.addMessage(CPU_Msg.getKoreanGreeting(), player);
                    }
                }
            });
        });

        return timeline;
    }
    public void resetGame(List<Player> prevPlayers){
        initPage();
        drawGamePage();

        deck.generateDeck();
        Collections.sort(prevPlayers, Collections.reverseOrder());

        transformPlayers(prevPlayers);
        for(Player player: players) System.out.println(player.getScore());

        if(Setting.isEnClicked()) log.setLogs("Setting Game...!", State.System);
        else log.setLogs("게임 설정 중...!" , State.System);

        SequentialTransition sequence = new SequentialTransition();

        sequence.getChildren().add(getSixCards());

        sequence.getChildren().add(putStartDummyCard());

        sequence.play();

        sequence.setOnFinished(event -> {
            delaySecond(()->{
                gameLoop();
                game.play();
                disableButtons(false);
            });
        });
        System.out.println("New Game Start");
        if(Setting.isEnClicked()) log.setLogs("New Game Start...!", State.System);
        else log.setLogs("새로운 게임 시작...!", State.System);
    }

    private void transformPlayers(List<Player> prevPlayers) {
        players = new ArrayList<>();
        int scoreId=0;
        int statusId = 0;
        for(int i=0; i<4; ++i){
            Player prevPlayer = prevPlayers.get(i);
            Player player = new Player(i);
            players.add(player);
            player.copyPlayer(prevPlayer);
            if(prevPlayer.isSelf()) {
                player.setSelf();
                player.setScoreId(scoreId++);
                player.setStatusId(3);
                new PlayerHandView(player, mainView);
                new PlayerScoreView(player, mainView);
            }else{
                player.setScoreId(scoreId++);
                player.setStatusId(statusId++);
                new PlayerStatusView(player, mainView);
                new PlayerScoreView(player, mainView);
            }
            player.resetHand();
            player.callNotify();
        }
    }

    private void createPlayers(){
        Random random = new Random();
        List<String> chosenPlayers = new ArrayList<>();
        chosenPlayers.add(userCharacter);
        for(int i = 0; i < playerNum-1; i++){
            Player player = new Player(i);
            players.add(player);
            new PlayerStatusView(player, mainView);
            new PlayerScoreView(player, mainView);
            String url;
            do{
                int intUser = random.nextInt(7) + 1;
                url = String.format("/avatar/User-0%d.png",intUser);
            }while(chosenPlayers.contains(url));

            player.setIcon(url);
            chosenPlayers.add(url);
        }
        createUser();
    }
    private void createUser(){
        Player player = new Player(playerNum-1);
        player.setSelf();
        players.add(player);
        new PlayerScoreView(player, mainView);
        new PlayerHandView(player, mainView);
        player.setIcon(userCharacter);
    }

    private ParallelTransition getSixCards(){
        ParallelTransition pt = new ParallelTransition();
        for(Player player : players){
            Timeline giveCard;
            if(player.isSelf()){
                giveCard = new Timeline(
                        new KeyFrame(Duration.seconds(1), event -> {
                            Animation animation = mainView.getCardAnimationToUser();
                            animation.setOnFinished(ev -> {
                                mainView.removeAnimationCard();
                                player.setCard(deck, false);
                            });

                            animation.play();
                        })
                );
            }else{
                giveCard = new Timeline(
                        new KeyFrame(Duration.seconds(1), event -> {
                            Animation animation = mainView.getCardAnimationToPlayer();
                            animation.setOnFinished(ev -> {
                                mainView.removeAnimationCard();
                                player.setCard(deck, false);
                            });

                            animation.play();
                        })
                );
            }
            giveCard.setCycleCount(6);
            pt.getChildren().add(giveCard);
        }
        if(Setting.isEnClicked()) log.setLogs("Give 6 cards to players!", State.System);
        else log.setLogs("플레이어들에게 6장의 카드 나눠 주는 중!", State.System);

        return pt;
    }
    public void delaySecond(Runnable action){
        PauseTransition delay = new PauseTransition(Duration.seconds(1)); // 1초 딜레이
        delay.setOnFinished(ev -> action.run());
        delay.play();
    }
    private void delayQueen(Runnable action){
        PauseTransition delay = new PauseTransition(Duration.millis(500)); // 0.5초 딜레이
        delay.setOnFinished(ev -> action.run());
        delay.play();
    }

    private void removeDeckEffects(){
        if(mainView.getDeck().getOnMouseClicked() != null){
            mainView.getDeck().setOnMouseClicked(null);
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
        root.setStyle("");
        mainView.drawMainPage(playerNum);

        mainView.getBack().setOnMouseClicked(event -> {
            mainView.setFadeOutSinglePlay(scene);
            game.stop();
        });
        mainView.getSetting().setOnMouseClicked(event -> settingView.generate());
        mainView.getRestart().setOnMouseClicked(event -> {
            mainView.resetGame(scene, mainPane, null);
            game.stop();
        });
        mainView.getMessage().setOnAction(event -> {
            String msg = mainView.getMessage().getText();
            if(!msg.isEmpty()){
                chat.addMessage(msg);
                mainView.getMessage().clear();
            }

        });

        disableButtons(true);
    }

    private void initPage(){
        root.getChildren().add(mainPane);
        scene.setRoot(root);
    }

    public void setTurnEffect(){
        if(players.get(statusManager.getTurn()).isSelf()){
            mainView.setTimerEffect();
        } else {
            mainView.delTimerEffect();
        }
    }

    /**
     * IDK
     * @param player
     * @return
     */
    private boolean clamping(Player player){
        int maxCanDraw = 12 - player.getCardLeft();
        if(stackGetCard > maxCanDraw) {
            stackGetCard = maxCanDraw;
        }

        if(stackGetCard == 0){
            if(DEBUG) System.out.println("Player has 12 cards");
            if(Setting.isEnClicked()) log.setLogs("Player has 12 cards", State.System);
            else log.setLogs("플레이어 12장의 카드를 가지고 있음", State.System);
            stackGetCard = 1;
            statusManager.doPassTurn();
            return true;
        }
        return false;
    }

    private void disableButtons(boolean disable) {
        mainView.getBack().setDisable(disable);
        mainView.getSetting().setDisable(disable);
        mainView.getRestart().setDisable(disable);
    }


}
