package com.codewithrj.agentic_ai.tools;


import com.codewithrj.agentic_ai.agent.QAAgent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class AgentController {

    private QAAgent qaAgent;

    private static UUID uuid;

    public AgentController(QAAgent qaAgent) {
        this.qaAgent = qaAgent;
    }


    @GetMapping("/qa-agent")
    String budgestQuestion(@RequestParam String query){
        String conversationID = null;
        if(uuid == null){
            uuid = UUID.randomUUID();
        }
        conversationID = uuid.toString();
        uuid = UUID.randomUUID();
        return qaAgent.ask(conversationID, query);
    }
}
