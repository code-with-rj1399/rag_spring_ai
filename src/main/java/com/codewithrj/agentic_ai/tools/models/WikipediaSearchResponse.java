package com.codewithrj.agentic_ai.tools.models;

import java.util.List;

public record WikipediaSearchResponse(
        List<WikipediaPage> pages
) {
}