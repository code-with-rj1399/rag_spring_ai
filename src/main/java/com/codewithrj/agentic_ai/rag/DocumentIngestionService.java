package com.codewithrj.agentic_ai.rag;


import org.springframework.ai.document.Document;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.transformer.splitter.TextSplitter;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DocumentIngestionService implements CommandLineRunner {

    private final VectorStore vectorStore;

    @Value("classpath:/budget/Budget_Speech_2024-2025.txt")
    private Resource budgetTextSource;

    @Value("classpath:/budget/Union Budget 2023-24 Analysis.pdf")
    private Resource budgetPdfSource;

    public DocumentIngestionService(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    @Override
    public void run(String... args) {

        System.out.println("Starting document ingestion...");

        List<Document> allDocuments = new ArrayList<>();


        // Reading text file
        TextReader textReader = new TextReader(budgetTextSource);
        textReader.getCustomMetadata()
                .put("fileName", "budget/Budget_Speech_2024-2025.txt");
        List<Document> textDocuments = textReader.get();
        allDocuments.addAll(textDocuments);

        // Reading text file
        PagePdfDocumentReader pagePdfDocumentReader = new PagePdfDocumentReader(budgetPdfSource);
        List<Document> pdfDocuments = pagePdfDocumentReader.get();
        allDocuments.addAll(pdfDocuments);



        TextSplitter textSplitter = new TokenTextSplitter();
        List<Document> splitDocuments = textSplitter.apply(allDocuments);
        vectorStore.add(splitDocuments);

    }
}