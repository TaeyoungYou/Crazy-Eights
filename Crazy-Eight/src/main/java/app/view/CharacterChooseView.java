package app.view;

import app.animation.AnimationCharacter;
import app.style.StyleCharacter;
import javafx.animation.Animation;
import javafx.geometry.Pos;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class CharacterChooseView {
    private final StackPane pane;
    private StackPane overlay;
    private StyleCharacter style;
    private AnimationCharacter animation;

    private List<Pair<ImageView, String>> characters;

    public CharacterChooseView(StackPane pane) {
        this.pane = pane;
        overlay = new StackPane();
        style = new StyleCharacter();
        animation = new AnimationCharacter();
        characters = new ArrayList<>();
    }

    public void generate(){
        pane.setStyle(style.gameRootStyle());
        overlay.setStyle(style.overlayStyle());
        overlay.prefWidthProperty().bind(pane.widthProperty());
        overlay.prefHeightProperty().bind(pane.heightProperty());
        overlay.setAlignment(Pos.CENTER);

        for(int i=1; i<8; ++i){
            String url = "/avatar/User-0" + i + ".png";
            characters.add(new Pair<>(new ImageView(getClass().getResource(url).toExternalForm()), url));
        }

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

        charactersContainer.getChildren().addAll(charactersLayer01, charactersLayer02);
        overlay.getChildren().add(charactersContainer);
        pane.getChildren().add(overlay);

        animation.fadeInPane(overlay);
        setCharacterAnimation();
    }
    public Animation getFadeOutPaneAnimation() {
        return animation.fadeOutPane(overlay);
    }
    public StackPane getPane() {
        return pane;
    }
    public StackPane getOverlay() {
        return overlay;
    }
    private void setCharacterAnimation(){
        for(Pair<ImageView, String> pair: characters){
            animation.characterHoverAnimation(pair.getKey());
        }
    }
    public List<Pair<ImageView, String>> getCharacters() {
        return characters;
    }
}