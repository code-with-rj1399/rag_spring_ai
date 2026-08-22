# Memory

This module demonstrates **short-term and long-term memory** for an AI agent using Spring AI.

## What is Memory?

Memory allows an AI agent to retain relevant information from previous interactions and use it when processing future queries.

- **Short-Term Memory** — maintains context within a conversation.
- **Long-Term Memory** — persists important user information across conversations.

## Short-Term Memory

Short-term memory maintains the context of an ongoing conversation using a unique **Conversation ID**.

```text
User
  ↓
Conversation ID
  ↓
Conversation History
  ↓
LLM Context
```

When a subsequent query uses the same `conversationId`, the application retrieves the previous conversation history and provides it to the LLM.

## Long-Term Memory

Long-term memory stores important information that should remain available after a conversation ends.

Unlike short-term memory, long-term memory is associated with a **User ID** rather than a Conversation ID.

```text
User ID
   ↓
Long-Term Memory
   ↓
Vector Store
```

For example:

```text
User: I prefer Java over Python for backend development.
```

The application can persist:

```text
User prefers Java over Python for backend development.
```

along with metadata such as:

```text
userId = user-123
memoryType = preference
```

## How Long-Term Memory Works

The implementation follows a **store → retrieve → context enrichment** pattern.

```text
                 User Query
                     |
                     v
             Retrieve Memories
                     |
                     v
                Vector Store
                     |
                     v
          Relevant User Memories
                     |
                     v
              Context Enrichment
                     |
                     v
                    LLM
                     |
                     v
                 Response
```

### 1. Store Memory

Important user information is stored as a document in the existing Spring AI `VectorStore`. The memory is embedded and persisted in the vector database with user-related metadata.

### 2. Retrieve Memory

When a new query arrives, the query is used for semantic search against the vector store. Only memories relevant to the current query are retrieved, scoped by `userId`.

### 3. Add Memory to Context

Retrieved memories are added to the LLM context before the current query is processed.

```text
Relevant information about the user:
- User prefers Java.
- User is interested in backend development.

User Query:
Suggest a backend project.
```

This enables the agent to generate personalized responses.

## Short-Term vs Long-Term Memory

| Feature | Short-Term Memory | Long-Term Memory |
|---|---|---|
| Scope | Conversation | User |
| Identifier | Conversation ID | User ID |
| Lifetime | Current/recent conversation | Persistent |
| Storage | Conversation history | Vector Store |
| Retrieval | Conversation history | Semantic search |
| Purpose | Maintain conversation context | Remember important user information |

## Architecture

```text
                         AI Agent
                            |
                 ┌──────────┴──────────┐
                 |                     |
          Short-Term Memory     Long-Term Memory
                 |                     |
        Conversation ID             User ID
                 |                     |
        Conversation History       Vector Store
                 |                     |
                 └──────────┬──────────┘
                            |
                            v
                           LLM
```

## Key Design Principle

Do not store every user message as long-term memory. Only information that is useful across future conversations should be persisted.

Examples:

```text
"I prefer Java."                         → Remember
"I'm preparing for Staff Engineer."     → Remember
"What is Kafka?"                        → Don't remember
"Explain Redis."                        → Don't remember
```

## Technologies

- Java
- Spring Boot
- Spring AI
- LLM
- Vector Store
- Embeddings

## Summary

**Short-Term Memory**

```text
Conversation ID → Conversation History → LLM Context
```

**Long-Term Memory**

```text
User ID → Vector Store → Relevant Memories → LLM Context
```

Together, they allow the AI agent to maintain conversational context while remembering important user information across independent conversations.
