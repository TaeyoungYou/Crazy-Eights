package app;

import app.ui.MainMenu;


import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.stage.Screen;
import javafx.stage.Stage;



public class App extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        final double screen_width = Screen.getPrimary().getVisualBounds().getWidth();
        final double screen_height = Screen.getPrimary().getVisualBounds().getHeight();

        BorderPane root = new BorderPane();
        Scene scene = new Scene(root);
        configStage(primaryStage);
        primaryStage.setScene(scene);
        primaryStage.show();

        MainMenu menu = new MainMenu(scene);
        menu.generate();
    }

    public static void main(String[] args) {
        launch(args);
    }
    private void configStage(Stage primaryStage) {
        primaryStage.setTitle("Crazy Eights");
        primaryStage.setResizable(false);
        primaryStage.setFullScreen(true);
        primaryStage.setFullScreenExitHint("");
        primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
    }
}
