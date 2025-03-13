package app.model;

/**
 * GameStatusManager is a class responsible for managing the state
 * of the game, including turn tracking, timing, and various
 * statuses such as user actions, turn passing, and game effects.
 */
public class GameStatusManager {
    // 유저가 카드를 내거나 받거나 했을 때 플래그
    private boolean userDid;
    // 유저가 어떤 동작을 수행하고 애니메이션이 동작을 할때 잠깐 멈추는 플래그
    private boolean passTurn;
    // 수행 시간을 나타내는 정수
    private int time;
    // 누구의 턴인지 인덱스를 가리키는 정수
    private int turn;

    private int order;

    private boolean fourTime;
    private boolean queenTime;

    private final int playerNumber;

    /**
     * Constructs a new GameStatusManager instance to manage the state of
     * the game, initializing the game variables such as turn management,
     * user actions, and player numbers.
     *
     * @param playerNumber the total number of players in the game
     */
    public GameStatusManager(int playerNumber) {
        this.userDid = false;
        this.passTurn = false;
        this.time = 0;
        this.turn = -1;
        this.order = 1;
        this.fourTime = false;
        this.playerNumber = playerNumber;
    }

    /**
     * Checks if the user has performed an action, such as playing or receiving a card.
     *
     * @return true if the user has performed an action, false otherwise
     */
    public boolean isUserDid() {
        return userDid;
    }

    /**
     * Resets the user's action status by setting the userDid flag to false.
     * This method is typically used to indicate that the user has not
     * performed any actions, such as playing or receiving a card, after
     * the current game state is updated or reset.
     */
    public void resetUserDid() {
        userDid = false;
    }

    /**
     * Sets the user's action status to true, indicating that the user has performed
     * an action, such as playing or receiving a card. This method updates the internal
     * state of the GameStatusManager to reflect the user's participation in the current
     * game state.
     */
    public void doUserDid(){
        userDid = true;
    }

    /**
     * Checks if the current state indicates that the turn is passed.
     *
     * @return true if the turn is passed, false otherwise
     */
    public boolean isPassTurn() {
        return passTurn;
    }

    /**
     * Resets the pass turn status for the game.
     * This method sets the internal `passTurn` flag to false,
     * indicating that a player has not chosen to pass their turn.
     * It is typically used to reset the game state before the start of a new turn or round.
     */
    public void resetPassTurn(){
        this.passTurn = false;
    }

    /**
     * Marks the current turn as passed by setting the internal `passTurn` flag to true.
     * This method is used to indicate that a player has chosen to forfeit their turn
     * during the game. The game state is updated to reflect this decision.
     */
    public void doPassTurn() {
        this.passTurn = true;
    }
    /**
     * Retrieves the current value of the time variable.
     *
     * @return the current time as an integer.
     */
    public int getTime() {
        return time;
    }

    /**
     * Increments the game time counter by one.
     * This method updates the internal `time` field, which represents
     * the current time within the game's state management, by increasing its value.
     */
    public void addTime(){
        time++;
    }

    /**
     * Resets the game time counter to zero.
     * This method sets the internal `time` field to 0, initializing the game time state.
     * It is typically used to restart the time tracking at the beginning of a new game session
     * or round.
     */
    public void resetTime(){
        time = 0;
    }

    /**
     * Retrieves the current turn in the game.
     *
     * @return the current turn as an integer.
     */
    public int getTurn() {
        return turn;
    }

    /**
     * Advances the game to the next player's turn, based on the current turn order.
     * Determines the next player by applying the turn order, which can be positive
     * or negative, to the current turn, and adjusts for modular arithmetic to ensure
     * valid player indices.
     *
     * If the order is negative, the method computes the next turn by decrementing
     * the current turn and wrapping around in case of negative results.
     * If the order is positive, the method computes the next turn by incrementing
     * the current turn and applying modular arithmetic to ensure the turn index
     * remains within the range of valid players.
     *
     * The result is updated in the `turn` variable, ensuring it correctly points
     * to the next active player.
     */
    public void nextTurn(){
        if(order < 0){
            turn = ((turn + order) % playerNumber + playerNumber) % playerNumber;
        } else {
            turn = (turn+order)%playerNumber;
        }
    }

    /**
     * Checks if the current state indicates the "FourTime" condition is not active.
     * This method evaluates the internal `fourTime` flag and returns its logical negation.
     *
     * @return true if the "FourTime" condition is inactive, false otherwise.
     */
    public boolean isFourTime() {
        return !fourTime;
    }

    /**
     * Activates the "FourTime" condition by setting the internal `fourTime` flag to true.
     * This method updates the game state to reflect that the "FourTime" condition
     * is currently active.
     */
    public void setFourTime() {
        this.fourTime = true;
    }

    /**
     * Resets the "FourTime" condition in the game state.
     * This method sets the internal `fourTime` flag to false, indicating
     * that the "FourTime" condition is no longer active.
     * It is typically used to reset the "FourTime" state at the end of a
     * relevant game phase or when reinitializing the game state.
     */
    public void resetFourTime(){
        fourTime = false;
    }

    /**
     * Determines if the "QueenTime" condition is not currently active.
     * This method evaluates the internal `queenTime` flag and returns its logical negation.
     *
     * @return true if the "QueenTime" condition is inactive, false otherwise.
     */
    public boolean isQueenTime() {
        return !queenTime;
    }

    /**
     * Activates the "QueenTime" condition by setting the internal `queenTime` flag to true.
     * This method updates the game state to reflect that the "QueenTime" condition
     * is currently active. It is typically used as part of the game's state management
     * to signify specific gameplay conditions or phases associated with the "QueenTime" event.
     */
    public void setQueenTime() {
        this.queenTime = true;
    }

    /**
     * Resets the "QueenTime" condition in the game state by setting the
     * internal `queenTime` flag to false.
     *
     * This method is typically used to indicate that the "QueenTime"
     * condition is no longer active. It is intended to reset the state
     * at the end of a game phase or when initializing the game state
     * for a new round or session.
     */
    public void resetQueenTime(){
        queenTime = false;
    }

    /**
     * Reverses the game's turn order.
     *
     * This method changes the direction of the turn order by negating the
     * current value of the `order` field. If the current turn order is
     * positive, it will become negative, and if it's negative, it will
     * become positive. This effectively switches the game's flow of turns
     * between clockwise and counterclockwise directions.
     */
    public void setReverseOrder(){
        order = order * -1;
    }

    /**
     * Returns a string representation of the GameStatusManager object.
     * The string contains the values of the key fields in the current game state,
     * including user action status, turn-related flags, time, and the current turn.
     *
     * @return a string representation of the current state of the GameStatusManager instance.
     */
    @Override
    public String toString() {
        return "GameStatusManager{" +
                "userDid=" + userDid +
                ", passTurn=" + passTurn +
                ", fourTime=" + fourTime +
                ", time=" + time +
                ", turn=" + turn +
                "}\n";
    }
}
