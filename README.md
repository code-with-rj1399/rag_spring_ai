# Agentic AI With Spring AI

A hands-on repository for learning **Agentic AI using Java, Spring Boot, and Spring AI**.

The goal is to learn AI application development by building small, practical projects and understanding the concepts behind them.

## RAG — Retrieval-Augmented Generation

The first project in this repository focuses on building a **RAG application using Spring AI and Ollama**.

The RAG implementation demonstrates:

* Document ingestion
* Document chunking
* Embeddings
* Vector storage
* Similarity search
* Context retrieval
* LLM-based question answering

## Agentic AI Examples (All using Spring AI)

### **[RAG - Example](./src/main/java/com/codewithrj/agentic_ai/rag/RAG.md)**

### **[Tools calling - Example](./src/main/java/com/codewithrj/agentic_ai/tools/TOOLS.md)**

The next project demonstrates Tool Calling with Spring AI.

It shows how an LLM can use custom Java methods as tools, including:

* Custom `@Tool` methods
* Multiple tools
* Tool selection by the LLM
* RAG exposed as a tool

## Memory

This project demonstrates **short-term conversational memory** using Spring AI `ChatMemory` and `MessageWindowChatMemory`.

A `conversationId` is passed with each request and supplied to `MessageChatMemoryAdvisor`. Spring AI uses that ID to retrieve and maintain the conversation history before sending the request to the LLM.

The current implementation keeps a window of **20 messages per conversation**.

```text
conversationId + question
          |
          v
       QAAgent
          |
          v
 MessageChatMemoryAdvisor
          |
          v
      ChatMemory
          |
          v
  Previous conversation
          |
          v
          LLM
```

The memory implementation and request flow are documented in **[Memory](./src/main/java/com/codewithrj/agentic_ai/memory/README.md)**.

Long-term memory is a separate concept: it uses a **User ID** and persistent storage such as a vector store to retrieve relevant user information across independent conversations.

## Tools With Spring AI
