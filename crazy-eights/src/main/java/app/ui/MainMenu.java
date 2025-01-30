package app.ui;

import app.animation.AnimationMenu;
import app.style.StyleMenu;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class MainMenu {
    private final BorderPane pane;
    private final StyleMenu style;
    private final AnimationMenu animation;
    private final Scene scene;

    public MainMenu(Scene _scene) {
        scene = _scene;
        pane = new BorderPane();
        style = new StyleMenu();
        animation = new AnimationMenu();
    }

    public void generate(){
        scene.setRoot(pane);
        // pane 스타일 지정
        pane.setStyle(style.loadingBorderPaneStyle());

        // title 생성 및 스타일 지정
        Label title = new Label();
        title.setText("Crazy Eights");
        title.setFont(Font.loadFont(style.getLilitaOneFont(), 180));
        title.setStyle(style.loadingTitleStyle());

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

        menuPane.setStyle(style.loadingBoxStyle());
        bottomPane.setStyle(style.loadingBoxStyle());

        Node[] nodes = {singlePlay, multiPlay, bottomPane};

        // 컴포넌트 위치 설정
        pane.setCenter(menuPane);

        // 애니메이션 적용
        animation.menuAnimation(menuPane, nodes);
        animation.menuHover(nodes);
        animation.titleHover(title);

        singlePlay.setOnMouseClicked(event -> {
            SinglePlayGame game = new SinglePlayGame(scene);
            game.generate();
        });
    }
    private void configStage(Stage primaryStage) {
        primaryStage.setTitle("Crazy Eights");
        primaryStage.setResizable(false);
        primaryStage.setFullScreen(true);
        primaryStage.setFullScreenExitHint("");
        primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
    }
}
