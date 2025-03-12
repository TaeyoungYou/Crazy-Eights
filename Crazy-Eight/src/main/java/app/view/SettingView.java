package app.view;

import app.animation.AnimationSetting;
import app.model.Music;
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


        StackPane switchPane = new StackPane();
        ToggleButton toggleButton = new ToggleButton();
        toggleLanguage(switchPane, toggleButton);
        switchPane.setPrefSize(80, 30);

        StackPane buttonPane = new StackPane(switchPane, toggleButton);
        buttonPane.setStyle("-fx-background-color: #17171a; -fx-alignment: center; -fx-padding: 20;");

        volumeBox.getChildren().addAll(volumeTitle, volumeSlider);
        elementPane.getChildren().addAll(volumeBox, buttonPane);

        settingPane.getChildren().addAll(settingTitle, elementPane);


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

    public void toggleLanguage(StackPane switchPane, ToggleButton toggleButton) {
        Rectangle bg = new Rectangle(80, 30, Color.RED);
        bg.setArcWidth(30);
        bg.setArcHeight(30);

        Circle handle = new Circle(14, Color.WHITE);
        handle.setTranslateX(-20);

        Label label = new Label("EN");
        label.setTextFill(Color.WHITE);
        label.setStyle("-fx-font-size: 14px; -fx-font-weight: bold");

        toggleButton.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");

        TranslateTransition transition = new TranslateTransition(Duration.millis(200), handle);

        toggleButton.setOnAction(event -> {
            if (toggleButton.isSelected()) {
                bg.setFill(Color.BLUE); // KR 모드
                transition.setToX(20); // 핸들 이동 (오른쪽)
                label.setText("KR");
            } else {
                bg.setFill(Color.RED); // EN 모드
                transition.setToX(-20); // 핸들 이동 (왼쪽)
                label.setText("EN");
            }
            transition.play();
        });
        switchPane.getChildren().addAll(bg, label, handle);
    }

    public StackPane getPane() {
        return pane;
    }
}
