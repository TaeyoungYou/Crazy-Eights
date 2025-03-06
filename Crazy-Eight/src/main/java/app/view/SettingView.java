package app.view;

import app.animation.AnimationSetting;
import app.style.StyleSetting;
import javafx.event.Event;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.text.Font;

public class SettingView {
    private final StackPane pane;
    private final StyleSetting style;
    private final AnimationSetting animation;

    public SettingView(StackPane pane) {
        this.pane = pane;
        style = new StyleSetting();
        animation = new AnimationSetting();
    }

    public void generate(){
        StackPane overlay = new StackPane();
        overlay.setStyle(style.overlayStyle());
        overlay.prefWidthProperty().bind(pane.widthProperty());
        overlay.prefHeightProperty().bind(pane.heightProperty());
        overlay.setAlignment(Pos.CENTER);

        VBox settingPane = new VBox();
        settingPane.setStyle(style.settingPaneStyle());
        settingPane.setPrefSize(600, 400);
        settingPane.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        settingPane.setAlignment(Pos.TOP_CENTER);

        Label settingTitle = new Label("Settings");
        settingTitle.setFont(Font.loadFont(style.getLilitaOneFont(), 80));
        settingTitle.setStyle(style.settingTitleStyle());

        settingPane.getChildren().add(settingTitle);


        animation.fadeInSetting(overlay);
        overlay.getChildren().add(settingPane);
        pane.getChildren().add(overlay);

        animation.mouseInOutSetting(settingPane);

        settingPane.setOnMouseClicked(Event::consume);
        overlay.setOnMouseEntered(e->overlay.setCursor(Cursor.HAND));
        overlay.setOnMouseClicked(event -> {
            animation.fadeOutSetting(pane, overlay);
        });
    }

    public StackPane getPane() {
        return pane;
    }
}
