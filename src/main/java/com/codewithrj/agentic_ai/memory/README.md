# Memory

This module demonstrates how the agent maintains conversational context using **Spring AI ChatMemory**.

## Current Implementation

The current implementation provides **short-term conversational memory** using Spring AI's `ChatMemory` abstraction and `MessageWindowChatMemory`.

The memory configuration is defined in [`MemoryConfig.java`](./MemoryConfig.java):

```java
@Bean
public ChatMemory chatMemory() {
    return MessageWindowChatMemory.builder()
            .maxMessages(20)
            .build();
}
```

The application keeps a maximum window of **20 messages** for each conversation.

## How Conversation ID Is Used

The conversation ID is the key that connects multiple requests to the same conversation.

The flow in [`QAAgent.java`](../agent/QAAgent.java) is:

```text
Client
  |
  | conversationId + question
  v
QAAgent.ask()
  |
  v
ChatClient
  |
  v
MessageChatMemoryAdvisor
  |
  | ChatMemory.CONVERSATION_ID
  v
ChatMemory
  |
  | retrieve previous messages
  v
LLM
```

The `ask()` method receives both values:

```java
public String ask(String conversationId, String question)
```

The question is sent to the `ChatClient` using `.user(question)`. The same conversation ID is passed to the memory advisor:

```java
.advisors(
    advisor -> advisor.param(
        ChatMemory.CONVERSATION_ID,
        conversationId
    )
)
```

This tells Spring AI which conversation's history should be retrieved and updated.

## What Happens Across Requests

### First request

```text
conversationId = abc123
question = "What is Kafka?"
```

The memory advisor associates the request with `abc123` and stores the conversation messages.

### Second request

```text
conversationId = abc123
question = "How does it handle partitions?"
```

Because the same conversation ID is supplied, the memory advisor retrieves the previous messages for `abc123` and makes them available to the LLM.

The LLM can therefore understand that **"it" refers to Kafka**.

### New conversation

```text
conversationId = xyz789
question = "How does it handle partitions?"
```

This is a different conversation. The memory associated with `abc123` is not used for `xyz789`.

## Where Memory Is Added to the LLM Request

Memory is not manually concatenated with the user prompt in `QAAgent`.

Instead, `MessageChatMemoryAdvisor` is registered as a default `ChatClient` advisor:

```java
this.chatClient = ChatClient.builder(chatModel)
        .defaultAdvisors(
                MessageChatMemoryAdvisor.builder(chatMemory)
                        .build()
        )
        .build();
```

The advisor participates in the `ChatClient` request lifecycle. It uses the supplied conversation ID to retrieve relevant messages from `ChatMemory` and incorporates them into the request sent to the model. After the model interaction, the conversation messages are maintained by the memory implementation.

This keeps memory handling separate from the agent's business logic.

## Short-Term Memory

Short-term memory is conversation-scoped.

```text
Conversation ID
      |
      v
ChatMemory
      |
      v
Recent conversation messages
      |
      v
LLM context
```

The current implementation uses `MessageWindowChatMemory`, so only the configured message window is retained for the conversation.

## Long-Term Memory

Long-term memory is different from the current `ChatMemory` implementation.

It should persist important information about a **user** across independent conversations.

The intended flow is:

```text
User ID
  |
  v
Long-Term Memory
  |
  v
Vector Store
  |
  v
Relevant memories for the current query
  |
  v
LLM context
```

For example:

```text
"I prefer Java for backend development."
```

can be stored as a user memory. In a future conversation, semantic search can retrieve that memory and add it to the LLM context.

**Long-term memory is not implemented by `MessageWindowChatMemory`.** It is a separate capability that can be built on top of the existing vector-store infrastructure.

## Short-Term vs Long-Term Memory

| Feature | Short-Term Memory | Long-Term Memory |
|---|---|---|
| Scope | Conversation | User |
| Identifier | Conversation ID | User ID |
| Current implementation | Yes | Planned/next step |
| Storage | `ChatMemory` | Vector Store |
| Retrieval | Conversation history | Semantic search |
| Purpose | Maintain ongoing context | Remember useful information across conversations |

## Architecture

```text
                         QAAgent
                            |
                       ChatClient
                            |
                MessageChatMemoryAdvisor
                            |
                            v
                       ChatMemory
                            |
                 Conversation ID lookup
                            |
                            v
                 Recent conversation history
                            |
                            v
                           LLM
```

The important separation is:

```text
Conversation ID → Short-term conversational context
User ID         → Long-term persistent memory
```

## Key Design Principle

Do not treat the entire conversation history as long-term memory.

Short-term memory keeps the current conversation coherent. Long-term memory should contain only information that is useful across future conversations, such as stable preferences, goals, or user-specific facts.
