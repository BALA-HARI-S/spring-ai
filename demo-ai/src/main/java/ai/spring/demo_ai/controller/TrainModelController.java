package ai.spring.demo_ai.controller;

import ai.spring.demo_ai.service.VectorStoreService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/ai/model")
public class TrainModelController {

    @Value("classpath:/prompts/pdf-to-file-prompt.st")
    private Resource pdfToFilePrompt;

    private final VectorStoreService vectorStoreService;

    public TrainModelController(VectorStoreService vectorStoreService) {
        this.vectorStoreService = vectorStoreService;
    }

    @PostMapping("/train")
    public void trainModel(@RequestParam(value = "pdf") Resource pdfFile) throws IOException {
        vectorStoreService.trainModel(pdfFile);
    }

    @GetMapping("/test")
    public String testModel(@RequestParam(value = "message", defaultValue = "List tools to compile a .tex file to a .pdf file") String message) {
        return vectorStoreService.testModel(message, pdfToFilePrompt);
    }
}
