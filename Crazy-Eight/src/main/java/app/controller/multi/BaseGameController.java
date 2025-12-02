package app.controller.multi;

import app.model.multi.*;
import app.view.SettingView;
import app.view.multi.CharacterChooseView;
import app.view.multi.ChooseEightView;
import app.view.multi.MultiPlayGameView;
import app.view.multi.ScoringView;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * An abstract base class that serves as the controller for managing game logic in a multiplayer card game.
 * This class implements various observer interfaces to handle updates and interactions with game components
 * such as cards, decks, logs, and chat messages. It also manages the views, players, and game state.
 */
public abstract class BaseGameController implements CardObserver, DeckObserver, LogObserver, ChatObserver {
    protected Scene scene;
    protected StackPane root;
    protected BorderPane mainPane;

    protected MultiPlayGameView mainView;
    protected SettingView settingView;
    protected ChooseEightView chooseEightView;
    protected CharacterChooseView characterChooseView;
    protected ScoringView scoringView;

    protected List<Player> players = new ArrayList<>();
    protected List<Player> users = new ArrayList<>();
    protected int playerNum = 4;
    protected Deck deck;
    protected DummyCard dummyCard;
    protected Log log;
    protected Chat chat;

    protected int playerRanPutTime = ThreadLocalRandom.current().nextInt(3, 8);
    protected boolean playerDoChat = Math.random() < 0.7;
    protected int playerChatTime = ThreadLocalRandom.current().nextInt(1, playerRanPutTime);

    protected int playerId;
    protected GameStatusManager statusManager;

    protected final boolean DEBUG = true;

    protected int stackGetCard = 1;

    protected Timeline game = null;

    /**
     * Converts the list of users into a single string representation.
     * Each user's string representation is concatenated with a space separating them.
     *
     * @return a string containing the concatenated string representations of all users in the list.
     */
    public String userToString() {
        String str = "";
        for (Player player : users) {
            str += player.toString() + " ";
        }
        return str;
    }

    /**
     * Converts the list of players into a single string representation.
     * Each player's string representation is concatenated with a space separating them.
     *
     * @return a string containing the concatenated string representations of all players in the list.
     */
    public String playerToString() {
        String str = "";
        for (Player player : players) {
            str += player.toString() + " ";
        }
        return str;
    }
}
