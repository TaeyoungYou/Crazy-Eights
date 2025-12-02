package app.model.multi;

import app.view.multi.PlayerScoreView;
import app.view.single.PlayerHandView;
import app.view.single.PlayerStatusView;
import javafx.application.Platform;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Represents a player in a card game. This class maintains the details of a player's
 * game attributes such as score, status, hand of cards, and other player-specific
 * properties. It also supports observer notification for changes in the player's state.
 * <p>
 * Implements the Comparable interface to enable comparison based on score.
 */
public class Player implements Comparable<Player> {
    private int networkId;
    private int scoreId;
    private int statusId;
    private String icon;    // status와 score에 사용될
    private int score;      // score에 사용될

    private List<Card> hand;
    private List<PlayerObserver> observers;

    private boolean self;   // 자기 자신인지
    private boolean myTurn;
    private boolean handleCard;
    private boolean player;     // 나중에 실제 플레이어인지 CPU 구분할 용도

    /**
     * Constructs a Player instance with the given index.
     * Initializes the player with default values such as an avatar icon,
     * a score of zero, and empty hands.
     *
     * @param index the index used to initialize both the scoreId and statusId of the player
     */
    public Player(int index) {
        networkId = index;
        scoreId = index;
        statusId = index;
        icon = "/avatar/User-01.png";
        score = 0;
        self = false;
        myTurn = false;
        handleCard = false;
        hand = new ArrayList<>();
        observers = new ArrayList<>();
    }

    public String toString() {
        return networkId + ":" + icon;
    }

    /**
     * Copies the attributes of the given Player object to this Player object.
     *
     * @param player the Player object from which the attributes will be copied
     */
    public void copyPlayer(Player player) {
        this.icon = player.icon;
        this.score = player.score;
        this.self = player.self;
        this.myTurn = player.myTurn;
        this.handleCard = player.handleCard;
        this.hand = player.hand;
        this.observers = player.observers;
    }

    /**
     * Removes a card from the player's hand based on the specified index
     * and notifies all registered observers of the change.
     *
     * @param index the index of the card to be removed from the player's hand
     */
    public void removeCard(int index) {
        hand.remove(index);
        notifyObservers();
    }

    /**
     * Retrieves the score ID of the player.
     *
     * @return the integer value representing the player's score ID.
     */
    public int getNetworkId() {
        return networkId;
    }

    /**
     * Sets the score ID of the player.
     *
     * @param networkId the integer value representing the player's new score ID
     */
    public void setNetworkId(int networkId) {
        this.networkId = networkId;
    }

    /**
     * Sets the status ID of the player.
     *
     * @param statusId the integer value representing the player's new status ID
     */
    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }

    /**
     * Retrieves the status ID of the player.
     *
     * @return the integer value representing the player's status ID.
     */
    public int getStatusId() {
        return statusId;
    }

    /**
     * Sets the icon for the player using the specified URL and notifies all registered observers of the change.
     *
     * @param url the URL of the new icon to be set for the player
     */
    public void setIcon(String url, boolean skipObserver) {
        icon = url;
        if (!skipObserver) notifyObservers();
    }

    /**
     * Sets the turn status of the player and notifies all registered observers
     * about the change.
     *
     * @param myTurn a boolean indicating whether it is the player's turn
     */
    public void setMyTurn(boolean myTurn) {
        this.myTurn = myTurn;
        notifyObservers();
    }

    /**
     * Checks whether it is the player's turn.
     *
     * @return true if it is the player's turn, false otherwise.
     */
    public boolean isMyTurn() {
        return myTurn;
    }

    /**
     * Retrieves the icon associated with the player.
     *
     * @return a string representing the player's icon.
     */
    public String getIcon() {
        return icon;
    }

    /**
     * Retrieves the number of cards left in the player's hand.
     *
     * @return the integer value representing the number of cards in the player's hand.
     */
    public int getCardLeft() {
        return hand.size();
    }

    /**
     * Retrieves the current hand of the player, which consists of a list of {@code Card} objects
     * representing the player's cards.
     *
     * @return a list of {@code Card} objects representing the player's hand.
     */
    public List<Card> getHand() {
        return hand;
    }

    /**
     * Removes the specified card from the player's hand, updates the internal
     * state to indicate a change, and notifies all registered observers.
     *
     * @param card the {@code Card} object to be removed from the player's hand
     */
    public void removeCard(Card card) {
        hand.remove(card);
        handleCard = true;
        notifyObservers();
    }

    /**
     * Adds a card drawn from the specified deck to the player's hand. Updates the
     * player's internal state to indicate that a card has been handled.
     * Optionally skips notifying registered observers based on the provided flag.
     *
     * @param deck         the {@code Deck} object from which a card is drawn and added to the player's hand
     * @param skipObserver a boolean indicating whether to skip notifying observers;
     *                     {@code true} to skip notification, {@code false} to notify all observers
     */
    public Card setCard(Deck deck, boolean skipObserver) {
        Card temp = deck.drawCard();
        hand.add(temp);
        handleCard = true;
        if (!skipObserver) notifyObservers();

        return temp;
    }

    /**
     * Retrieves the current score of the player.
     *
     * @return the integer value representing the player's current score.
     */
    public int getScore() {
        return score;
    }

    /**
     * Updates the player's current score by adding the specified score value.
     *
     * @param score the integer value to be added to the player's current score
     */
    public void addScore(int score) {
        this.score += score;
    }

    /**
     * Determines whether the current player is the one represented by this instance.
     *
     * @return true if the current player is this instance, false otherwise.
     */
    public boolean isSelf() {
        return self;
    }

    /**
     * Marks this player instance as the "self" player by setting the internal `self` field to true.
     */
    public void setSelf() {
        this.self = true;
    }

    /**
     * Determines the suit (shape) with the highest occurrence in the player's hand.
     * If there is a tie, or in 20% of the cases (simulated randomly),
     * the method randomly selects a suit.
     *
     * @return an integer representing the suit with the highest occurrence:
     * 0 for spades, 1 for hearts, 2 for diamonds, 3 for clubs.
     */
    public int getMostShape() {
        if (Math.random() < 0.8) {
            int spade = 0, heart = 0, diamond = 0, club = 0;
            for (Card card : hand) {
                switch (card.getSuit()) {
                    case 0 -> spade++;
                    case 1 -> heart++;
                    case 2 -> diamond++;
                    case 3 -> club++;
                }
            }
            int maxCount = Math.max(Math.max(diamond, club), Math.max(spade, heart));

            if (maxCount == spade) return 0;
            if (maxCount == heart) return 1;
            if (maxCount == diamond) return 2;
            return 3;
        }
        return new Random().nextInt(4);
    }

    /**
     * Clears the player's hand by removing all cards from it.
     * This method is typically used to reset a player's hand
     * to an empty state during a game.
     */
    public void resetHand() {
        hand.clear();
    }

    /**
     * Notifies all registered observers about changes in the player's state.
     * This method serves as a convenience wrapper around the {@code notifyObservers}
     * method to ensure that any updates to the player's state are communicated to
     * the observers promptly.
     * <p>
     * The specific behavior of the notification depends on the observer type:
     * - If the observer is an instance of {@code PlayerStatusView}, it receives
     * updates with the current player state and the {@code handleCard} status.
     * - If the observer is an instance of {@code PlayerHandView} and the player
     * is the current player (as determined by {@code isSelf()}), it receives
     * updates with the player's state and a default value for {@code handleCard}.
     * - Other observers receive updates with the player's state and default values.
     * <p>
     * The {@code handleCard} flag is reset to {@code false} after the notification.
     */
    public void callNotify() {
        notifyObservers();
    }

    /**
     * Registers an observer to monitor changes in the player's state or actions.
     * The observer will be notified whenever relevant updates occur.
     *
     * @param observer the {@code PlayerObserver} instance to be registered
     */
    public void addObserver(PlayerObserver observer) {
        observers.add(observer);
    }

    /**
     * Notifies all registered observers about changes to the player's state.
     * <p>
     * The method iterates over all observers subscribed to the current player and
     * invokes their {@code update} method with specific parameters based on their type:
     * <p>
     * - If the observer is an instance of {@code PlayerStatusView}, it receives a call to the
     * {@code update} method with {@code this} player object and the current state of {@code handleCard}.
     * - If the observer is an instance of {@code PlayerHandView} and the player is identified as "self",
     * the observer's {@code update} method is invoked with {@code this} player object and {@code false}.
     * - All other observers are also notified using {@code this} player object and {@code false}.
     * <p>
     * After notifying all observers, the internal {@code handleCard} flag is reset to {@code false}.
     */
    public void notifyObservers() {
        for (PlayerObserver observer : observers) {
            Platform.runLater(() -> {
                if (observer instanceof PlayerStatusView && !isSelf()) {
                    observer.update(this, handleCard);
                } else if (observer instanceof PlayerHandView && isSelf()) {
                    observer.update(this, false);
                } else if (observer instanceof PlayerScoreView) {
                    observer.update(this, false);
                } else {
                    observer.update(this, false);
                }
            });
        }
        handleCard = false;
    }

    /**
     * Updates the player's status to indicate whether they are a player or not.
     *
     * @param player a boolean value where {@code true} indicates the entity is a player,
     *               and {@code false} indicates it is not.
     */
    public void setPlayer(boolean player) {
        this.player = player;
    }

    /**
     * Determines whether the current instance represents a "player."
     *
     * @return true if the current instance is marked as a player, false otherwise.
     */
    public boolean isPlayer() {
        return player;
    }

    /**
     * Retrieves the score ID of the player.
     *
     * @return the integer value representing the player's score ID.
     */
    public int getScoreId() {
        return scoreId;
    }

    /**
     * Sets the score ID of the player.
     *
     * @param id the integer value representing the new score ID to be assigned to the player
     */
    public void setScoreId(int id) {
        this.scoreId = id;
    }

    /**
     * Compares this player object with the specified player object for order based on scores.
     * Returns a negative integer, zero, or a positive integer as this player's score
     * is less than, equal to, or greater than the specified player's score.
     *
     * @param o the Player object to be compared with this player
     * @return a negative integer, zero, or a positive integer as this player's
     * score is less than, equal to, or greater than the specified player's score
     */
    @Override
    public int compareTo(Player o) {
        return this.score - o.getScore();
    }


    /**
     * Adds a card to the player's hand and notifies all registered observers about the change.
     *
     * @param card the {@code Card} object to be added to the player's hand
     */
    public void addCard(Card card) {
        hand.add(card);
        notifyObservers();
    }
}
