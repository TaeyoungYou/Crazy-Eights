package app.style;

public class StyleMultiMenu {

    public String loadingPaneStyle() {
        return "-fx-background-color: rgba(0,0,0,0.8);";
    }

    public String setButtonStyle() {
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

    public String multiMenuPaneStyle() {
        return "-fx-background-color: #1e1e1e;" +
                "-fx-border-color: #dddddd;" +
                "-fx-border-style: solid;" +
                "-fx-border-width: 2px;" +
                "-fx-border-radius: 20px;" +
                "-fx-background-radius: 20px;";
    }

    public String portBoxStyle() {
        return "-fx-background-color: #dddddd;" +
                "-fx-border-radius: 10px;" +
                "-fx-background-radius: 20px;" +
                "-fx-font-family: 'Comic Sans MS';" +
                "-fx-font-size: 18px;" +
                "-fx-text-fill: #17171A;" +
                "-fx-padding: 10px;" +
                "-fx-focus-color: transparent;" +
                "-fx-faint-focus-color: transparent;";
    }

    public String getLilitaOneFont() {
        return getClass().getResource("/font/LilitaOne-Regular.ttf").toExternalForm();
    }

    public String setTitleStyle() {
        return "-fx-text-fill: #dddddd;" +
                "-fx-font-weight: bold;";
    }

    public String setLabelStyle() {
        return "-fx-text-fill: #dddddd;";
    }

    public String sectionStyle() {
        return "-fx-background-color: transparent";
    }

    public String setSeparatorStyle() {
        return "-fx-background-color: linear-gradient(to bottom, #ffffff22 0%, #dddddd 50%, #ffffff22 100%)";
    }

    public String setPreStyle() {
        return "-fx-font-size: 14px;" +
                "-fx-font-family: 'Comic Sans MS';" +
                "-fx-text-fill: #999999";
    }
}
