package com.codewithrj.RAG.WIth.Spring.AI;


import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.logging.Logger;

@RestController
public class BudgetController {

    private final Logger logger = Logger.getLogger(BudgetController.class.getName());

    private ChatClient chatClient;
    private VectorStore vectorStore;

    public BudgetController(ChatClient.Builder builder, VectorStore vectorStore){
        this.chatClient = builder
                .defaultAdvisors(QuestionAnswerAdvisor.builder(vectorStore).build())
                .build();
        this.vectorStore = vectorStore;

    }


    @GetMapping("/budget")
    public String BudgetQandA(@RequestParam(value = "message") String query){
        return chatClient.prompt()
                .user(query)
                .call().content();
    }
}
