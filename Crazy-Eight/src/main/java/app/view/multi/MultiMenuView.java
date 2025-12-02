package app.view.multi;


import app.animation.AnimationMultiMenu;
import app.model.multi.Client;
import app.model.multi.MsgType;
import app.model.multi.Server;
import app.style.StyleMultiMenu;
import javafx.animation.Animation;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import javafx.scene.input.Clipboard;

import java.io.IOException;

/**
 * The MultiMenuView class is responsible for generating and managing the
 * multi-player menu interface in a JavaFX application. It provides a
 * graphical interface to create or join a multiplayer game, including
 * form input validation, animations, and event handling.
 *
 * This menu consists of two sections: "Create" and "Join". The create
 * section allows users to set up a game server with a specified port,
 * while the join section enables users to connect to an existing server
 * using an IP address and port number.
 *
 * The class also incorporates input validation for port numbers and IP
 * addresses, as well as animations to enhance the user experience.
 */
public class MultiMenuView {
    private StackPane pane;
    private StackPane overlay;
    private StyleMultiMenu style;
    private AnimationMultiMenu animation;

    private HBox multiMenuPane;
    private VBox createSection;
    private VBox joinSection;
    private VBox separatorContainer;
    private HBox portContainer;
    private HBox addressContainer;

    private Label createTitle;
    private Label joinTitle;
    private Label createButton;
    private Label prevAddress;
    private Label joinButton;

    private TextField port;
    private TextField addressIP;
    private TextField addressPort;

    private Runnable callGame;

    /**
     * Constructs an instance of the MultiMenuView class, initializing it with the provided
     * StackPane and setting up the associated styles and animations for the multi-menu interface.
     *
     * @param _pane The StackPane used to contain and display the multi-menu components.
     */
    public MultiMenuView(StackPane _pane) {
        pane = _pane;
        style = new StyleMultiMenu();
        animation = new AnimationMultiMenu();
    }

    /**
     * Configures and initializes the multi-menu interface for user interaction, including
     * create and join sections, input fields, and buttons. This method creates the entire
     * layout, sets styles, bindings, alignment, and event listeners, and incorporates animations.
     *
     * The method includes the following features:
     * - A styled overlay background to display the menu.
     * - Two main sections ("Create" and "Join") for interaction.
     * - Input fields for setting a port number in the "Create" section, and an IP address
     *   and port number in the "Join" section.
     * - Dynamically adjustable spacers and separators for layout organization.
     * - Styled labels and buttons for interaction, enhanced with cursor and hover effects.
     * - Validations for input fields, ensuring entered values meet specified criteria.
     * - Clipboard functionality for copying information from the "Create" section.
     * - Event handling for the "CREATE" and "JOIN" buttons to respond to user actions.
     * - Fade-in and fade-out animations for smooth transitions in the menu display.
     *
     * The method ensures user interaction is intuitive and visually cohesive, while also
     * integrating server-client communication functionality.
     */
    public void generate() {
        overlay = new StackPane();
        overlay.setStyle(style.loadingPaneStyle());
        overlay.prefWidthProperty().bind(pane.widthProperty());
        overlay.prefHeightProperty().bind(pane.heightProperty());
        overlay.setAlignment(Pos.CENTER);

        // main window
        multiMenuPane = new HBox();
        multiMenuPane.setStyle(style.multiMenuPaneStyle());
        multiMenuPane.setPrefSize(750, 500);
        multiMenuPane.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        multiMenuPane.setAlignment(Pos.TOP_CENTER);

        // create section
        createSection = new VBox();
        createSection.setAlignment(Pos.TOP_CENTER);
        createSection.setPrefSize(374, 500);
        createSection.setStyle(style.sectionStyle());
        createSection.setSpacing(10);

        createTitle = new Label("Create");
        createTitle.setFont(Font.loadFont(style.getLilitaOneFont(), 60));
        createTitle.setStyle(style.setTitleStyle());

        // spacer
        Region spacerFromTitleToPort = new Region();
        spacerFromTitleToPort.setPrefHeight(100);

        // port container
        portContainer = new HBox();
        portContainer.setPrefSize(374, 50);
        portContainer.setAlignment(Pos.CENTER);
        portContainer.setSpacing(10);

        port = new TextField();
        port.setPrefWidth(200);
        port.setPrefHeight(50);
        port.setStyle(style.portBoxStyle());
        port.setAlignment(Pos.CENTER);
        port.setPromptText("Port Number");

        portContainer.getChildren().addAll( port);

        prevAddress = new Label();
        prevAddress.setStyle(style.setPreStyle());
        prevAddress.setCursor(Cursor.HAND);

        // spacer
        Region spacerFromPortToBtn = new Region();
        spacerFromPortToBtn.setPrefHeight(150);

        // create button
        createButton = new Label("CREATE");
        createButton.setPrefSize(150, 50);
        createButton.setStyle(style.setButtonStyle());

        createSection.getChildren().addAll(createTitle, spacerFromTitleToPort, portContainer, prevAddress, spacerFromPortToBtn, createButton);

        // separator component
        Region separator = new Region();
        separator.setPrefWidth(2);
        separator.prefHeightProperty().bind(multiMenuPane.heightProperty());
        separator.setStyle(style.setSeparatorStyle());

        separatorContainer = new VBox(separator);
        separatorContainer.setAlignment(Pos.CENTER);
        VBox.setMargin(separator, new Insets(20, 0, 20, 0));

        // join section
        joinSection = new VBox();
        joinSection.setAlignment(Pos.TOP_CENTER);
        joinSection.setPrefSize(374, 500);
        joinSection.setStyle(style.sectionStyle());

        joinTitle = new Label("Join");
        joinTitle.setFont(Font.loadFont(style.getLilitaOneFont(), 60));
        joinTitle.setStyle(style.setTitleStyle());

        // spacer
        Region spacerFromTitleToIP = new Region();
        spacerFromTitleToIP.setPrefHeight(120);

        // port container
        addressContainer = new HBox();
        addressContainer.setPrefSize(374, 50);
        addressContainer.setAlignment(Pos.CENTER);
        addressContainer.setSpacing(10);

        addressIP = new TextField();
        addressIP.setPrefWidth(200);
        addressIP.setPrefHeight(50);
        addressIP.setStyle(style.portBoxStyle());
        addressIP.setAlignment(Pos.CENTER);
        addressIP.setPromptText("IP Address");

        addressPort = new TextField();
        addressPort.setPrefWidth(100);
        addressPort.setPrefHeight(50);
        addressPort.setStyle(style.portBoxStyle());
        addressPort.setAlignment(Pos.CENTER);
        addressPort.setPromptText("Port");

        addressContainer.getChildren().addAll(addressIP, addressPort);

        // spacer
        Region spacerFromAddressToBtn = new Region();
        spacerFromAddressToBtn.setPrefHeight(200);

        joinButton = new Label("JOIN");
        joinButton.setPrefSize(150, 50);
        joinButton.setStyle(style.setButtonStyle());

        joinSection.getChildren().addAll(joinTitle, spacerFromTitleToIP, addressContainer, spacerFromAddressToBtn, joinButton);


        multiMenuPane.getChildren().addAll(createSection, separatorContainer, joinSection);
        overlay.getChildren().add(multiMenuPane);
        pane.getChildren().add(overlay);

        animation.fadeInMultiMenu(overlay);
        animation.mouseInOutMultiMenu(multiMenuPane);

        multiMenuPane.setOnMouseClicked(Event::consume);
        overlay.setOnMouseEntered(e -> overlay.setCursor(Cursor.HAND));
        overlay.setOnMouseClicked(event -> animation.fadeOutMultiMenu(pane, overlay));

        port.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.trim().isEmpty() || !isNumeric(newValue)) {
                prevAddress.setText("");
            } else if (Integer.parseInt(newValue.trim()) < 10000 || Integer.parseInt(newValue.trim()) > 65535) {
                prevAddress.setText("Invalid port number (10000 ~ 65535)");
            } else {
                prevAddress.setText(getLocalAddress() + " : " + newValue);
            }
        });
        prevAddress.setOnMouseClicked(event -> {
            String textToCopy = prevAddress.getText();

            Clipboard clipboard = Clipboard.getSystemClipboard();
            ClipboardContent content = new ClipboardContent();
            content.putString(textToCopy);
            clipboard.setContent(content);
        });
        createButton.setOnMouseEntered(e -> createButton.setCursor(Cursor.HAND));
        createButton.setOnMouseClicked(event ->{
            if(!port.getText().trim().isEmpty() && isNumeric(port.getText().trim()) && Integer.parseInt(port.getText().trim()) <= 65535 && Integer.parseInt(port.getText().trim()) >= 10000){
                System.out.println("Create button clicked");
                Server.start(Integer.parseInt(port.getText().trim()));

                Animation temp = animation.closeMultiMenu(overlay);
                temp.play();
                temp.setOnFinished(ev -> {
                    pane.getChildren().remove(overlay);
                    callGame.run();
                });
            }
        });
        joinButton.setOnMouseEntered(e -> joinButton.setCursor(Cursor.HAND));
        joinButton.setOnMouseClicked(event ->{
            if(!addressIP.getText().trim().isEmpty() && !addressPort.getText().trim().isEmpty() && isNumeric(addressPort.getText().trim()) && Integer.parseInt(addressPort.getText().trim()) <= 65535 && Integer.parseInt(addressPort.getText().trim()) >= 10000){
                System.out.println("Join button clicked");

                int port = Integer.parseInt(addressPort.getText().trim());
                String ip = addressIP.getText().trim();

                Animation temp = animation.closeMultiMenu(overlay);
                temp.play();
                temp.setOnFinished(ev -> {
                    pane.getChildren().remove(overlay);

                    new Thread(()->{
                        try{
                            Client.connect(ip, port);

                            Client.send("-1#JOIN_GAME#NULL");

                            Platform.runLater(()->{
                                callGame.run();
                            });
                        } catch (IOException e){
                            System.out.println("Can not find server at " + ip + ":" + port + " or server is not running.");
                        }
                    }).start();

                });
            }
        });
    }

    /**
     * Retrieves the local IP address of the machine. If the IP address cannot be determined,
     * it returns a default fallback value of "127.0.0.1" (localhost).
     *
     * @return The local IP address as a String, or "127.0.0.1" if the address cannot be resolved.
     */
    private String getLocalAddress() {
        try {
            return java.net.InetAddress.getLocalHost().getHostAddress();
        } catch (Exception e) {
            return "127.0.0.1";
        }
    }

    /**
     * Determines if the provided string represents a numeric value.
     * A string is considered numeric if it contains only digits (0-9).
     *
     * @param str the string to be evaluated
     * @return true if the string is not null, not empty, and contains only digits; false otherwise
     */
    private boolean isNumeric(String str) {
        if (str == null || str.isEmpty()) return false;
        return str.matches("\\d+");
    }

    /**
     * Sets the Runnable that will be executed for a specific action within the multi-menu interface.
     *
     * @param runnable the Runnable to be associated with the action. This allows the user to define
     *                 custom behavior that will be executed when the action is triggered.
     */
    public void setRunnable(Runnable runnable) {
        callGame = runnable;
    }
}
