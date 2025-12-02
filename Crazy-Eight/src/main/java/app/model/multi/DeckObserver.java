package app.model.multi;

/**
 * DeckObserver is an interface used to observe changes or updates
 * in a deck of cards or any related observable entity. Implementing
 * this interface allows an object to receive notifications when
 * updates occur.
 */
public interface DeckObserver {
    /**
     * Notifies the implementing observer of an update or change in the observed subject.
     * The specific implementation of this method will define the behavior
     * when an update notification is received.
     */
    void update();
}
