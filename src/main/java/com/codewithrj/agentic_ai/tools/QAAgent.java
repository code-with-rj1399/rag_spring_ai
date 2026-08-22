package com.codewithrj.agentic_ai.tools;


import com.codewithrj.agentic_ai.tools.models.WikipediaTool;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Component;

@Component
public class QAAgent {
    private final ChatClient chatClient;
    private final BudgetSearchTool budgetSearchTool;
    private final MathematicalTool mathematicalTool;
    private final WikipediaTool wikipediaTool;


    public QAAgent(ChatClient.Builder chatClientBuilder, BudgetSearchTool budgetSearchTool, MathematicalTool mathematicalTool, WikipediaTool wikipediaTool){
        this.chatClient = chatClientBuilder.build();
        this.budgetSearchTool = budgetSearchTool;
        this.mathematicalTool = mathematicalTool;
        this.wikipediaTool = wikipediaTool;
    }

    public String ask(String question){
        ChatClient.CallResponseSpec callResponseSpec = chatClient
                .prompt()
                .user(question)
                .tools(budgetSearchTool, mathematicalTool, wikipediaTool)
                .call();

        return callResponseSpec.content() +"\n\n\n" + callResponseSpec.chatResponse();
    }
}
