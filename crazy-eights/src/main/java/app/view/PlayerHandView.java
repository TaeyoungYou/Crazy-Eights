package app.view;

import app.model.Card;
import app.model.Player;
import app.model.PlayerObserver;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.ImageView;


public class PlayerHandView implements PlayerObserver {
    private ObservableList<ImageView> cards;

    private SinglePlayGameView mainView;

    public PlayerHandView(Player player, SinglePlayGameView _mainView) {
        player.addObserver(this);
        cards = FXCollections.observableArrayList();
        mainView = _mainView;
    }

    public void setCards(Card card){
        ImageView cardImg = new ImageView(getClass().getResource(card.getCardURL()).toExternalForm());
        cardImg.setFitWidth(220);
        cardImg.setPreserveRatio(true);
        cards.add(cardImg);
    }

    @Override
    public void update(Player player) {
        cards.clear();
        for(Card card:player.getHand()){
            setCards(card);
        }
        mainView.setUserHand(cards);
    }
}
