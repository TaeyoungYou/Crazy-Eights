package app.model;

import java.util.ArrayList;
import java.util.Collections;

public class Deck {
    private ArrayList<Card> deck;

    public Deck() {
        deck = new ArrayList<>();
    }
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

    public Card drawCard() {
        if(deck.isEmpty()) {
            System.out.println("Deck is empty");
            return null;
        }
        Card card = deck.remove(0);
        System.out.println(card.getCardURL());
        return card;
    }
}
