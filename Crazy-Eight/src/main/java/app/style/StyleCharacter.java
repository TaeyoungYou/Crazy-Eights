package app.style;

/**
 * Provides styling configurations for specific character-related UI components.
 * This class defines various CSS style properties and font resources to be applied
 * to different elements in the application's user interface.
 */
public class StyleCharacter {
    /**
     * Returns the overlay style configuration for a UI component.
     *
     * @return A CSS string defining a semi-transparent black background.
     */
    public String overlayStyle(){
        return "-fx-background-color: rgba(0,0,0,0.8);";
    }

    /**
     * Retrieves the file path for the Lilita One font resource.
     *
     * @return The file path of the Lilita One font as a string.
     */
    public String getLilitaOneFont() {
        return getClass().getResource("/font/LilitaOne-Regular.ttf").toExternalForm();
    }

    /**
     * Returns the root style configuration for the game UI.
     *
     * @return A CSS string defining the background color for the root element.
     */
    public String gameRootStyle() {
        return "-fx-background-color: #1e1e1e";
    }
}
