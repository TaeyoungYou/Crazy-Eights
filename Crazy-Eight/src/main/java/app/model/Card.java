package app.model;

import java.util.Random;

public class Card {
    private int suit;
    private int rank;

    public Card() {
        this.suit = -1;
        this.rank = -1;
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
    public String getRankString(){
        if(rank == 0) return "A";
        if(rank == 10) return "J";
        if(rank == 11) return "Q";
        if(rank == 12) return "K";
        return String.valueOf(rank + 1);
    }
    public void setRank(int rank) {
        this.rank = rank;
    }

    public String getImogeSuit(){
        if (suit == 0) return "♠";
        if (suit == 1) return "♥";
        if (suit == 2) return "♦";
        if (suit == 3) return "♣";
        return "Empty";
    }
    public String getKoreanSuit(){
        if (suit == 0) return "스페이드";
        if (suit == 1) return "하트";
        if (suit == 2) return "다이아몬드";
        if (suit == 3) return "클로버";
        return "비어있음";
    }

    public String getCardURL(){
        if(suit == -1 && rank == -1){
            return "/card/Card-Empty.png";
        }
        return String.format("/card/Card-%d-%d.png", suit, rank);
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return this.suit == card.suit && this.rank == card.rank;
    }
}
