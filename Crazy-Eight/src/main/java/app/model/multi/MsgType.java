package app.model.multi;

/**
 * Represents the various types of messages that can be exchanged in the application.
 * These message types are primarily used for communication between the client
 * and the server in a multiplayer game setting. Each message type signifies a
 * specific action, event, or state, allowing for structured and efficient message handling.
 * <p>
 * The enumerated constants include, but are not limited to:
 * - Actions related to game creation, joining, and exiting.
 * - Notifications for player and game state updates.
 * - Specific gameplay events like drawing cards, playing cards, and handling special rules.
 * - System-related or error notifications.
 */
public enum MsgType {
    CREATE_GAME,
    JOIN_GAME,
    ASSIGN_ID,
    SELECT_CHARACTER,
    READY,
    UNREADY,
    INIT_PAGE,
    INIT_DECK,
    INIT_PLAYERS,
    CREATE_PLAYERS,
    UPDATE_PLAYERS,
    DRAW_CARD,
    PUT_DUMMY,
    UPDATE_TURN,
    TIME_SET,
    PUT_CARD,
    REQUEST_DRAW_CARD,
    TIME_OUT,
    STACK,
    SERVER_EIGHT,
    CRAZY_EIGHT,
    CRAZY_EIGHT_DONE,
    REVERSE_ORDER,
    QUEEN,
    END,
    CONTINUE,
    EXIT,
    FORCE_EXIT,
    CHAT,
    LOG,
    SYSTEM,
    ERROR,
}
