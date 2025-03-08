package app.model;

import app.view.PlayerHandView;
import app.view.PlayerStatusView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Player implements Comparable<Player>{
    private int id;
    private String icon;    // status와 score에 사용될
    private int score;      // score에 사용될
    private Personality personality;

    private List<Card> hand;
    private List<PlayerObserver> observers;

    private boolean self;   // 자기 자신인지
    private boolean myTurn;
    private boolean handleCard;

    public Player(int index){
        id = index;
        icon = "/avatar/User-01.png";
        score = 0;
        self = false;
        myTurn = false;
        handleCard = false;
        hand = new ArrayList<>();
        observers = new ArrayList<>();
        personality = Personality.getRandomPersonality();
    }
    public void removeCard(int index){
        hand.remove(index);
        notifyObservers();
    }
    public int getId(){
        return id;
    }
    public void setId(int id){
        this.id = id;
        notifyObservers();
    }
    public void setIcon(String url){
        icon = url;
        notifyObservers();
    }
    public void setMyTurn(boolean myTurn){
        this.myTurn = myTurn;
        notifyObservers();
    }
    public boolean isMyTurn(){
        return myTurn;
    }
    public String getIcon(){
        return icon;
    }
    public int getCardLeft(){
        return hand.size();
    }
    public List<Card> getHand(){
        return hand;
    }
    public void removeCard(Card card){
        hand.remove(card);
        handleCard = true;
        notifyObservers();
    }
    public void setCard(Deck deck){
        hand.add(deck.drawCard());
        handleCard = true;
        notifyObservers();
    }
    public int getScore(){
        return score;
    }
    public void setScore(int score){
        this.score = score;
        notifyObservers();
    }
    public Personality getPersonality(){
        return personality;
    }
    public boolean isSelf(){
        return self;
    }
    public void setSelf(){
        this.self = true;
    }

    public int getMostShape(){
        if(Math.random() < 0.8){
            int spade = 0, heart = 0, diamond = 0, club = 0;
            for(Card card : hand){
                switch (card.getSuit()){
                    case 0 -> spade++;
                    case 1 -> heart++;
                    case 2 -> diamond++;
                    case 3 -> club++;
                }
            }
            int maxCount = Math.max(Math.max(diamond, club), Math.max(spade, heart));

            if(maxCount == spade) return 0;
            if(maxCount == heart) return 1;
            if(maxCount == diamond) return 2;
            return 3;
        }
        return new Random().nextInt(4);
    }


    public void addObserver(PlayerObserver observer){
        observers.add(observer);
    }
    public void notifyObservers(){
        for(PlayerObserver observer : observers){
            if(observer instanceof PlayerStatusView){
                observer.update(this, handleCard);
            } else if(observer instanceof PlayerHandView && isSelf()){
                observer.update(this, false);
            }else {
                observer.update(this, false);
            }
        }
        handleCard = false;
    }

    @Override
    public int compareTo(Player o) {
        return this.score - o.getScore();
    }
}
