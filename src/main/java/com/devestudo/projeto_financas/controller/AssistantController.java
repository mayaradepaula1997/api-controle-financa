package com.devestudo.projeto_financas.controller;


import com.devestudo.projeto_financas.services.AssistantAiService;
import dev.langchain4j.service.Result;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/assistant")
public class AssistantController {

    private final AssistantAiService assistantAiService;

    //Injeção de dependencia
    public AssistantController(AssistantAiService assistantAiService) {
        this.assistantAiService = assistantAiService;
    }

    @PostMapping
    public ResponseEntity<?> askAssistant(@RequestBody String userMessage) {
        try {
            Result<String> result = assistantAiService.handleRequest(userMessage);

            return ResponseEntity.ok(result.content());

        } catch (Exception e) {
            e.printStackTrace(); // ISSO É O MAIS IMPORTANTE
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
