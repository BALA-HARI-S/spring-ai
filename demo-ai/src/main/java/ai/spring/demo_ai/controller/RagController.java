package ai.spring.demo_ai.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/ai")
public class RagController {

    @Value("classpath:/prompts/olympics-prompt.st")
    private Resource olympicsPrompt;

    private ChatClient chatClient;
    private VectorStore vectorStore;

    public RagController(ChatClient.Builder builder, VectorStore vectorStore) {
        this.chatClient = builder
                .defaultAdvisors(new QuestionAnswerAdvisor(vectorStore, SearchRequest.defaults()))
                .build();
        this.vectorStore = vectorStore;
    }

    @GetMapping("/prompt/olympics")
    public String faq(@RequestParam(value = "message", defaultValue = "What is summer olympics 2028") String message) {

        List<Document> documents = vectorStore.similaritySearch(SearchRequest.query(message));
        StringBuilder stringBuilder = new StringBuilder();

        for (Document document: documents) {
            stringBuilder.append(document.getContent());
        }

        Message userMessage = new UserMessage(message);
        Message systemMessage = new SystemPromptTemplate(olympicsPrompt)
                .createMessage(Map.of("input", message, "documents", stringBuilder.toString()));
        Prompt prompt = new Prompt(List.of(systemMessage, userMessage));

        return chatClient.prompt(prompt)
                .call()
                .content();
    }
}
