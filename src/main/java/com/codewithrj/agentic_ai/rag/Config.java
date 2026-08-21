package com.codewithrj.agentic_ai.rag;


import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.transformer.splitter.TextSplitter;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Configuration
public class Config {

    private final Logger logger = Logger.getLogger(Config.class.getName());

    @Value("classpath:/budget/Budget_Speech_2024-2025.txt")
    private Resource budgetTextSource;

    @Value("classpath:/budget/Union Budget 2023-24 Analysis.pdf")
    private Resource budgetPdfSource;

//    @Bean
//    SimpleVectorStore simpleVectorStore(EmbeddingModel embeddingModel){
//        SimpleVectorStore vectorStore = SimpleVectorStore.builder(embeddingModel)
//                .build();
//
//        List<Document> allDocuments = new ArrayList<>();
//
//
//        // Reading text file
//        TextReader textReader = new TextReader(budgetTextSource);
//        textReader.getCustomMetadata()
//                .put("fileName", "budget/Budget_Speech_2024-2025.txt");
//        List<Document> textDocuments = textReader.get();
//        allDocuments.addAll(textDocuments);
//
//        // Reading text file
//        PagePdfDocumentReader pagePdfDocumentReader = new PagePdfDocumentReader(budgetPdfSource);
//        List<Document> pdfDocuments = pagePdfDocumentReader.get();
//        allDocuments.addAll(pdfDocuments);
//
//
//
//        TextSplitter textSplitter = new TokenTextSplitter();
//        List<Document> splitDocuments = textSplitter.apply(allDocuments);
//        vectorStore.add(splitDocuments);
//
//        return vectorStore;
//    }


}
