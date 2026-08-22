package com.codewithrj.agentic_ai.tools;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AgentController {

    private QAAgent qaAgent;

    public AgentController(QAAgent qaAgent) {
        this.qaAgent = qaAgent;
    }


    @GetMapping("/qa-agent")
    String budgestQuestion(@RequestParam String query){
        return qaAgent.ask(query);
    }
}
