package app.view;

import app.animation.AnimationScoring;
import app.model.Player;
import app.style.StyleScoring;
import app.style.StyleSetting;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.util.Pair;

import java.util.List;
import java.util.Map;

public class ScoringView {
    private final StackPane pane;
    private StackPane overlay;
    private final StyleScoring style;
    private final AnimationScoring animation;

    private Label continueButton;
    private Label exitButton;

    public ScoringView(StackPane pane) {
        this.pane = pane;
        style = new StyleScoring();
        animation = new AnimationScoring();
    }

    public void generate(List<Player> players, Map<Player, Pair<Integer, Integer>> information){
        overlay = new StackPane();
        overlay.setStyle(style.overlayStyle());
        overlay.prefWidthProperty().bind(pane.widthProperty());
        overlay.prefHeightProperty().bind(pane.heightProperty());
        overlay.setAlignment(Pos.CENTER);

        VBox scoringBox = new VBox();
        scoringBox.setStyle(style.settingPaneStyle());
        scoringBox.setPrefSize(600, 400);
        scoringBox.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        scoringBox.setAlignment(Pos.TOP_CENTER);
        scoringBox.setSpacing(20);

        Label settingTitle = new Label("Score");
        settingTitle.setFont(Font.loadFont(style.getLilitaOneFont(), 80));
        settingTitle.setStyle(style.settingTitleStyle());

        VBox elementPane = new VBox();
        elementPane.setAlignment(Pos.CENTER);

        HBox placeBox = new HBox(105);
        placeBox.setAlignment(Pos.CENTER);

        HBox placeIcon = new HBox(30);
        placeIcon.setAlignment(Pos.CENTER);

        HBox scorePlace = new HBox(100);
        scorePlace.setAlignment(Pos.CENTER);

        HBox buttonBox = new HBox(30);
        buttonBox.setAlignment(Pos.CENTER);

        for (Player player : players) {
            int rank = information.get(player).getKey();
            int score = information.get(player).getValue();

            // 🟢 1. 순위 추가
            Label place = new Label(rank + "");
            place.setFont(Font.loadFont(style.getLilitaOneFont(), 50));
            place.setStyle(style.settingTitleStyle());
            placeBox.getChildren().add(place);

            // 🟢 2. 아이콘 추가
            ImageView icon = new ImageView(getClass().getResource(player.getIcon()).toExternalForm());
            icon.setFitWidth(100);
            icon.setPreserveRatio(true);
            placeIcon.getChildren().add(icon);

            // 🟢 3. 점수 추가
            Label scoreLabel = new Label("+" + score);
            scoreLabel.setFont(Font.loadFont(style.getLilitaOneFont(), 30));
            scoreLabel.setStyle(style.settingTitleStyle());
            scorePlace.getChildren().add(scoreLabel);
        }


        continueButton = new Label("Continue");
        exitButton = new Label("Exit");

        continueButton.setPrefSize(150, 50);
        exitButton.setPrefSize(150, 50);

        continueButton.setStyle(style.loadingButtonCommonStyle());
        exitButton.setStyle(style.loadingButtonCommonStyle());

        buttonBox.getChildren().addAll(continueButton, exitButton);
        elementPane.getChildren().addAll(placeBox, placeIcon, scorePlace);
        scoringBox.getChildren().addAll(settingTitle, elementPane, buttonBox);
        overlay.getChildren().add(scoringBox);
        pane.getChildren().add(overlay);

        animation.fadeInScoring(overlay);
    }

    public StackPane getPane() {
        return pane;
    }
    public void fadeOutPane(){
        animation.fadeOutScoring(pane, overlay);
    }

    public Label getContinueButton(){
        return continueButton;
    }
    public Label getExitButton(){
        return exitButton;
    }
}
