package com.codewithrj.agentic_ai.tools.models;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class WikipediaService {

    private final RestClient restClient;

    public WikipediaService(RestClient.Builder restClientBuilder) {
        this.restClient = restClientBuilder
                .baseUrl("https://en.wikipedia.org")
                .defaultHeader(
                        HttpHeaders.USER_AGENT,
                        "agentic-ai-spring/1.0 (https://github.com/code-with-rj1399/agentic_ai_spring)"
                )
                .build();
    }

    public String search(String query) {

        WikipediaSearchResponse response = restClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/w/rest.php/v1/search/page")
                        .queryParam("q", query)
                        .queryParam("limit", 5)
                        .build())
                .retrieve()
                .body(WikipediaSearchResponse.class);

        if (response == null || response.pages() == null || response.pages().isEmpty()) {
            return "No Wikipedia results found for: " + query;
        }

        StringBuilder result = new StringBuilder();

        for (WikipediaPage page : response.pages()) {
            result.append("Title: ")
                    .append(page.title())
                    .append("\n");

            result.append("Description: ")
                    .append(page.description())
                    .append("\n");

            result.append("Excerpt: ")
                    .append(page.excerpt())
                    .append("\n");

            result.append("URL: ")
                    .append("https://en.wikipedia.org/wiki/")
                    .append(page.key())
                    .append("\n\n");
        }

        return result.toString();
    }
}