package app.view.multi;


import app.animation.AnimationMultiMenu;
import app.style.StyleMultiMenu;
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

    private Label createTitle;
    private Label joinTitle;
    private Label createPort;
    private Label createButton;
    private Label prevAddress;

    private TextField port;

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

        createPort = new Label("Port");
        createPort.setFont(Font.loadFont(style.getLilitaOneFont(), 30));
        createPort.setStyle(style.setLabelStyle());

        port = new TextField();
        port.setPrefWidth(100);
        port.setPrefHeight(50);
        port.setStyle(style.portBoxStyle());

        portContainer.getChildren().addAll(createPort, port);

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

        joinSection.getChildren().add(joinTitle);


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
                prevAddress.setText(getLcoalAddress() + " : " + newValue);
            }
        });
        prevAddress.setOnMouseClicked(event -> {
            String textToCopy = prevAddress.getText();

            Clipboard clipboard = Clipboard.getSystemClipboard();
            ClipboardContent content = new ClipboardContent();
            content.putString(textToCopy);
            clipboard.setContent(content);
        });
    }

    private String getLcoalAddress() {
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

}
