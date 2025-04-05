package app.view.multi;

import app.animation.AnimationCharacter;
import app.style.StyleCharacter;
import javafx.animation.Animation;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Duration;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 * The CharacterChooseView class is responsible for creating and managing
 * a character selection interface in a JavaFX application. This view enables
 * users to choose from a predefined set of characters, represented as images,
 * with hover animations and fade transitions.
 */
public class CharacterChooseView {
    private final StackPane pane;
    private StackPane overlay;
    private StyleCharacter style;
    private AnimationCharacter animation;

    private List<Pair<ImageView, String>> characters;
    
    private Label ready;

    /**
     * Constructs a CharacterChooseView object, initializing its components for creating
     * and managing a character selection interface in a JavaFX application.
     *
     * @param pane The StackPane where the character selection interface will be added.
     */
    public CharacterChooseView(StackPane pane) {
        this.pane = pane;
        overlay = new StackPane();
        style = new StyleCharacter();
        animation = new AnimationCharacter();
        characters = new ArrayList<>();
    }

    /**
     * Generates the character selection view in the application.
     * This method configures styles, layouts, and animations for the character
     * selection interface. It organizes character avatars into a user-friendly
     * display and applies associated animations.
     *
     * Details of the generate method functionality:
     * - Sets the style of the root pane and overlay using predefined styles.
     * - Binds the size of the overlay to match the dimensions of the root pane.
     * - Initializes a list of character images and organizes them into two rows.
     * - Adds the rows to a container and embeds it into the overlay.
     * - Sets up a fade-in animation for the overlay.
     * - Configures hover animations for character selection elements.
     */
    public void generate(){
        pane.setStyle(style.gameRootStyle());
        overlay.setStyle(style.overlayStyle());
        overlay.prefWidthProperty().bind(pane.widthProperty());
        overlay.prefHeightProperty().bind(pane.heightProperty());
        overlay.setAlignment(Pos.TOP_CENTER);

        for(int i=1; i<8; ++i){
            String url = "/avatar/User-0" + i + ".png";
            characters.add(new Pair<>(new ImageView(getClass().getResource(url).toExternalForm()), url));
        }

        Region padding = new Region();
        padding.setPrefHeight(150);

        VBox charactersContainer = new VBox(40);
        HBox charactersLayer01 = new HBox(20);
        HBox charactersLayer02 = new HBox(20);

        charactersContainer.setAlignment(Pos.CENTER);
        charactersLayer01.setAlignment(Pos.CENTER);
        charactersLayer02.setAlignment(Pos.CENTER);

        for(int i=0; i<4; ++i){
            charactersLayer01.getChildren().add(characters.get(i).getKey());
        }
        for(int i=4; i<characters.size(); ++i){
            charactersLayer02.getChildren().add(characters.get(i).getKey());
        }

        Region space = new Region();
        space.setPrefHeight(100);

        ready = new Label("READY");
        ready.setPrefSize(250, 80);
        ready.setStyle(style.setButtonStyle());

        charactersContainer.getChildren().addAll(padding, charactersLayer01, charactersLayer02, space, ready);
        overlay.getChildren().add(charactersContainer);
        pane.getChildren().add(overlay);

        animation.fadeInPane(overlay);
        setCharacterAnimation();
    }
    /**
     * Retrieves the fade-out animation for the overlay pane in the character selection view.
     * The animation transitions the overlay pane's opacity from fully visible (1.0)
     * to fully transparent (0.0) over a predefined duration.
     *
     * @return An Animation object representing the fade-out effect for the overlay pane.
     */
    public Animation getFadeOutPaneAnimation() {
        return animation.fadeOutPane(overlay);
    }
    /**
     * Retrieves the main StackPane element associated with the CharacterChooseView.
     * This is the root container used to organize and display the character selection interface.
     *
     * @return The StackPane instance representing the root pane of the character selection view.
     */
    public StackPane getPane() {
        return pane;
    }
    /**
     * Retrieves the overlay pane used in the character selection view.
     * The overlay is a StackPane designed to display UI elements, styles,
     * and animations associated with the character selection interface.
     *
     * @return The StackPane representing the overlay pane.
     */
    public StackPane getOverlay() {
        return overlay;
    }
    /**
     * Configures hover animations for the character elements in the character selection view.
     * This method iterates over the list of character pairs, where each pair consists of an
     * ImageView representing a character and a corresponding identifier string. The hover
     * effect is applied to each ImageView, causing a visual scaling animation when hovered over.
     *
     * Utilizes the `characterHoverAnimation` method from the animation object to define the
     * scaling behavior. The hover animation enhances user interaction by providing feedback
     * when a character is selected.
     */
    private void setCharacterAnimation(){
        for(Pair<ImageView, String> pair: characters){
            animation.characterHoverAnimation(pair.getKey());
        }
    }
    /**
     * Retrieves the list of characters available in the character selection view.
     * Each character is represented as a pair consisting of an ImageView (visual representation)
     * and a string identifier.
     *
     * @return A list of pairs, where each pair contains an ImageView and a corresponding
     *         string identifier representing a character.
     */
    public List<Pair<ImageView, String>> getCharacters() {
        return characters;
    }

    public void setTempSelect(ImageView character){
        DropShadow edgeGlow = new DropShadow();
        edgeGlow.setRadius(20);
        edgeGlow.setSpread(0.5);
        edgeGlow.setColor(Color.WHITE);
        edgeGlow.setOffsetX(0);
        edgeGlow.setOffsetY(0);
        character.setEffect(edgeGlow);
    }

    public void setSelect(ImageView character){
        DropShadow edgeGlow = new DropShadow();
        edgeGlow.setRadius(20);
        edgeGlow.setSpread(0.5);
        edgeGlow.setColor(Color.GREEN);
        edgeGlow.setOffsetX(0);
        edgeGlow.setOffsetY(0);
        character.setEffect(edgeGlow);
    }

    public void removeEffect(ImageView character){
        character.setEffect(null);
    }

    public Label getReady() {
        return ready;
    }

    public void setReadyStyle(){
        ready.setStyle(style.readyPressedStyle());
    }
    public void setUnReadyStyle(){
        ready.setStyle(style.setButtonStyle());
    }

    public void startGameCountDown(Runnable function){
        SequentialTransition seq = new SequentialTransition();
        // 3부터 0까지 순차적으로 진행
        for (int i = 3; i >= 0; i--) {
            seq.getChildren().add(createNumberTransition(i));
        }
        seq.play();
        seq.setOnFinished(e->{
            animation.fadeOutPane(overlay);
            function.run();
        });
    }

    // 각 숫자 하나에 대한 Transition을 만드는 헬퍼 메서드
    private SequentialTransition createNumberTransition(int number) {
        // 새로운 Label을 생성하고, 해당 숫자로 설정
        Label countDown = createCountLabel();
        if(number == 0){
            countDown.setText("Start");
        }else {
            countDown.setText(String.valueOf(number));
        }


        SequentialTransition step = new SequentialTransition();

        // 0초짜리 PauseTransition을 이용해, Label을 화면에 추가
        PauseTransition addLabel = new PauseTransition(Duration.ZERO);
        addLabel.setOnFinished(e -> overlay.getChildren().add(countDown));

        // countDownAnimation()은 해당 Label에 대한 애니메이션을 반환한다고 가정
        Animation anim = animation.countDownAnimation(countDown);
        // 애니메이션이 끝나면 Label을 제거
        anim.setOnFinished(e -> overlay.getChildren().remove(countDown));

        // addLabel 후에 anim을 실행하도록 순차적으로 연결
        step.getChildren().addAll(addLabel, anim);
        return step;
    }


    public Label createCountLabel(){
        Label countDown = new Label("3");
        countDown.setFont(Font.loadFont(style.getLilitaOneFont(),800));
        countDown.setStyle(style.countDownStyle());
        countDown.setOpacity(0);
        return countDown;
    }
}