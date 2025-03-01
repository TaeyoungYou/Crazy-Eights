package app.model;

import app.controller.SinglePlayGameController;
import javafx.scene.image.Image;

public class DummyCard {
    private Image image;
    private Card card;
    private CardObserver observer;
    private SinglePlayGameController controller;

    public DummyCard(SinglePlayGameController controller) {
        this.controller = controller;
    }
    public Image getImage() {
        return image;
    }
    public void setImage(){
        image = new Image(getClass().getResource(card.getCardURL()).toExternalForm());
    }
    public void setImage(Image image) {
        this.image = image;
    }
    public Card getCard() {
        return card;
    }
    public void setCard(Card card) {
        this.card = card;
        notifyObserver();
    }

    public void notifyObserver(){
        controller.update(card);
    }

}
