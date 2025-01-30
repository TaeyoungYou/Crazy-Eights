package app.ui;

import app.animation.AnimationGame;
import app.style.StyleGame;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

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

        pane.setStyle(style.loadingBorderPaneStyle());

        Label lbl = new Label();
        lbl.setText("Welcome to Crazy-Eights");
        VBox vbox = new VBox();
        vbox.setAlignment(Pos.CENTER);
        vbox.getChildren().add(lbl);

        pane.setCenter(vbox);
    }
}
