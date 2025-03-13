package app.style;

/**
 * Provides various CSS style configurations for theming UI components.
 * This class defines styling details for overlays, settings panes, fonts, titles,
 * and button components used in the application's user interface.
 */
public class StyleScoring {

    /**
     * Returns the style for an overlay with a semi-transparent black background.
     *
     * @return A CSS string defining the background color with rgba transparency.
     */
    public String overlayStyle(){
        return "-fx-background-color: rgba(0,0,0,0.8);";
    }

    /**
     * Returns the CSS style string defining the appearance of the settings pane.
     * The style includes properties for background color, border, and corner radius.
     *
     * @return A CSS string specifying the background color, border color, border style,
     *         border width, and rounded corners for the settings pane.
     */
    public String settingPaneStyle() {
        return "-fx-background-color: #1e1e1e;" +
                "-fx-border-color: #dddddd;" +
                "-fx-border-style: solid;" +
                "-fx-border-width: 2px;" +
                "-fx-border-radius: 20px;" +
                "-fx-background-radius: 20px;";
    }

    /**
     * Retrieves the file path for the Lilita One font resource.
     *
     * @return The file path of the Lilita One font as a string in URL format.
     */
    public String getLilitaOneFont() {
        return getClass().getResource("/font/LilitaOne-Regular.ttf").toExternalForm();
    }

    /**
     * Returns the CSS style string defining the appearance of a title in the settings pane.
     * The style includes properties for text color and font weight.
     *
     * @return A CSS string specifying the text fill color and font weight for the title.
     */
    public String settingTitleStyle() {
        return "-fx-text-fill: #dddddd;" +
                "-fx-font-weight: bold;";
    }

    /**
     * Returns the common CSS style string for a loading button.
     * The style includes properties for background color, text color, font settings,
     * alignment, and border radius.
     *
     * @return A CSS string defining the appearance of a loading button, including background color,
     *         text color, font family, font size, font weight, alignment, and border radius.
     */
    public String loadingButtonCommonStyle() {
        return "-fx-background-color: #dddddd;" +
                "-fx-text-fill: #1e1e1e;" +
                "-fx-background-insets: 0;" +
                "-fx-background-radius: 10px;" +
                "-fx-font-size: 20px;" +
                "-fx-font-family: 'Comic Sans MS';" +
                "-fx-font-weight: bold;" +
                "-fx-alignment: center;" +
                "-fx-text-alignment: center;";
    }

}
