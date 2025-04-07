package app.controller.multi;

import app.model.multi.*;
import app.view.SettingView;
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

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;


/**
 * The GameClientController class manages the client-side game logic, including
 * handling server messages, player actions, game state, and visual effects.
 * It provides methods for updating the game view, managing turns, assigning
 * scores, and implementing game rule logic.
 * <p>
 * This class integrates player and game state updates with animations
 * and localized logging, ensuring a smooth and interactive user experience.
 */
public class GameClientController extends BaseGameController implements CardObserver, DeckObserver, LogObserver, ChatObserver {
    private int stack = 1;

    /**
     * Constructs a GameClientController instance for managing game views, game status,
     * and other components associated with the game client.
     *
     * @param _scene the primary scene to which all views and game components will be added
     */
    public GameClientController(Scene _scene) {
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
        statusManager = new GameStatusManager(playerNum);
    }

    /**
     * Handles server messages received by the client and processes them based on the message type.
     * Each message type triggers specific actions or updates in the game state or UI.
     *
     * @param message the raw string message received from the server. This message is parsed into
     *                a structured format and its type and data are used to execute corresponding game logic.
     */
    public void handleServerMessage(String message) {
        try {
            MessageParser.ParsedMessage parsed = MessageParser.parse(message);

            switch (parsed.getMsgType()) {
                case INIT_PAGE:
                    if (DEBUG) System.out.println("Multi Play Game Start!");
                    Platform.runLater(() -> {
                        try {
                            initPage();
                            disableButtons(false);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                    break;
                case INIT_DECK:
                    GameDTO.initDeck(parsed.getData(), deck);
                    break;
                case INIT_PLAYERS:
                    GameDTO.initPlayers(parsed.getData(), players, users);
                    break;
                case CREATE_PLAYERS:
                    GameDTO.createPlayers(parsed.getData(), players);
                    createPlayers();
                    System.out.println("플레이어 생성완료!");
                    break;
                case UPDATE_PLAYERS:
                    List<Player> previewPlayers = new ArrayList<>(players);
                    Collections.sort(previewPlayers, Collections.reverseOrder());
                    transformPlayers(previewPlayers);
                    break;
                case DRAW_CARD:
                    Pair<Integer, Card> info = GameDTO.drawCard(parsed.getData());
                    if (info.getKey() == playerId) {
                        Platform.runLater(() -> {
                            Animation animation = mainView.getCardAnimationToUser();
                            animation.play();
                            animation.setOnFinished(e -> {
                                mainView.removeAnimationCard();
                                players.get(playerId).addCard(info.getValue());
                            });
                        });

                    } else {
                        Platform.runLater(() -> {
                            Animation animation = mainView.getCardAnimationToPlayer();
                            animation.play();
                            animation.setOnFinished(e -> {
                                mainView.removeAnimationCard();
                                players.get(info.getKey()).addCard(info.getValue());
                            });
                        });

                    }
                    System.out.println("드로우 완료!");
                    break;
                case PUT_DUMMY:
                    Platform.runLater(() -> {
                        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
                            Card card = GameDTO.putDummy(parsed.getData());
                            putCardDummy(card, true);
                        }));
                        timeline.play();
                        timeline.setOnFinished(e -> {
                            if (Setting.isEnClicked()) log.setLogs("Put Dummy Card!", State.Log);
                            else log.setLogs("게임 시작!", State.System);
                        });
                    });
                    break;
                case UPDATE_TURN:
                    int turn = Integer.parseInt(parsed.getData());
                    for (Player player : players) {
                        player.setMyTurn(false);
                        removeCardEffects();
                        removeDeckEffects();
                    }
                    Player player = players.get(turn);
                    player.setMyTurn(true);

                    if (players.get(turn).isSelf()) {
                        Platform.runLater(() -> {
                            addCardEffects(player);
                            addDeckEffects(player);
                            mainView.setTimerEffect();
                        });
                        // 로직 추가
                    } else {
                        mainView.delTimerEffect();
                    }
                    break;
                case TIME_SET:
                    Platform.runLater(() -> {
                        int time = Integer.parseInt(parsed.getData());
                        mainView.setTimer(10 - time);
                    });
                    break;
                case PUT_CARD:
                    if (playerId == parsed.getSenderPlayerId()) break;

                    Pair<Integer, Card> infoPut = GameDTO.putCard(parsed.getData());
                    Platform.runLater(() -> {
                        Animation animation = mainView.putCardAnimationWithPlayer();
                        animation.play();
                        animation.setOnFinished(e -> {
                            // server put card > do animation, update hand count
                            // CPU(server) put card > do animation, update hand count
                            players.get(infoPut.getKey()).removeCard(infoPut.getValue());
                            putCardDummy(infoPut.getValue(), true);
                        });
                    });
                    break;
                case REQUEST_DRAW_CARD:
                    break;
                case TIME_OUT:
                    int id = Integer.parseInt(parsed.getData());
                    if (id == playerId) {
                        Client.send(playerId + "#REQUEST_DRAW_CARD#" + playerId);   // draw card request to server
                    }
                    break;
                case STACK:
                    stack = Integer.parseInt(parsed.getData());
                    System.out.println("현재 스택 = " + stack);
                    break;
                case CRAZY_EIGHT:
                    Platform.runLater(() -> {
                        chooseEightView.generate();
                        chooseEightView.getSpace().setOnMouseClicked(new SpaceClickHandler());
                        chooseEightView.getHeart().setOnMouseClicked(new HeartClickHandler());
                        chooseEightView.getDiamond().setOnMouseClicked(new DiamondClickHandler());
                        chooseEightView.getClub().setOnMouseClicked(new ClubClickHandler());
                    });
                    break;
                case CRAZY_EIGHT_DONE:
                    break;
                case SERVER_EIGHT:
                    Pair<Integer, Card> card = GameDTO.putCard(parsed.getData());
                    putCardDummy(card.getValue(), true);
                    break;
                case REVERSE_ORDER:
                    System.out.println("reverse order");
                    break;
                case QUEEN:
                    System.out.println("next player skip");
                    break;
                case END:
                    Platform.runLater(() -> {
                        endGame();
                    });
                    break;
                case CONTINUE:
                    scoringView.fadeOutPane();
                    mainView.resetGameClient(scene, mainPane, players);
                    break;
                case EXIT:
                    int exitId = Integer.parseInt(parsed.getData());
                    if (exitId != playerId) {
                        Platform.runLater(() -> {
                            scoringView.fadeOutPane();
                            mainView.setFadeOutGame(scene);
                        });
                    }
                    break;
                case FORCE_EXIT:
                    int forceExitId = Integer.parseInt(parsed.getData());
                    if (forceExitId != playerId) {
                        System.out.println("다른 플레이어 종료");
                        mainView.setFadeOutGame(scene);
                    }
                    break;
                case READY:
                    break;
                case CHAT:
                    int chatId = parsed.getSenderPlayerId();
                    if (chatId != playerId) {
                        chat.addMessage(parsed.getData(), players.get(chatId));
                    }
                    break;
                case LOG:
                    Platform.runLater(() -> {
                        log.setLogs(parsed.getData(), State.Log);
                    });
                    break;
                case SYSTEM:
                    Platform.runLater(() -> {
                        log.setLogs(parsed.getData(), State.System);
                    });
                    break;
                case ERROR:
                    Platform.runLater(() -> {
                        log.setLogs(parsed.getData(), State.Error);
                    });
                    break;
                default:
                    System.err.println("알 수 없는 메시지 타입: " + parsed.getMsgType());
            }

        } catch (MessageParser.InvalidMessageFormatException e) {
            System.err.println("메시지 파싱 실패: " + e.getMessage());
        }
    }

    /**
     * Saves the information for a player by storing their ID.
     *
     * @param playerId The unique identifier of the player whose information is to be saved.
     */
    public void saveInfo(int playerId) {
        this.playerId = playerId;
    }


    /**
     * Handles the end of the game logic.
     * <p>
     * This method pauses the game, logs a "Game Over" message in the console if debug mode is enabled,
     * and records a localized "Game Over" message in the system log. It then initializes the scoring
     * view, generates scores for
     */
    private void endGame() {
        if (DEBUG) System.out.println("Game is done..!");
        if (Setting.isEnClicked()) log.setLogs("Game Over!", State.System);
        else log.setLogs("게임 종료!", State.System);

        scoringView.generate(players, scoring());
        scoringView.buttonAnimation();
        scoringView.getContinueButton().setOnMouseClicked(new ContinueButtonHandler());
        scoringView.getExitButton().setOnMouseClicked(new ExitButtonHandler());
    }

    private class ContinueButtonHandler implements EventHandler<MouseEvent> {
        @Override
        public void handle(MouseEvent e) {
            Client.send(playerId + "#READY#" + playerId);
            scoringView.getContinueButton().setText("Waiting");
        }
    }

    private class ExitButtonHandler implements EventHandler<MouseEvent> {
        @Override
        public void handle(MouseEvent e) {
            Client.send(playerId + "#EXIT#" + playerId);
            scoringView.fadeOutPane();
            mainView.setFadeOutGame(scene);
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
                Client.send(playerId + "#REQUEST_DRAW_CARD#" + playerId);   // draw card request to server
                removeDeckEffects();
                mainView.getDeck().setDisable(false);
            }
        });
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
        if (DEBUG) System.out.println(String.format(" - Put %s %d card", card.getImogeSuit(), card.getRank() + 1));
        if (Setting.isEnClicked())
            log.setLogs(String.format("Put %s %s card", card.getImogeSuit(), card.getRankString()), State.Log);
        else log.setLogs(String.format("카드 %s %s 놓음", card.getKoreanSuit(), card.getRankString()), State.Log);

        removeDeckEffects();
        removeCardEffects();
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


    private class SpaceClickHandler implements EventHandler<MouseEvent> {
        @Override
        public void handle(MouseEvent event) {
            event.consume();
            putCardDummy(new Card(0, 7), true);
            fadeOutPane();
            if (DEBUG) System.out.println("Change to Space!");
            if (Setting.isEnClicked()) log.setLogs("Change to Space!", State.System);
            else log.setLogs("스페이드로 바꿈!", State.System);
            Client.send(playerId + "#CRAZY_EIGHT_DONE#" + playerId + " " + "0:7");
        }
    }

    private class HeartClickHandler implements EventHandler<MouseEvent> {
        @Override
        public void handle(MouseEvent event) {
            event.consume();
            putCardDummy(new Card(1, 7), true);
            fadeOutPane();
            if (DEBUG) System.out.println("Change to Heart!!");
            if (Setting.isEnClicked()) log.setLogs("Change to Heart!", State.System);
            else log.setLogs("하트로 바꿈!", State.System);
            Client.send(playerId + "#CRAZY_EIGHT_DONE#" + playerId + " " + "1:7");
        }
    }

    private class DiamondClickHandler implements EventHandler<MouseEvent> {
        @Override
        public void handle(MouseEvent event) {
            event.consume();
            putCardDummy(new Card(2, 7), true);
            fadeOutPane();
            if (DEBUG) System.out.println("Change to Diamond!!");
            if (Setting.isEnClicked()) log.setLogs("Change to Diamond!", State.System);
            else log.setLogs("다이아몬드로 바꿈!", State.System);
            Client.send(playerId + "#CRAZY_EIGHT_DONE#" + playerId + " " + "2:7");
        }
    }

    private class ClubClickHandler implements EventHandler<MouseEvent> {
        @Override
        public void handle(MouseEvent event) {
            event.consume();
            putCardDummy(new Card(3, 7), true);
            fadeOutPane();
            if (DEBUG) System.out.println("Change to Club!");
            if (Setting.isEnClicked()) log.setLogs("Change to Club!", State.System);
            else log.setLogs("크로버로 바꿈!", State.System);
            Client.send(playerId + "#CRAZY_EIGHT_DONE#" + playerId + " " + "3:7");
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
        for (Player player : players) {
            if (player.getNetworkId() == playerId) {
                player.setSelf();
                new PlayerScoreView(player, mainView);
                new PlayerHandView(player, mainView);
                player.setIcon(player.getIcon(), false);
            } else {
                new PlayerStatusView(player, mainView);
                new PlayerScoreView(player, mainView);
                player.setIcon(player.getIcon(), false);
            }
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
        }
    }

    private class SettingButtonHandler implements EventHandler<MouseEvent> {
        @Override
        public void handle(MouseEvent event) {
            settingView.generate();
        }
    }

    private class RestartButtonHandler implements EventHandler<MouseEvent> {
        @Override
        public void handle(MouseEvent event) {
            mainView.resetGame(scene, mainPane, null);
            game.stop();
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
        if (!root.getChildren().contains(mainPane)) root.getChildren().add(mainPane);
        scene.setRoot(root);


        drawGamePage();
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
            if (Setting.isEnClicked()) log.setLogs("Player has 12 cards", State.System);
            else log.setLogs("플레이어 12장의 카드를 가지고 있음", State.System);
            stackGetCard = 1;
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

    /**
     * Retrieves the list of players.
     *
     * @return a list of Player objects.
     */
    public List<Player> getPlayers() {
        return players;
    }

}
