package app.model.single;

import app.controller.single.SinglePlayGameController;
import javafx.scene.image.Image;

/**
 * The DummyCard class serves as a placeholder or proxy for a Card object,
 * maintaining an association with an Image, a Card, a CardObserver, and a
 * SinglePlayGameController. It is responsible for managing the state and behavior
 * of a Card, including updates to its image and notifying observers when
 * changes occur.
 */
public class DummyCard {
    private Image image;
    private Card card;
    private CardObserver observer;
    private SinglePlayGameController controller;

    /**
     * Constructs a DummyCard object, which serves as a placeholder or proxy
     * for a Card object and associates it with the given SinglePlayGameController.
     *
     * @param controller the SinglePlayGameController instance responsible for
     *                    managing the interactions and updates related to this DummyCard
     */
    public DummyCard(SinglePlayGameController controller) {
        this.controller = controller;
    }
    /**
     * Retrieves the image associated with this DummyCard.
     *
     * @return the image of this DummyCard, or null if no image has been set.
     */
    public Image getImage() {
        return image;
    }
    /**
     * Sets the image associated with this DummyCard by retrieving the external form
     * of the URL linked to the card's image. The URL is generated based on the card's
     * properties (suit and rank) via the card's `getCardURL` method.
     *
     * This method creates an instance of the Image class using the resource identifier
     * of the card's URL. The generated image is then assigned to the `image` property
     * of this DummyCard.
     *
     * The card's URL is expected to follow the format "/card/Card-{suit}-{rank}.png",
     * or "/card/Card-Empty.png" if the card is unassigned (both suit and rank are -1).
     *
     * Assumes that the `card` property has been initialized with a valid Card object
     * before calling this method.
     */
    public void setImage(){
        image = new Image(getClass().getResource(card.getCardURL()).toExternalForm());
    }
    /**
     * Sets the image associated with this DummyCard.
     *
     * @param image the Image object to associate with this DummyCard
     */
    public void setImage(Image image) {
        this.image = image;
    }
    /**
     * Retrieves the card associated with this DummyCard.
     *
     * @return the Card object currently associated with this DummyCard,
     *         or null if no card has been set.
     */
    public Card getCard() {
        return card;
    }

    /**
     * Updates the card associated with this DummyCard and notifies relevant observers
     * about the change, unless explicitly instructed to skip notification.
     *
     * @param card the new Card object to associate with this DummyCard
     * @param skipObserver a boolean flag indicating whether to skip observer notifications
     *                     when updating the card. If false, observers will be notified.
     */
    public void setCard(Card card, boolean skipObserver) {
        Card prevCard = this.card;
        this.card = card;
        if(!skipObserver) {
            notifyObserver();
            notifyAddObserver(prevCard);
        }
    }
    /**
     * Notifies the associated observer about state changes in the DummyCard instance.
     * This method triggers the `update` method of the SinglePlayGameController associated
     * with the DummyCard, passing the current card instance as a parameter.
     *
     * The observer is generally responsible for handling the changes or actions resulting
     * from the card being played or interacted with in the game. The behavior implemented
     * in the `update` method can include logging the actions, applying specific card effects,
     * and updating the game state accordingly.
     *
     * This method assumes that a valid Card instance has been associated with the DummyCard
     * through the `setCard(Card card, boolean skipObserver)` method before calling.
     */
    public void notifyObserver(){
        controller.update(card);
    }

    /**
     * Notifies the SinglePlayGameController associated with this DummyCard about the addition
     * of a card to the deck. This method updates the game controller with the provided Card object.
     *
     * @param card the Card object being added to the deck and passed to the controller for updates
     */
    public void notifyAddObserver(Card card){
        controller.updateAddToDeck(card);
    }

}
