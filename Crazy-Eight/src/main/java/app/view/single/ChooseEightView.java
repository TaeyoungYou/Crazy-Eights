package app.view.single;

import app.animation.AnimationEight;
import app.style.StyleEight;
import javafx.animation.Animation;
import javafx.geometry.Pos;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

/**
 * The {@code ChooseEightView} class is responsible for managing and displaying
 * a selection of "eight" playing cards with animations and styles in a JavaFX application.
 * It provides methods to generate the card selection overlay, apply animations,
 * and retrieve card-specific elements.
 */
public class ChooseEightView {
    private final StackPane pane;
    private StackPane overlay;
    private final StyleEight style;
    private final AnimationEight animation;

    private ImageView space;
    private ImageView heart;
    private ImageView diamond;
    private ImageView club;

    /**
     * Constructs a {@code ChooseEightView} instance and initializes it with the given {@code StackPane}.
     * This constructor sets up the required styling and animations for the view.
     *
     * @param pane The {@code StackPane} that serves as the base container for the view.
     */
    public ChooseEightView(StackPane pane) {
        this.pane = pane;
        style = new StyleEight();
        animation = new AnimationEight();
    }

    /**
     * Generates the overlay containing the "eight" playing cards and adds it to the pane.
     * It creates a StackPane with a semi-transparent background and dynamically binds
     * its width and height to the pane's dimensions. The method also creates a horizontal
     * container (HBox) that holds four ImageView objects, each representing a playing card.
     * The container is added to the overlay, which is then added to the pane. A fade-in
     * animation is applied to the overlay, and hover animations are enabled for
     * the individual cards.
     */
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
    /**
     * Retrieves the fade-out animation for the overlay pane.
     * This method applies a fade-out effect to the overlay, gradually
     * reducing its opacity to zero to create a smooth disappearance effect.
     *
     * @return An {@code Animation} instance that performs the fade-out effect on the overlay pane.
     */
    public Animation getFadeOutPaneAnimation() {
        return animation.fadeOutPane(overlay);
    }
    /**
     * Retrieves the primary container pane of the view.
     *
     * @return The {@code StackPane} serving as the base container for this view.
     */
    public StackPane getPane() {
        return pane;
    }
    /**
     * Retrieves the overlay StackPane used for displaying additional content or effects.
     *
     * @return The {@code StackPane} serving as the overlay component of this view.
     */
    public StackPane getOverlay() {
        return overlay;
    }
    /**
     * Configures hover animations for the card ImageView elements.
     * This method applies a hover effect to each card (space, heart, diamond, and club)
     * by invoking the {@code cardHoverAnimation} method defined in the {@code animation} field.
     * When a card is hovered over, it scales up, and when the mouse exits, it returns to its original size.
     * This visually enhances the interaction with the cards.
     */
    private void setCardAnimation(){
        animation.cardHoverAnimation(space);
        animation.cardHoverAnimation(heart);
        animation.cardHoverAnimation(diamond);
        animation.cardHoverAnimation(club);
    }

    /**
     * Retrieves the ImageView representation of the space card.
     *
     * @return The {@code ImageView} instance associated with the space card.
     */
    public ImageView getSpace() {
        return space;
    }

    /**
     * Retrieves the ImageView representing the heart card.
     *
     * @return The {@code ImageView} object associated with the heart card.
     */
    public ImageView getHeart() {
        return heart;
    }

    /**
     * Retrieves the ImageView representing the diamond card.
     *
     * @return The {@code ImageView} instance associated with the diamond card.
     */
    public ImageView getDiamond() {
        return diamond;
    }

    /**
     * Retrieves the ImageView representing the club card.
     *
     * @return The {@code ImageView} instance associated with the club card.
     */
    public ImageView getClub() {
        return club;
    }
}
