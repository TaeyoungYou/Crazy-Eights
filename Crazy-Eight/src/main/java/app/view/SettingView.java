package app.view;

import app.animation.AnimationSetting;
import app.model.Music;
import app.model.Setting;
import app.style.StyleSetting;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.util.Duration;

public class SettingView {
    private final StackPane pane;
    private final StyleSetting style;
    private final AnimationSetting animation;

    private Label enButton;
    private Label krButton;

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
        settingPane.setSpacing(30);

        Label settingTitle = new Label("Settings");
        settingTitle.setFont(Font.loadFont(style.getLilitaOneFont(), 80));
        settingTitle.setStyle(style.settingTitleStyle());

        VBox elementPane = new VBox();
        elementPane.setAlignment(Pos.CENTER);

        VBox volumeBox = new VBox();
        volumeBox.setAlignment(Pos.CENTER);
        volumeBox.setSpacing(10);
        volumeBox.setPrefWidth(400);

        Label volumeTitle = new Label("Music Volume");
        volumeTitle.setFont(Font.loadFont(style.getLilitaOneFont(), 30));
        volumeTitle.setStyle(style.settingTitleStyle());

        Slider volumeSlider = new Slider(0, 100, Music.getVolume() * 100);
        volumeSlider.setPrefWidth(400);
        volumeSlider.setMaxWidth(400);
        volumeSlider.setMinHeight(30);

        volumeSlider.setStyle(style.setSliderStyle());

        Platform.runLater(() -> {
            var thumb = volumeSlider.lookup(".thumb");
            if (thumb != null) {
                thumb.setStyle(style.setThumbStyle());
            }
            var track = volumeSlider.lookup(".track");
            if(track != null) {
                track.setStyle(style.setTrackStyle());
            }
        });
        volumeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            Music.setVolume(newValue.doubleValue() / 100.0);
        });

        HBox buttonBox = new HBox(100);
        buttonBox.setAlignment(Pos.CENTER);

        Label langTitle = new Label("Language");
        langTitle.setFont(Font.loadFont(style.getLilitaOneFont(), 30));
        langTitle.setStyle(style.settingTitleStyle());

        enButton = new Label("ENGLISH");
        krButton = new Label("한국어");

        enButton.setPrefSize(150, 50);
        krButton.setPrefSize(150, 50);

        if(Setting.isEnClicked()){
            enButton.setStyle(style.enButtonPressedStyle());
            krButton.setStyle(style.krButtonCommonStyle());
            krButton.setFont(Font.loadFont(style.getCookieRunFont(), 20));
        } else {
            enButton.setStyle(style.enButtonCommonStyle());
            krButton.setStyle(style.krButtonPressedStyle());
            krButton.setFont(Font.loadFont(style.getCookieRunFont(), 20));
        }



        buttonBox.getChildren().addAll(enButton, krButton);

        volumeBox.getChildren().addAll(volumeTitle, volumeSlider);
        elementPane.getChildren().addAll(volumeBox);

        settingPane.getChildren().addAll(settingTitle, elementPane, langTitle, buttonBox);

        animation.fadeInSetting(overlay);
        overlay.getChildren().add(settingPane);
        pane.getChildren().add(overlay);

        animation.mouseInOutSetting(settingPane);
        animation.buttonAnimation(enButton);
        animation.buttonAnimation(krButton);

        settingPane.setOnMouseClicked(Event::consume);
        overlay.setOnMouseEntered(e->overlay.setCursor(Cursor.HAND));
        overlay.setOnMouseClicked(event -> {
            animation.fadeOutSetting(pane, overlay);
        });
        enButton.setOnMouseClicked(e->{
            System.out.println("ENGLISH BUTTON CLICKED");
            Setting.setEnClicked(true);
            Setting.setKrClicked(false);

            enButton.setStyle(style.enButtonPressedStyle());
            krButton.setStyle(style.krButtonCommonStyle());
            krButton.setFont(Font.loadFont(style.getCookieRunFont(), 20));
        });
        krButton.setOnMouseClicked(e->{
            System.out.println("KOREAN BUTTON CLICKED");
            Setting.setEnClicked(false);
            Setting.setKrClicked(true);

            enButton.setStyle(style.enButtonCommonStyle());
            krButton.setStyle(style.krButtonPressedStyle());
            krButton.setFont(Font.loadFont(style.getCookieRunFont(), 20));
        });
    }



    public StackPane getPane() {
        return pane;
    }
}
