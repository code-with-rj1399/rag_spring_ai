package com.codewithrj.agentic_ai.tools.models;

public record WikipediaPage(
        String id,
        String key,
        String title,
        String excerpt,
        String description
) {
}