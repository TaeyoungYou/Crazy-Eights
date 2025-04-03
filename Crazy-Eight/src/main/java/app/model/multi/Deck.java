package app.model.multi;

import app.controller.MultiPlayGameController;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Represents a deck of cards used in a single-play game.
 * The deck consists of a list of Card objects, and it interacts with
 * a SinglePlayGameController to send updates about its state.
 */
public class Deck {
    private ArrayList<Card> deck;
    private MultiPlayGameController controller;

    /**
     * Constructs a new Deck object and initializes it with the specified game controller.
     * The deck starts as an empty collection of cards.
     *
     * @param controller the SinglePlayGameController instance used to interact with the deck
     *                   and notify about changes in its state
     */
    public Deck(MultiPlayGameController controller) {
        this.controller = controller;
        deck = new ArrayList<>();
    }
    /**
     * Initializes a full deck of playing cards consisting of 52 unique cards,
     * using all combinations of suits and ranks. The deck is created by iterating through
     * four suits (0 to 3) and thirteen ranks (0 to 12). Each card is represented as a
     * combination of a suit and a rank, and they are added to the deck list.
     *
     * Once all cards have been added to the deck, the collection is shuffled randomly
     * to provide a randomized order of cards.
     */
    public void generateDeck() {
        int[] suits = {0,1,2,3};
        int[] ranks = {0,1,2,3,4,5,6,7,8,9,10,11,12};
        for(int suit : suits) {
            for(int rank : ranks) {
                deck.add(new Card(suit, rank));
            }
        }
        Collections.shuffle(deck);
    }

    /**
     * Removes the first card from the deck and returns it.
     * This method also sends a notification to the associated controller
     * to update the game state after the card is drawn.
     *
     * @return the Card object representing the first card removed from the deck.
     */
    public Card drawCard() {
        Card card = deck.removeFirst();
        notification();
        return card;
    }
    /**
     * Adds a card to the deck and shuffles the entire deck to introduce randomness.
     *
     * @param card the Card object to be added to the deck
     */
    public void add(Card card) {
        deck.add(card);
        Collections.shuffle(deck);
    }

    /**
     * Retrieves the number of cards currently in the deck.
     *
     * @return the total number of cards remaining in the deck.
     */
    public int deckSize(){
        return deck.size();
    }

    /**
     * Sends a notification to the associated game controller by calling its update method.
     * This method is intended to inform the controller about changes or updates to the game state.
     * It is used internally within the Deck class to maintain synchronization between the deck
     * and the game controller.
     */
    private void notification(){
        controller.update();
    }
}
