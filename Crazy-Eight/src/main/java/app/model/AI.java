package app.model;

import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.service.OpenAiService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class AI {
    private final static String key = getAPI();

    private final static OpenAiService service = new OpenAiService(key);

    public static String generate(String message, Player player){
        List<ChatMessage> messages = new ArrayList<ChatMessage>();
        String systemMessage = "You are a friendly and casual player. Respond naturally, as if you were chatting with a friend. Use contractions, informal expressions, and sometimes even humor. Keep your responses short, exactly one sentence only, under 30 characters. Each response must be unique and should not repeat previous ideas, phrases, or sentence structures.";
        systemMessage += switch(player.getPersonality()){
            case Sarcastic -> " You are sarcastic and witty. Make playful and teasing comments, but keep them lighthearted.";
            case Friendly -> " You are friendly and supportive. Encourage and engage positively with other players.";
            case Serious -> " You are focused and strategic. Your responses should be analytical and logical.";
            case Flirty -> " You are playful and charming. Your responses should be teasing but lighthearted.";
            case Lazy -> " You are laid-back and unenthusiastic. Keep your responses short and dismissive.";
            case Edgy -> " You are direct, blunt, and sometimes use mild profanity for humor. Be playful, but don’t be too offensive.";
        };

        messages.add(new ChatMessage("system", "Current game situation: " + systemMessage));
        messages.add(new ChatMessage("user", message));

        ChatCompletionRequest request = ChatCompletionRequest
                .builder()
                .model("gpt-3.5-turbo")
                .messages(messages)
                .maxTokens(50)
                .temperature(0.8)
                .build();

        return service.createChatCompletion(request)
                .getChoices()
                .getFirst()
                .getMessage()
                .getContent().trim();
    }

    private static String getAPI(){
        Properties prop = new Properties();
        try{
            prop.load(AI.class.getClassLoader().getResourceAsStream("config.properties"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return prop.getProperty("OPENAI_API_KEY");
    }
}
