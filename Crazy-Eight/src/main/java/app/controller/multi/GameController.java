package app.controller.multi;

import app.model.multi.*;
import app.view.SettingView;
import app.view.multi.MultiPlayGameView;
import app.view.multi.*;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import javafx.util.Pair;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;


/**
 * The SinglePlayGameController class handles the main logic and state of a single-player game.
 * It manages the game flow, UI interactions, and game state updates for a turn-based card game.
 */
public class GameController extends BaseGameController {

    /**
     * Constructs a SinglePlayGameController
     */
    public GameController(Scene _scene) {
        scene = _scene;
        root = new StackPane();
        mainPane = new BorderPane();
        mainView = new MultiPlayGameView(mainPane);
        settingView = new SettingView(root);
        chooseEightView = new ChooseEightView(root);
        characterChooseView = new CharacterChooseView(root);
        scoringView = new ScoringView(root);
        deck = new Deck(this);
        dummyCard = new DummyCard(this);
        log = new Log(this);
        chat = new Chat(this);
        statusManager = new GameStatusManager(4);
    }

    public void handleServerMessage(String message) {
        try {
            MessageParser.ParsedMessage parsed = MessageParser.parse(message);

            switch (parsed.getMsgType()) {
                case INIT_DECK:
                case INIT_PLAYERS:
                case INIT_PAGE:
                case CREATE_PLAYERS:
                case TIME_SET:
                case TIME_OUT:
                case STACK:
                case CRAZY_EIGHT:
                case SERVER_EIGHT:
                case REVERSE_ORDER:
                case QUEEN:
                case END:
                case CONTINUE:
                case UPDATE_PLAYERS:
                case LOG:
                case SYSTEM:
                case ERROR:
                    System.out.println("서버로부터 온 메시지 - 스킵");
                    break;
                case DRAW_CARD:
                    if (parsed.getSenderPlayerId() == playerId) {
                        System.out.println("서버로부터 온 메세지 - 드로우");
                        break;
                    }
                    break;
                case REQUEST_DRAW_CARD:
                    int id = Integer.parseInt(parsed.getData());
                    drawCards(players.get(id));
                    break;
                case PUT_DUMMY:
                    if (parsed.getSenderPlayerId() == playerId) {
                        System.out.println("서버로부터 온 메세지 - 더미카드 추가");
                        break;
                    }
                    break;
                case UPDATE_TURN:
                    if (parsed.getSenderPlayerId() == playerId) {
                        System.out.println("서버로부터 온 메세지 - 턴 업데이트");
                        break;
                    }
                    break;
                case PUT_CARD:
                    if (parsed.getSenderPlayerId() == playerId || !players.get(parsed.getSenderPlayerId()).isPlayer()) {
                        System.out.println("서버로부터 온 메세지 - 카드 놓음");
                        break;
                    }
                    Pair<Integer, Card> infoPut = GameDTO.putCard(parsed.getData());
                    Platform.runLater(() -> {
                        Animation animation = mainView.putCardAnimationWithPlayer();
                        animation.play();
                        animation.setOnFinished(e -> {
                            players.get(infoPut.getKey()).removeCard(infoPut.getValue());
                            putCardDummy(infoPut.getValue(), false);

                            if (infoPut.getValue().getRank() != 3 && infoPut.getValue().getRank() != 7) {
                                statusManager.doPassTurn();
                            }
                        });
                    });
                    break;
                case CRAZY_EIGHT_DONE:
                    Pair<Integer, Card> card = GameDTO.putCard(parsed.getData());
                    putCardDummy(card.getValue(), true);
                    statusManager.doPassTurn();
                    break;
                case READY:
                    statusManager.addReadyPlayer(Integer.parseInt(parsed.getData()));
                    System.out.println(users.size() - statusManager.getReadyPlayer() + " 플레이어 남음");
                    break;
                case EXIT:
                    int exitId = Integer.parseInt(parsed.getData());
                    if (exitId != playerId) {
                        System.out.println("다른 플레이어 종료");
                        Platform.runLater(() -> {
                            scoringView.fadeOutPane();
                            mainView.setFadeOutGame(scene);
                            game.stop();
                            Server.getInstance().stop();
                        });
                    }
                    break;
                case FORCE_EXIT:
                    int forceExitId = Integer.parseInt(parsed.getData());
                    if (forceExitId != playerId) {
                        System.out.println("다른 플레이어 종료");
                        mainView.setFadeOutGame(scene);
                        game.stop();
                    }
                    break;
                case CHAT:
                    int chatId = parsed.getSenderPlayerId();
                    if (chatId != playerId) {
                        chat.addMessage(parsed.getData(), players.get(chatId));
                    }
                    break;
                default:
                    System.err.println("알 수 없는 메시지 타입: " + parsed.getMsgType());
            }

        } catch (MessageParser.InvalidMessageFormatException e) {
            System.err.println("메시지 파싱 실패: " + e.getMessage());
        }
    }

    public void saveInfo(int playerId, Map<Integer, String> playerCharacterInfo) {
        this.playerId = playerId;
        for (Map.Entry<Integer, String> entry : playerCharacterInfo.entrySet()) {
            int id = entry.getKey();
            String character = entry.getValue();
            Player player = new Player(id);
            player.setIcon(character, true);
            users.add(player);
            players.add(player);
        }
    }

    /**
     * Initializes and starts the game.
     * <p>
     * This method sets up the game environment, including drawing the game page,
     * generating the deck, and creating players. It manages the sequential
     * animations for dealing cards and placing the starting dummy card. Once the
     * setup is complete, it begins the main game loop and enables user interaction.
     * <p>
     * Key functionalities:
     * - Draws the main game page on the interface.
     * - Generates the deck and initializes player roles.
     * - Logs the game setup process in the specified system language.
     * - Executes a sequence of animations for dealing cards and placing a starting card.
     * - Initiates the core game logic and enables gameplay actions.
     */
    public void startGame() {
        if (DEBUG) System.out.println("Multi Play Game Start!");
        initPage();
        drawGamePage();
        Client.send(playerId + "#INIT_PAGE#NULL");

        deck.generateDeck();
        Client.send(playerId + "#INIT_DECK#" + deck.toString());

        Client.send(playerId + "#INIT_PLAYERS#" + userToString());
        createPlayers();
        System.out.println("플레이어 생성 완료!");

        if (Setting.isEnClicked()) {
            log.setLogs("Setting Game...", State.System);
            Client.send(playerId + "#SYSTEM#Setting Game...");
        } else {
            log.setLogs("게임 설정 중...", State.System);
            Client.send(playerId + "#SYSTEM#Setting Game...");
        }


        SequentialTransition sequence = new SequentialTransition();

        sequence.getChildren().add(getSixCards());

        sequence.getChildren().add(putStartDummyCard());

        sequence.play();

        sequence.setOnFinished(new GameStartHandler());
    }

    private class GameStartHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            delaySecond(() -> {
                gameLoop();
                game.play();
                disableButtons(false);
            });
        }
    }


    /**
     * Represents the main game loop
     */
    private void gameLoop() {
        Timeline gameLoop = new Timeline(
                new KeyFrame(Duration.seconds(1), new GameLoopHandler())
        );
        gameLoop.setCycleCount(Timeline.INDEFINITE);

        game = gameLoop;
    }

    private class GameLoopHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            if (statusManager.getTime() % 11 == 0) {
                statusManager.nextTurn();
                statusManager.resetPassTurn();
                statusManager.resetUserDid();
                statusManager.resetTime();
                statusManager.resetFourTime();
                statusManager.resetQueenTime();
                statusManager.setClientEightTime(false);
                if (DEBUG)
                    System.out.printf("Turn changed : %d -> %d\n", statusManager.getTurn() - 1, statusManager.getTurn());
                if (DEBUG) System.out.println("Current deck size: " + deck.deckSize());

                updatePlayerTurn();

                setTurnEffect();

                if (playerChatTime == 1) playerDoChat = false;

                if (Setting.isEnClicked()) {
                    log.setLogs(String.format("Player %d turn", statusManager.getTurn() + 1), State.Log);
                    Client.send(playerId + "#LOG#Player " + statusManager.getTurn() + " turn");
                } else {
                    log.setLogs(String.format("플레이어 %d 차례", statusManager.getTurn() + 1), State.Log);
                    Client.send(playerId + "#LOG#플레이어 " + statusManager.getTurn() + " 차례");
                }
            }
            if (DEBUG) System.out.printf(statusManager.toString());

            if (statusManager.isFourTime() && statusManager.isQueenTime()) {
                Player player = players.get(statusManager.getTurn());
                if (player.isPlayer()) {
                    if (player.isSelf()) {
                        if (statusManager.getTime() == 10 && !statusManager.isUserDid()) {    // 이 userDid의 플래그는 다르게 애니메이션이 시작되기 전에 플래그를 바꿔줘야함
                            if (Setting.isEnClicked()) {
                                log.setLogs("Time out!", State.Error);
                                Client.send(playerId + "#ERROR#Time out!");
                            } else {
                                log.setLogs("시간 초과!", State.Error);
                                Client.send(playerId + "#ERROR#시간 초과!");
                            }

                            userDrawCard(player);
                        }
                    } else {  // 다른 플레이어 일때
                        if (statusManager.getTime() == 10 && !statusManager.isUserDid() && !statusManager.isClientEightTime()) {
                            statusManager.doUserDid();
                            Client.send(playerId + "#TIME_OUT#" + player.getScoreId());
                            if (Setting.isEnClicked()) {
                                log.setLogs("Time out!", State.Error);
                                Client.send(playerId + "#ERROR#Time out!");
                            } else {
                                log.setLogs("시간 초과!", State.Error);
                                Client.send(playerId + "#ERROR#시간 초과!");
                            }
                        }
                    }
                    // 유저는 3가지 상태가 있음.
                    // 1. 핸드의 카드가 release될때
                    // 2. 덱을 클릭을 했을때
                    // 3. time out이 되어 강제로 카드를 먹어야 할때
                } else if (statusManager.getTime() == playerRanPutTime && !statusManager.isCardTimeActive()) {
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
                if (statusManager.getTime() < 10) {
                    statusManager.addTime();
                    Client.send(playerId + "#TIME_SET#" + statusManager.getTime());
                }
            }

            // 이 플래그가 중요! 항상 carTime은 턴이 바뀔때 딱 한번 초기화를 시킴. 또한, 카드가 움직이고, 애니메이션이 끝날때 false로 끝남을 알림
            // 이것이 false로 바뀔 때까지 그 플레이어의 턴은 끝나지 않음
            if (statusManager.isPassTurn()) {
                if (players.get(statusManager.getTurn()).getCardLeft() == 0) {
                    endGame();
                }

                statusManager.resetTime();
                Client.send(playerId + "#TIME_SET#" + statusManager.getTime());

                playerRanPutTime = ThreadLocalRandom.current().nextInt(3, 8);
                playerDoChat = Math.random() < 0.7;
                playerChatTime = ThreadLocalRandom.current().nextInt(1, playerRanPutTime);
                statusManager.setCardTimeActive(false);
            }
        }
    }


    /**
     * Handles the end of the game logic.
     * <p>
     * This method pauses the game, logs a "Game Over" message in the console if debug mode is enabled,
     * and records a localized "Game Over" message in the system log. It then initializes the scoring
     * view, generates scores for
     */
    private void endGame() {
        Client.send(playerId + "#END#NULL");
        game.pause();
        if (DEBUG) System.out.println("Game is done..!");
        if (Setting.isEnClicked()) {
            log.setLogs("Game Over!", State.System);
            Client.send(playerId + "#SYSTEM#Game Over!");
        } else {
            log.setLogs("게임 종료!", State.System);
            Client.send(playerId + "SYSTEM#게임 종료!");
        }

        scoringView.generate(players, scoring());
        scoringView.buttonAnimation();
        scoringView.getContinueButton().setOnMouseClicked(new ContinueButtonHandler());
        scoringView.getExitButton().setOnMouseClicked(new ExitButtonHandler());
    }

    private class ContinueButtonHandler implements EventHandler<MouseEvent> {
        @Override
        public void handle(MouseEvent e) {
            if (statusManager.getReadyPlayer() == users.size()) {
                Client.send(playerId + "#CONTINUE#NULL");
                scoringView.fadeOutPane();
                mainView.resetGame(scene, mainPane, players);
                game.stop();
                statusManager.resetReadyPlayer();
            } else {
                statusManager.addReadyPlayer(playerId);
                scoringView.getContinueButton().setText("Start");
                log.setLogs("Waiting for " + statusManager.getReadyPlayer() + " player(s) to join..!", State.System);
                Client.send(playerId + "#SYSTEM#Waiting for " + statusManager.getReadyPlayer() + " player(s) to join..!");
            }
        }
    }

    private class ExitButtonHandler implements EventHandler<MouseEvent> {
        @Override
        public void handle(MouseEvent e) {
            Client.send(playerId + "#EXIT#" + playerId);
            scoringView.fadeOutPane();
            mainView.setFadeOutGame(scene);
            game.stop();
        }
    }

    /**
     * Calculates and assigns ranks and scores to players based on the number of their remaining cards.
     * <p>
     * The method performs the following steps:
     * 1. Sorts players in ascending order based on the number of remaining cards.
     * 2. Assigns ranks and corresponding scores to players, considering ties and specifying rules for each rank.
     * 3. Handles cases where no 3rd place exists by promoting 4th place players to 3rd place.
     * 4. Re-sorts the players based on their assigned ranks.
     *
     * @return A map containing players as keys and their corresponding rank and score pairs as values.
     */
    private Map<Player, Pair<Integer, Integer>> scoring() {
        // Sort players based on the number of remaining cards in ascending order
        players.sort(new CardCountComparator());

        Map<Player, Pair<Integer, Integer>> scoreMap = new HashMap<>(); // {Player -> (Rank, Score)}
        int[] scoreTable = {5, 3, 1, 0};  // Points for 1st, 2nd, 3rd, and 4th place

        int rank = 1;  // Current rank
        int scoreIndex = 0;  // Index for the score table
        int countInRank = 0;  // Number of players with the same rank
        boolean hasThirdPlace = false;  // Flag to check if 3rd place exists

        for (int i = 0; i < players.size(); ++i) {
            // If the current player has more cards than the previous one, increase rank
            if (i > 0 && players.get(i).getCardLeft() > players.get(i - 1).getCardLeft()) {
                if (rank == 1) rank = 2;  // Only one player can be 1st
                else if (rank == 2 && countInRank >= 3) rank = 3;  // If 2nd place has 3 players, the next rank is 3rd
                else if (rank == 3 && countInRank >= 2) rank = 4;  // If 3rd place has 2 players, the next rank is 4th
                else rank++;

                if (rank == 3) hasThirdPlace = true; // Mark if 3rd place exists
                scoreIndex = Math.min(rank - 1, 3); // Adjust score index
                countInRank = 0;
            }

            countInRank++;  // Increment the count of players in the same rank
            int score = scoreTable[scoreIndex];  // Assign corresponding score
            players.get(i).addScore(score);

            // Store player rank and score
            scoreMap.put(players.get(i), new Pair<>(rank, score));
        }

        // If there's no 3rd place, promote 4th place to 3rd
        if (!hasThirdPlace) {
            for (Map.Entry<Player, Pair<Integer, Integer>> entry : scoreMap.entrySet()) {
                if (entry.getValue().getKey() == 4) {
                    scoreMap.put(entry.getKey(), new Pair<>(3, scoreTable[2]));  // Assign 3rd place score (1 point)
                }
            }
        }

        // Re-sort players based on their ranks
        players.sort(new RankComparator(scoreMap));

        return scoreMap;
    }

    private class CardCountComparator implements Comparator<Player> {
        @Override
        public int compare(Player p1, Player p2) {
            return Integer.compare(p1.getCardLeft(), p2.getCardLeft());
        }
    }

    private class RankComparator implements Comparator<Player> {
        private final Map<Player, Pair<Integer, Integer>> scoreMap;

        public RankComparator(Map<Player, Pair<Integer, Integer>> scoreMap) {
            this.scoreMap = scoreMap;
        }

        @Override
        public int compare(Player p1, Player p2) {
            return Integer.compare(scoreMap.get(p1).getKey(), scoreMap.get(p2).getKey());
        }
    }


    /**
     * Handles the logic for a player putting down a card.
     * It checks if the player can put down a card that matches the dummy card
     * based on the rank or suit, and applies the necessary animations and logic.
     *
     * @param player The player attempting to put down a card.
     */
    private void playerPutCard(Player player) {
        // If there are stacked cards (e.g., from a 2-card effect)
        if (stackGetCard > 1) {
            // If the dummy card is a 2
            if (dummyCard.getCard().getRank() == 1) {
                for (Card card : player.getHand()) {
                    if (dummyCard.getCard().getRank() == card.getRank()) {
                        Client.send(playerId + "#PUT_CARD#" + statusManager.getTurn() + " " + card.toString());
                        Animation putCard = mainView.putCardAnimationWithPlayer();
                        putCard.setOnFinished(new PutCardHandler(card, player, false));
                        putCard.play();
                        return;
                    }
                }
                // If no matching card, the player must draw a card
                drawCards(player);
                return;
            }
        }

        // Normal case: No stacking effect, check for valid moves
        for (Card card : player.getHand()) {
            if (dummyCard.getCard().getSuit() == card.getSuit() || dummyCard.getCard().getRank() == card.getRank()) {
                Client.send(playerId + "#PUT_CARD#" + statusManager.getTurn() + " " + card.toString());
                Animation putCard = mainView.putCardAnimationWithPlayer();
                putCard.setOnFinished(new PutCardHandler(card, player, false));
                putCard.play();
                return;
            }
        }

        // If no valid card to play, the player must draw a card
        drawCards(player);
    }

    private class PutCardHandler implements EventHandler<ActionEvent> {
        private final Card card;
        private final Player player;
        private final boolean someFlag;

        public PutCardHandler(Card card, Player player, boolean someFlag) {
            this.card = card;
            this.player = player;
            this.someFlag = someFlag;
        }

        @Override
        public void handle(ActionEvent e) {
            putCardDummy(card, someFlag);
            player.removeCard(card);
        }
    }

    /**
     * Executes the logic for drawing cards for the specified player.
     * This includes animations, log updates, card addition to the player's hand, and turn management.
     * The number of cards drawn is determined by the game's state and player eligibility.
     *
     * @param player The player instance for whom the cards are to be drawn.
     */
    private void drawCards(Player player) {
        if (clamping(player)) return;

        System.out.println("서버에서 스택: " + stackGetCard);
        final Timeline drawMotion = new Timeline(new KeyFrame(Duration.seconds(1), new DrawCardEventHandler(player)));
        drawMotion.setCycleCount(stackGetCard);
        drawMotion.setOnFinished(new DrawMotionFinishedEventHandler(player));

        drawMotion.play();
    }

    private class DrawCardEventHandler implements EventHandler<ActionEvent> {
        private final Player player;

        public DrawCardEventHandler(Player player) {
            this.player = player;
        }

        @Override
        public void handle(ActionEvent event) {
            Animation getCard = mainView.getCardAnimationToPlayer();
            getCard.setOnFinished(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {
                    if (DEBUG) System.out.println("Drawing card");
                    if (Setting.isEnClicked()) {
                        log.setLogs("Drawing card!", State.Log);
                        Client.send(playerId + "#LOG#Drawing card!");
                    } else {
                        log.setLogs("카드 드로우!", State.Log);
                        Client.send(playerId + "#LOG#카드 드로우!");
                    }

                    Card card = player.setCard(deck, false);
                    // CPU draw > client
                    // client request > client, CPU draw card > client
                    Client.send(playerId + "#DRAW_CARD#" + player.getScoreId() + " " + card.toString());
                }
            });
            getCard.play();
        }
    }

    private class DrawMotionFinishedEventHandler implements EventHandler<ActionEvent> {
        private final Player player;

        public DrawMotionFinishedEventHandler(Player player) {
            this.player = player;
        }

        @Override
        public void handle(ActionEvent event) {
            if (!players.get(statusManager.getTurn()).isPlayer()) {
                if (Setting.isEnClicked()) chat.addMessage(CPU_Msg.getEnglishBadDraw(), player);
                else chat.addMessage(CPU_Msg.getKoreanBadDraw(), player);
            }
            delaySecond(new Runnable() {
                @Override
                public void run() {
                    stackGetCard = 1;
                    Client.send(playerId + "#STACK#" + stackGetCard);
                    statusManager.doPassTurn();
                    statusManager.resetFourTime();
                }
            });
        }
    }

    /**
     * Adds a card to the deck and shuffles the deck to ensure randomness.
     *
     * @param card The {@code Card} instance to be added to the deck. This card will be
     *             included in the deck's card collection and the deck will be reshuffled.
     */
    @Override
    public void updateAddToDeck(Card card) {
        deck.add(card);
    }


    /**
     * Allows the user to draw a card by executing the following steps:
     * - Verifies if the player is eligible to draw cards using the {@code clamping} method.
     * - Updates the status to indicate the user's action.
     * - Animates the card drawing process and adds the card to the player's hand.
     * - Updates game logs based on the current language setting.
     * - Handles turn passing and resets relevant game states upon animation completion.
     *
     * @param player The {@code Player} instance representing the user who is drawing a card.
     */
    private void userDrawCard(Player player) {
        if (clamping(player)) return;

        statusManager.doUserDid();  // 플래그 바꿈! Time out에 다시는 들어가지 않음

        Timeline timeoutAnimation = new Timeline(new KeyFrame(Duration.seconds(1), new DrawCardAnimationEventHandler(player)));
        timeoutAnimation.setCycleCount(stackGetCard);
        timeoutAnimation.setOnFinished(new TimeoutAnimationFinishedEventHandler());
        timeoutAnimation.play();
    }

    private class DrawCardAnimationEventHandler implements EventHandler<ActionEvent> {
        private final Player player;

        public DrawCardAnimationEventHandler(Player player) {
            this.player = player;
        }

        @Override
        public void handle(ActionEvent event) {
            Animation getCards = mainView.getCardAnimationToUser();
            getCards.setOnFinished(new GetCardAnimationFinishedEventHandler(player));
            getCards.play();
        }
    }

    private class GetCardAnimationFinishedEventHandler implements EventHandler<ActionEvent> {
        private final Player player;

        public GetCardAnimationFinishedEventHandler(Player player) {
            this.player = player;
        }

        @Override
        public void handle(ActionEvent e) {
            Card card = player.setCard(deck, false);
            if (DEBUG) System.out.println("Drawing card");
            if (Setting.isEnClicked()) log.setLogs("Drawing card!", State.Log);
            else log.setLogs("카드 드로우!", State.Log);
            // time out server draw card > client
            Client.send(playerId + "#DRAW_CARD#" + playerId + " " + card.toString());
        }
    }

    private class TimeoutAnimationFinishedEventHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent e) {
            delaySecond(new Runnable() {
                @Override
                public void run() {
                    statusManager.doPassTurn();
                    stackGetCard = 1;
                    Client.send(playerId + "#STACK#" + stackGetCard);
                    statusManager.resetFourTime();  // 4일 경우, 여기도 들어오니 초기화
                }
            });
        }
    }

    /**
     * Updates the turn state of the players and applies appropriate effects based on the game status.
     * <p>
     * The method performs the following steps:
     * - Iterates through all players and resets their turn status to false.
     * - Clears card and deck interaction effects using helper methods.
     * - Sets the current player (determined by the status manager) as the active player.
     * - If the game is in the four-time state and the current player is marked as self, additional card and deck effects are applied to the player.
     */
    private void updatePlayerTurn() {
        for (Player player : players) {
            player.setMyTurn(false);
            removeCardEffects();
            removeDeckEffects();
        }
        Player player = players.get(statusManager.getTurn());
        player.setMyTurn(true);
        if (statusManager.isFourTime() && player.isSelf()) {
            Platform.runLater(() -> {
                addCardEffects(player);
                addDeckEffects(player);
            });
        }

        Client.send(playerId + "#UPDATE_TURN#" + statusManager.getTurn());
    }

    /**
     * Applies click effects to the deck element, enabling players to draw cards
     * and trigger corresponding animations and game logic updates.
     *
     * @param player The {@code Player} instance interacting with the deck.
     */
    private void addDeckEffects(Player player) {
        mainView.getDeck().setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                mainView.getDeck().setDisable(true);

                if (GameController.this.clamping(player)) return;

                statusManager.doUserDid();  // 플래그 바꿈! - Time out이 안됨

                Timeline getCards = new Timeline(new KeyFrame(Duration.seconds(1), new GetCardsEventHandler(player)));
                getCards.setOnFinished(new GetCardsFinishedEventHandler(player));
                getCards.setCycleCount(stackGetCard);

                getCards.play();
            }
        });
    }

    private class GetCardsEventHandler implements EventHandler<ActionEvent> {
        private final Player player;

        public GetCardsEventHandler(Player player) {
            this.player = player;
        }

        @Override
        public void handle(ActionEvent e) {
            Animation getAnimation = mainView.setGetCardAnimation(player.getCardLeft());
            getAnimation.setOnFinished(new GetAnimationFinishedEventHandler(player));
            getAnimation.play();
        }
    }

    private class GetCardsFinishedEventHandler implements EventHandler<ActionEvent> {
        private final Player player;

        public GetCardsFinishedEventHandler(Player player) {
            this.player = player;
        }

        @Override
        public void handle(ActionEvent e) {
            delaySecond(new Runnable() {
                @Override
                public void run() {
                    statusManager.doPassTurn();
                    stackGetCard = 1;
                    Client.send(playerId + "#STACK#" + stackGetCard);
                }
            });
        }
    }

    private class GetAnimationFinishedEventHandler implements EventHandler<ActionEvent> {
        private final Player player;

        public GetAnimationFinishedEventHandler(Player player) {
            this.player = player;
        }

        @Override
        public void handle(ActionEvent ev) {
            mainView.removeAnimationCard();
            Card card = player.setCard(deck, false);
            // server draw card > client
            Client.send(playerId + "#DRAW_CARD#" + playerId + " " + card.toString());
            removeDeckEffects();
            mainView.getDeck().setDisable(false);
        }
    }

    /**
     * Adds interactive effects to the cards in a player's hand. These effects include hover scaling,
     * drag-and-drop functionality, and validity checks when a card is played.
     *
     * @param player The {@code Player} instance representing the user whose hand cards will be updated with effects.
     */
    private void addCardEffects(Player player) {
        // 유저 핸드의 카드 움직임과 핸드 이벤트들 (매 카드가 추가가 될때 마다 실행)
        for (Pair<Card, ImageView> info : mainView.getCurCardInfo()) {
            Card card = info.getKey();
            ImageView cardImg = info.getValue();
            if (cardImg.getOnMouseEntered() == null) {
                cardImg.setOnMouseEntered(ev -> {
                    mainView.setCardHoverScaleUp(cardImg);
                });
            }
            if (cardImg.getOnMouseExited() == null) {
                cardImg.setOnMouseExited(ev -> {
                    mainView.setCardHoverScaleDown(cardImg);
                });
            }
            if (cardImg.getOnMousePressed() == null) {
                cardImg.setOnMousePressed(ev -> {
                    mainView.setDragPressed(ev, cardImg);
                });
            }
            if (cardImg.getOnMouseDragged() == null) {
                cardImg.setOnMouseDragged(ev -> {
                    mainView.setDragDragged(ev, cardImg);
                });
            }
            // 유저가 카드를 놓았을때!
            if (cardImg.getOnMouseReleased() == null) {
                cardImg.setOnMouseReleased(ev -> {
                    Card dummy = dummyCard.getCard();
                    if (stackGetCard > 1) {
                        // 카드가 2
                        if (dummy.getRank() == 1 && dummy.getRank() == card.getRank()) {
                            statusManager.doUserDid();
                            mainView.setDragReleased(ev, cardImg, player, dummyCard, true);
                        } else {
                            mainView.setDragReleased(ev, cardImg, player, dummyCard, false);
                        }
                    } else {
                        if (dummy.getSuit() == card.getSuit() || dummy.getRank() == card.getRank()) {
                            statusManager.doUserDid();
                            if (mainView.setDragReleased(ev, cardImg, player, dummyCard, true)) {
                                Client.send(playerId + "#PUT_CARD#" + player.getScoreId() + " " + card.toString());
                            }
                        } else {
                            mainView.setDragReleased(ev, cardImg, player, dummyCard, false);
                        }
                    }
                });

            }
        }
    }

    /**
     * Updates the dummy card displayed in the game with the specified card and optionally skips notifying observers.
     * The method performs the following actions:
     * - Sets the dummy card to the specified card.
     * - Updates the visual representation of the dummy card by setting an image corresponding to the card.
     * - Updates the main view with the newly set dummy card.
     *
     * @param card         The {@code Card} instance to be set as the dummy card.
     * @param skipObserver A boolean indicating whether to skip notifying the observers of the change.
     */
    private void putCardDummy(Card card, boolean skipObserver) {
        dummyCard.setCard(card, skipObserver);
        dummyCard.setImage();
        mainView.setCardDummy(dummyCard);
    }

    /**
     * Updates the game state based on the card being played.
     * It logs the action, evaluates specific card ranks to invoke
     * corresponding behaviors, passes the turn if none of the specified
     * conditions are met, and clears all current deck and card effects.
     *
     * @param card The {@code Card} instance that is played, containing its suit and rank information.
     */
    @Override
    public void update(Card card) {
        statusManager.setCardTimeActive(true);
        if (DEBUG) System.out.println(String.format(" - Put %s %d card", card.getImogeSuit(), card.getRank() + 1));
        if (Setting.isEnClicked()) {
            log.setLogs(String.format("Put %s %s card", card.getImogeSuit(), card.getRankString()), State.Log);
            Client.send(playerId + "#LOG#Put " + card.getImogeSuit() + " " + card.getRankString());
        } else {
            log.setLogs(String.format("카드 %s %s 놓음", card.getKoreanSuit(), card.getRankString()), State.Log);
            Client.send(playerId + "#LOG#카드 " + card.getKoreanSuit() + " " + card.getRankString() + " 놓음");
        }

        if (players.get(statusManager.getTurn()).getCardLeft() == 1 && !players.get(statusManager.getTurn()).isSelf()) {
            players.get(statusManager.getTurn()).removeCard(0);
            endGame();
            return;
        }

        if (card.getRank() == 7) {
            whenCardEight();
        } else if (card.getRank() == 0) {
            whenCardAce();
        } else if (card.getRank() == 1) {
            whenCardTwo();
        } else if (card.getRank() == 3) {
            whenCardFour();
        } else if (card.getRank() == 11) {
            whenCardQueen();
        } else {
            statusManager.doPassTurn();
        }

        removeDeckEffects();
        removeCardEffects();
    }

    private void whenCardAce() {
        statusManager.setReverseOrder();
        if (DEBUG) System.out.println("Turn is reversed..!");
        if (Setting.isEnClicked()) {
            log.setLogs("Turn is reversed..!", State.System);
            Client.send(playerId + "#SYSTEM#Turn is reversed..!");
        } else {
            log.setLogs("순서가 바뀜..!", State.System);
            Client.send(playerId + "#SYSTEM#순서가 바뀜..!");
        }
        Client.send(playerId + "#REVERSE_ORDER#NULL");
        statusManager.doPassTurn();
    }

    /**
     * Handles the game logic when a Queen card is played.
     * <p>
     * This method updates the game state and player turns, applies
     * relevant effects, logs the action in the selected language,
     * and schedules a delayed operation for passing the turn.
     * <p>
     * The following operations are performed sequentially:
     * - Sets the queen time using the `statusManager`.
     * - Advances the game to the next turn.
     * - Updates the current player's turn and applies turn effects.
     * - Logs the action in either English or Korean, based on the application's settings.
     * - Schedules a delayed action to:
     * - Pass the turn using the `statusManager`.
     * - Reset the queen time to its default state.
     * <p>
     * If debugging mode is enabled, outputs the skipped player's turn
     * to the console for verification.
     */
    private void whenCardQueen() {
        statusManager.setQueenTime();
        statusManager.nextTurn();
        updatePlayerTurn();
        setTurnEffect();
        Client.send(playerId + "#QUEEN#NULL");

        if (DEBUG) System.out.printf("Skip player %d\n", statusManager.getTurn());
        if (Setting.isEnClicked()) {
            log.setLogs("Skip next player!", State.System);
            Client.send(playerId + "#SYSTEM#Skip next player!");
        } else {
            log.setLogs("다음 플레이어 스킵!", State.System);
            Client.send(playerId + "#SYSTEM#다음 플레이어 스킵!");
        }
        delayQueen(new Runnable() {
            @Override
            public void run() {
                statusManager.doPassTurn();
                statusManager.resetQueenTime();
            }
        });
    }

    /**
     * Executes the logic associated with playing a card of rank 4 in the game.
     * <p>
     * The method performs the following actions:
     * - Checks if the current player has only one card left. If so, it removes that card,
     * ends the game, and exits the method.
     * - Activates "Four Time" mode in the game's status manager.
     * - Increases the card stack count by 3, which will affect the cards drawn by the next player.
     * - Sends an attack message to the in-game chat, based on the selected language setting.
     * - Updates the game state by advancing the turn to the next player.
     * - Creates logs to document the effect of the card, showing the number of cards the next player must draw.
     * - If the next turn belongs to the user, it triggers the user's card draw logic.
     * - If the turn belongs to a CPU player, it triggers the CPU player's card draw logic.
     */
    private void whenCardFour() {
        if (DEBUG) System.out.printf("Card Left %d\n", players.get(statusManager.getTurn()).getCardLeft());
        statusManager.setFourTime();
        stackGetCard += 3;

        if (!players.get(statusManager.getTurn()).isPlayer()) {
            if (Setting.isEnClicked())
                chat.addMessage(CPU_Msg.getEnglishAttack(), players.get(statusManager.getTurn()));
            else chat.addMessage(CPU_Msg.getKoreanAttack(), players.get(statusManager.getTurn()));
        }

        statusManager.nextTurn();
        updatePlayerTurn();
        setTurnEffect();
        Player player = players.get(statusManager.getTurn());
        if (Setting.isEnClicked()) {
            log.setLogs(String.format("Player %d gets 4 cards!", player.getNetworkId()), State.System);
            Client.send(playerId + "#SYSTEM#Player " + player.getNetworkId() + " gets 4 cards!");
        } else {
            log.setLogs(String.format("플레이어 %d 4장 카드 드로우!", player.getNetworkId()), State.System);
            Client.send(playerId + "#SYSTEM#플레이어 " + player.getNetworkId() + " 4장 카드 드로우!");
        }
        if (player.isSelf()) {
            userDrawCard(player);   // 여기서 cardTimeDid를 호출
            return;
        }
        drawCards(player);  // 여기서 cardTimeDid를 호출
    }

    /**
     * Handles the logic to be executed when the card "two" is played in the game.
     * <p>
     * This method updates the stack of cards to draw based on the current value
     * of `stackGetCard`. It increments by 1 if the stack is currently at 1;
     * otherwise, it increments by 2.
     * <p>
     * The method also proceeds to the next player's turn using the `statusManager.doPassTurn()`.
     * <p>
     * Depending on the language setting in `Setting.isEnClicked()`, it adds a corresponding
     * attack message to the chat for the current player.
     * <p>
     * If debugging is enabled (`DEBUG`), it logs the current stack value to the console.
     * Additionally, it logs the current stack of cards in a system log, with messages
     * adjusted according to the language setting.
     */
    private void whenCardTwo() {
        if (stackGetCard == 1) stackGetCard += 1;
        else stackGetCard += 2;

        statusManager.doPassTurn();    // 여기가 마지막 초기화함!

        if (!players.get(statusManager.getTurn()).isPlayer()) {
            if (Setting.isEnClicked())
                chat.addMessage(CPU_Msg.getEnglishAttack(), players.get(statusManager.getTurn()));
            else chat.addMessage(CPU_Msg.getKoreanAttack(), players.get(statusManager.getTurn()));
        }

        if (DEBUG) System.out.println("Current stack: " + stackGetCard);
        if (Setting.isEnClicked()) {
            log.setLogs(String.format("Current %d cards are stacked", stackGetCard), State.System);
            Client.send(playerId + "#SYSTEM#Current " + stackGetCard + " cards are stacked");
        } else {
            log.setLogs(String.format("현재 %d개 카드 쌓임", stackGetCard), State.System);
            Client.send(playerId + "#SYSTEM#현재 " + stackGetCard + " 개 카드 쌓임");
        }
        Client.send(playerId + "#STACK#" + stackGetCard);
    }

    private void whenCardEight() {
        if (players.get(statusManager.getTurn()).isSelf()) {
            log.setLogs("Crazy Eight Time!", State.System);
            Client.send(playerId + "#SYSTEM#Crazy Eight Time!");
            chooseEightView.generate();

            chooseEightView.getSpace().setOnMouseClicked(new SpaceClickHandler());
            chooseEightView.getHeart().setOnMouseClicked(new HeartClickHandler());
            chooseEightView.getDiamond().setOnMouseClicked(new DiamondClickHandler());
            chooseEightView.getClub().setOnMouseClicked(new ClubClickHandler());
        } else if (players.get(statusManager.getTurn()).isPlayer()) {
            statusManager.setClientEightTime(true);
            Client.send(playerId + "#CRAZY_EIGHT#" + players.get(statusManager.getTurn()).getScoreId());
        } else {
            int shape = players.get(statusManager.getTurn()).getMostShape();
            switch (shape) {
                case 0:
                    if (Setting.isEnClicked()) {
                        log.setLogs("Change to Space!", State.System);
                        Client.send(playerId + "#SYSTEM#Change to Space!");
                    } else {
                        log.setLogs("스페이드로 바꿈!", State.System);
                        Client.send(playerId + "#SYSTEM#스페이드로 바꿈!");
                    }
                    Client.send(playerId + "#SERVER_EIGHT#" + playerId + " " + "0:7");
                    break;
                case 1:
                    if (Setting.isEnClicked()) {
                        log.setLogs("Change to Heart!", State.System);
                        Client.send(playerId + "#SYSTEM#Change to Heart!");
                    } else {
                        log.setLogs("하트로 바꿈!", State.System);
                        Client.send(playerId + "#SYSTEM#하트로 바꿈!");
                    }
                    Client.send(playerId + "#SERVER_EIGHT#" + playerId + " " + "1:7");
                    break;
                case 2:
                    if (Setting.isEnClicked()) {
                        log.setLogs("Change to Diamond!", State.System);
                        Client.send(playerId + "#SYSTEM#Change to Diamond!");
                    } else {
                        log.setLogs("다이아몬드로 바꿈!", State.System);
                        Client.send(playerId + "#SYSTEM#다이아몬드로 바꿈!");
                    }
                    Client.send(playerId + "#SERVER_EIGHT#" + playerId + " " + "2:7");
                    break;
                case 3:
                    if (Setting.isEnClicked()) {
                        log.setLogs("Change to Club!", State.System);
                        Client.send(playerId + "#SYSTEM#Change to Club!");
                    } else {
                        log.setLogs("크로버로 바꿈!", State.System);
                        Client.send(playerId + "#SYSTEM#크로버로 바꿈!");
                    }
                    Client.send(playerId + "#SERVER_EIGHT#" + playerId + " " + "3:7");
                    break;
            }

            if (DEBUG) System.out.println("Changed the shape!");
            putCardDummy(new Card(shape, 7), true);
            statusManager.doPassTurn();
        }
    }

    private class SpaceClickHandler implements EventHandler<MouseEvent> {
        @Override
        public void handle(MouseEvent event) {
            event.consume();
            putCardDummy(new Card(0, 7), true);
            fadeOutPane();
            if (DEBUG) System.out.println("Change to Space!");
            if (Setting.isEnClicked()) {
                log.setLogs("Change to Space!", State.System);
                Client.send(playerId + "#SYSTEM#Change to Space!");
            } else {
                log.setLogs("스페이드로 바꿈!", State.System);
                Client.send(playerId + "#SYSTEM#스페이드로 바꿈!");
            }
            Client.send(playerId + "#SERVER_EIGHT#" + statusManager.getTurn() + " " + "0:7");
        }
    }

    private class HeartClickHandler implements EventHandler<MouseEvent> {
        @Override
        public void handle(MouseEvent event) {
            event.consume();
            putCardDummy(new Card(1, 7), true);
            fadeOutPane();
            if (DEBUG) System.out.println("Change to Heart!!");
            if (Setting.isEnClicked()) {
                log.setLogs("Change to Heart!", State.System);
                Client.send(playerId + "#SYSTEM#Change to Heart!");
            } else {
                log.setLogs("하트로 바꿈!", State.System);
                Client.send(playerId + "#SYSTEM#하트로 바꿈!");
            }
            Client.send(playerId + "#SERVER_EIGHT#" + statusManager.getTurn() + " " + "1:7");
        }
    }

    private class DiamondClickHandler implements EventHandler<MouseEvent> {
        @Override
        public void handle(MouseEvent event) {
            event.consume();
            putCardDummy(new Card(2, 7), true);
            fadeOutPane();
            if (DEBUG) System.out.println("Change to Diamond!!");
            if (Setting.isEnClicked()) {
                log.setLogs("Change to Diamond!", State.System);
                Client.send(playerId + "#SYSTEM#Change to Diamond!");
            } else {
                log.setLogs("다이아몬드로 바꿈!", State.System);
                Client.send(playerId + "#SYSTEM#다이아몬드로 바꿈!");
            }
            Client.send(playerId + "#SERVER_EIGHT#" + statusManager.getTurn() + " " + "2:7");
        }
    }

    private class ClubClickHandler implements EventHandler<MouseEvent> {
        @Override
        public void handle(MouseEvent event) {
            event.consume();
            putCardDummy(new Card(3, 7), true);
            fadeOutPane();
            if (DEBUG) System.out.println("Change to Club!");
            if (Setting.isEnClicked()) {
                log.setLogs("Change to Club!", State.System);
                Client.send(playerId + "#SYSTEM#Change to Club!");
            } else {
                log.setLogs("크로버로 바꿈!", State.System);
                Client.send(playerId + "#SYSTEM#크로버로 바꿈!");
            }
            Client.send(playerId + "#SERVER_EIGHT#" + statusManager.getTurn() + " " + "3:7");
        }
    }


    /**
     * Handles the fade-out animation for a pane and performs subsequent tasks upon animation completion.
     * <p>
     * The method retrieves the fade-out animation associated with the pane and initiates its playback.
     * Once the animation finishes, it proceeds to perform additional operations, such as:
     * - Applying a delay before executing further actions.
     * - Removing a specific overlay element from the pane.
     * - Triggering a status update to pass the turn using the status manager.
     * <p>
     * This method ensures a smooth transition and manages post-animation tasks seamlessly.
     */
    private void fadeOutPane() {
        Animation fadeOutPane = chooseEightView.getFadeOutPaneAnimation();
        fadeOutPane.play();
        fadeOutPane.setOnFinished(new FadeOutFinishedHandler());
    }

    private class FadeOutFinishedHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            delaySecond(new DelayHandler());
        }
    }

    private class DelayHandler implements Runnable {
        @Override
        public void run() {
            chooseEightView.getPane().getChildren().remove(chooseEightView.getOverlay());
            statusManager.doPassTurn();
        }
    }

    /**
     * Updates the current state or information.
     * This method is intended to be overridden to provide
     * specific update functionality based on the class implementation.
     */
    @Override
    public void update() {

    }

    /**
     * Updates the log in the main view with a specified message and state.
     *
     * @param message the log message to be added
     * @param state   the state associated with the log message
     */
    @Override
    public void updateLog(String message, State state) {
        mainView.addLog(message, state);
    }

    /**
     * Updates the chat by adding a message from the user or a specific player.
     *
     * @param message The message to be added to the chat.
     * @param player  The player sending the message. If null, the message is treated as being from the user.
     */
    @Override
    public void updateChat(String message, Player player) {
        Platform.runLater(() -> {
            if (player == null) {
                mainView.addMsgFromUser(message);
            } else {
                mainView.addMsgFromPlayer(message, player);
            }
        });
    }

    /**
     * Creates and returns a Timeline object that handles the initialization sequence
     * for starting a card game.
     * A dummy card is drawn and a delay sequence is executed where players receive
     * starter logging messages and random greeting messages upon game start.
     *
     * @return a Timeline object configured to initialize the start of the game
     */
    private Timeline putStartDummyCard() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            Card card = deck.drawCard();
            putCardDummy(card, true);
            Client.send(playerId + "#PUT_DUMMY#" + card.toString());
        }));

        timeline.setOnFinished(e -> {
            delaySecond(new Runnable() {
                @Override
                public void run() {
                    if (Setting.isEnClicked()) {
                        log.setLogs("Start Game!", State.System);
                        Client.send(playerId + "#SYSTEM#Start Game!");
                    } else {
                        log.setLogs("게임 시작!", State.System);
                        Client.send(playerId + "#SYSTEM#게임 시작!");
                    }

                }
            });
        });

        return timeline;
    }

    /**
     * Resets the game state and initializes a new game session.
     * This method performs the following operations:
     * 1. Initializes and draws the game page.
     * 2. Resets the deck and transforms the list of players.
     * 3. Sets up a sequence of animations and transitions.
     * 4. Starts the game loop and enables game interactions.
     *
     * @param prevPlayers The list of players from the previous game session,
     *                    sorted in descending order and transformed for the new game.
     */
    public void resetGame(List<Player> prevPlayers) {
        Client.send(playerId + "#INIT_PAGE#NULL");
        initPage();
        Client.send(playerId + "#INIT_PAGE#NULL");
        drawGamePage();

        deck.generateDeck();
        Client.send(playerId + "#INIT_DECK#" + deck.toString());
        Collections.sort(prevPlayers, Collections.reverseOrder());

        transformPlayers(prevPlayers);
        for (Player player : players) System.out.println(player.getScore());

        if (Setting.isEnClicked()) {
            log.setLogs("Setting Game...!", State.System);
            Client.send(playerId + "#SYSTEM#Setting Game...!");
        } else {
            log.setLogs("게임 설정 중...!", State.System);
            Client.send(playerId + "#SYSTEM#게임 설정 중...!");
        }


        SequentialTransition sequence = new SequentialTransition();

        sequence.getChildren().add(getSixCards());

        sequence.getChildren().add(putStartDummyCard());

        sequence.play();

        sequence.setOnFinished(new SequenceFinishedHandler());
        System.out.println("New Game Start");
        if (Setting.isEnClicked()) {
            log.setLogs("New Game Start...!", State.System);
            Client.send(playerId + "#SYSTEM#New Game Start...!");
        } else {
            log.setLogs("새로운 게임 시작...!", State.System);
            Client.send(playerId + "#SYSTEM#새로운 게임 시작...!");
        }

    }

    private class SequenceFinishedHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            delaySecond(new StartGameHandler());
        }
    }

    private class StartGameHandler implements Runnable {
        @Override
        public void run() {
            gameLoop();
            game.play();
            disableButtons(false);
        }
    }

    /**
     * Transforms the list of previous players into a new list of players, initializes their states
     * and creates associated views for score, status, or hand based on their roles.
     *
     * @param prevPlayers the list of previous players to be transformed
     */
    private void transformPlayers(List<Player> prevPlayers) {
        players = new ArrayList<>();
        int scoreId = 0;
        int statusId = 0;
        for (int i = 0; i < 4; ++i) {
            Player prevPlayer = prevPlayers.get(i);
            Player player = new Player(i);
            players.add(player);
            player.copyPlayer(prevPlayer);
            if (prevPlayer.isSelf()) {
                player.setSelf();
                player.setScoreId(scoreId++);
                player.setStatusId(3);
                new PlayerHandView(player, mainView);
                new PlayerScoreView(player, mainView);
            } else {
                player.setScoreId(scoreId++);
                player.setStatusId(statusId++);
                new PlayerStatusView(player, mainView);
                new PlayerScoreView(player, mainView);
            }
            player.resetHand();
            player.callNotify();
        }
        Client.send(playerId + "#UPDATE_PLAYERS#NULL");
    }

    /**
     * Creates and initializes the players for the game, including the user and other AI players.
     * This method sets up the player instances, assigns unique avatars to each player, and updates
     * the views related to player status and score.
     * <p>
     * The method performs the following actions:
     * - Adds the user's character to the list of chosen player icons.
     * - Iterates through the number of players minus one (excluding the user).
     * - For each non-user player, it initializes a new player instance, associates it with views,
     * and assigns a unique avatar icon to the player, ensuring no duplicates among chosen players.
     * - Invokes the `createUser` method to handle user-specific setup.
     */
    private void createPlayers() {
        createUser();

        List<String> chosenPlayers = new ArrayList<>();
        for (Player player : players) {
            chosenPlayers.add(player.getIcon());
        }

        Random random = new Random();
        for (int i = players.size(); i < 4; i++) {
            Player player = new Player(i);
            players.add(player);
            new PlayerStatusView(player, mainView);
            new PlayerScoreView(player, mainView);
            String url;
            do {
                int intUser = random.nextInt(7) + 1;
                url = String.format("/avatar/User-0%d.png", intUser);
            } while (chosenPlayers.contains(url));

            player.setIcon(url, false);
            player.setPlayer(false);
            chosenPlayers.add(url);
        }
        Client.send(playerId + "#CREATE_PLAYERS#" + playerToString());
        System.out.println(players.size() + " " + users.size());
    }

    /**
     * Creates a new user by initializing a Player instance and setting up its associated views.
     * <p>
     * This method performs the following steps:
     * 1. Instantiates a new Player object with the adjusted player number (playerNum - 1).
     * 2. Marks the player as the self-player using the setSelf method.
     * 3. Adds the newly created Player object to the players collection.
     * 4. Creates a PlayerScoreView for the player and associates it with the mainView.
     * 5. Creates a PlayerHandView for the player and associates it with the mainView.
     * 6. Sets the player's icon using the predefined userCharacter.
     */
    private void createUser() {
        for (Player player : players) {
            if (player.getNetworkId() == playerId) {
                player.setSelf();
                player.setPlayer(true);
                new PlayerScoreView(player, mainView);
                new PlayerHandView(player, mainView);
                player.setIcon(player.getIcon(), false);
            } else {
                player.setPlayer(true);
                new PlayerStatusView(player, mainView);
                new PlayerScoreView(player, mainView);
                player.setIcon(player.getIcon(), false);
            }
        }
    }

    /**
     * Creates and returns a {@code ParallelTransition} animation that simulates
     * dealing six cards to each player sequentially. The card dealing animation
     * varies depending on whether the player is the user or another player.
     *
     * @return A {@code ParallelTransition} consisting of card animation timelines
     * for each player.
     */
    private ParallelTransition getSixCards() {
        ParallelTransition pt = new ParallelTransition();
        for (Player player : players) {
            Timeline giveCard;
            if (player.isSelf()) {
                giveCard = new Timeline(
                        new KeyFrame(Duration.seconds(1), new UserCardEventHandler(player))
                );
            } else {
                giveCard = new Timeline(
                        new KeyFrame(Duration.seconds(1), new PlayerCardEventHandler(player))
                );
            }
            giveCard.setCycleCount(6);
            pt.getChildren().add(giveCard);
        }
        if (Setting.isEnClicked()) {
            log.setLogs("Give 6 cards to players!", State.System);
            Client.send(playerId + "#SYSTEM#Give 6 cards to players!");
        } else {
            log.setLogs("플레이어들에게 6장의 카드 나눠 주는 중!", State.System);
            Client.send(playerId + "#SYSTEM#플레이어들에게 6장의 카드 나눠 주는 중!");
        }


        return pt;
    }

    private class UserCardEventHandler implements EventHandler<ActionEvent> {
        private final Player player;

        public UserCardEventHandler(Player player) {
            this.player = player;
        }

        @Override
        public void handle(ActionEvent event) {
            Animation animation = mainView.getCardAnimationToUser();
            animation.setOnFinished(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent ev) {
                    mainView.removeAnimationCard();
                    Card card = player.setCard(deck, false);
                    Client.send(playerId + "#DRAW_CARD#" + player.getNetworkId() + " " + card.toString());
                }
            });

            animation.play();
        }
    }

    private class PlayerCardEventHandler implements EventHandler<ActionEvent> {
        private final Player player;

        public PlayerCardEventHandler(Player player) {
            this.player = player;
        }

        @Override
        public void handle(ActionEvent event) {
            Animation animation = mainView.getCardAnimationToPlayer();
            animation.setOnFinished(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent ev) {
                    mainView.removeAnimationCard();
                    Card card = player.setCard(deck, false);
                    Client.send(playerId + "#DRAW_CARD#" + player.getNetworkId() + " " + card.toString());
                }
            });

            animation.play();
        }
    }

    /**
     * Delays the execution of a given action by one second.
     *
     * @param action the action to be executed after the delay
     */
    public void delaySecond(Runnable action) {
        PauseTransition delay = new PauseTransition(Duration.seconds(1)); // 1초 딜레이
        delay.setOnFinished(new PauseTransitionHandler(action));
        delay.play();
    }

    private class PauseTransitionHandler implements EventHandler<ActionEvent> {
        private final Runnable action;

        public PauseTransitionHandler(Runnable action) {
            this.action = action;
        }

        @Override
        public void handle(ActionEvent event) {
            action.run();
        }
    }

    /**
     * Executes the specified action after a delay of 500 milliseconds.
     *
     * @param action the Runnable action to be executed after the delay
     */
    private void delayQueen(Runnable action) {
        PauseTransition delay = new PauseTransition(Duration.millis(500)); // 0.5초 딜레이
        delay.setOnFinished(new DelayQueenHandler(action));
        delay.play();
    }

    private class DelayQueenHandler implements EventHandler<ActionEvent> {
        private final Runnable action;

        public DelayQueenHandler(Runnable action) {
            this.action = action;
        }

        @Override
        public void handle(ActionEvent event) {
            action.run();
        }
    }

    /**
     * Removes any mouse click event handlers currently assigned to the deck component
     * in the main view. If the deck has a non-null click event handler, it will be cleared
     * by setting it to null. This method ensures that the deck no longer responds to mouse
     * click events.
     */
    private void removeDeckEffects() {
        if (mainView.getDeck().getOnMouseClicked() != null) {
            mainView.getDeck().setOnMouseClicked(null);
        }
    }

    /**
     * Removes all mouse event handlers from the currently displayed cards.
     * <p>
     * This method iterates through the list of cards retrieved from the mainView
     * and checks for any non-null mouse event handlers (e.g., onMouseEntered,
     * onMouseExited, onMousePressed, onMouseDragged, onMouseReleased). If any of
     * these event handlers are found, they are cleared by setting them to null.
     * <p>
     * The purpose of this method is to ensure that no mouse event interactions
     * are associated with the card elements, effectively disabling all mouse-based
     * card effects or interactivity.
     */
    private void removeCardEffects() {
        for (ImageView card : mainView.getCurCards()) {
            if (card.getOnMouseEntered() != null) {
                card.setOnMouseEntered(null);
            }
            if (card.getOnMouseExited() != null) {
                card.setOnMouseExited(null);
            }
            if (card.getOnMousePressed() != null) {
                card.setOnMousePressed(null);
            }
            if (card.getOnMouseDragged() != null) {
                card.setOnMouseDragged(null);
            }
            if (card.getOnMouseReleased() != null) {
                card.setOnMouseReleased(null);
            }
        }
    }

    /**
     * Handles the rendering and setup for the main game page. Configures UI
     * elements such as buttons and fields, and assigns their respective
     * event handlers for functionality like restarting the game, navigating
     * back to the main menu, and sending chat messages.
     * <p>
     * This method also initializes the necessary visual and interactive
     * components for the game page, applying the appropriate styles and
     * controls to the stage and scene.
     * <p>
     * Functionality:
     * - Resets the root styling for the page.
     * - Draws the main game page view based on the specified number of players.
     * - Configures button actions for back navigation, displaying settings,
     * restarting the game, and sending chat messages.
     * - Disables or enables buttons based on the game state.
     */
    private void drawGamePage() {
        root.setStyle("");
        mainView.drawMainPage(playerNum);

        mainView.getBack().setOnMouseClicked(new BackButtonHandler());
        mainView.getSetting().setOnMouseClicked(new SettingButtonHandler());
        mainView.getMessage().setOnAction(new MessageHandler());

        disableButtons(true);
    }

    private class BackButtonHandler implements EventHandler<MouseEvent> {
        @Override
        public void handle(MouseEvent event) {
            Client.send(playerId + "#FORCE_EXIT#" + playerId);
            mainView.setFadeOutGame(scene);
            game.stop();
        }
    }

    private class SettingButtonHandler implements EventHandler<MouseEvent> {
        @Override
        public void handle(MouseEvent event) {
            settingView.generate();
        }
    }

    private class MessageHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            String msg = mainView.getMessage().getText();
            if (!msg.isEmpty()) {
                chat.addMessage(msg);
                mainView.getMessage().clear();
                Client.send(playerId + "#CHAT#" + msg);
            }
        }
    }

    /**
     * Initializes the main page of the application by adding the mainPane to the root
     * and setting the root as the scene's root element. This method sets up the primary
     * UI layout for the application.
     */
    private void initPage() {
        root.getChildren().add(mainPane);
        scene.setRoot(root);
    }

    /**
     * Updates the visual effect of the timer based on the current player's turn.
     * If the current turn belongs to the local player, the timer effect is activated.
     * Otherwise, the timer effect is deactivated.
     */
    public void setTurnEffect() {
        if (players.get(statusManager.getTurn()).isSelf()) {
            mainView.setTimerEffect();
        } else {
            mainView.delTimerEffect();
        }
    }


    /**
     * Adjusts the number of cards a player can draw to ensure it does not exceed
     * the allowed limit and manages game state accordingly.
     *
     * @param player the player whose card draw limit needs to be clamped
     * @return {@code true} if the maximum card limit has been reached and the
     * player's turn is passed; {@code false} otherwise
     */
    private boolean clamping(Player player) {
        int maxCanDraw = 12 - player.getCardLeft();
        if (stackGetCard > maxCanDraw) {
            stackGetCard = maxCanDraw;
        }

        if (stackGetCard == 0) {
            if (DEBUG) System.out.println("Player has 12 cards");
            if (Setting.isEnClicked()) {
                log.setLogs("Player has 12 cards", State.System);
                Client.send(playerId + "#SYSTEM#Player has 12 cards");
            } else {
                log.setLogs("플레이어 12장의 카드를 가지고 있음", State.System);
                Client.send(playerId + "#SYSTEM#플레이어 12장의 카드를 가지고 있음");
            }

            stackGetCard = 1;
            Client.send(playerId + "#STACK#" + stackGetCard);
            statusManager.doPassTurn();
            return true;
        }
        return false;
    }

    /**
     * Disables or enables specific buttons in the main view based on the provided parameter.
     *
     * @param disable if true, the buttons will be disabled; if false, the buttons will be enabled
     */
    private void disableButtons(boolean disable) {
        mainView.getBack().setDisable(disable);
        mainView.getSetting().setDisable(disable);
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void addPlayer(Player player) {
        players.add(player);
    }

}
