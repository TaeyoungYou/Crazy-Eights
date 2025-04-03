package app.view;

import app.animation.AnimationSetting;
import app.model.single.Music;
import app.model.single.Setting;
import app.style.StyleSetting;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Font;

/**
 * The SettingView class is responsible for displaying and managing the settings interface
 * for the application. It provides functionalities for customizing user preferences such as
 * language selection and music volume, as well as applying animations and styles to GUI components.
 */
public class SettingView {
    private final StackPane pane;
    private final StyleSetting style;
    private final AnimationSetting animation;

    private Label enButton;
    private Label krButton;

    /**
     * Constructs a SettingView instance.
     * Initializes the given pane, as well as style and animation settings.
     *
     * @param pane the StackPane instance that serves as the central pane for the settings view
     */
    public SettingView(StackPane pane) {
        this.pane = pane;
        style = new StyleSetting();
        animation = new AnimationSetting();
    }

    /**
     * Generates and displays the settings overlay in the application. This method creates a
     * semi-transparent overlay with a styled settings pane containing configurable options
     * such as music volume and language settings. It incorporates animations for a smooth
     * transition and interactive elements with customized styles.
     *
     * Functionality:
     * - Creates a stack pane styled as a semi-transparent overlay.
     * - Adds a settings pane with a structured layout for configurable settings.
     * - Includes a volume slider that adjusts the background music's volume.
     * - Displays language selection buttons (English and Korean) with toggling functionality
     *   based on user interaction.
     * - Applies custom styles to various elements, such as titles, sliders, and buttons.
     * - Adds animations for the overlay and settings pane.
     * - Provides event handling for interactions, including button clicks and mouse events.
     *
     * Behavior:
     * - Dynamically binds the overlay's size to the parent pane's dimensions.
     * - Configures event listeners for slider adjustments to update music volume.
     * - Sets up language toggle buttons with corresponding styles and actions.
     * - Allows the overlay to close upon clicking outside the settings pane.
     *
     * Infrastructure:
     * - Utilizes classes and methods from external utility members, including `style`,
     *   `animation`, and `Music`.
     * - Relies on JavaFX components for UI and styling.
     * - Executes certain UI element setup in the JavaFX application thread using `Platform.runLater`.
     */
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

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                var thumb = volumeSlider.lookup(".thumb");
                if (thumb != null) {
                    thumb.setStyle(style.setThumbStyle());
                }
                var track = volumeSlider.lookup(".track");
                if (track != null) {
                    track.setStyle(style.setTrackStyle());
                }
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
        enButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                System.out.println("ENGLISH BUTTON CLICKED");
                Setting.setEnClicked(true);
                Setting.setKrClicked(false);

                enButton.setStyle(style.enButtonPressedStyle());
                krButton.setStyle(style.krButtonCommonStyle());
                krButton.setFont(Font.loadFont(style.getCookieRunFont(), 20));
            }
        });
        krButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                System.out.println("KOREAN BUTTON CLICKED");
                Setting.setEnClicked(false);
                Setting.setKrClicked(true);

                enButton.setStyle(style.enButtonCommonStyle());
                krButton.setStyle(style.krButtonPressedStyle());
                krButton.setFont(Font.loadFont(style.getCookieRunFont(), 20));
            }
        });
    }



    /**
     * Retrieves the StackPane instance associated with this class.
     *
     * @return The StackPane object used as the central pane in the settings view.
     */
    public StackPane getPane() {
        return pane;
    }
}
