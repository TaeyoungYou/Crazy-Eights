package app.model.single;

/**
 * The PlayerObserver interface represents an observer in the observer design pattern
 * for tracking changes in a Player object. Classes implementing this interface
 * can observe and respond to updates in the state of a Player.
 */
public interface PlayerObserver {
    /**
     * Updates the state of the observer based on the provided player and
     * whether card handling needs to be performed.
     *
     * @param player the Player object associated with the update
     * @param handleCard a boolean flag indicating if card handling should be performed
     */
    void update(Player player, boolean handleCard);
}
