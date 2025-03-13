package app.controller;

import app.view.MenuView;
import app.view.SettingView;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

/**
 * The {@code MenuController} class manages the main menu of the application.
 * It handles navigation between the menu and settings, as well as initializing the UI components.
 */
public class MenuController {
    private Scene scene;
    private StackPane root;
    private BorderPane mainPane;

    private MenuView menuView;
    private SettingView settingView;

    private static boolean started = false;

    /**
     * Constructs a {@code MenuController} with the specified scene.
     *
     * @param _scene The main scene of the application.
     */
    public MenuController(Scene _scene) {
        scene = _scene;
        root = new StackPane();
        mainPane = new BorderPane();
        menuView = new MenuView(mainPane);
        settingView = new SettingView(root);
    }

    /**
     * Initializes the page layout and sets up the menu view.
     */
    private void initPage() {
        root.getChildren().add(mainPane);
        scene.setRoot(root);
        menuView.setPaneStyle();
    }

    /**
     * Draws the menu interface and sets up event handlers for the menu buttons.
     * If this is the first time displaying the menu, it initializes the menu view.
     * Otherwise, it simply displays the existing menu.
     */
    public void drawMenu() {
        initPage();
        if (!started) {
            menuView.initDisplayMenu();
            started = true;
        } else {
            menuView.displayMenu();
        }

        menuView.getSinglePlayButton().setOnMouseClicked(event -> menuView.fadeOutMenuAnimation(scene));
        menuView.getSettingsButton().setOnMouseClicked(event -> settingView.generate());
        menuView.getQuitButton().setOnMouseClicked(event -> Platform.exit());
    }
}
