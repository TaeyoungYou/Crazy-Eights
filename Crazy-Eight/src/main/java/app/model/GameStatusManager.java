package app.model;

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

    public GameStatusManager(int playerNumber) {
        this.userDid = false;
        this.passTurn = false;
        this.time = 0;
        this.turn = -1;
        this.order = 1;
        this.fourTime = false;
        this.playerNumber = playerNumber;
    }

    public boolean isUserDid() {
        return userDid;
    }

    public void resetUserDid() {
        userDid = false;
    }

    public void doUserDid(){
        userDid = true;
    }

    public boolean isPassTurn() {
        return passTurn;
    }

    public void resetPassTurn(){
        this.passTurn = false;
    }

    public void doPassTurn() {
        this.passTurn = true;
    }
    public int getTime() {
        return time;
    }

    public void addTime(){
        time++;
    }

    public void resetTime(){
        time = 0;
    }

    public int getTurn() {
        return turn;
    }

    public void nextTurn(){
        if(order < 0){
            turn = ((turn + order) % playerNumber + playerNumber) % playerNumber;
        } else {
            turn = (turn+order)%playerNumber;
        }
    }

    public boolean isFourTime() {
        return !fourTime;
    }

    public void setFourTime() {
        this.fourTime = true;
    }

    public void resetFourTime(){
        fourTime = false;
    }

    public boolean isQueenTime() {
        return !queenTime;
    }

    public void setQueenTime() {
        this.queenTime = true;
    }

    public void resetQueenTime(){
        queenTime = false;
    }

    public void setReverseOrder(){
        order = order * -1;
    }

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
