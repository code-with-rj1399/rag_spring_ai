package com.codewithrj.agentic_ai.tools;


import com.codewithrj.agentic_ai.rag.DocumentIngestionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PdfTool {

    private static final Logger log = LoggerFactory.getLogger(DocumentIngestionService.class);

    private final VectorStore vectorStore;

    public PdfTool(VectorStore vectorStore){
        this.vectorStore = vectorStore;
    }


    @Tool(description = "Use this tool to create PDF with title  'title' and content as 'content'")
    public String createPdf(String title, String content){
        log.info("Creating PDF with title {} \n \"And content \\n\\n{}\\n\\n\"", title, content);
        return "Done";
    }
}
