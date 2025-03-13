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

/**
 * The ScoringView class is responsible for creating and managing the scoring view
 * in a JavaFX-based application. It dynamically generates a visual representation
 * of player rankings and scores, adds interactive buttons, and applies animations
 * for a smooth user experience.
 *
 * This class integrates UI styling via the StyleScoring class and visual transitions
 * through the AnimationScoring class. The view is created inside a given root StackPane
 * that serves as the base UI container.
 *
 * This class provides methods to create the scoring overlay, manage button interactions,
 * and handle animations for component visibility and responsiveness.
 */
public class ScoringView {
    private final StackPane pane;
    private StackPane overlay;
    private final StyleScoring style;
    private final AnimationScoring animation;

    private Label continueButton;
    private Label exitButton;

    /**
     * Constructs a ScoringView instance with a specified StackPane.
     * The constructor initializes the scoring view's pane and its associated styling
     * and animation components.
     *
     * @param pane the StackPane that serves as the base for the scoring view
     */
    public ScoringView(StackPane pane) {
        this.pane = pane;
        style = new StyleScoring();
        animation = new AnimationScoring();
    }

    /**
     * Generates the scoring UI overlay for the game. It displays the ranking, icons, and scores of players,
     * along with options to continue or exit.
     *
     * @param players      the list of players whose scores and rankings will be shown
     * @param information  a map containing each player's rank and score as a pair of integers
     */
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

            Label place = new Label(rank + "");
            place.setFont(Font.loadFont(style.getLilitaOneFont(), 50));
            place.setStyle(style.settingTitleStyle());
            placeBox.getChildren().add(place);

            ImageView icon = new ImageView(getClass().getResource(player.getIcon()).toExternalForm());
            icon.setFitWidth(100);
            icon.setPreserveRatio(true);
            placeIcon.getChildren().add(icon);

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

    /**
     * Retrieves the main pane of the scoring view.
     *
     * @return The StackPane object representing the main pane of the scoring view.
     */
    public StackPane getPane() {
        return pane;
    }
    /**
     * Applies a fade-out animation to the scoring view's pane and its overlay.
     * This method uses the `fadeOutScoring` function from the associated animation class
     * to gradually reduce the opacity of the pane to zero, effectively hiding it.
     * The pane and overlay are typically removed from the layout after the animation completes.
     */
    public void fadeOutPane(){
        animation.fadeOutScoring(pane, overlay);
    }
    /**
     * Applies button animations to specific buttons in the scoring view.
     * The method leverages the animation functionality by invoking the
     * `buttonAnimation` method on each button to add hover effects, such as
     * fade-in and fade-out transitions, when the mouse enters or exits the button.
     * This enhances the interactivity and visual appeal of the buttons.
     *
     * The buttons targeted by this method include:
     * - `continueButton`: A button labeled for continuing the game or proceeding
     *   to the next step.
     * - `exitButton`: A button labeled for exiting the game or the current view.
     */
    public void buttonAnimation(){
        animation.buttonAnimation(continueButton);
        animation.buttonAnimation(exitButton);
    }

    /**
     * Retrieves the "Continue" button label in the scoring view.
     * The "Continue" button allows the user to proceed to the next action or screen.
     *
     * @return A Label object representing the "Continue" button in the scoring view.
     */
    public Label getContinueButton(){
        return continueButton;
    }
    /**
     * Retrieves the "Exit" button label in the scoring view.
     * The "Exit" button allows the user to exit the current view or game state.
     *
     * @return A Label object representing the "Exit" button in the scoring view.
     */
    public Label getExitButton(){
        return exitButton;
    }
}
