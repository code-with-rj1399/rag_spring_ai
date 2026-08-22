# Tools With Spring AI

A hands-on **Spring AI Tool Calling** example using Java, Spring Boot, Ollama, and custom application tools.

This project demonstrates how an LLM can decide when it needs an external capability and invoke a Java method exposed as a Spring AI `@Tool`.

## Architecture

```text
User Question
      ↓
  AgentController
      ↓
     QAAgent
      ↓
   ChatClient
      ↓
   Ollama / LLM
      ↓
  Tool Selection
   ↙         ↘
BudgetSearchTool  MathematicalTool
   ↓                 ↓
VectorStore       addTwoNumbers()
   ↓
Relevant Budget Data
      ↓
   Tool Result
      ↓
     LLM
      ↓
    Answer
```

## What This Project Demonstrates

- Spring AI `ChatClient`
- Function / tool calling
- Custom Java methods exposed with `@Tool`
- Multiple tools available to one LLM
- Retrieval exposed as a tool
- LLM-driven tool selection
- Returning tool results back to the LLM

The important idea is that the LLM does **not** directly execute Java code. Spring AI manages the tool-calling interaction: the model requests a tool, Spring AI invokes the corresponding Java method, and the result is supplied back to the model.

## Tools

### 1. BudgetSearchTool

`BudgetSearchTool` exposes a method named `searchBudget` using Spring AI's `@Tool` annotation. It searches the configured `VectorStore` for information related to the user's query and combines the retrieved document text into the tool result. fileciteturn6file0

```java
@Tool(description = "Search the 2024-25 Indian budget document for relevant information")
public String searchBudget(String query) {
    List<Document> documentList = vectorStore.similaritySearch(query);
    return documentList.stream()
            .map(Document::getText)
            .collect(Collectors.joining("\n\n"));
}
```

This is a useful pattern because an existing RAG capability can be exposed as a tool that an agent can invoke when it needs domain-specific information.

### 2. MathematicalTool

`MathematicalTool` exposes a simple Java method for addition through `@Tool`. fileciteturn7file0

```java
@Tool(description = "Use this tool to add 2 numbers")
int addTwoNumbers(int a, int b) {
    return a + b;
}
```

The LLM can choose this tool when the user's question requires adding two numbers.

## Agent

`QAAgent` creates a Spring AI `ChatClient` and registers both tools with the model. fileciteturn5file0

```java
ChatClient.CallResponseSpec callResponseSpec = chatClient
        .prompt()
        .user(question)
        .tools(budgetSearchTool, mathematicalTool)
        .call();
```

Conceptually:

```text
User Question
      ↓
    LLM
      ↓
Does the question require a tool?
      ↓
   ┌──┴──┐
  No     Yes
  ↓       ↓
Answer  Select Tool
          ↓
      Java Method
          ↓
      Tool Result
          ↓
         LLM
          ↓
        Answer
```

## API

`AgentController` exposes the agent through the `/qa-agent` endpoint and passes the user's query to `QAAgent`. fileciteturn4file0

Example:

```bash
curl "http://localhost:8080/qa-agent?query=What does the budget say about infrastructure?"
```

For a mathematical question:

```bash
curl "http://localhost:8080/qa-agent?query=What is 25 plus 17?"
```

The model can decide whether it needs the budget search tool, the mathematical tool, or neither.

## Configuration

The application currently uses Ollama locally. The configured chat model is `llama3.2:3b`, and `nomic-embed-text` is used for embeddings. Elasticsearch is configured as the vector-store backend. fileciteturn10file0

```properties
spring.ai.ollama.base-url=http://localhost:11434
spring.ai.ollama.chat.options.model=llama3.2:3b
spring.ai.ollama.embedding.options.model=nomic-embed-text

spring.elasticsearch.uris=http://localhost:9200
spring.ai.vectorstore.elasticsearch.initialize-schema=true
spring.ai.vectorstore.elasticsearch.dimensions=768
```

## Setup

Start Ollama and make sure the configured models are available:

```bash
ollama pull llama3.2:3b
ollama pull nomic-embed-text
ollama serve
```

Start Elasticsearch locally if it is not already running:

```bash
docker pull docker.elastic.co/elasticsearch/elasticsearch:9.1.0

docker run --name elasticsearch \
  -p 9200:9200 \
  -e discovery.type=single-node \
  -e xpack.security.enabled=false \
  docker.elastic.co/elasticsearch/elasticsearch:9.1.0
```

Verify Elasticsearch:

```bash
curl http://localhost:9200
```

Then start the Spring Boot application:

```bash
./mvnw spring-boot:run
```

## Tool Calling vs RAG

RAG and tool calling solve different problems, although they can work together.

### RAG

RAG retrieves relevant information and supplies it to the LLM.

```text
Question
   ↓
Embedding
   ↓
Vector Search
   ↓
Relevant Documents
   ↓
LLM
   ↓
Answer
```

### Tool Calling

Tool calling allows the LLM to decide when to invoke an external capability.

```text
Question
   ↓
LLM
   ↓
Tool Selection
   ↓
Java Tool
   ↓
Tool Result
   ↓
LLM
   ↓
Answer
```

### Combining Them

This project combines both ideas by exposing vector search through `BudgetSearchTool`.

```text
                    ┌── MathematicalTool
                    │
User → ChatClient → LLM
                    │
                    └── BudgetSearchTool
                              ↓
                         VectorStore
                              ↓
                       Relevant Documents
```

This is an important step toward building **AI agents**: the LLM is no longer limited to generating text; it can choose application capabilities based on the task.

## Key Mental Model

Remember the distinction:

> **RAG gives the LLM access to relevant knowledge. Tool calling gives the LLM access to actions or capabilities.**

A tool can perform many kinds of work:

```text
Database query
API call
Search
Calculation
File operation
Business operation
RAG retrieval
```

The LLM decides **which tool to use**, while the application owns the actual implementation and execution.

## Learning Progression

A useful progression is:

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

The next conceptual step after understanding individual tools is learning how an agent can combine multiple tools, maintain state, handle failures, and execute multi-step tasks.
