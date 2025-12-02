package app.style;

/**
 * Provides styling configurations for the application.
 * This class contains various CSS styles used for different UI components.
 */
public class StyleSetting {

    /**
     * Returns the CSS style for an overlay with a semi-transparent black background.
     *
     * @return A CSS string defining the background color with rgba transparency.
     */
    public String overlayStyle(){
        return "-fx-background-color: rgba(0,0,0,0.8);";
    }

    /**
     * Returns the CSS style for the settings pane.
     * The style includes configurations for background color, border color,
     * border style, border width, and border/background radius.
     *
     * @return A CSS string defining the visual style of the settings pane.
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
     * Retrieves the file path for the Lilita One font.
     *
     * @return The file path of the "LilitaOne-Regular.ttf" font as a string in external form.
     */
    public String getLilitaOneFont() {
        return getClass().getResource("/font/LilitaOne-Regular.ttf").toExternalForm();
    }
    /**
     * Retrieves the file path for the Cookie Run font.
     *
     * @return The file path of the "CookieRun-Regular.ttf" font as a string in its external form.
     */
    public String getCookieRunFont(){
        return getClass().getResource("/font/CookieRun-Regular.ttf").toExternalForm();
    }

    /**
     * Returns the CSS style for the title in settings.
     * The style includes configurations for text color and font weight.
     *
     * @return A CSS string defining the visual style of the title in the settings section.
     */
    public String settingTitleStyle() {
        return "-fx-text-fill: #dddddd;" +
                "-fx-font-weight: bold;";
    }

    /**
     * Returns the CSS style for configuring the appearance of a slider.
     * The style includes transparency settings and a preferred height.
     *
     * @return A CSS string defining the visual style of a slider.
     */
    public String setSliderStyle() {
        return "-fx-background-color: transparent;" +  // 기본 바 색상
                "-fx-control-inner-background: transparent;" +
                "-fx-pref-height: 10px;";  // 바의 높이
    }

    /**
     * Returns the CSS style for configuring the visual appearance of a thumb element.
     * The style includes settings for background color, border color, border width,
     * dimensions, and background radius.
     *
     * @return A CSS string defining the visual style of a thumb element.
     */
    public String setThumbStyle(){
        return "-fx-background-color: #17171a;" +
                "-fx-background-radius: 50%;" +
                "-fx-border-color: #dddddd;" +
                "-fx-border-width: 3px;" +
                "-fx-pref-width: 20px;" +
                "-fx-pref-height: 20px;";
    }
    /**
     * Returns the CSS style for configuring the visual appearance of a track element.
     * The style includes background color, background radius, and preferred height.
     *
     * @return A CSS string defining the visual style of a track element.
     */
    public String setTrackStyle(){
        return "-fx-background-color: #dddddd;" +
                "-fx-background-radius: 5px;" +
                "-fx-pref-height: 6px;";
    }

    /**
     * Returns the common CSS style for an English button.
     * This style includes configurations for background color, text color,
     * background insets, background radius, font size, font family, font weight,
     * alignment, and text alignment.
     *
     * @return A CSS string defining the visual style of an English button.
     */
    public String enButtonCommonStyle() {
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
    /**
     * Returns the CSS style for configuring the appearance of a Korean language button.
     * The style includes settings for background color, text color, background insets,
     * background radius, alignment, and text alignment.
     *
     * @return A CSS string defining the visual style of the Korean language button.
     */
    public String krButtonCommonStyle() {
        return "-fx-background-color: #dddddd;" +
                "-fx-text-fill: #1e1e1e;" +
                "-fx-background-insets: 0;" +
                "-fx-background-radius: 10px;" +
                "-fx-alignment: center;" +
                "-fx-text-alignment: center;";
    }

    /**
     * Returns the CSS style for configuring the appearance of a pressed English button.
     * The style includes settings for background color, text color, border color,
     * border thickness, border radius, font size, font family, font weight,
     * alignment, and text alignment.
     *
     * @return A CSS string defining the visual style of a pressed English button.
     */
    public String enButtonPressedStyle() {
        return "-fx-background-color: #1e1e1e;" +  // 내부 색 변경
                "-fx-text-fill: #dddddd;" +        // 글자 색 변경
                "-fx-background-radius: 10px;" +
                "-fx-font-size: 20px;" +
                "-fx-font-family: 'Comic Sans MS';" +
                "-fx-font-weight: bold;" +
                "-fx-border-color: #dddddd;" +
                "-fx-border-width: 2px;" + // 테두리 두께 추가
                "-fx-border-radius: 10px;" + // 테두리 둥글기 추가
                "-fx-alignment: center;" +
                "-fx-text-alignment: center;";
    }

    /**
     * Returns the CSS style for configuring the appearance of a pressed Korean language button.
     * The style includes settings for background color, text color, border color, border thickness,
     * border radius, alignment, and text alignment.
     *
     * @return A CSS string defining the visual style of a pressed Korean button.
     */
    public String krButtonPressedStyle() {
        return "-fx-background-color: #1e1e1e;" +  // 내부 색 변경
                "-fx-text-fill: #dddddd;" +        // 글자 색 변경
                "-fx-background-insets: 0;" +
                "-fx-background-radius: 10px;" +
                "-fx-border-color: #dddddd;" +
                "-fx-border-width: 2px;" + // 테두리 두께 추가
                "-fx-border-radius: 10px;" + // 테두리 둥글기 추가
                "-fx-alignment: center;" +
                "-fx-text-alignment: center;";
    }

}
