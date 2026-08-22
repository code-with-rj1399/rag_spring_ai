package com.codewithrj.agentic_ai.tools;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AgentController {

    private BudgetAgent budgetAgent;

    public AgentController(BudgetAgent budgetAgent) {
        this.budgetAgent = budgetAgent;
    }


    @GetMapping("/budgent-agent")
    String budgestQuestion(@RequestParam String question){
        return budgetAgent.ask(question);
    }
}
