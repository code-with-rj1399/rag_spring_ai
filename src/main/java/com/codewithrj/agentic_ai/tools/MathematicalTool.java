package com.codewithrj.agentic_ai.tools;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

@Component
public class MathematicalTool {

    @Tool(description = "Use this tool to add 2 numbers")
    int addTwoNumbers(int a, int b){
        return a+b;
    }
}
