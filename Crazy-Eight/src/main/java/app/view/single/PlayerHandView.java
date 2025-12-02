package app.view.single;

import app.model.single.Card;
import app.model.single.Player;
import app.model.single.PlayerObserver;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.ImageView;


/**
 * The PlayerHandView class is responsible for visualizing the cards
 * in a player's hand within the game interface. It observes a Player object
 * and updates the displayed hand whenever changes occur.
 *
 * This class implements the PlayerObserver interface to respond to changes in the
 * Player's hand. It maintains a list of ImageView objects representing the cards
 * and interacts with the main game view to update the display.
 */
public class PlayerHandView implements PlayerObserver {
    private ObservableList<ImageView> cards;

    private SinglePlayGameView mainView;

    /**
     * Constructs a PlayerHandView instance to visualize the cards
     * in a player's hand. This view observes the given Player object
     * and updates its state based on changes in the player's hand.
     *
     * @param player The Player object whose hand is being observed and displayed.
     * @param _mainView The main game view where this player's hand view is integrated.
     */
    public PlayerHandView(Player player, SinglePlayGameView _mainView) {
        player.addObserver(this);
        cards = FXCollections.observableArrayList();
        mainView = _mainView;
    }

    /**
     * Adds a card to the current collection of cards displayed in the player's hand.
     * The method creates an {@code ImageView} for the card using its image URL,
     * adjusts its visual properties like width and aspect ratio, and adds it to the list of cards.
     *
     * @param card the {@code Card} object representing the card to be added to the player's hand.
     *             The card's image URL is retrieved via its {@code getCardURL()} method.
     */
    public void setCards(Card card){
        ImageView cardImg = new ImageView(getClass().getResource(card.getCardURL()).toExternalForm());
        cardImg.setFitWidth(220);
        cardImg.setPreserveRatio(true);
        cards.add(cardImg);
    }

    /**
     * Updates the visual representation of the player's hand based on the current state
     * of the player's cards. This method clears the existing card views and repopulates them
     * with updated card data from the player's hand. Additionally, updates the main game view
     * with the new hand representation and hand information.
     *
     * @param player The Player object whose hand is being updated and visualized.
     * @param handleCard A boolean indicating whether any special handling of the card update logic is required.
     */
    @Override
    public void update(Player player, boolean handleCard) {
        cards.clear();
        for(Card card:player.getHand()){
            setCards(card);
        }
        mainView.setUserHand(cards);
        mainView.setUserHandInfo(player.getHand());
    }
}
