package app;

import app.controller.MenuController;
import app.model.multi.Client;
import app.model.multi.Server;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.awt.*;

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
        StackPane root = new StackPane();
        root.setPrefSize(1920, 1090);
        Scene scene = new Scene(root);
        configStage(primaryStage);
        primaryStage.setScene(scene);
        primaryStage.show();

        MenuController menuController = new MenuController(scene);
        menuController.drawMenu();
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
    private void configStage(Stage primaryStage) throws AWTException {
        primaryStage.setTitle("Crazy Eights");
        primaryStage.setResizable(false);
        primaryStage.setFullScreen(false);
        primaryStage.setFullScreenExitHint("");
        primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        primaryStage.getIcons().add(new Image(getClass().getResource("/icon/icon.png").toExternalForm()));
        primaryStage.setOnCloseRequest(event -> {
            event.consume();
            new Thread(() -> {
                try {
                    Server server = Server.getInstance();
                    if (server != null && server.getServerSocket() != null) {
                        System.out.println("[EXIT] Sending force exit message");
                        Client.send("-1#FORCE_EXIT#-1");
                        System.out.println("[EXIT] Stopping server...");
                        server.stop();
                        System.out.println("[EXIT] Server stopped");
                    }
                    System.out.println("[EXIT] Stopping client...");
                    Client.close();
                    System.out.println("[EXIT] Client stopped");
                } catch (Exception e) {
                    System.err.println("[EXIT] Error during shutdown:");
                    e.printStackTrace();
                } finally {
                    System.out.println("[EXIT] Program exiting");
                    Platform.exit(); // JavaFX 애플리케이션 종료
                    System.exit(0);  // JVM 종료
                }
            }).start();
        });
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("[SHUTDOWN] Shutdown hook triggered");
            Server server = Server.getInstance();
            try {
                if (server != null && server.getServerSocket() != null) {
                    System.out.println("[SHUTDOWN] Stopping server...");
                    server.stop();
                    System.out.println("[SHUTDOWN] Server stopped");
                }
                System.out.println("[SHUTDOWN] Stopping client...");
                Client.close();
                System.out.println("[SHUTDOWN] Client stopped");
            } catch (Exception e) {
                System.err.println("[SHUTDOWN] Error during shutdown:");
                e.printStackTrace();
            }
        }));
    }
}
