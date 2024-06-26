package ai.spring.demo_ai.controller;

import ai.spring.demo_ai.entity.Author;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/ai")
public class DemoController {

    // client interface that interacts with a chatbot or AI service to process and respond to user inputs.
    private final ChatClient chatClient;

    @Value("classpath:/prompts/youtube.st")
    private Resource ytPrompt;

    @Value("classpath:/prompts/list-prompt.st")
    private Resource listPrompt;

    public DemoController(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    @GetMapping("/prompt/string")
    public String getYoutubers(@RequestParam(value = "genre", defaultValue = "tech") String genre) {

        return this.chatClient.prompt()
                .user(u -> u.text(ytPrompt).param("genre", genre)) // sets the text of the prompt to the multi-line string message
                .call() // send the prompt to the AI and gets the response back
                .content(); // extract the output
    }

//    @GetMapping("/prompt-user/joke")
//    public String call(@RequestParam(value = "genre", defaultValue = "tell me a simple joke") String genre) {
//
//        String message = """
//            List 10 of the most popular YouTubers in {genre} along with their current subscriber counts. If you don't know
//            the answer , just say "I don't know".
//            """;
//        PromptTemplate promptTemplate = new PromptTemplate(message);
//        Prompt prompt = promptTemplate.create(Map.of("genre",genre));
//        return this.chatClient.prompt(prompt)
//                .call()
//                .content();
//    }

    // StructuredOutputConverter - BeanOutputConverter, ListOutputConverter and MapOutputConverter

    // ListOutputConverter
    @GetMapping("/prompt/list")
    public List<Author> getListOfAuthors() {
        return this.chatClient.prompt()
                .user(u -> u.text(listPrompt))
                .call()
                .entity(new ParameterizedTypeReference<>() {});
    }

    // BeanOutputConverter
    @GetMapping("/prompt/list")
    public Author getAuthor() {
        return this.chatClient.prompt()
                .user(u -> u.text(listPrompt))
                .call()
                .entity(Author.class);
    }
}
