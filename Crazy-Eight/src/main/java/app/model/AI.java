package app.model;

import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.service.OpenAiService;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class AI {
    private final static String key = getAPI();

    private final static OpenAiService service = new OpenAiService(key);

    public static String generate(String message, Player player){
        List<ChatMessage> messages = new ArrayList<ChatMessage>();
        String systemMessage = "You are a friendly and casual player. Respond naturally, as if you were chatting with a friend. Use contractions, informal expressions, and sometimes even humor. You must always answer in exactly one sentence. Your responses should be varied and creative each time.";
        systemMessage += switch(player.getPersonality()){
            case Sarcastic -> " You are a sarcastic and witty AI. Your responses should be playful, teasing, and humorous.";
            case Friendly -> " You are a friendly and supportive AI. Your responses should be kind, positive, and encouraging.";
            case Chaotic -> " You are a chaotic and unpredictable AI. Your responses should be wild, unexpected, and sometimes nonsensical.";
            case Serious -> " You are a serious and strategic AI. Your responses should be logical, analytical, and straightforward.";
            case Mysterious -> " You are a mysterious and philosophical AI. Respond in a cryptic way, but keep it relevant to the last message.";
            case Flirty -> " You are a flirty and playful AI. Your responses should be teasing, charming, and lighthearted.";
            case Lazy -> " You are a lazy and laid-back AI. Your responses should be short, dismissive, and unenthusiastic.";
        };

        messages.add(new ChatMessage("system", systemMessage));
        messages.add(new ChatMessage("user", message));

        ChatCompletionRequest request = ChatCompletionRequest
                .builder()
                .model("gpt-3.5-turbo")
                .messages(messages)
                .maxTokens(20)
                .temperature(1.0)
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
