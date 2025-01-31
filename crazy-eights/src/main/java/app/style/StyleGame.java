package app.style;

public class StyleGame {
    public String gameBorderPaneStyle(){
        return "-fx-background-color: #1e1e1e";
    }

    public String gameSidePaneStyle(){
        return "-fx-background-color: #17171A;";
    }

    public String getLilitaOneFont(){
        return getClass().getResource("/font/LilitaOne-Regular.ttf").toExternalForm();
    }

    public String sideLabelStyle(){
        return "-fx-text-fill: #dddddd;";
    }

    public String sideChatBox(){
        return "-fx-background-color: #17171A;" +
                "-fx-border-color: #dddddd;" +
                "-fx-border-style: solid;" +
                "-fx-border-width: 2px;" +
                "-fx-border-radius: 20px;";
    }

    public String sideScrollPane(){
        return "-fx-background-color: transparent;" +
                "-fx-focus-color: transparent;" +
                "-fx-faint-focus-color: transparent;" +
                "-fx-border-color: transparent;" +
                "-fx-background-insets: 0;";
    }

    public String sideMessageBox(){
        return "-fx-background-color: #dddddd;" +
                "-fx-border-radius: 10px;" +
                "-fx-background-radius: 20px;" +
                "-fx-font-size: 16px;" +
                "-fx-padding: 10px;" +
                "-fx-focus-color: transparent;" +
                "-fx-faint-focus-color: transparent;";
    }
}
