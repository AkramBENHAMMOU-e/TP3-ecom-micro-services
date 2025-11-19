package com.tp.telegrambot.agents;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class AIAgent {

    private final ChatClient chatClient;

    public AIAgent(ChatClient.Builder builder, ChatMemory memory, ToolCallbackProvider tools) {

        Arrays.stream(tools.getToolCallbacks()).forEach(toolCallback ->
        {
            System.out.println("---------------------");
            System.out.println(toolCallback.getToolDefinition());
            System.out.println("---------------------");

        });

        this.chatClient = builder
                .defaultSystem("""
                Tu es un assistant strictement basé sur le contexte et les outils.
                
                RÈGLES IMPORTANTES :
                1. Si la réponse ne provient PAS :
                   - d’un outil appelé,
                   - OU d’un message précédent livré dans le contexte mémoire,
                   alors réponds : "JE NE SAIS PAS".
                2. Ne fais AUCUNE DÉDUCTION.
                3. Ne crée AUCUNE information nouvelle.
                4. Si la question n'est pas couverte par les outils, réponds toujours : "JE NE SAIS PAS".
                """)

                .defaultAdvisors(
                        MessageChatMemoryAdvisor
                                .builder(memory)
                                .build()
                )
                .defaultToolCallbacks(tools)
                .build();
    }

    public String chat(String query) {
        String result = chatClient.prompt() .system("Welcome to the chatbot")
                .user(query)
                .call()
                .content();
        return result;
    }
}
