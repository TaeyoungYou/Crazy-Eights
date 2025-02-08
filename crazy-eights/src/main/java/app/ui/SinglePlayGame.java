package app.ui;

import app.animation.AnimationGame;
import app.style.StyleGame;
import javafx.animation.Animation;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
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

import java.util.ArrayList;
import java.util.Random;

/**
 * Represents the single-player game screen for Crazy Eights.
 * Handles UI components and game state management.
 */
public class SinglePlayGame {
    private final StackPane root;
    private final BorderPane pane;
    private final StyleGame style;
    private final AnimationGame animation;
    private final Scene scene;

    private final ObservableList<ImageView> cards = FXCollections.observableArrayList();

    private ImageView cardDummy = null;

    /**
     * Constructs a SinglePlayGame instance.
     *
     * @param _scene The scene where the single-player game is displayed.
     */
    public SinglePlayGame(Scene _scene) {
        scene = _scene;
        root = new StackPane();
        pane = new BorderPane();
        style = new StyleGame();
        animation = new AnimationGame();
    }

    /**
     * Generates and sets up the game layout.
     */
    public void generate() {
        initPage();

        // game ground and sidebar
        BorderPane gameGround = new BorderPane();
        VBox sidebar = new VBox();
        sidebar.setPrefSize(385, 1080);

        // sidebar config
        sidebarConfig(sidebar);

        // player status area
        VBox gamePlayerStatus = new VBox();
        gamePlayerStatus.setPrefSize(450, 1080);

        gamePlayerStatusConfig(gamePlayerStatus);

        gameGround.setLeft(gamePlayerStatus);

        // my card place area
        AnchorPane cardPlace = new AnchorPane();
        cardPlace.setPrefSize(870, 1080);
        cardPlace.setPadding(new Insets(0, 0, 0, 0));

        createCard(cardPlace, 6);

        createDeck(cardPlace);

        gameGround.setCenter(cardPlace);

        //myTurnEffect(cardPlace);
        // set pane
        pane.setCenter(gameGround);
        pane.setRight(sidebar);

        observerListen();

        animation.fadeInSinglePlay(pane);
    }

    /**
     * Highlights the UI to indicate the player's turn.
     *
     * @param pane The AnchorPane where the effect will be applied.
     */
    private void myTurnEffect(AnchorPane pane) {
        Region highlight = new Region();
        highlight.setPrefHeight(10);

        highlight.setStyle(style.myTurnEffectStyle());

        AnchorPane.setBottomAnchor(highlight, 0.0);
        AnchorPane.setLeftAnchor(highlight, 0.0);
        AnchorPane.setRightAnchor(highlight, 0.0);
        highlight.setPrefHeight(20);

        pane.getChildren().add(highlight);
    }

    /**
     * Creates and displays the deck and a placeholder for the current card.
     * Adds hover animation and click event to draw a new card.
     *
     * @param deckPlace The AnchorPane where the deck and placeholder card are displayed.
     */
    private void createDeck(AnchorPane deckPlace) {
        ImageView deck = new ImageView(new Image(getClass().getResource("/card/Card-Deck.png").toExternalForm()));
        cardDummy = new ImageView(new Image(getClass().getResource("/card/Card-Empty.png").toExternalForm()));
        deck.setFitWidth(250);
        deck.setPreserveRatio(true);
        cardDummy.setFitWidth(220);
        cardDummy.setPreserveRatio(true);

        deck.setLayoutX(300);
        deck.setLayoutY(180);
        cardDummy.setLayoutX(600);
        cardDummy.setLayoutY(200);

        deckPlace.getChildren().add(deck);
        deckPlace.getChildren().add(cardDummy);

        animation.deckHoverAnimation(deck);
        deck.setOnMouseClicked(event -> {
            if(cards.size() < 12){
                ImageView animatedCard = animation.getCardAnimation(deckPlace, cards);
                Animation cardAnimation = animation.getCardTranslateAnimation(animatedCard, cards);
                cardAnimation.setOnFinished(e->{
                    deckPlace.getChildren().remove(animatedCard);
                    createCard(deckPlace, 1);
                });
                cardAnimation.play();
            }
        });
    }

    /**
     * Generates and adds a specified number of random playing cards to the game UI.
     * Ensures the total number of cards does not exceed the limit (12).
     * Applies animations and updates the card positions accordingly.
     *
     * @param cardPlace The AnchorPane where the cards will be displayed.
     * @param total     The number of cards to generate.
     */
    private void createCard(AnchorPane cardPlace, int total) {
        if (cards.size() + total <= 12) {
            Random random = new Random();
            for (int i = 0; i < total; ++i) {
                int randomCard = random.nextInt(52) + 1;
                String url = String.format("/card/Card-%d.png", randomCard);
                ImageView card = new ImageView(new Image(getClass().getResource(url).toExternalForm()));
                card.setFitWidth(220);
                card.setPreserveRatio(true);
                cards.add(card);

                animation.cardAnimation(card, cardPlace, cards);
                cardPlace.getChildren().add(card);
            }
            resettingPosCard(cardPlace);
        }
    }

    /**
     * Adjusts the positions of the player's cards in a visually organized manner.
     * Ensures that cards are spaced evenly for better readability.
     *
     * @param cardPlace The AnchorPane containing the player's cards.
     */
    private void resettingPosCard(AnchorPane cardPlace) {
        int i = 0;

        for (Node node : cardPlace.getChildren()) {
            if (node instanceof ImageView && cards.contains((ImageView) node)) {
                ImageView card = (ImageView) node;
                card.setLayoutX(i * 75);
                card.setLayoutY(1080 - 300);
                i++;
            }
        }
    }

    /**
     * Configures the game player status area, displaying avatars and remaining card counts.
     * Also sets up the score and timer container for the players.
     *
     * @param gamePlayerStatus The VBox where the player status components will be added.
     */
    private void gamePlayerStatusConfig(VBox gamePlayerStatus) {
        // Player area
        HBox playerPlace01 = new HBox();
        HBox playerPlace02 = new HBox();
        HBox playerPlace03 = new HBox();

        createPlayerStatus(playerPlace01, "/avatar/User-01.png", 5);
        createPlayerStatus(playerPlace02, "/avatar/User-03.png", 10);
        createPlayerStatus(playerPlace03, "/avatar/User-05.png", 7);

        HBox scoreTimeContainer = new HBox();

        scoreTimeContainerConfig(scoreTimeContainer);

        gamePlayerStatus.getChildren().addAll(playerPlace01, playerPlace02, playerPlace03, scoreTimeContainer);

        gamePlayerStatus.setAlignment(Pos.TOP_LEFT);
        gamePlayerStatus.setSpacing(10);
        gamePlayerStatus.setPadding(new Insets(30, 30, 30, 30));
    }

    /**
     * Configures the score and timer container.
     * Displays the players' scores in a list and includes a countdown timer.
     *
     * @param scoreTimeContainer The HBox where the score and timer elements will be added.
     */
    private void scoreTimeContainerConfig(HBox scoreTimeContainer) {
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
        scoreBox.setPadding(new Insets(10, 10, 10, 10));
        scoreBox.setSpacing(10);
        HBox me = new HBox();
        HBox player01 = new HBox();
        HBox player02 = new HBox();
        HBox player03 = new HBox();

        createScorePlayer(me, "/avatar/User-02.png", 15);
        createScorePlayer(player01, "/avatar/User-01.png", 20);
        createScorePlayer(player02, "/avatar/User-03.png", 5);
        createScorePlayer(player03, "/avatar/User-05.png", 12);

        scoreBox.getChildren().addAll(player01, me, player03, player02);

        scoreContainer.getChildren().addAll(score, scoreBox);

        Label timer = new Label("10");
        timer.setFont(Font.loadFont(style.getLilitaOneFont(), 80));
        timer.setStyle(style.sideLabelStyle());

        scoreTimeContainer.getChildren().addAll(scoreContainer, timer);
        scoreTimeContainer.setAlignment(Pos.CENTER_LEFT);
        scoreTimeContainer.setSpacing(55);
        scoreTimeContainer.setPadding(new Insets(0, 0, 0, 0));
    }

    /**
     * Creates and configures a player's score display with an avatar and score label.
     *
     * @param scoreBox The HBox where the player's score will be displayed.
     * @param imageURL The file path to the player's avatar image.
     * @param score    The player's current score.
     */
    private void createScorePlayer(HBox scoreBox, String imageURL, int score) {
        ImageView player = new ImageView(new Image(getClass().getResource(imageURL).toExternalForm()));
        player.setFitWidth(70);
        player.setFitHeight(70);

        Label scoreLbl = new Label(String.valueOf(score));
        scoreLbl.setFont(Font.loadFont(style.getLilitaOneFont(), 40));
        scoreLbl.setStyle(style.sideLabelStyle());
        scoreBox.getChildren().addAll(player, scoreLbl);
        scoreBox.setAlignment(Pos.CENTER_LEFT);
        scoreBox.setSpacing(60);
    }

    /**
     * Creates and configures a player's status display with an avatar, card back, and remaining card count.
     *
     * @param playerPlace The HBox where the player's status will be displayed.
     * @param avatarURL   The file path to the player's avatar image.
     * @param leftCard    The number of cards the player has left.
     */
    private void createPlayerStatus(HBox playerPlace, String avatarURL, int leftCard) {
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

        playerPlace.setAlignment(Pos.CENTER_LEFT);
        playerPlace.getChildren().add(cardLeft);
    }

    /**
     * Configures the sidebar with buttons, chat area, and a message input field.
     * Includes interactive elements such as volume control, settings, and back button.
     *
     * @param sidebar The VBox where sidebar components will be added.
     */
    private void sidebarConfig(VBox sidebar) {
        sidebar.setPadding(new Insets(50, 20, 20, 20));
        sidebar.setAlignment(Pos.TOP_CENTER);
        sidebar.setSpacing(10);
        sidebar.setStyle(style.gameSidePaneStyle());

        HBox buttonBar = new HBox();
        buttonBar.setSpacing(40);
        buttonBar.setAlignment(Pos.CENTER);
        ImageView volume = new ImageView(new Image(getClass().getResource("/button/volume-on.png").toExternalForm()));
        ImageView setting = new ImageView(new Image(getClass().getResource("/button/settings.png").toExternalForm()));
        ImageView back = new ImageView(new Image(getClass().getResource("/button/back.png").toExternalForm()));

        animation.buttonAnimation(setting);
        animation.buttonAnimation(volume);
        animation.buttonAnimation(back);
        buttonBar.getChildren().addAll(volume, setting, back);

        sidebar.getChildren().add(buttonBar);

        Region spacer = new Region();
        spacer.setPrefHeight(40);

        sidebar.getChildren().add(spacer);

        ScrollPane logScroll = new ScrollPane();
        logScroll.setStyle(style.sideScrollPane());
        VBox log =new VBox();
        log.setPrefWidth(340);
        log.setPrefHeight(236);
        log.setStyle(style.sideChatBox());
        logScroll.setContent(log);
        logScroll.setFitToHeight(true);
        logScroll.setFitToWidth(true);
        sidebar.getChildren().add(logScroll);


        ScrollPane msgScroll = new ScrollPane();
        msgScroll.setStyle(style.sideScrollPane());
        VBox chats = new VBox();
        chats.setPrefWidth(340);
        chats.setPrefHeight(574);
        chats.setStyle(style.sideChatBox());

        msgScroll.setContent(chats);
        msgScroll.setFitToHeight(true);
        msgScroll.setFitToWidth(true);
        sidebar.getChildren().add(msgScroll);

        TextField message = new TextField();
        message.setPrefWidth(300);
        message.setPrefHeight(50);
        message.setStyle(style.sideMessageBox());

        sidebar.getChildren().add(message);

        back.setOnMouseClicked(e -> {
            animation.fadeOutSinglePlay(scene, pane);
        });

        Setting settingPane = new Setting(root);
        setting.setOnMouseClicked(e-> {
            settingPane.generate();
        });
        volume.setOnMouseClicked(e -> {
            if (Music.isVolumeOn()) {
                volume.setImage(new Image(getClass().getResource("/button/volume-off.png").toExternalForm()));
                Music.volumeOff();
            } else {
                volume.setImage(new Image(getClass().getResource("/button/volume-on.png").toExternalForm()));
                Music.volumeOn();
            }
        });
    }

    /**
     * Initializes the game page by setting the root layout and applying styles.
     */
    private void initPage() {
        root.getChildren().add(pane);
        scene.setRoot(root);
        pane.setStyle(style.gameBorderPaneStyle());
    }

    private void observerListen(){
        cards.addListener((ListChangeListener<? super ImageView>) change -> {
            while(change.next()){
                if(change.wasRemoved()){
                    cardDummy.setImage(change.getRemoved().get(0).getImage());
                }
            }
        });
    }
}
