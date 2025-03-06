package app.controller;

import app.view.MenuView;
import app.view.SettingView;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

public class MenuController {
    private Scene scene;
    private StackPane root;
    private BorderPane mainPane;

    private MenuView menuView;
    private SettingView settingView;

    private static boolean started = false;

    public MenuController(Scene _scene) {
        scene = _scene;
        root = new StackPane();
        mainPane = new BorderPane();
        menuView = new MenuView(mainPane);
        settingView = new SettingView(root);
    }

    private void initPage(){
        root.getChildren().add(mainPane);
        scene.setRoot(root);

        menuView.setPaneStyle();
    }

    public void drawMenu(){
        initPage();
        if(!started){
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
