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


/**
 * This class manages the functionality of the character selection screen in a multiplayer game.
 * It handles player interactions, message communication with the server,
 * and manages the state of character selections and ready status of players.
 */
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

    /**
     * Constructs a new CharacterChooseController instance.
     * Initializes the main UI components and associates the provided scene.
     *
     * @param _scene the JavaFX Scene object to associate with this controller,
     *               which serves as the primary scene for switching and rendering UI components.
     */
    public CharacterChooseController(Scene _scene) {
        scene = _scene;
        pane = new StackPane();
        style = new StyleCharacter();
        mainView = new CharacterChooseView(pane);
    }

    /**
     * Initializes the main page of the CharacterChooseController by setting up the
     * root pane for the scene and applying the necessary styling.
     * <p>
     * This method configures the visual elements by associating the scene's root element
     * with the designated pane and applying the style defined in the gameRootStyle method.
     */
    private void initPage() {
        scene.setRoot(pane);
        pane.setStyle(style.gameRootStyle());
    }

    /**
     * Initializes and renders the character selection pane by setting up the main interface elements,
     * event handlers, and establishing a communication mechanism with the server.
     * <p>
     * This method performs the following steps:
     * - Calls {@code initPage()} to set the scene's root to the main pane and configures default styles.
     * - Triggers the generation of the main character selection interface via {@code mainView.generate()}.
     * - Establishes the server communication handler by assigning {@code this::handleServerMessage} to handle incoming server messages
     * and starts listening for server communications using {@code Client.listen()}.
     * - Catches and logs {@code IOException} that may occur during the server communication setup.
     * - Configures user interaction by setting up character-related event listeners using {@code setupCharacterEvents()}.
     * - Configures ready-state event listeners using {@code setupReadyEvents()}.
     * <p>
     * This method must be called to properly initialize and display the character selection interface and ensure server interactions are handled.
     * Any failure during server communication does not interrupt the setup of the UI components.
     */
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

    /**
     * Handles server messages received during the character selection phase of the game.
     * This method processes and responds to different message types to update the game state
     * or trigger specific actions for the client. It ensures that the messages are parsed
     * correctly, and the corresponding actions are applied based on the provided data.
     *
     * @param message the raw message received from the server, expected to follow the format
     *                "{senderPlayerId}#{MsgType}#{data}".
     *                - senderPlayerId: The ID of the sender.
     *                - MsgType: The type of the message indicating the action (e.g., ASSIGN_ID, SELECT_CHARACTER).
     *                - data: Additional data associated with the message.
     */
    private void handleServerMessage(String message) {
        try {
            MessageParser.ParsedMessage parsed = MessageParser.parse(message);

            switch (parsed.getMsgType()) {
                case ASSIGN_ID:
                    if ("character".equals(parsed.getData())) {
                        if (playerId == -1) {
                            playerId = parsed.getSenderPlayerId();
                            System.out.println(playerId);
                        }
                        totalPlayer++;
                    }
                    ;
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

    /**
     * Configures event handlers for character selection in the character selection interface.
     * <p>
     * This method iterates through all available characters retrieved from the {@code mainView.getCharacters()}
     * method. Each character is associated with an {@code ImageView} and a string URL identifier.
     * <p>
     * For each character:
     * - Sets up a mouse click event listener on the corresponding {@code ImageView}.
     * - When a character is clicked and the {@code ready} state is false, the following actions are performed:
     * - Calls {@code applySelectEffect(playerId, characterUrl)} to visually indicate the selection
     * and update the relevant mappings for the selected character.
     * - Sends a selection message to the server through {@code Client.send()} in the format
     * "{playerId}#SELECT_CHARACTER#{characterSelection}".
     * <p>
     * This method ensures that the user interactions in selecting characters update the interface
     * and communicate the selection to the server.
     */
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

    /**
     * Configures the events associated with the "ready" interface element to handle user interactions.
     * <p>
     * Sets up the mouse-entered event to change the cursor to a hand when hovering over the ready element.
     * Configures the mouse-clicked event to toggle the ready status, applying the corresponding visual effect
     * and sending a ready or unready status update to the client.
     * <p>
     * Updates include:
     * - Changing the visual style of the interface element (ready or unready).
     * - Applying visual effects to the user's character selection based on the ready state.
     * - Sending the ready/unready status and associated character selection to the server.
     */
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

    /**
     * Applies a visual selection effect for a character and manages the current player selections.
     * This method ensures that the character selection is visually updated according to the player's
     * input while maintaining the integrity of the character selection mappings.
     * <p>
     * The following actions are performed:
     * - If the character (identified by the given URL) is already selected, the method exits immediately.
     * - Adds or updates the association between the player ID and the selected character URL
     * in the relevant data structures via {@code addKeyValue()}.
     * - Iterates over all characters retrieved from {@code mainView.getCharacters()} and updates their visual effects:
     * - Removes any existing effect from characters that are not in the "ready" state.
     * - Sets a temporary selection effect for characters already selected by a player.
     *
     * @param id  the player ID associated with the character selection
     * @param url the URL identifier of the character being selected
     */
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

    /**
     * Applies the "ready" effect for a designated character identified by its URL.
     * This method updates the internal state and applies a visual highlight effect
     * to the character in the user interface, signaling the "ready" status.
     *
     * @param url the unique URL identifier of the character to which the "ready" effect will be applied.
     *            This URL is used to find the associated character and update its visual representation.
     */
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

    /**
     * Applies the "unready" effect to a character associated with the specified URL.
     * <p>
     * This method removes the specified URL from the `readyUser` collection, signaling
     * that the corresponding character is no longer in a "ready" state. It also updates
     * the visual interface by applying a temporary selection effect to the character
     * associated with the URL if it is part of the player's selections.
     *
     * @param url the unique URL identifier of the character for which the "unready" effect
     *            will be applied. This URL identifies the character to be processed.
     */
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

    /**
     * Adds or updates the association between a player ID and a character URL in the relevant mappings.
     * <p>
     * This method ensures that the character selection is properly managed by:
     * - Removing the previous mapping of a character URL if it was already associated with the given player ID.
     * - Adding the new mapping for the provided `id` and `url` to the `playerCharacterSelections` and `characterPlayerSelections` maps.
     *
     * @param id  the player ID for which the character selection is being added or updated
     * @param url the URL identifier of the character being associated with the specified player ID
     */
    private void addKeyValue(int id, String url) {
        if (playerCharacterSelections.containsKey(id)) {
            String oldUrl = playerCharacterSelections.get(id);
            characterPlayerSelections.remove(oldUrl);
        }
        characterPlayerSelections.put(url, id);
        playerCharacterSelections.put(id, url);
    }

    /**
     * Initiates the countdown sequence for starting the game. This method triggers the visual
     * countdown animation in the main view and transitions to the appropriate game mode (server
     * or client) based on the player's ID upon completion of the countdown.
     * <p>
     * The countdown is executed on the JavaFX application thread using {@code Platform.runLater},
     * ensuring proper synchronization with the UI. Once the countdown finishes, the corresponding
     * game controller is initialized and started:
     * <p>
     * - If the player ID is `0`, it initializes a server-side game controller, sets the message
     * handler, saves player and character selection information, and starts the game.
     * - If the player ID is not `0`, it initializes a client-side game controller, sets the
     * message handler, saves the player information, and transitions into the client game.
     * <p>
     * This method relies on the implementation of the {@code mainView.startGameCountDown} method
     * to execute the countdown animation and subsequently executes the provided logic upon
     * completion.
     */
    private void startGameCountDown() {
        System.out.println("Count Down Start!!!");
        Platform.runLater(new CountdownRunnable());
    }

    private class CountdownRunnable implements Runnable {
        @Override
        public void run() {
            mainView.startGameCountDown(new GameStartRunnable());
        }
    }

    private class GameStartRunnable implements Runnable {
        @Override
        public void run() {
            if (playerId == 0) {
                System.out.println("SERVER GAME ENTERED");
                GameController gameController = new GameController(scene);
                Client.setHandler(gameController::handleServerMessage);
                gameController.saveInfo(playerId, playerCharacterSelections);
                gameController.startGame();
            } else {
                System.out.println("CLIENT GAME ENTERED" + playerId);
                GameClientController gameClientController = new GameClientController(scene);
                Client.setHandler(gameClientController::handleServerMessage);
                gameClientController.saveInfo(playerId);
            }
        }
    }
}
