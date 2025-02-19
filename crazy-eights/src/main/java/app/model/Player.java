package app.model;

import app.view.PlayerHandView;
import app.view.PlayerStatusView;

import java.util.ArrayList;
import java.util.List;

public class Player implements Comparable<Player>{
    private int id;
    private String icon;    // status와 score에 사용될
    private int score;      // score에 사용될

    private List<Card> hand;
    private List<PlayerObserver> observers;

    private boolean self;   // 자기 자신인지
    private boolean myTurn;

    public Player(int index){
        id = index;
        icon = "/avatar/User-01.png";
        score = 0;
        self = false;
        myTurn = false;
        hand = new ArrayList<>();
        observers = new ArrayList<>();
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
    public void setCard(Deck deck){
        hand.add(deck.drawCard());
        notifyObservers();
    }
    public int getScore(){
        return score;
    }
    public void setScore(int score){
        this.score = score;
        notifyObservers();
    }
    public boolean isSelf(){
        return self;
    }
    public void setSelf(){
        this.self = true;
    }

    public void addObserver(PlayerObserver observer){
        observers.add(observer);
    }
    public void removeObserver(PlayerObserver observer){
        observers.remove(observer);
    }
    public void notifyObservers(){
        for(PlayerObserver observer : observers){
            if(observer instanceof PlayerStatusView){
                observer.update(this);
            } else if(observer instanceof PlayerHandView && isSelf()){
                observer.update(this);
            }else {
                observer.update(this);
            }
        }
    }

    @Override
    public int compareTo(Player o) {
        return this.score - o.getScore();
    }
}
