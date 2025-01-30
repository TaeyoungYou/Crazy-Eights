package app.style;

public class StyleMenu {

    public String loadingBorderPaneStyle(){
        return "-fx-background-color: #1e1e1e";
    }
    public String loadingTitleStyle(){
        return "-fx-text-fill: #dddddd; " +
                "-fx-font-weight: bold;" +
                "-fx-opacity: 0.0";
    }
    public String getLilitaOneFont(){
        return getClass().getResource("/font/LilitaOne-Regular.ttf").toExternalForm();
    }
    public String loadingButtonCommonStyle(){
        return "-fx-background-color: #dddddd;" +
                "-fx-text-fill: #1e1e1e;" +
                "-fx-background-insets: 0;" +
                "-fx-background-radius: 10px;" +
                "-fx-font-size: 40px;" +
                "-fx-font-family: 'Comic Sans MS';" +
                "-fx-font-weight: bold;";
    }
    public String loadingButtonLargeStyle() {
        return "-fx-pref-width: 800px;" +
                "-fx-pref-height: 50px;";
    }
    public String loadingButtonSmallStyle() {
        return "-fx-pref-width: 395px;" +
                "-fx-pref-height: 50px;";
    }
    public String loadingBoxStyle(){
        return "-fx-spacing: 10px;";
    }
}
