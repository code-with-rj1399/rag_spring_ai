package com.codewithrj.agentic_ai.tools;


import org.springframework.ai.document.Document;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class BudgetSearchTool {

    private final VectorStore vectorStore;

    public BudgetSearchTool(VectorStore vectorStore){
        this.vectorStore = vectorStore;
    }


    @Tool(description = "Search the Indian budget documents for relevant information")
    public String searchBudget(String query){
        List<Document> documentList = vectorStore.similaritySearch(query);
        return documentList.stream()
                .map(Document::getText)
                .collect(Collectors.joining("\n\n"));
    }
}
