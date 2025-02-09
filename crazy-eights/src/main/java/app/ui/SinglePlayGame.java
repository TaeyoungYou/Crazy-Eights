package app.ui;

import app.animation.AnimationGame;
import app.model.Music;
import app.model.Player;
import app.style.StyleGame;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
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
import javafx.util.Duration;

import java.util.*;

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
    private List<Player> players = new ArrayList<>();

    private AnchorPane cardPlace;

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
        cardPlace = new AnchorPane();
        cardPlace.setPrefSize(870, 1080);
        cardPlace.setPadding(new Insets(0, 0, 0, 0));

        createDeck();

        gameGround.setCenter(cardPlace);

        //myTurnEffect(cardPlace);
        // set pane
        pane.setCenter(gameGround);
        pane.setRight(sidebar);

        // Game Start
        start();

        observerListen();

        animation.fadeInSinglePlay(pane);
    }

    // game logic
    public void start(){
        shareCards();

    }

    private void shareCards(){
        Timeline giveCard = new Timeline(
                new KeyFrame(Duration.seconds(1), event -> {
                    ImageView card = animation.getCardAnimation(cardPlace);
                    Animation animationCard = animation.getCardTranslateToPlayersAnimation(card, cards);
                    animationCard.setOnFinished(e->{
                        cardPlace.getChildren().remove(card);
                        for(Player player: players) {
                            player.setCardLeft(player.getCardLeftValue() + 1);
                            animation.addLeftCardAnimation(player.getCardLeft());
                        }
                    });
                    animationCard.play();
                })
        );
        giveCard.setCycleCount(6);
        giveCard.play();
        Timeline initCard = new Timeline(
                new KeyFrame(Duration.seconds(1), event->{
                    ImageView card = animation.getCardAnimation(cardPlace);
                    Animation animationCard = animation.getCardTranslateAnimation(card, cards);
                    animationCard.setOnFinished(e->{
                        cardPlace.getChildren().remove(card);
                        createCard(1);
                    });
                    animationCard.play();
                })
        );
        initCard.setCycleCount(6);
        initCard.play();
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
     */
    private void createDeck() {
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

        cardPlace.getChildren().add(deck);
        cardPlace.getChildren().add(cardDummy);

        animation.deckHoverAnimation(deck);
        deck.setOnMouseClicked(event -> {
            if(cards.size() < 12){
                ImageView animatedCard = animation.getCardAnimation(cardPlace);
                Animation cardAnimation = animation.getCardTranslateAnimation(animatedCard, cards);
                cardAnimation.setOnFinished(e->{
                    cardPlace.getChildren().remove(animatedCard);
                    createCard(1);
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
     * @param total     The number of cards to generate.
     */
    private void createCard(int total) {
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
        HBox scoreTimeContainer = new HBox();

        Random random = new Random();
        for(int i = 0; i < 4; ++i) {
            HBox playerPlace = new HBox();
            if(i==3){
                createPlayerStatus(playerPlace, "/avatar/User-02.png", true);
            }else{
                int ranUser = random.nextInt(7) + 1;
                createPlayerStatus(playerPlace, String.format("/avatar/User-0%d.png",ranUser), false);
                gamePlayerStatus.getChildren().add(playerPlace);
            }
        }

        scoreTimeContainerConfig(scoreTimeContainer);

        gamePlayerStatus.getChildren().add(scoreTimeContainer);

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
        scoreBox.setPadding(new Insets(10, 10, 10, 20));
        scoreBox.setSpacing(10);

        players.sort(Comparator.reverseOrder());
        for(Player player : players){
            HBox hBox = new HBox();
            createScorePlayer(hBox, player);
            scoreBox.getChildren().add(hBox);
        }

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
     */
    private void createScorePlayer(HBox scoreBox, Player player) {
        ImageView playerIcon = player.getIcon();
        playerIcon.setFitWidth(70);
        playerIcon.setFitHeight(70);

        Label scoreLbl = new Label(String.valueOf(player.getScore()));
        scoreLbl.setFont(Font.loadFont(style.getLilitaOneFont(), 40));
        scoreLbl.setStyle(style.sideLabelStyle());
        scoreBox.getChildren().addAll(playerIcon, scoreLbl);
        scoreBox.setAlignment(Pos.CENTER_LEFT);
        scoreBox.setSpacing(60);
    }

    /**
     * Creates and configures a player's status display with an avatar, card back, and remaining card count.
     *
     * @param playerPlace The HBox where the player's status will be displayed.
     */
    private void createPlayerStatus(HBox playerPlace, String url, boolean self) {
        Player player = new Player();
        player.setIcon(url);
        players.add(player);

        if(self){
            player.setSelf(self);
            return;
        }

        // 내가 왜 이렇게 코드를 짰냐고..? 객체만들고 이미지로드까지 시간이 걸려서 UI에 바로 안나와 씨발
        ImageView icon = new ImageView(new Image(getClass().getResource(url).toExternalForm()));
        icon.setFitWidth(150);
        icon.setFitHeight(150);

        ImageView cardBack = new ImageView(new Image(getClass().getResource("/card/Card-0.png").toExternalForm()));
        cardBack.setFitWidth(150);
        cardBack.setFitHeight(200);

        Label cardLeft = player.getCardLeft();
        cardLeft.setFont(Font.loadFont(style.getLilitaOneFont(), 40));
        cardLeft.setStyle(style.sideLabelStyle());

        playerPlace.setAlignment(Pos.CENTER_LEFT);
        playerPlace.getChildren().addAll(icon, cardBack, cardLeft);
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
        spacer.setPrefHeight(30);

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
        root.setPrefSize(1920,1080);
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
