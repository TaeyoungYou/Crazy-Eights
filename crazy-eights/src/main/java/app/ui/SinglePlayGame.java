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

import java.util.Random;

public class SinglePlayGame {
    private final BorderPane pane;
    private final StyleGame style;
    private final AnimationGame animation;
    private final Scene scene;

    public SinglePlayGame(Scene _scene){
        scene = _scene;
        pane = new BorderPane();
        style = new StyleGame();
        animation = new AnimationGame();
    }

    public void generate(){
        scene.setRoot(pane);

        pane.setStyle(style.gameBorderPaneStyle());

        BorderPane gameGround = new BorderPane();
        VBox sidebar = new VBox();

        sidebarConfig(sidebar);

        VBox gamePlayerStatus = new VBox();

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

        StackPane cardPlace = new StackPane();
        cardPlace.setPrefHeight(500);

        Random random = new Random();
        for(int i=0; i< 12; ++i){
            int randomCard = random.nextInt(52) + 1;
            String url = String.format("/card/Card-%d.png",randomCard);
            ImageView card = new ImageView(new Image(getClass().getResource(url).toExternalForm()));
            card.setFitWidth(220);
            card.setPreserveRatio(true);
            cardPlace.getChildren().add(card);
            resettingPosCard(cardPlace);
        }

        cardPlace.setAlignment(Pos.CENTER);
        gameGround.setBottom(cardPlace);

        HBox deckPlace = new HBox();
        deckPlace.setAlignment(Pos.CENTER);
        deckPlace.setSpacing(50);

        ImageView deck = new ImageView(new Image(getClass().getResource("/card/Card-Deck.png").toExternalForm()));
        ImageView cardDummy = new ImageView(new Image(getClass().getResource("/card/Card-Empty.png").toExternalForm()));
        deck.setFitWidth(250);
        deck.setPreserveRatio(true);
        cardDummy.setFitWidth(220);
        cardDummy.setPreserveRatio(true);

        deckPlace.getChildren().addAll(deck, cardDummy);

        gameGround.setCenter(deckPlace);

        pane.setCenter(gameGround);
        pane.setRight(sidebar);
    }
    private void resettingPosCard(StackPane cardPlace){
        int total = cardPlace.getChildren().size();
        int mid = total / 2;
        for(int i=0; i< total; ++i){
            cardPlace.getChildren().get(i).setTranslateX((i-mid) * 70);
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
