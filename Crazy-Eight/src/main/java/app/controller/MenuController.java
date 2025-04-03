package app.controller;

import app.view.MenuView;
import app.view.SettingView;
import app.view.multi.MultiMenuView;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
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
    private MultiMenuView multiMenuView;

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
        multiMenuView = new MultiMenuView(root);
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

        menuView.getSinglePlayButton().setOnMouseClicked(new SinglePlayButtonHandler());
        menuView.getMultiPlayButton().setOnMouseClicked(new MultiPlayButtonHandler());
        menuView.getSettingsButton().setOnMouseClicked(new SettingsButtonHandler());
        menuView.getQuitButton().setOnMouseClicked(new QuitButtonHandler());
    }

    private class SinglePlayButtonHandler implements EventHandler<MouseEvent> {
        @Override
        public void handle(MouseEvent event) {
            menuView.fadeOutMenuAnimation(scene);
        }
    }

    private class MultiPlayButtonHandler implements EventHandler<MouseEvent> {
        @Override
        public void handle(MouseEvent event) {
            multiMenuView.generate();
        }
    }

    private class SettingsButtonHandler implements EventHandler<MouseEvent> {
        @Override
        public void handle(MouseEvent event) {
            settingView.generate();
        }
    }

    private class QuitButtonHandler implements EventHandler<MouseEvent> {
        @Override
        public void handle(MouseEvent event) {
            Platform.exit();
        }
    }
}
