/**
 * Provides styling configurations for the game screen components.
 * This class defines various style properties used in the game UI.
 */
package app.style;

public class StyleGame {

    /**
     * Returns the style for the game's BorderPane.
     *
     * @return A CSS string defining the background color.
     */
    public String gameBorderPaneStyle() {
        return "-fx-background-color: #1e1e1e";
    }

    /**
     * Returns the style for the game's side panel.
     *
     * @return A CSS string defining the background color.
     */
    public String gameSidePaneStyle() {
        return "-fx-background-color: #17171A;";
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
     * Returns the style for side labels in the game UI.
     *
     * @return A CSS string defining text color.
     */
    public String sideLabelStyle() {
        return "-fx-text-fill: #dddddd;";
    }

    /**
     * Returns the style for the side chat box.
     *
     * @return A CSS string defining background color, border color, and radius.
     */
    public String sideChatBox() {
        return "-fx-background-color: #17171A;" +
                "-fx-border-color: #dddddd;" +
                "-fx-border-style: solid;" +
                "-fx-border-width: 2px;" +
                "-fx-border-radius: 20px;";
    }

    /**
     * Returns the style for the side scroll pane.
     *
     * @return A CSS string defining transparency settings.
     */
    public String sideScrollPane() {
        return "-fx-background-color: transparent;" +
                "-fx-focus-color: transparent;" +
                "-fx-faint-focus-color: transparent;" +
                "-fx-border-color: transparent;" +
                "-fx-background-insets: 0;";
    }

    /**
     * Returns the style for the side message box.
     *
     * @return A CSS string defining background color, border radius, font size, and padding.
     */
    public String sideMessageBox() {
        return "-fx-background-color: #dddddd;" +
                "-fx-border-radius: 10px;" +
                "-fx-background-radius: 20px;" +
                "-fx-font-size: 16px;" +
                "-fx-padding: 10px;" +
                "-fx-focus-color: transparent;" +
                "-fx-faint-focus-color: transparent;";
    }

    /**
     * Returns the style for the turn effect highlight.
     *
     * @return A CSS string defining a linear gradient background.
     */
    public String myTurnEffectStyle() {
        return "-fx-background-color: linear-gradient(to top, #dddddd, transparent);";
    }

    /**
     * Returns the style for the status score box.
     *
     * @return A CSS string defining background color, border color, and radius.
     */
    public String statusScoreBoxStyle() {
        return "-fx-background-color: #1e1e1e;" +
                "-fx-border-color: #dddddd;" +
                "-fx-border-style: solid;" +
                "-fx-border-width: 2px;" +
                "-fx-border-radius: 20px;";
    }

    /**
     * Returns the style for status messages.
     *
     * @return A CSS string defining text color, font family, and font size.
     */
    public String statusMsgStyle() {
        return "-fx-text-fill: #549159;" +
                "-fx-font-family: 'Comic Sans MS';" +
                "-fx-font-size: 30px;";
    }

    /**
     * Returns the style for error messages.
     *
     * @return A CSS string defining text color, font family, and font size.
     */
    public String errorMsgStyle() {
        return "-fx-text-fill: #EE4035;" +
                "-fx-font-family: 'Comic Sans MS';" +
                "-fx-font-size: 30px;";
    }
}
