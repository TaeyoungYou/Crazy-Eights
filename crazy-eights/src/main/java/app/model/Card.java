package app.model;

import java.util.Random;

public class Card {
    private int suit;
    private int rank;

    public Card() {
        this.suit = 0;
        this.rank = 0;
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
    public Card setCard(){
        // 나중에 이걸 duplicate 되지 않게 조정을 할거임
        Random rand = new Random();
        int ranSuit = rand.nextInt(4);
        int ranRank = rand.nextInt(13);

        setSuit(ranSuit);
        setRank(ranRank);

        return this;
    }
}
