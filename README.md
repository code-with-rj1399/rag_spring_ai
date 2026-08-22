# Agentic AI With Spring AI

A hands-on project for building and understanding **Agentic AI applications using Java, Spring Boot, and Spring AI**.

The goal is to progressively build an AI agent by adding one capability at a time and understanding how each capability contributes to an agentic system.

## What Are We Building?

We are building an AI agent incrementally by adding the core capabilities required by an agent:

```text
                    AI Agent
                       |
        ┌──────────────┼──────────────┐
        |              |              |
       RAG         Tool Calling     Memory
        |              |              |
        └──────────────┼──────────────┘
                       |
                Context + Tools
                       |
                       v
                      LLM
```

The project will progressively cover:

1. **RAG** — Give the agent access to external knowledge.
2. **Tool Calling** — Allow the agent to execute actions using Java methods.
3. **Short-Term Memory** — Maintain context within a conversation.
4. **Long-Term Memory** — Remember useful information about a user across conversations.
5. **Agentic Workflows** — Combine reasoning, tools, memory, and retrieval to accomplish multi-step tasks.

---

## 1. RAG — Retrieval-Augmented Generation

RAG allows the agent to retrieve relevant information from an external knowledge base before generating a response.

### What we achieve

- Document ingestion
- Document chunking
- Embeddings
- Vector storage
- Similarity search
- Context retrieval
- LLM-based question answering

**Implementation:** [RAG Example](./src/main/java/com/codewithrj/agentic_ai/rag/RAG.md)

---

## 2. Tool Calling

Tool calling allows the LLM to decide when it needs to execute an external function.

In this project, Java methods are exposed as Spring AI tools.

### What we achieve

- Custom `@Tool` methods
- Multiple tools
- LLM-based tool selection
- Passing arguments to tools
- RAG exposed as a tool
- Returning tool results to the LLM

**Implementation:** [Tools Calling Example](./src/main/java/com/codewithrj/agentic_ai/tools/TOOLS.md)

---

## 3. Memory

Memory allows the agent to maintain context across interactions.

There are two types we are implementing.

### Short-Term Memory

Maintains context within a conversation.

```text
Conversation ID
       ↓
ChatMemory
       ↓
Conversation History
       ↓
LLM Context
```

The current implementation uses Spring AI's `ChatMemory` and `MessageWindowChatMemory`. A `conversationId` is passed with each request and supplied to `MessageChatMemoryAdvisor`, allowing Spring AI to retrieve and maintain the appropriate conversation history.

**Implementation:** [Memory](./src/main/java/com/codewithrj/agentic_ai/memory/README.md)

### Long-Term Memory

Long-term memory allows the agent to remember useful information about a user across independent conversations.

```text
User ID
   ↓
Memory
   ↓
Vector Store
   ↓
Relevant Memories
   ↓
LLM Context
```

For example:

```text
User:
"I prefer Java for backend development."

Future conversation:
"Suggest a backend project for me."

        ↓

Retrieved memory:
"User prefers Java for backend development."

        ↓

Personalized LLM response
```

Long-term memory will build on the existing vector-store infrastructure.

---

## 4. Agentic Workflows

The final goal is to combine the capabilities above into workflows where the agent can determine what information it needs, which tools to use, what memory is relevant, and how to complete a multi-step task.

This part will be developed incrementally as the individual capabilities become available.

---

## Project Roadmap

| Capability | Status |
|---|---|
| RAG | ✅ Implemented |
| Tool Calling | ✅ Implemented |
| Short-Term Memory | ✅ Implemented |
| Long-Term Memory | 🚧 In Progress |
| Agentic Workflows | ⏳ Planned |

---

## Why Build It This Way?

Instead of jumping directly into a large AI-agent framework, this project builds the agent **capability by capability**.

```text
Knowledge
   +
Tools
   +
Memory
   +
Reasoning
   ↓
Agentic AI
```

Each capability has its own implementation and documentation so that the underlying Spring AI concepts remain clear and easy to understand.
