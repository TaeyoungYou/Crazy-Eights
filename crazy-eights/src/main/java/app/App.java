package app;

import app.ui.Menu;


import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;
import javafx.stage.Stage;



public class App extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        final double screen_width = Screen.getPrimary().getVisualBounds().getWidth();
        final double screen_height = Screen.getPrimary().getVisualBounds().getHeight();

        Menu menu = new Menu(primaryStage);
        menu.generate();

        configStage(primaryStage);
        primaryStage.show();
    }

    /**
     * 루트 stage에 대한 설정 메서드
     * @param primaryStage
     */
    private void configStage(Stage primaryStage) {
        primaryStage.setTitle("Crazy Eights");
        primaryStage.setResizable(false);
        primaryStage.setFullScreen(true);
        primaryStage.setFullScreenExitHint("");
        primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
