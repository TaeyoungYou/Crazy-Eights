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

    public String setButtonStyle() {
        return "-fx-background-color: #dddddd;" +
                "-fx-text-fill: #1e1e1e;" +
                "-fx-background-insets: 0;" +
                "-fx-background-radius: 10px;" +
                "-fx-font-size: 25px;" +
                "-fx-font-family: 'Comic Sans MS';" +
                "-fx-font-weight: bold;" +
                "-fx-alignment: center;" +
                "-fx-text-alignment: center;";
    }

    public String readyPressedStyle() {
        return "-fx-background-color: #1e1e1e;" +  // 내부 색 변경
                "-fx-text-fill: #dddddd;" +        // 글자 색 변경
                "-fx-background-radius: 10px;" +
                "-fx-font-size: 25px;" +
                "-fx-font-family: 'Comic Sans MS';" +
                "-fx-font-weight: bold;" +
                "-fx-border-color: #dddddd;" +
                "-fx-border-width: 2px;" + // 테두리 두께 추가
                "-fx-border-radius: 10px;" + // 테두리 둥글기 추가
                "-fx-alignment: center;" +
                "-fx-text-alignment: center;";
    }

    public String countDownStyle() {
        return "-fx-text-fill: #dddddd;";
    }
}
