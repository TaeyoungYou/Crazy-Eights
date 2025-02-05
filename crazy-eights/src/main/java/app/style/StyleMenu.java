/**
 * Provides styling configurations for the main menu components.
 * This class defines various style properties used in the UI.
 */
package app.style;

public class StyleMenu {

    /**
     * Returns the style for the main menu's BorderPane.
     *
     * @return A CSS string defining the background color.
     */
    public String loadingBorderPaneStyle() {
        return "-fx-background-color: #1e1e1e";
    }

    /**
     * Returns the style for the main menu title.
     *
     * @return A CSS string defining text color and font weight.
     */
    public String loadingTitleStyle() {
        return "-fx-text-fill: #dddddd; " +
                "-fx-font-weight: bold;";
    }

    /**
     * Retrieves the file path for the Lilita One font.
     *
     * @return The file path of the font as a string.
     */
    public String getLilitaOneFont() {
        return getClass().getResource("/font/LilitaOne-Regular.ttf").toExternalForm();
    }

    /**
     * Returns the common style for buttons in the main menu.
     *
     * @return A CSS string defining background color, text styling, and alignment.
     */
    public String loadingButtonCommonStyle() {
        return "-fx-background-color: #dddddd;" +
                "-fx-text-fill: #1e1e1e;" +
                "-fx-background-insets: 0;" +
                "-fx-background-radius: 10px;" +
                "-fx-font-size: 40px;" +
                "-fx-font-family: 'Comic Sans MS';" +
                "-fx-font-weight: bold;" +
                "-fx-alignment: center;" +
                "-fx-text-alignment: center;";
    }

    /**
     * Returns the style for large buttons in the main menu.
     *
     * @return A CSS string defining the preferred width and height.
     */
    public String loadingButtonLargeStyle() {
        return "-fx-pref-width: 800px;" +
                "-fx-pref-height: 85px;";
    }

    /**
     * Returns the style for small buttons in the main menu.
     *
     * @return A CSS string defining the preferred width and height.
     */
    public String loadingButtonSmallStyle() {
        return "-fx-pref-width: 395px;" +
                "-fx-pref-height: 85px;";
    }

    /**
     * Returns the style for the layout box in the main menu.
     *
     * @return A CSS string defining spacing between elements.
     */
    public String loadingBoxStyle() {
        return "-fx-spacing: 10px;";
    }
}
