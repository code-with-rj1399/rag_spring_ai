package com.codewithrj.agentic_ai.tools;


import com.codewithrj.agentic_ai.tools.models.WikipediaTool;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
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

        return callResponseSpec.content() +"\n\n\n" + formatChatResponse(callResponseSpec.chatResponse());
    }

    private String formatChatResponse(ChatResponse chatResponse) {

        if(chatResponse == null)
                return "";

        StringBuilder result = new StringBuilder();

        result.append("─── Agent Details ───\n\n");

        result.append("Tool Calls:\n");

        chatResponse.getResult()
                .getOutput()
                .getToolCalls()
                .forEach(toolCall -> {

                    result.append("🔧 ")
                            .append(toolCall.name())
                            .append("\n");

                    result.append("   Arguments: ")
                            .append(toolCall.arguments())
                            .append("\n\n");
                });

        result.append("Metadata:\n");

        result.append("   Prompt Tokens: ")
                .append(chatResponse.getMetadata().getUsage().getPromptTokens())
                .append("\n");

        result.append("   Completion Tokens: ")
                .append(chatResponse.getMetadata().getUsage().getCompletionTokens())
                .append("\n");

        result.append("   Total Tokens: ")
                .append(chatResponse.getMetadata().getUsage().getTotalTokens())
                .append("\n");

        return result.toString();
    }
}
