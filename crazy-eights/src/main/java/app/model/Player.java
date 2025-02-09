package app.model;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Player implements Comparable<Player>{
    private ImageView icon;
    private Label cardLeft;
    private int score;

    private boolean self;

    public Player(){
        icon = new ImageView(new Image(getClass().getResource("/avatar/User-01.png").toExternalForm()));
        cardLeft = new Label("x 0");
        score = 0;
        self = false;
    }
    public void setIcon(String url){
        icon.setImage(new Image(getClass().getResource(url).toExternalForm()));
    }
    public ImageView getIcon(){
        return icon;
    }
    public Label getCardLeft(){
        return cardLeft;
    }
    public int getCardLeftValue(){
        return Integer.parseInt(getCardLeft().getText().split(" ")[1]);
    }
    public int getScore(){
        return score;
    }
    public void setScore(int score){
        this.score = score;
    }
    public boolean isSelf(){
        return self;
    }
    public void setSelf(boolean self){
        this.self = self;
    }
    public void setCardLeft(int left){
        cardLeft.setText(String.format("x %d", left));
    }

    @Override
    public int compareTo(Player o) {
        return this.score - o.getScore();
    }
}
