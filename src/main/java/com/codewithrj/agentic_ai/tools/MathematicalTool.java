package com.codewithrj.agentic_ai.tools;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

@Component
public class MathematicalTool {

    @Tool(description = """
              Adds exactly two numbers together.
                Use this tool only when the user explicitly asks to
                calculate the sum of two numbers.
                Do not use this tool for general conversation,
                personal information, greetings, or unrelated questions.
            """)
    int addTwoNumbers(int a, int b){
        return a+b;
    }
}
