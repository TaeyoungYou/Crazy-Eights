package app.model;

/**
 * The CardObserver interface defines the contract for observing changes related to a Card object.
 * Classes implementing this interface can be notified of specific events concerning a Card.
 */
public interface CardObserver {
    /**
     * Updates the state or appearance of a given card.
     *
     * @param card the Card object whose state or appearance needs to be updated.
     */
    void update(Card card);
    /**
     * Notifies the observer to update and handle the addition of the specified card to a deck.
     *
     * @param card the Card object that is being added to the deck and requires an update notification.
     */
    void updateAddToDeck(Card card);
}
