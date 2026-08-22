package com.codewithrj.agentic_ai.tools.models;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

@Component
public class WikipediaTool {

    private final WikipediaService wikipediaService;

    public WikipediaTool(WikipediaService wikipediaService) {
        this.wikipediaService = wikipediaService;
    }

    @Tool(description = "Search Wikipedia for information about a topic")
    public String searchWikipedia(String query) {
        return wikipediaService.search(query);
    }
}