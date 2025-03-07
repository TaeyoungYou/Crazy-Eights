
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.service.OpenAiService;

import java.util.Arrays;

public class OpenAiExample {
    public static void main(String[] args) {
        String apiKey = "너의_OPENAI_API_KEY"; // OpenAI API 키 입력
        OpenAiService service = new OpenAiService(apiKey);

        ChatCompletionRequest request = ChatCompletionRequest.builder()
                .model("gpt-3.5-turbo")
                .messages(Arrays.asList(new ChatMessage("user", "Hello, how are you?")))
                .maxTokens(50)
                .build();

        service.createChatCompletion(request)
                .getChoices()
                .forEach(choice -> System.out.println(choice.getMessage().getContent()));
    }
}


