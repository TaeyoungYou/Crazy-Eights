package app.model.multi;

public class MessageParser {

    public static ParsedMessage parse(String message) throws InvalidMessageFormatException {
        if(message == null || message.trim().isEmpty()) {
            throw new InvalidMessageFormatException("메시지가 비어있거나 null입니다.");
        }

        String[] tokens = message.split("#");
        if(tokens.length != 3) {
            throw new InvalidMessageFormatException("메시지 형식이 잘못되었습니다: 정확히 3개의 토큰이 필요합니다. 수신한 메시지: " + message);
        }

        int senderPlayerId;
        try {
            senderPlayerId = Integer.parseInt(tokens[0].trim());
        } catch(NumberFormatException e) {
            throw new InvalidMessageFormatException("senderPlayerId가 숫자가 아닙니다: " + tokens[0]);
        }

        MsgType msgType;
        try {
            msgType = MsgType.valueOf(tokens[1].trim());
        } catch(IllegalArgumentException e) {
            throw new InvalidMessageFormatException("유효하지 않은 메시지 타입(MsgType)입니다: " + tokens[1]);
        }

        String data = tokens[2].trim();
        return new ParsedMessage(senderPlayerId, msgType, data);
    }

    public static class ParsedMessage {
        private final int senderPlayerId;
        private final MsgType msgType;
        private final String data;

        public ParsedMessage(int senderPlayerId, MsgType msgType, String data) {
            this.senderPlayerId = senderPlayerId;
            this.msgType = msgType;
            this.data = data;
        }

        public int getSenderPlayerId() {
            return senderPlayerId;
        }

        public MsgType getMsgType() {
            return msgType;
        }

        public String getData() {
            return data;
        }
    }

    public static class InvalidMessageFormatException extends Exception {
        public InvalidMessageFormatException(String msg) {
            super(msg);
        }
    }
}