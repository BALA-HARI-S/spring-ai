package ai.spring.demo_ai.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ai")
public class DemoController {

    // client interface that interacts with a chatbot or AI service to process and respond to user inputs.
    private final ChatClient chatClient;

    public DemoController(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    @GetMapping("/prompt-user/youtube")
    public String call(@RequestParam(value = "genre", defaultValue = "tech") String genre) {

        String message = """
                List 10 of the most popular YouTubers in {genre} along with their current subscriber counts. If you don't know
                the answer , just say "I don't know".
                """;

        return this.chatClient.prompt().user(u -> u.text(message).param("genre", genre)) // sets the text of the prompt to the multi-line string message
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

}
