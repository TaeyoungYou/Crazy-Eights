package app.model.multi;

/**
 * The MessageParser class is responsible for parsing messages into structured
 * objects of type ParsedMessage. It enforces a specific message format and validates
 * the input against message type definitions and other constraints.
 */
public class MessageParser {

    /**
     * Parses the given message string into a {@link ParsedMessage} object. This method expects the message
     * to follow a specific format: three tokens separated by the "#" character. The first token should
     * be a numeric sender ID, the second token should be a valid {@link MsgType}, and the third token
     * represents message-specific data.
     *
     * @param message the string containing the message to be parsed, formatted as "senderPlayerId#msgType#data"
     * @return a {@link ParsedMessage} object containing the parsed senderPlayerId, msgType, and data
     * @throws InvalidMessageFormatException if the message is null, empty, improperly formatted,
     *                                       or contains invalid values (e.g., non-numeric senderPlayerId or
     *                                       invalid {@link MsgType})
     */
    public static ParsedMessage parse(String message) throws InvalidMessageFormatException {
        if (message == null || message.trim().isEmpty()) {
            throw new InvalidMessageFormatException("메시지가 비어있거나 null입니다.");
        }

        String[] tokens = message.split("#");
        if (tokens.length != 3) {
            throw new InvalidMessageFormatException("메시지 형식이 잘못되었습니다: 정확히 3개의 토큰이 필요합니다. 수신한 메시지: " + message);
        }

        int senderPlayerId;
        try {
            senderPlayerId = Integer.parseInt(tokens[0].trim());
        } catch (NumberFormatException e) {
            throw new InvalidMessageFormatException("senderPlayerId가 숫자가 아닙니다: " + tokens[0]);
        }

        MsgType msgType;
        try {
            msgType = MsgType.valueOf(tokens[1].trim());
        } catch (IllegalArgumentException e) {
            throw new InvalidMessageFormatException("유효하지 않은 메시지 타입(MsgType)입니다: " + tokens[1]);
        }

        String data = tokens[2].trim();
        return new ParsedMessage(senderPlayerId, msgType, data);
    }

    /**
     * The ParsedMessage class represents a parsed message containing a sender's player ID, a message type,
     * and associated data. This class is typically used to interpret and encapsulate parsed message
     * components extracted from a formatted message string.
     */
    public static class ParsedMessage {
        private final int senderPlayerId;
        private final MsgType msgType;
        private final String data;

        /**
         * Constructs a ParsedMessage instance with the specified sender player ID, message type, and data.
         *
         * @param senderPlayerId the ID of the sender player associated with this message
         * @param msgType        the type of the message, represented by an enum constant of MsgType
         * @param data           the additional data or content carried by this message
         */
        public ParsedMessage(int senderPlayerId, MsgType msgType, String data) {
            this.senderPlayerId = senderPlayerId;
            this.msgType = msgType;
            this.data = data;
        }

        /**
         * Retrieves the sender player ID associated with this parsed message.
         *
         * @return the ID of the sender player as an integer.
         */
        public int getSenderPlayerId() {
            return senderPlayerId;
        }

        /**
         * Retrieves the message type associated with this parsed message.
         *
         * @return the message type as an instance of the MsgType enum.
         */
        public MsgType getMsgType() {
            return msgType;
        }

        /**
         * Retrieves the data associated with this parsed message.
         *
         * @return the data content of the parsed message as a String.
         */
        public String getData() {
            return data;
        }
    }

    /**
     * An exception that is thrown when a message fails to meet the expected format
     * during parsing. This exception is typically used to signal that the input
     * string does not conform to the required structure or contains invalid data.
     * <p>
     * This exception may occur under various circumstances, such as:
     * - The message string is null or empty.
     * - The message is improperly formatted (e.g., missing tokens or invalid separator).
     * - The senderPlayerId within the message is not a valid numeric value.
     * - The message contains an invalid {@code MsgType}.
     */
    public static class InvalidMessageFormatException extends Exception {
        /**
         * Constructs a new InvalidMessageFormatException with the specified detail message.
         *
         * @param msg the detail message providing additional context about the exception.
         */
        public InvalidMessageFormatException(String msg) {
            super(msg);
        }
    }
}