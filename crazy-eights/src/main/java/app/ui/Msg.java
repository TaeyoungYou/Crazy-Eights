/**
 * Represents a message with a status, which can be either a normal status or an error.
 * This class is used to store and manage messages related to game status updates.
 */
package app.ui;

/**
 * Class for handling messages with different statuses.
 */
public class Msg {

    /**
     * Enum representing the type of message status.
     */
    public enum status {status, error}

    ;

    private String msg;
    private status msg_status;

    /**
     * Constructs a Msg object with the given message and status.
     *
     * @param msg        The message content.
     * @param msg_status The status of the message (either status or error).
     */
    public Msg(String msg, status msg_status) {
        this.msg = msg;
        this.msg_status = msg_status;
    }

    /**
     * Retrieves the message content.
     *
     * @return The message string.
     */
    public String getMsg() {
        return msg;
    }

    /**
     * Retrieves the status of the message.
     *
     * @return The status of the message.
     */
    public status getMsg_status() {
        return msg_status;
    }

    /**
     * Sets the message content.
     *
     * @param msg The new message content.
     */
    public void setMsg(String msg) {
        this.msg = msg;
    }

    /**
     * Sets the status of the message.
     *
     * @param msg_status The new status of the message.
     */
    public void setMsg_status(status msg_status) {
        this.msg_status = msg_status;
    }
}
