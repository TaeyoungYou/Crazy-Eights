package app.ui;

import app.animation.AnimationGame;
import app.style.StyleGame;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Screen;

import java.util.ArrayList;
import java.util.Random;

public class SinglePlayGame {
    private final BorderPane pane;
    private final StyleGame style;
    private final AnimationGame animation;
    private final Scene scene;

    private final double screen_width = Screen.getPrimary().getVisualBounds().getWidth();
    private final double screen_height = Screen.getPrimary().getVisualBounds().getHeight();

    private final ArrayList<ImageView> cards = new ArrayList<>();

    public SinglePlayGame(Scene _scene){
        scene = _scene;
        pane = new BorderPane();
        style = new StyleGame();
        animation = new AnimationGame();
    }

    public void generate(){
        initPage();

        // game ground and sidebar
        BorderPane gameGround = new BorderPane();
        VBox sidebar = new VBox();
        sidebar.setPrefSize(350,1080);

        // sidebar config
        sidebarConfig(sidebar);

        // player status area
        VBox gamePlayerStatus = new VBox();
        gamePlayerStatus.setPrefSize(450, 1080);

        gamePlayerStatusConfig(gamePlayerStatus);

        gameGround.setLeft(gamePlayerStatus);

        // my card place area
        AnchorPane cardPlace = new AnchorPane();
        cardPlace.setPrefSize(870,1080);
        cardPlace.setPadding(new Insets(0,0,0,0));

        createCard(cardPlace);

        createDeck(cardPlace);

        gameGround.setCenter(cardPlace);

        myTurnEffect(cardPlace);
        // set pane
        pane.setCenter(gameGround);
        pane.setRight(sidebar);
    }
    private void myTurnEffect(AnchorPane pane){
        Region highlight = new Region();
        highlight.setPrefHeight(10);

        highlight.setStyle(style.myTurnEffectStyle());

        AnchorPane.setBottomAnchor(highlight, 0.0);
        AnchorPane.setLeftAnchor(highlight, 0.0);
        AnchorPane.setRightAnchor(highlight, 0.0);
        highlight.setPrefHeight(20);

        pane.getChildren().add(highlight);
    }

    private void createDeck(AnchorPane deckPlace){
        ImageView deck = new ImageView(new Image(getClass().getResource("/card/Card-Deck.png").toExternalForm()));
        ImageView cardDummy = new ImageView(new Image(getClass().getResource("/card/Card-Empty.png").toExternalForm()));
        deck.setFitWidth(250);
        deck.setPreserveRatio(true);
        cardDummy.setFitWidth(220);
        cardDummy.setPreserveRatio(true);

        deck.setLayoutX(300);
        deck.setLayoutY(200);
        cardDummy.setLayoutX(600);
        cardDummy.setLayoutY(220);

        deckPlace.getChildren().add(deck);
        deckPlace.getChildren().add(cardDummy);
    }

    private void createCard(AnchorPane cardPlace){
        Random random = new Random();
        for(int i=0; i<12; ++i){
            int randomCard = random.nextInt(52) + 1;
            String url = String.format("/card/Card-%d.png",randomCard);
            ImageView card = new ImageView(new Image(getClass().getResource(url).toExternalForm()));
            card.setFitWidth(220);
            card.setPreserveRatio(true);
            cards.add(card);

            animation.cardAnimation(card, cardPlace, cards);
            cardPlace.getChildren().add(card);
        }
        resettingPosCard(cardPlace);
    }

    private void resettingPosCard(AnchorPane cardPlace){
        int i = 0;

        for(Node node: cardPlace.getChildren()){
            if(node instanceof ImageView && cards.contains((ImageView)node)){
                ImageView card = (ImageView)node;
                card.setLayoutX( i * 75);
                card.setLayoutY(1080-200);
                i++;
            }
        }
    }

    private void gamePlayerStatusConfig(VBox gamePlayerStatus){
        // Player area
        HBox playerPlace01 = new HBox();
        HBox playerPlace02 = new HBox();
        HBox playerPlace03 = new HBox();

        createPlayerStatus(playerPlace01, "/avatar/User-01.png", 5);
        createPlayerStatus(playerPlace02, "/avatar/User-03.png", 10);
        createPlayerStatus(playerPlace03, "/avatar/User-05.png", 7);

        Region spacer = new Region();
        spacer.setPrefHeight(20);
        HBox scoreTimeContainer = new HBox();

        scoreTimeContainerConfig(scoreTimeContainer);

        gamePlayerStatus.getChildren().addAll(playerPlace01, playerPlace02, playerPlace03, spacer, scoreTimeContainer);

        gamePlayerStatus.setAlignment(Pos.TOP_LEFT);
        gamePlayerStatus.setSpacing(30);
        gamePlayerStatus.setPadding(new Insets(30,30,30,30));
    }

    private void scoreTimeContainerConfig(HBox scoreTimeContainer){
        VBox scoreContainer = new VBox();
        scoreContainer.setSpacing(10);
        scoreContainer.setAlignment(Pos.CENTER);

        Label score = new Label("Score");
        score.setFont(Font.loadFont(style.getLilitaOneFont(), 40));
        score.setStyle(style.sideLabelStyle());


        VBox scoreBox = new VBox();
        scoreBox.setStyle(style.statusScoreBoxStyle());
        scoreBox.setPrefSize(250, 300);
        scoreBox.setAlignment(Pos.CENTER);
        scoreBox.setPadding(new Insets(10,10,10,10));
        scoreBox.setSpacing(10);
        HBox me = new HBox();
        HBox player01 = new HBox();
        HBox player02 = new HBox();
        HBox player03 = new HBox();

        createScorePlayer(me, "/avatar/User-02.png", 12);
        createScorePlayer(player01, "/avatar/User-01.png", 20);
        createScorePlayer(player02, "/avatar/User-03.png", 5);
        createScorePlayer(player03, "/avatar/User-05.png", 15);

        scoreBox.getChildren().addAll(player01, player03, me, player02);

        scoreContainer.getChildren().addAll(score, scoreBox);


        Label timer = new Label("10");
        timer.setFont(Font.loadFont(style.getLilitaOneFont(), 80));
        timer.setStyle(style.sideLabelStyle());

        scoreTimeContainer.getChildren().addAll(scoreContainer, timer);
        scoreTimeContainer.setAlignment(Pos.CENTER_LEFT);
        scoreTimeContainer.setSpacing(55);
        scoreTimeContainer.setPadding(new Insets(0,0,0,0));
    }
    private void createScorePlayer(HBox scoreBox, String imageURL, int score){
        ImageView player = new ImageView(new Image(getClass().getResource(imageURL).toExternalForm()));
        player.setFitWidth(70);
        player.setFitHeight(70);

        Label scoreLbl = new Label(String.valueOf(score));
        scoreLbl.setFont(Font.loadFont(style.getLilitaOneFont(), 40));
        scoreLbl.setStyle(style.sideLabelStyle());
        scoreBox.getChildren().addAll(player, scoreLbl);
        scoreBox.setAlignment(Pos.CENTER);
        scoreBox.setSpacing(60);
    }
    private void createPlayerStatus(HBox playerPlace, String avatarURL, int leftCard){
        ImageView player = new ImageView(new Image(getClass().getResource(avatarURL).toExternalForm()));
        player.setFitWidth(150);
        player.setFitHeight(150);

        playerPlace.getChildren().add(player);

        ImageView cardBack = new ImageView(new Image(getClass().getResource("/card/Card-0.png").toExternalForm()));
        cardBack.setFitWidth(150);
        cardBack.setFitHeight(200);

        playerPlace.getChildren().add(cardBack);

        Label cardLeft = new Label(String.format("x %d", leftCard));
        cardLeft.setFont(Font.loadFont(style.getLilitaOneFont(), 40));
        cardLeft.setStyle(style.sideLabelStyle());

        playerPlace.setAlignment(Pos.CENTER);
        playerPlace.getChildren().add(cardLeft);
    }
    private void sidebarConfig(VBox sidebar){
        sidebar.setPadding(new Insets(40,20,20,20));
        sidebar.setAlignment(Pos.BOTTOM_CENTER);
        sidebar.setPrefWidth(384);
        sidebar.setSpacing(10);
        sidebar.setStyle(style.gameSidePaneStyle());

        HBox buttonBar = new HBox();
        buttonBar.setSpacing(40);
        buttonBar.setAlignment(Pos.CENTER);
        ImageView volumeOn = new ImageView(new Image(getClass().getResource("/button/volume-on.png").toExternalForm()));
        ImageView setting = new ImageView(new Image(getClass().getResource("/button/settings.png").toExternalForm()));
        ImageView back = new ImageView(new Image(getClass().getResource("/button/back.png").toExternalForm()));

        animation.backAnimation(back);
        buttonBar.getChildren().addAll(volumeOn, setting, back);

        sidebar.getChildren().add(buttonBar);

        Region spacer = new Region();
        spacer.setPrefHeight(40);

        sidebar.getChildren().add(spacer);

        ScrollPane scroll = new ScrollPane();
        scroll.setStyle(style.sideScrollPane());
        VBox chats = new VBox();
        chats.setPrefWidth(340);
        chats.setPrefHeight(950);
        chats.setStyle(style.sideChatBox());

        scroll.setContent(chats);
        scroll.setFitToHeight(true);
        scroll.setFitToWidth(true);
        sidebar.getChildren().add(scroll);

        TextField message = new TextField();
        message.setPrefWidth(300);
        message.setPrefHeight(50);
        message.setStyle(style.sideMessageBox());

        sidebar.getChildren().add(message);

        back.setOnMouseClicked(e -> {
            MainMenu menu = new MainMenu(scene);
            menu.generate();
        });
    }
    private void initPage(){
        scene.setRoot(pane);
        pane.setStyle(style.gameBorderPaneStyle());
    }
}
