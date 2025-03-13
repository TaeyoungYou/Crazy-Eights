package app.style;

/**
 * Provides styling configurations for various UI components.
 * This class defines several CSS styles used for theming.
 */
public class StyleEight {
    /**
     * Returns the style for an overlay with a semi-transparent black background.
     *
     * @return A CSS string defining the background color with rgba transparency.
     */
    public String overlayStyle(){
        return "-fx-background-color: rgba(0,0,0,0.8);";
    }

    /**
     * Retrieves the file path for the Lilita One font.
     *
     * @return The file path to the "LilitaOne-Regular.ttf" font as a string.
     * Provides the external form of the URL resource path where the font file is located.
     */
    public String getLilitaOneFont() {
        return getClass().getResource("/font/LilitaOne-Regular.ttf").toExternalForm();
    }
}
