package com.lrbell.llamabot.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.ollama.OllamaChatClient;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OllamaConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(OllamaConfiguration.class);

    @Value("${spring.ai.ollama.base-url}")
    private String baseUrl;

    @Value("${spring.ai.ollama.model}")
    private String model;

    @Bean
    public OllamaChatClient ollamaChatClient() {
        logger.debug("Creating OllamaChatClient with model: {}", model);
        final OllamaApi ollamaApi = new OllamaApi(baseUrl);
        return new OllamaChatClient(ollamaApi).withModel(model);
    }

}
