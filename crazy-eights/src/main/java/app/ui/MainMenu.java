package app.ui;

import app.animation.AnimationMenu;
import app.style.StyleMenu;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class MainMenu {
    private final BorderPane pane;
    private final StyleMenu style;
    private final AnimationMenu animation;
    private final Scene scene;

    private static boolean started = false;

    public MainMenu(Scene _scene) {
        scene = _scene;
        pane = new BorderPane();
        style = new StyleMenu();
        animation = new AnimationMenu();
    }

    public void generate(){
        initPage();

        if(!started){
            startGameMenu();
            started = true;
        } else{
            gameMenu();
        }
    }
    private void gameMenu(){
        Label title = createTitle();

        // button 생성 및 스타일 지정
        Label singlePlay = new Label();
        Label multiPlay = new Label();
        Label setting = new Label();
        Label quit = new Label();

        singlePlay.setText("SinglePlayer");
        multiPlay.setText("MultiPlayer");
        setting.setText("Setting");
        quit.setText("Quit");

        singlePlay.setStyle(style.loadingButtonCommonStyle() + style.loadingButtonLargeStyle());
        multiPlay.setStyle(style.loadingButtonCommonStyle() + style.loadingButtonLargeStyle());
        setting.setStyle(style.loadingButtonCommonStyle() + style.loadingButtonSmallStyle());
        quit.setStyle(style.loadingButtonCommonStyle() + style.loadingButtonSmallStyle());

        VBox menuPane = new VBox();
        menuPane.setAlignment(Pos.CENTER);
        HBox bottomPane = new HBox(setting, quit);
        bottomPane.setAlignment(Pos.CENTER);

        Region space = new Region();
        space.setPrefHeight(10);

        menuPane.setStyle(style.loadingBoxStyle());
        bottomPane.setStyle(style.loadingBoxStyle());

        menuPane.getChildren().addAll(title, space, singlePlay, multiPlay, bottomPane);
        pane.setCenter(menuPane);

        Label[] tmp = {singlePlay, multiPlay, setting, quit};
        animation.menuAnimation(title, tmp);

        singlePlay.setOnMouseClicked(event -> {
            SinglePlayGame game = new SinglePlayGame(scene);
            game.generate();
        });
        quit.setOnMouseClicked(event -> {
            Platform.exit();;
        });
    }

    private void startGameMenu() {
        Label title = createTitle();

        // button 생성 및 스타일 지정
        Label singlePlay = new Label();
        Label multiPlay = new Label();
        Label setting = new Label();
        Label quit = new Label();

        singlePlay.setText("SinglePlayer");
        multiPlay.setText("MultiPlayer");
        setting.setText("Setting");
        quit.setText("Quit");

        singlePlay.setStyle(style.loadingButtonCommonStyle() + style.loadingButtonLargeStyle());
        multiPlay.setStyle(style.loadingButtonCommonStyle() + style.loadingButtonLargeStyle());
        setting.setStyle(style.loadingButtonCommonStyle() + style.loadingButtonSmallStyle());
        quit.setStyle(style.loadingButtonCommonStyle() + style.loadingButtonSmallStyle());

        // Menu 생성 및 정의
        VBox menuPane = new VBox(title);
        menuPane.setAlignment(Pos.CENTER);
        HBox bottomPane = new HBox(setting, quit);
        bottomPane.setAlignment(Pos.CENTER);

        Region space = new Region();
        space.setPrefHeight(10);
        menuPane.getChildren().add(space);

        menuPane.setStyle(style.loadingBoxStyle());
        bottomPane.setStyle(style.loadingBoxStyle());

        Node[] nodes = {singlePlay, multiPlay, bottomPane};

        // 컴포넌트 위치 설정
        pane.setCenter(menuPane);

        // 애니메이션 적용
        animation.startMenuAnimation(menuPane, nodes, title);
        animation.menuHover(nodes);

        singlePlay.setOnMouseClicked(event -> {
            SinglePlayGame game = new SinglePlayGame(scene);
            game.generate();
        });
        quit.setOnMouseClicked(event -> {
            Platform.exit();;
        });
    }

    private void initPage(){
        scene.setRoot(pane);
        pane.setStyle(style.loadingBorderPaneStyle());
    }
    private Label createTitle(){
        // title 생성 및 스타일 지정
        Label title = new Label();
        title.setText("Crazy Eights");
        title.setFont(Font.loadFont(style.getLilitaOneFont(), 180));
        title.setStyle(style.loadingTitleStyle());
        return title;
    }
}
