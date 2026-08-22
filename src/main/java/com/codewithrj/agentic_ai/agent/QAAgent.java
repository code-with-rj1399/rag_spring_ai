package com.codewithrj.agentic_ai.agent;


import com.codewithrj.agentic_ai.tools.BudgetSearchTool;
import com.codewithrj.agentic_ai.tools.MathematicalTool;
import com.codewithrj.agentic_ai.tools.PdfTool;
import com.codewithrj.agentic_ai.tools.WikipediaTool;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class QAAgent {
    private final ChatClient chatClient;
    private final ChatMemory chatMemory;
    private final BudgetSearchTool budgetSearchTool;
    private final MathematicalTool mathematicalTool;
    private final WikipediaTool wikipediaTool;
    private final PdfTool pdfTool;


    public QAAgent(@Qualifier("googleGenAiChatModel") ChatModel chatModel,
                   ChatMemory chatMemory,
                   BudgetSearchTool budgetSearchTool,
                   MathematicalTool mathematicalTool,
                   WikipediaTool wikipediaTool,
                   PdfTool pdfTool
                   ){
        this.pdfTool = pdfTool;
        this.chatClient = ChatClient.builder(chatModel)
                .defaultAdvisors(
                        MessageChatMemoryAdvisor.builder(chatMemory)
                                .build()
                )
                .build();
        this.chatMemory = chatMemory;
        this.budgetSearchTool = budgetSearchTool;
        this.mathematicalTool = mathematicalTool;
        this.wikipediaTool = wikipediaTool;
    }

    public String ask(String conversationId, String question){
        ChatClient.CallResponseSpec callResponseSpec = chatClient
                .prompt()
                .user(question)
                .tools(budgetSearchTool, mathematicalTool, wikipediaTool, pdfTool)
                .advisors(
                        advisor -> advisor.param(
                                ChatMemory.CONVERSATION_ID,
                                conversationId
                        )
                )
                .call();

        return callResponseSpec.content() +"\n\n\n" +
                "conversationId " + conversationId +"\n\n\n"+
                formatChatResponse(callResponseSpec.chatResponse());
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
