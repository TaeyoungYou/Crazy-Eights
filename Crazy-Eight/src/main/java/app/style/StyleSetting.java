package app.style;

public class StyleSetting {

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
    public String getCookieRunFont(){
        return getClass().getResource("/font/CookieRun-Regular.ttf").toExternalForm();
    }

    public String settingTitleStyle() {
        return "-fx-text-fill: #dddddd;" +
                "-fx-font-weight: bold;";
    }

    public String setSliderStyle() {
        return "-fx-background-color: transparent;" +  // 기본 바 색상
                "-fx-control-inner-background: transparent;" +
                "-fx-pref-height: 10px;";  // 바의 높이
    }

    public String setThumbStyle(){
        return "-fx-background-color: #17171a;" +
                "-fx-background-radius: 50%;" +
                "-fx-border-color: #dddddd;" +
                "-fx-border-width: 3px;" +
                "-fx-pref-width: 20px;" +
                "-fx-pref-height: 20px;";
    }
    public String setTrackStyle(){
        return "-fx-background-color: #dddddd;" +
                "-fx-background-radius: 5px;" +
                "-fx-pref-height: 6px;";
    }

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
    public String krButtonCommonStyle() {
        return "-fx-background-color: #dddddd;" +
                "-fx-text-fill: #1e1e1e;" +
                "-fx-background-insets: 0;" +
                "-fx-background-radius: 10px;" +
                "-fx-alignment: center;" +
                "-fx-text-alignment: center;";
    }

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
