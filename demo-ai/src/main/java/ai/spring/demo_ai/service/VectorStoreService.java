package ai.spring.demo_ai.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.transformer.splitter.TextSplitter;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
public class VectorStoreService {

    private ChatClient chatClient;
    private VectorStore vectorStore;

    public VectorStoreService(ChatClient.Builder builder, VectorStore vectorStore) {
        this.chatClient = builder
                .defaultAdvisors(new QuestionAnswerAdvisor(vectorStore, SearchRequest.defaults()))
                .build();
        this.vectorStore = vectorStore;
    }
    public void trainModel(Resource pdfFile) throws IOException {
        PagePdfDocumentReader documentReader = new PagePdfDocumentReader(pdfFile);
        TextSplitter textSplitter = new TokenTextSplitter();
        List<Document> documents = textSplitter.apply(documentReader.get());
        vectorStore.add(documents);
    }
    public String testModel(String message, Resource systemPrompt) {
        List<Document> documents = vectorStore.similaritySearch(SearchRequest.query(message));
        StringBuilder stringBuilder = new StringBuilder();
        for (Document document: documents) {
            stringBuilder.append(document.getContent());
        }

        OpenAiChatOptions chatOption = OpenAiChatOptions.builder()
                .withFunction("currentWeatherFunction")
                .build();

        Message userMessage = new UserMessage(message);
        Message systemMessage = new SystemPromptTemplate(systemPrompt)
                .createMessage(Map.of("input", message, "documents", stringBuilder.toString()));
        Prompt prompt = new Prompt(List.of(systemMessage, userMessage), chatOption);
        return chatClient.prompt(prompt)
                .call()
                .content();
    }

}
