package com.codewithrj.agentic_ai.rag;


import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.transformer.splitter.TextSplitter;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.File;
import java.util.List;
import java.util.logging.Logger;

@Configuration
public class Config {

    private final Logger logger = Logger.getLogger(Config.class.getName());

    @Value("classpath:/Budget_Speech.txt")
    private Resource resource;

    @Bean
    SimpleVectorStore simpleVectorStore(EmbeddingModel embeddingModel){
        SimpleVectorStore vectorStore = SimpleVectorStore.builder(embeddingModel)
                .build();
        File vectorStoreFile = new File("/Users/rajesh/repos/rag_spring_ai/src/main/resources/vectorstore.json");
        if(vectorStoreFile.exists()){
            System.out.println("Load vector file");
            vectorStore.load(vectorStoreFile);
        }else{
            System.out.println("Creating Vector file");
            TextReader textReader = new TextReader(resource);
            textReader.getCustomMetadata()
                    .put("fileName", "Budget_Speech.txt");
            List<Document> documents = textReader.get();

            TextSplitter textSplitter = new TokenTextSplitter();
            List<Document> splitDocuments = textSplitter.apply(documents);
            vectorStore.add(splitDocuments);
        }

        return vectorStore;
    }
}
