package app.controller.multi;

import app.model.multi.Client;
import app.model.multi.MessageParser;
import app.style.StyleCharacter;
import app.view.multi.CharacterChooseView;
import javafx.application.Platform;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public class CharacterChooseController {
    private Scene scene;
    private StackPane pane;

    private CharacterChooseView mainView;
    private StyleCharacter style;

    private boolean ready = false;
    private static int playerId = -1;
    private int totalPlayer = 0;

    private Map<Integer, String> playerCharacterSelections = new HashMap<>();
    private Map<String, Integer> characterPlayerSelections = new HashMap<>();
    private Set<String> readyUser = new HashSet<>();

    public CharacterChooseController(Scene _scene) {
        scene = _scene;
        pane = new StackPane();
        style = new StyleCharacter();
        mainView = new CharacterChooseView(pane);
    }

    private void initPage() {
        scene.setRoot(pane);
        pane.setStyle(style.gameRootStyle());
    }

    public void drawPane() {
        initPage();

        mainView.generate();

        try {
            Client.setHandler(this::handleServerMessage);
            Client.listen();
        } catch (IOException e) {
            e.printStackTrace();
        }

        setupCharacterEvents();
        setupReadyEvents();
    }

    private void handleServerMessage(String message) {
        try {
            MessageParser.ParsedMessage parsed = MessageParser.parse(message);

            switch (parsed.getMsgType()) {
                case ASSIGN_ID:
                    if("character".equals(parsed.getData())){
                        if(playerId == -1){
                            playerId = parsed.getSenderPlayerId();
                            System.out.println(playerId);
                        }
                        totalPlayer++;
                    };
                    break;
                case SELECT_CHARACTER:
                    if (parsed.getSenderPlayerId() != playerId) {
                        System.out.println("SELECT_CHARACTER " + parsed.getSenderPlayerId());
                        applySelectEffect(parsed.getSenderPlayerId(), parsed.getData());
                    }
                    break;
                case UNREADY:
                    if (parsed.getSenderPlayerId() != playerId) {
                        System.out.println("UNREADY " + parsed.getSenderPlayerId());
                        applyUnReadyEffect(parsed.getData());
                    }
                    break;
                case READY:
                    if (parsed.getSenderPlayerId() != playerId) {
                        System.out.println("READY " + parsed.getSenderPlayerId());
                        applyReadyEffect(parsed.getData());
                    }
                    if (totalPlayer > 1 && totalPlayer == readyUser.size()) {
                        startGameCountDown();
                    }
                    break;
                case JOIN_GAME:
                    if (parsed.getSenderPlayerId() != playerId) {
                        System.out.println("JOIN_GAME " + parsed.getSenderPlayerId());
                        characterPlayerSelections.forEach((url, id) -> Client.send(id + "#SELECT_CHARACTER#" + url));
                        readyUser.forEach(url -> Client.send("-1#READY#" + url));
                        totalPlayer++;
                    }
                    break;
                default:
                    System.err.println("알 수 없는 메시지 타입: " + parsed.getMsgType());
            }

        } catch (MessageParser.InvalidMessageFormatException e) {
            System.err.println("메시지 파싱 실패: " + e.getMessage());
        }
    }

    private void setupCharacterEvents() {
        for (var pair : mainView.getCharacters()) {
            ImageView characterImage = pair.getKey();
            String characterUrl = pair.getValue();

            characterImage.setOnMouseClicked(event -> {
                if (ready) return;

                applySelectEffect(playerId, characterUrl);

                Client.send(playerId + "#SELECT_CHARACTER#" + playerCharacterSelections.get(playerId));
            });
        }
    }

    private void setupReadyEvents() {
        mainView.getReady().setOnMouseEntered(event -> {
            mainView.getReady().setCursor(Cursor.HAND);
        });
        mainView.getReady().setOnMouseClicked(event -> {
            if (!ready) {
                mainView.setReadyStyle();
                applyReadyEffect(playerCharacterSelections.get(playerId));
                Client.send(playerId + "#READY#" + playerCharacterSelections.get(playerId));
                ready = true;
            } else {
                mainView.setUnReadyStyle();
                applyUnReadyEffect(playerCharacterSelections.get(playerId));
                Client.send(playerId + "#UNREADY#" + playerCharacterSelections.get(playerId));
                ready = false;
            }
        });
    }

    // 액션을 취하는 곳에서 사용할 메서드
    private void applySelectEffect(int id, String url) {
        if (characterPlayerSelections.containsKey(url)) return;
        addKeyValue(id, url);

        for (var pair : mainView.getCharacters()) {
            ImageView characterImage = pair.getKey();
            String characterUrl = pair.getValue();

            if (readyUser.contains(characterUrl)) continue;

            mainView.removeEffect(characterImage);
            if (characterPlayerSelections.containsKey(characterUrl)) {
                mainView.setTempSelect(characterImage);
            }
        }
    }

    private void applyReadyEffect(String url) {
        readyUser.add(url);
        for (var pair : mainView.getCharacters()) {
            ImageView characterImage = pair.getKey();
            String characterUrl = pair.getValue();

            if (characterPlayerSelections.containsKey(characterUrl) && characterUrl.equals(url)) {
                mainView.setSelect(characterImage);
            }
        }
    }

    private void applyUnReadyEffect(String url) {
        readyUser.remove(url);
        for (var pair : mainView.getCharacters()) {
            ImageView characterImage = pair.getKey();
            String characterUrl = pair.getValue();

            if (characterPlayerSelections.containsKey(characterUrl) && characterUrl.equals(url)) {
                mainView.setTempSelect(characterImage);
            }
        }
    }

    private void addKeyValue(int id, String url) {
        if (playerCharacterSelections.containsKey(id)) {
            String oldUrl = playerCharacterSelections.get(id);
            characterPlayerSelections.remove(oldUrl);
        }
        characterPlayerSelections.put(url, id);
        playerCharacterSelections.put(id, url);
    }

    private void startGameCountDown() {
        System.out.println("Count Down 시작!!!");
        Platform.runLater(() -> {
            mainView.startGameCountDown(()->{

                if(playerId == 0){
                    System.out.println("SERVER GAME ENTERED");
                    GameController gameController = new GameController(scene);
                    Client.setHandler(gameController::handleServerMessage);
                    gameController.saveInfo(playerId, playerCharacterSelections);
                    gameController.startGame();
                }else{
                    System.out.println("CLIENT GAME ENTERED" + playerId);
                    GameClientController gameClientController = new GameClientController(scene);
                    Client.setHandler(gameClientController::handleServerMessage);
                    gameClientController.saveInfo(playerId);
                }

            });
        });
    }
}
