package app.view;

import app.model.Player;
import app.model.PlayerObserver;
import app.style.StyleGame;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;

public class PlayerStatusView implements PlayerObserver {
    private HBox playerStatus;
    private ImageView playerIcon;
    private ImageView cardBack;
    private Label playerCardLeft;

    private SinglePlayGameView mainView;
    private StyleGame style;

    public PlayerStatusView(Player player, SinglePlayGameView _mainView) {
        player.addObserver(this);

        playerIcon = new ImageView(getClass().getResource(player.getIcon()).toExternalForm());
        cardBack = new ImageView(getClass().getResource("/card/Card-Back.png").toExternalForm());
        playerCardLeft = new Label(String.format("x %d", player.getCardLeft()));

        mainView = _mainView;
        playerStatus = new HBox();
        style = new StyleGame();

        updatePlayerStatus();
    }
    @Override
    public void update(Player player) {
        playerIcon.setImage(new Image(getClass().getResource(player.getIcon()).toExternalForm()));
        playerCardLeft.setText("x " + (player.getCardLeft()+""));
        mainView.setPlayerStatus(playerStatus, player);
    }

    private void updatePlayerStatus() {
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
