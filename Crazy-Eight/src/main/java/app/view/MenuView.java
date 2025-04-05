package app.view;

import app.animation.AnimationMenu;
import app.style.StyleMenu;
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

    /**
     * Constructs an instance of the MenuView class, initializing the user interface
     * elements and associated styles for the application menu.
     *
     * @param _pane The BorderPane container used as the root layout for the menu.
     */
    public MenuView(BorderPane _pane) {
        pane = _pane;
        style = new StyleMenu();
        animation = new AnimationMenu();
    }

    /**
     * Initializes the main menu page by setting up the layout, creating UI elements,
     * and applying styles to them. This method configures the main title, buttons,
     * and layout containers such as `VBox` and `HBox` to build the menu structure.
     * Additionally, it associates style definitions from the `style` object to the
     * respective UI elements.
     *
     * The following elements are created and styled in this method:
     * - Title label with a specific font and style for the menu.
     * - Buttons for navigating the menu, including SinglePlayer, MultiPlayer, Setting, and Quit options,
     *   each styled with common and size-specific styles.
     * - A `VBox` (`menuPane`) that acts as the main container for the menu, positioned at the center.
     * - An `HBox` (`bottomButtonPane`) for placing the Setting and Quit buttons at the bottom, also aligned centrally.
     *
     * A small spacing region is added to improve layout organization, and the containers are styled
     * using a consistent box style. Finally, the `menuPane` is set as the center element of the main layout pane (`pane`).
     */
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

    /**
     * Fades out the main menu elements through an animation sequence and transitions
     * to the next scene or menu state. This is typically utilized to provide a smooth
     * user experience when navigating away from the current menu.
     *
     * @param scene The current scene where the fade-out animation is applied.
     */
    public void fadeOutMenuAnimation(Scene scene, boolean isSingle){
        animation.fadeOutMainMenu(scene, menuPane, isSingle);
    }

    /**
     * Retrieves the single play button label, which represents the "Single Player" option
     * in the menu.
     *
     * @return A Label object representing the single play button in the main menu.
     */
    public Label getSinglePlayButton(){
        return singlePlayButton;
    }

    public Label getMultiPlayButton(){
        return multiPlayButton;
    }

    /**
     * Retrieves the settings button label, which represents the "Settings" option
     * in the menu.
     *
     * @return A Label object representing the settings button in the main menu.
     */
    public Label getSettingsButton(){
        return settingsButton;
    }
    /**
     * Retrieves the quit button label, which represents the "Quit" option in the menu.
     *
     * @return A Label object representing the quit button in the main menu.
     */
    public Label getQuitButton(){
        return quitButton;
    }

}
