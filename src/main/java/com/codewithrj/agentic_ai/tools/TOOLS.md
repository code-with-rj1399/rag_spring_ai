# Tools With Spring AI

A simple **Spring AI Tool Calling** project using Java, Spring Boot, and Ollama.

The project demonstrates how an LLM can use custom Java methods as tools.

## Architecture

```text
User Question
      ↓
  ChatClient
      ↓
      LLM
      ↓
  Tool Selection
   ↙        ↘
Budget Tool  Math Tool
   ↓           ↓
VectorStore   Java Method
   ↓           ↓
Tool Result ←──┘
      ↓
     LLM
      ↓
    Answer
```

## Tools

### BudgetSearchTool

Searches the budget document using the configured `VectorStore`.

```java
@Tool(description = "Search the 2024-25 Indian budget document")
public String searchBudget(String query) {
    // Vector similarity search
}
```

### MathematicalTool

Provides a simple calculation capability.

```java
@Tool(description = "Use this tool to add 2 numbers")
int addTwoNumbers(int a, int b) {
    return a + b;
}
```

## Agent

`QAAgent` registers both tools with `ChatClient`:

```java
chatClient
    .prompt()
    .user(question)
    .tools(budgetSearchTool, mathematicalTool)
    .call();
```

The LLM decides whether a tool is required and which tool to invoke.

## API


http://localhost:8080/

```bash
curl "http://localhost:8080/qa-agent?query=What is 25 plus 17?"
```

## RAG + Tool Calling

RAG provides **knowledge** to the LLM.

Tool Calling provides **capabilities/actions** to the LLM.

```text
RAG → Retrieve information
Tools → Perform actions
LLM → Decide what it needs
```

## Mental Model

```text
LLM
 ↓
Prompt Engineering
 ↓
RAG
 ↓
Tool Calling
 ↓
AI Agent
 ↓
Agentic AI
```
