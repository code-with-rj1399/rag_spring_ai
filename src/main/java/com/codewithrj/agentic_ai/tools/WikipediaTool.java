package com.codewithrj.agentic_ai.tools;

import com.codewithrj.agentic_ai.tools.models.WikipediaService;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

@Component
public class WikipediaTool {

    private final WikipediaService wikipediaService;

    public WikipediaTool(WikipediaService wikipediaService) {
        this.wikipediaService = wikipediaService;
    }

    @Tool(description = "Use this tool when query contains words [wiki, wikipedia, wiki pedia]" )
    public String searchWikipedia(String query) {
        return wikipediaService.search(query);
    }
}