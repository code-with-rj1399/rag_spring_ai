package com.codewithrj.agentic_ai.tools;


import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Component;

@Component
public class QAAgent {
    private final ChatClient chatClient;
    private final BudgetSearchTool budgetSearchTool;
    private final MathematicalTool mathematicalTool;


    public QAAgent(ChatClient.Builder chatClientBuilder, BudgetSearchTool budgetSearchTool, MathematicalTool mathematicalTool){
        this.chatClient = chatClientBuilder.build();
        this.budgetSearchTool = budgetSearchTool;
        this.mathematicalTool = mathematicalTool;
    }

    public String ask(String question){
        return chatClient
                .prompt()
                .user(question)
                .tools(budgetSearchTool, mathematicalTool)
                .call().content();
    }
}
