package app.view;

import app.animation.AnimationMenu;
import app.style.StyleMenu;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;

/**
 * Represents the main menu of the Crazy Eights game.
 * This class initializes and manages the user interface for the main menu.
 */
public class MenuView {
    private BorderPane pane;
    private final StyleMenu style;
    private final AnimationMenu animation;

    private VBox menuPane;
    private HBox bottomButtonPane;

    private Label title;
    private Label singlePlayButton;
    private Label multiPlayButton;
    private Label settingsButton;
    private Label quitButton;

    public MenuView(BorderPane _pane) {
        pane = _pane;
        style = new StyleMenu();
        animation = new AnimationMenu();
    }

    private void initializePage() {
        title = new Label("Crazy Eights");
        title.setFont(Font.loadFont(style.getLilitaOneFont(), 180));
        title.setStyle(style.loadingTitleStyle());

        singlePlayButton = new Label("SinglePlayer");
        multiPlayButton = new Label("MultiPlayer");
        settingsButton = new Label("Setting");
        quitButton = new Label("Quit");

        singlePlayButton.setStyle(style.loadingButtonCommonStyle() + style.loadingButtonLargeStyle());
        multiPlayButton.setStyle(style.loadingButtonCommonStyle() + style.loadingButtonLargeStyle());
        settingsButton.setStyle(style.loadingButtonCommonStyle() + style.loadingButtonSmallStyle());
        quitButton.setStyle(style.loadingButtonCommonStyle() + style.loadingButtonSmallStyle());

        menuPane = new VBox(title);
        menuPane.setAlignment(Pos.CENTER);
        bottomButtonPane = new HBox(settingsButton, quitButton);
        bottomButtonPane.setAlignment(Pos.CENTER);

        Region space = new Region();
        space.setPrefHeight(10);
        menuPane.getChildren().add(space);

        menuPane.setStyle(style.loadingBoxStyle());
        bottomButtonPane.setStyle(style.loadingBoxStyle());

        pane.setCenter(menuPane);
    }

    /**
     * Displays the start game menu with animations.
     */
    public void initDisplayMenu() {
        initializePage();
        Node[] nodes = {singlePlayButton, multiPlayButton, bottomButtonPane};

        animation.startMenuAnimation(menuPane, nodes, title);
        animation.menuHover(nodes);
    }

    /**
     * Displays the game menu with available options.
     */
    public void displayMenu() {
        initializePage();
        menuPane.getChildren().addAll(singlePlayButton, multiPlayButton, bottomButtonPane);

        Label[] tmp = {singlePlayButton, multiPlayButton, settingsButton, quitButton};
        animation.menuAnimation(title, tmp);
        animation.fadeInMainMenu(menuPane);
    }

    /**
     * Initializes the main page layout.
     */
    public void setPaneStyle() {
        pane.setStyle(style.loadingBorderPaneStyle());

        ImageView bg = new ImageView(new Image(getClass().getResource("/background/menu_background.png").toExternalForm()));
        bg.setPreserveRatio(true);
        bg.fitWidthProperty().bind(pane.widthProperty());

        pane.getChildren().add(bg);
    }

    public void fadeOutMenuAnimation(Scene scene){
        animation.fadeOutMainMenu(scene, menuPane);
    }

    public Label getSinglePlayButton(){
        return singlePlayButton;
    }

    public Label getSettingsButton(){
        return settingsButton;
    }
    public Label getQuitButton(){
        return quitButton;
    }

}
