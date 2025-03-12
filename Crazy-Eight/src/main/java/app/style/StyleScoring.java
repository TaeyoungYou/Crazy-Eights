package app.style;

public class StyleScoring {

    public String overlayStyle(){
        return "-fx-background-color: rgba(0,0,0,0.8);";
    }

    public String settingPaneStyle() {
        return "-fx-background-color: #1e1e1e;" +
                "-fx-border-color: #dddddd;" +
                "-fx-border-style: solid;" +
                "-fx-border-width: 2px;" +
                "-fx-border-radius: 20px;" +
                "-fx-background-radius: 20px;";
    }

    public String getLilitaOneFont() {
        return getClass().getResource("/font/LilitaOne-Regular.ttf").toExternalForm();
    }

    public String settingTitleStyle() {
        return "-fx-text-fill: #dddddd;" +
                "-fx-font-weight: bold;";
    }

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
