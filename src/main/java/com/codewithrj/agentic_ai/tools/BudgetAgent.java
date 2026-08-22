package com.codewithrj.agentic_ai.tools;


import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Component;

@Component
public class BudgetAgent {
    private final ChatClient chatClient;
    private final BudgetSearchTool budgetSearchTool;

    public BudgetAgent(ChatClient.Builder chatClientBuilder, BudgetSearchTool budgetSearchTool){
        this.chatClient = chatClientBuilder.build();
        this.budgetSearchTool = budgetSearchTool;
    }

    public String ask(String question){
        return chatClient
                .prompt()
                .user(question)
                .tools(budgetSearchTool)
                .call().content();
    }
}
