package app;

import app.ui.MainMenu;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * The main entry point for the Crazy Eights game application.
 * This class initializes the JavaFX application and sets up the main menu.
 */
public class App extends Application {    /**
     * Starts the JavaFX application by setting up the primary stage.
     *
     * @param primaryStage The main window of the application.
     * @throws Exception If an error occurs during startup.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        BorderPane root = new BorderPane();
        Scene scene = new Scene(root);
        configStage(primaryStage);
        primaryStage.setScene(scene);
        primaryStage.show();

        MainMenu menu = new MainMenu(scene);
        menu.generate();
    }

    /**
     * The main method that launches the JavaFX application.
     *
     * @param args Command-line arguments.
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Configures the primary stage settings such as title, fullscreen mode, and exit key.
     *
     * @param primaryStage The primary stage to configure.
     */
    private void configStage(Stage primaryStage) {
        primaryStage.setTitle("Crazy Eights");
        primaryStage.setResizable(false);
        primaryStage.setFullScreen(true);
        primaryStage.setFullScreenExitHint("");
        primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
    }
}
