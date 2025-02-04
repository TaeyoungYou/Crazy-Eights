package app.ui;

public class Msg {
    public enum status {status, error};

    private String msg;
    private status msg_status;

    public Msg(String msg, status msg_status) {
        this.msg = msg;
        this.msg_status = msg_status;
    }

    public String getMsg() {
        return msg;
    }
    public status getMsg_status() {
        return msg_status;
    }
    public void setMsg(String msg) {
        this.msg = msg;
    }
    public void setMsg_status(status msg_status) {
        this.msg_status = msg_status;
    }
}
