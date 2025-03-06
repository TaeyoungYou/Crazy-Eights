package app.view;

import app.animation.AnimationEight;
import app.style.StyleEight;
import javafx.animation.Animation;
import javafx.event.Event;
import javafx.geometry.Pos;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

public class ChooseEightView {
    private final StackPane pane;
    private StackPane overlay;
    private final StyleEight style;
    private final AnimationEight animation;

    private ImageView space;
    private ImageView heart;
    private ImageView diamond;
    private ImageView club;

    public ChooseEightView(StackPane pane) {
        this.pane = pane;
        style = new StyleEight();
        animation = new AnimationEight();
    }

    public void generate(){
        overlay = new StackPane();
        overlay.setStyle(style.overlayStyle());
        overlay.prefWidthProperty().bind(pane.widthProperty());
        overlay.prefHeightProperty().bind(pane.heightProperty());
        overlay.setAlignment(Pos.CENTER);

        HBox eightsContainer = new HBox(20);
        eightsContainer.setAlignment(Pos.CENTER);

        space = new ImageView(getClass().getResource("/card/Card-0-7.png").toExternalForm());
        heart = new ImageView(getClass().getResource("/card/Card-1-7.png").toExternalForm());
        diamond = new ImageView(getClass().getResource("/card/Card-2-7.png").toExternalForm());
        club = new ImageView(getClass().getResource("/card/Card-3-7.png").toExternalForm());

        eightsContainer.getChildren().addAll(space, heart, diamond, club);

        overlay.getChildren().add(eightsContainer);
        pane.getChildren().add(overlay);


        animation.fadeInPane(overlay);
        setCardAnimation();
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
    private void setCardAnimation(){
        animation.cardHoverAnimation(space);
        animation.cardHoverAnimation(heart);
        animation.cardHoverAnimation(diamond);
        animation.cardHoverAnimation(club);
    }

    public ImageView getSpace() {
        return space;
    }

    public ImageView getHeart() {
        return heart;
    }

    public ImageView getDiamond() {
        return diamond;
    }

    public ImageView getClub() {
        return club;
    }
}
