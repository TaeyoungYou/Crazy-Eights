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

    public MultiMenuView(StackPane _pane) {
        pane = _pane;
        style = new StyleMultiMenu();
        animation = new AnimationMultiMenu();
    }

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

    private String getLocalAddress() {
        try {
            return java.net.InetAddress.getLocalHost().getHostAddress();
        } catch (Exception e) {
            return "127.0.0.1";
        }
    }

    private boolean isNumeric(String str) {
        if (str == null || str.isEmpty()) return false;
        return str.matches("\\d+");
    }

    public void setRunnable(Runnable runnable) {
        callGame = runnable;
    }
}
