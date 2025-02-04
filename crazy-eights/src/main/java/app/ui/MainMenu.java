package app.ui;

import app.animation.AnimationMenu;
import app.style.StyleMenu;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

/**
 * Represents the main menu of the Crazy Eights game.
 * This class initializes and manages the user interface for the main menu.
 */
public class MainMenu {
    private final BorderPane pane;
    private final StyleMenu style;
    private final AnimationMenu animation;
    private final Scene scene;

    private static boolean started = false;

    /**
     * Constructs a MainMenu instance.
     *
     * @param _scene The scene where the main menu will be displayed.
     */
    public MainMenu(Scene _scene) {
        scene = _scene;
        pane = new BorderPane();
        style = new StyleMenu();
        animation = new AnimationMenu();
    }

    /**
     * Generates the main menu. If the game has already started, it directly shows the game menu.
     */
    public void generate() {
        initPage();

        if (!started) {
            startGameMenu();
            started = true;
        } else {
            gameMenu();
        }
    }

    /**
     * Displays the game menu with available options.
     */
    private void gameMenu() {
        Label title = createTitle();

        // Create and style buttons
        Label singlePlay = new Label("SinglePlayer");
        Label multiPlay = new Label("MultiPlayer");
        Label setting = new Label("Setting");
        Label quit = new Label("Quit");

        singlePlay.setStyle(style.loadingButtonCommonStyle() + style.loadingButtonLargeStyle());
        multiPlay.setStyle(style.loadingButtonCommonStyle() + style.loadingButtonLargeStyle());
        setting.setStyle(style.loadingButtonCommonStyle() + style.loadingButtonSmallStyle());
        quit.setStyle(style.loadingButtonCommonStyle() + style.loadingButtonSmallStyle());

        VBox menuPane = new VBox();
        menuPane.setAlignment(Pos.CENTER);
        HBox bottomPane = new HBox(setting, quit);
        bottomPane.setAlignment(Pos.CENTER);

        Region space = new Region();
        space.setPrefHeight(10);

        menuPane.setStyle(style.loadingBoxStyle());
        bottomPane.setStyle(style.loadingBoxStyle());

        menuPane.getChildren().addAll(title, space, singlePlay, multiPlay, bottomPane);
        pane.setCenter(menuPane);

        Label[] tmp = {singlePlay, multiPlay, setting, quit};
        animation.menuAnimation(title, tmp);

        singlePlay.setOnMouseClicked(event -> {
            SinglePlayGame game = new SinglePlayGame(scene);
            game.generate();
        });
        quit.setOnMouseClicked(event -> {
            Platform.exit();
        });
    }

    /**
     * Displays the start game menu with animations.
     */
    private void startGameMenu() {
        Label title = createTitle();

        // Create and style buttons
        Label singlePlay = new Label("SinglePlayer");
        Label multiPlay = new Label("MultiPlayer");
        Label setting = new Label("Setting");
        Label quit = new Label("Quit");

        singlePlay.setStyle(style.loadingButtonCommonStyle() + style.loadingButtonLargeStyle());
        multiPlay.setStyle(style.loadingButtonCommonStyle() + style.loadingButtonLargeStyle());
        setting.setStyle(style.loadingButtonCommonStyle() + style.loadingButtonSmallStyle());
        quit.setStyle(style.loadingButtonCommonStyle() + style.loadingButtonSmallStyle());

        // Create menu structure
        VBox menuPane = new VBox(title);
        menuPane.setAlignment(Pos.CENTER);
        HBox bottomPane = new HBox(setting, quit);
        bottomPane.setAlignment(Pos.CENTER);

        Region space = new Region();
        space.setPrefHeight(10);
        menuPane.getChildren().add(space);

        menuPane.setStyle(style.loadingBoxStyle());
        bottomPane.setStyle(style.loadingBoxStyle());

        Node[] nodes = {singlePlay, multiPlay, bottomPane};

        // Set up layout
        pane.setCenter(menuPane);

        // Apply animations
        animation.startMenuAnimation(menuPane, nodes, title);
        animation.menuHover(nodes);

        singlePlay.setOnMouseClicked(event -> {
            SinglePlayGame game = new SinglePlayGame(scene);
            game.generate();
        });
        quit.setOnMouseClicked(event -> {
            Platform.exit();
        });
    }

    /**
     * Initializes the main page layout.
     */
    private void initPage() {
        scene.setRoot(pane);
        pane.setStyle(style.loadingBorderPaneStyle());
    }

    /**
     * Creates and returns the title label with appropriate styling.
     *
     * @return The title label for the main menu.
     */
    private Label createTitle() {
        Label title = new Label("Crazy Eights");
        title.setFont(Font.loadFont(style.getLilitaOneFont(), 180));
        title.setStyle(style.loadingTitleStyle());
        return title;
    }
}
