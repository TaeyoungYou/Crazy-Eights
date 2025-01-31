package app.ui;

import app.animation.AnimationGame;
import app.style.StyleGame;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
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
        // Scene default config
        scene.setRoot(pane);

        pane.setStyle(style.gameBorderPaneStyle());

        // game ground and sidebar
        BorderPane gameGround = new BorderPane();
        VBox sidebar = new VBox();
        sidebar.setPrefSize(350,1080);

        // sidebar config
        sidebarConfig(sidebar);

        // player status area
        VBox gamePlayerStatus = new VBox();
        gamePlayerStatus.setPrefSize(450, 1080);

        HBox playerPlace01 = new HBox();
        HBox playerPlace02 = new HBox();
        HBox playerPlace03 = new HBox();

        player01Config(playerPlace01);
        player02Config(playerPlace02);
        player03Config(playerPlace03);

        gamePlayerStatus.getChildren().addAll(playerPlace01, playerPlace02, playerPlace03);

        gamePlayerStatus.setAlignment(Pos.TOP_LEFT);
        gamePlayerStatus.setSpacing(30);
        gamePlayerStatus.setPadding(new Insets(30,30,30,30));

        gameGround.setLeft(gamePlayerStatus);

        // my card place area
        AnchorPane cardPlace = new AnchorPane();
        cardPlace.setPrefSize(870,1080);
        cardPlace.setPadding(new Insets(0,0,0,0));

        createCard(cardPlace);

        createDeck(cardPlace);

        gameGround.setCenter(cardPlace);

        // set pane
        pane.setCenter(gameGround);
        pane.setRight(sidebar);
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

            animation.cardAnimation(card, cardPlace);
            cardPlace.getChildren().add(card);
        }
        resettingPosCard(cardPlace);
    }

    private void resettingPosCard(AnchorPane cardPlace){
        int total = cardPlace.getChildren().size();
        for(int i=0; i<total; ++i){
            ImageView card = (ImageView) cardPlace.getChildren().get(i);
            card.setLayoutX( i * 75);
            card.setLayoutY(1080-200);
        }
    }

    private void player01Config(HBox playerPlace){
        ImageView player = new ImageView();
        player.setImage(new Image(getClass().getResource("/avatar/User-01.png").toExternalForm()));
        player.setFitWidth(150);
        player.setFitHeight(150);

        playerPlace.getChildren().add(player);

        ImageView cardBack = new ImageView();
        cardBack.setImage(new Image(getClass().getResource("/card/Card-0.png").toExternalForm()));
        cardBack.setFitHeight(200);
        cardBack.setFitWidth(151);

        playerPlace.getChildren().add(cardBack);

        Label cardLeft = new Label("x 5");
        cardLeft.setFont(Font.loadFont(style.getLilitaOneFont(), 40));
        cardLeft.setStyle(style.sideLabelStyle());

        playerPlace.setAlignment(Pos.CENTER_LEFT);
        playerPlace.getChildren().add(cardLeft);
    }
    private void player02Config(HBox playerPlace){
        ImageView player = new ImageView();
        player.setImage(new Image(getClass().getResource("/avatar/User-03.png").toExternalForm()));
        player.setFitWidth(150);
        player.setFitHeight(150);

        playerPlace.getChildren().add(player);

        ImageView cardBack = new ImageView();
        cardBack.setImage(new Image(getClass().getResource("/card/Card-0.png").toExternalForm()));
        cardBack.setFitHeight(200);
        cardBack.setFitWidth(151);

        playerPlace.getChildren().add(cardBack);

        Label cardLeft = new Label("x 10");
        cardLeft.setFont(Font.loadFont(style.getLilitaOneFont(), 40));
        cardLeft.setStyle(style.sideLabelStyle());

        playerPlace.setAlignment(Pos.CENTER_LEFT);
        playerPlace.getChildren().add(cardLeft);
    }
    private void player03Config(HBox playerPlace){
        ImageView player = new ImageView();
        player.setImage(new Image(getClass().getResource("/avatar/User-05.png").toExternalForm()));
        player.setFitWidth(150);
        player.setFitHeight(150);

        playerPlace.getChildren().add(player);

        ImageView cardBack = new ImageView();
        cardBack.setImage(new Image(getClass().getResource("/card/Card-0.png").toExternalForm()));
        cardBack.setFitHeight(200);
        cardBack.setFitWidth(151);

        playerPlace.getChildren().add(cardBack);

        Label cardLeft = new Label("x 7");
        cardLeft.setFont(Font.loadFont(style.getLilitaOneFont(), 40));
        cardLeft.setStyle(style.sideLabelStyle());

        playerPlace.setAlignment(Pos.CENTER_LEFT);
        playerPlace.getChildren().add(cardLeft);
    }
    private void sidebarConfig(VBox sidebar){
        sidebar.setPadding(new Insets(80,20,20,20));
        sidebar.setAlignment(Pos.TOP_CENTER);
        sidebar.setPrefWidth(384);
        sidebar.setSpacing(10);
        sidebar.setStyle(style.gameSidePaneStyle());

        Label score = new Label("Score: 0");
        Label timer = new Label("Time: 0");
        score.setFont(Font.loadFont(style.getLilitaOneFont(), 40));
        timer.setFont(Font.loadFont(style.getLilitaOneFont(), 40));
        score.setStyle(style.sideLabelStyle());
        timer.setStyle(style.sideLabelStyle());

        sidebar.getChildren().addAll(score, timer);

        Region spacer = new Region();
        spacer.setPrefHeight(70);

        sidebar.getChildren().add(spacer);

        ScrollPane scroll = new ScrollPane();
        scroll.setStyle(style.sideScrollPane());
        VBox chats = new VBox();
        chats.setPrefWidth(340);
        chats.setPrefHeight(840);
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
    }
}
