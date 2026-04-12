package com.devestudo.projeto_financas.config;

import com.devestudo.projeto_financas.ai.AssistantTools;
import com.devestudo.projeto_financas.services.AssistantAiService;
import dev.langchain4j.model.googleai.GoogleAiGeminiChatModel;
import dev.langchain4j.service.AiServices;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AssistantConfig {

    @Value("${langchain4j.google-ai-gemini.api-key}")
    private String geminiApiKey;

    @Value("${langchain4j.google-ai-gemini.model-name}")
    private String geminiModel;

        @Bean
        public GoogleAiGeminiChatModel googleAiGeminiChatModel() {
            return GoogleAiGeminiChatModel.builder()
                    .apiKey(geminiApiKey)
                    .modelName(geminiModel)
                    .build();
        }

        @Bean
        public AssistantAiService assistant(GoogleAiGeminiChatModel model, AssistantTools assistantTools) {
            return AiServices.builder(AssistantAiService.class)
                    .chatModel(model)
                    .tools(assistantTools) // registra a ferramenta no Serviço IA
                    .build();
        }
    }

