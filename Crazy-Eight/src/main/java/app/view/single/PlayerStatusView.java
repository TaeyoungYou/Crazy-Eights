package app.view.single;

import app.model.single.Player;
import app.model.single.PlayerObserver;
import app.style.StyleGame;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 * The PlayerStatusView class represents a UI component responsible for displaying
 * and updating the status of a player in a game. It observes a Player object
 * and updates the visual components accordingly whenever the player's state changes.
 *
 * This class implements the PlayerObserver interface, adhering to the observer
 * design pattern to listen for updates from the Player instance it observes.
 */
public class PlayerStatusView implements PlayerObserver {
    private HBox playerStatus;
    private ImageView playerIcon;
    private ImageView cardBack;
    private Label playerCardLeft;

    private SinglePlayGameView mainView;
    private StyleGame style;

    /**
     * Constructs a PlayerStatusView object that serves as a visual representation
     * of the player's current status within the game. This view observes the provided
     * Player object and updates accordingly when the player's state changes, such as
     * the number of cards left, the player's icon, or their turn status.
     *
     * @param player the Player object whose status is being observed and displayed.
     *               The Player object provides details such as the player icon, number
     *               of cards left, and whether it is the player's turn.
     * @param _mainView the primary game view associated with this PlayerStatusView.
     *                  This is used for updating the main view with relevant player status.
     */
    public PlayerStatusView(Player player, SinglePlayGameView _mainView) {
        player.addObserver(this);

        playerIcon = new ImageView(getClass().getResource(player.getIcon()).toExternalForm());
        cardBack = new ImageView(getClass().getResource("/card/Card-Back.png").toExternalForm());
        playerCardLeft = new Label(String.format("x %d", player.getCardLeft()));

        mainView = _mainView;
        style = new StyleGame();

        updatePlayerStatus();
    }
    /**
     * Updates the player status view based on the given player and game-related actions.
     * This method refreshes the UI components to reflect the current state of the player.
     * If the player is not the observer itself, the player's icon, card count, and status
     * effects are updated accordingly. Additionally, it delegates further UI updates
     * to the main game view.
     *
     * @param player the Player object whose status is being updated
     * @param handleCard a boolean flag indicating whether to trigger additional card-related actions
     */
    @Override
    public void update(Player player, boolean handleCard) {
        if(!player.isSelf()){
            updatePlayerStatus();
            playerIcon.setImage(new Image(getClass().getResource(player.getIcon()).toExternalForm()));
            playerCardLeft.setText("x " + (player.getCardLeft()+""));
            if(player.isMyTurn()){
                playerIcon.setEffect(createDropShadow());
            } else {
                playerIcon.setEffect(null);
            }
            mainView.setPlayerStatus(playerStatus, playerCardLeft, player, handleCard);
        }
    }
    /**
     * Creates and returns a configured DropShadow effect to be applied to JavaFX elements.
     * The DropShadow is customized with a white color, a radius of 10 pixels, and a spread of 0.5.
     * The offsets on both the x-axis and y-axis are set to 0 to ensure symmetry around the target element.
     *
     * @return A configured DropShadow instance with specified properties.
     */
    private DropShadow createDropShadow(){
        DropShadow edgeGlow = new DropShadow();
        edgeGlow.setRadius(10);
        edgeGlow.setSpread(0.5);
        edgeGlow.setColor(Color.WHITE);
        edgeGlow.setOffsetX(0);
        edgeGlow.setOffsetY(0);
        return edgeGlow;
    }

    /**
     * Updates the visual representation of the player's status, including the player's
     * icon, the card back image, and the label displaying the number of cards left.
     * This method initializes and configures the layout container (`HBox`) and its child
     * components with the appropriate styles and dimensions.
     *
     * The following functionalities are performed within this method:
     * - Creates a new `HBox` instance for `playerStatus` to ensure alignment and layout of UI elements.
     * - Configures the dimensions of the player's icon and card back image by setting appropriate
     *   width and height values.
     * - Applies a custom font and style to the card count label using details retrieved
     *   from the `style` object.
     * - Aligns the components of the `HBox` container (`playerIcon`, `cardBack`, and
     *   `playerCardLeft`) to the left, maintaining a visually consistent layout.
     * - Adds the configured elements to the `HBox` container, creating a cohesive UI
     *   representation of the player's current status.
     */
    private void updatePlayerStatus() {
        playerStatus = new HBox();

        playerIcon.setFitWidth(150);
        playerIcon.setFitHeight(150);

        playerCardLeft.setFont(Font.loadFont(style.getLilitaOneFont(), 40));
        playerCardLeft.setStyle(style.sideLabelStyle());

        cardBack.setFitWidth(150);
        cardBack.setFitHeight(200);

        playerStatus.setAlignment(Pos.CENTER_LEFT);
        playerStatus.getChildren().addAll(playerIcon, cardBack, playerCardLeft);
    }
}
