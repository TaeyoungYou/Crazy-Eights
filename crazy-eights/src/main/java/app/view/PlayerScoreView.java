package app.view;

import app.model.Player;
import app.model.PlayerObserver;
import app.style.StyleGame;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class PlayerScoreView implements PlayerObserver {
    private HBox playerScoreBox;
    private ImageView playerIcon;
    private Label playerScore;

    private SinglePlayGameView mainView;
    private StyleGame style;

    public PlayerScoreView(Player player, SinglePlayGameView _mainView) {
        player.addObserver(this);

        playerIcon = new ImageView(getClass().getResource(player.getIcon()).toExternalForm());
        playerScore = new Label(player.getScore() + "");

        mainView = _mainView;
        style = new StyleGame();
        playerScoreBox = new HBox();
        updatePlayerScore();
    }

    @Override
    public void update(Player player) {
        playerIcon.setImage(new Image(getClass().getResource(player.getIcon()).toExternalForm()));
        playerScore.setText((player.getScore() + ""));
        mainView.setScoreBox(playerScoreBox, player);
    }


    private void updatePlayerScore() {
        playerIcon.setFitWidth(70);
        playerIcon.setFitHeight(70);

        playerScore.setFont(Font.loadFont(style.getLilitaOneFont(), 40));
        playerScore.setStyle(style.sideLabelStyle());
        playerScoreBox.getChildren().addAll(playerIcon, playerScore);
        playerScoreBox.setAlignment(Pos.CENTER_LEFT);
        playerScoreBox.setSpacing(60);
    }
}
