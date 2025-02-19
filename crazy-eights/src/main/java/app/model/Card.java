package app.model;

import java.util.Random;

public class Card {
    private int suit;
    private int rank;

    public Card() {
        this.suit = 0;
        this.rank = 0;
    }
    public Card(int suit, int rank) {
        this.suit = suit;
        this.rank = rank;
    }
    public int getSuit() {
        return suit;
    }
    public void setSuit(int suit) {
        this.suit = suit;
    }
    public int getRank() {
        return rank;
    }
    public void setRank(int rank) {
        this.rank = rank;
    }
    public String getCardURL(){
        return String.format("/card/Card-%d-%d.png", suit, rank);
    }
}
