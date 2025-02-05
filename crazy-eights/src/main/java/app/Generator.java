package app;

/**
 * The Generator class serves as an entry point to launch the JavaFX application.
 * It delegates the execution to the main method of the {@link App} class.
 */
public class Generator {
    /**
     * The main method that starts the JavaFX application by calling {@link App#main(String[])}.
     *
     * @param args Command-line arguments passed to the application.
     * @throws Exception If an error occurs during application startup.
     */
    public static void main(String[] args) throws Exception {
        App.main(args);
    }
}
