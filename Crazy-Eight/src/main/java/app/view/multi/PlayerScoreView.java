package app.view.multi;

import app.model.multi.Player;
import app.model.multi.PlayerObserver;
import app.style.StyleGame;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;

/**
 * The PlayerScoreView class represents the visual component responsible
 * for displaying a player's score and icon in the game. It implements
 * the PlayerObserver interface to track changes in the player's state
 * and update the corresponding view accordingly.
 *
 * This class observes changes in a Player object and dynamically updates
 * its graphical representation, including the player's icon and score,
 * within the game's user interface.
 *
 * The view is constructed using an HBox layout that aligns the player's
 * icon and score label horizontally, with specific styles and spacings
 * applied.
 */
public class PlayerScoreView implements PlayerObserver {
    private HBox playerScoreBox;
    private ImageView playerIcon;
    private Label playerScore;

    private MultiPlayGameView mainView;
    private StyleGame style;

    /**
     * Constructs a new PlayerScoreView object, which is responsible for displaying
     * a player's score and icon in the game. This view observes the player object
     * and updates the displayed information based on changes in the player's state.
     *
     * @param player The Player object whose score and icon will be displayed and observed.
     * @param _mainView The main game view (SinglePlayGameView) that will contain this player's score view.
     */
    public PlayerScoreView(Player player, MultiPlayGameView _mainView) {
        player.addObserver(this);

        playerIcon = new ImageView(getClass().getResource(player.getIcon()).toExternalForm());
        playerScore = new Label(player.getScore() + "");

        mainView = _mainView;
        style = new StyleGame();
        updatePlayerScore();
    }

    /**
     * Updates the player's score and icon in the view based on the player's current state.
     * This method refreshes the player's graphical representation in the user interface,
     * including updating the player's score label and icon image.
     *
     * @param player The Player object whose state is being updated and displayed in the view.
     * @param handleCard Indicates whether the update involves handling the player's card in the game.
     */
    @Override
    public void update(Player player, boolean handleCard) {
        updatePlayerScore();
        playerIcon.setImage(new Image(getClass().getResource(player.getIcon()).toExternalForm()));
        playerScore.setText((player.getScore() + ""));
        mainView.setScoreBox(playerScoreBox, player);
    }


    /**
     * Updates the player's score view by initializing and configuring the graphical
     * elements used to display the player's score and icon. This method creates an
     * `HBox` container to arrange the player's icon and score label horizontally, applies
     * specific styles, and sets the layout's alignment and spacing.
     *
     * The method performs the following actions:
     * - Initializes the `HBox` container (playerScoreBox) to hold the player's icon and score.
     * - Sets the dimensions of the player's icon by defining its width and height.
     * - Configures the font and style of the player's score label using the custom font and style
     *   retrieved from the `StyleGame` class.
     * - Adds the icon and score label to the `HBox` container.
     * - Aligns the container's elements to the left and applies spacing between them.
     *
     * This setup ensures that the player's score and icon are displayed consistently
     * within the visual interface of the game.
     */
    private void updatePlayerScore() {
        playerScoreBox = new HBox();

        playerIcon.setFitWidth(70);
        playerIcon.setFitHeight(70);

        playerScore.setFont(Font.loadFont(style.getLilitaOneFont(), 40));
        playerScore.setStyle(style.sideLabelStyle());
        playerScoreBox.getChildren().addAll(playerIcon, playerScore);
        playerScoreBox.setAlignment(Pos.CENTER_LEFT);
        playerScoreBox.setSpacing(60);
    }
}
