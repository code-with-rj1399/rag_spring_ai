# RAG With Spring AI

A simple hands-on **Retrieval-Augmented Generation (RAG)** project using **Spring AI and Ollama**.

The project uses a budget speech as the knowledge source and demonstrates how to retrieve relevant information from the document before generating an answer with a local LLM.

## Architecture

```text
Budget_Speech.txt
       ↓
   TextReader
       ↓
TokenTextSplitter
       ↓
   Embeddings
       ↓
SimpleVectorStore
       ↓
QuestionAnswerAdvisor
       ↓
   ChatClient
       ↓
  Qwen3 1.7B
       ↓
    Answer
```

## Key Components

* **Spring Boot** — application framework
* **Spring AI** — AI/RAG integration
* **SimpleVectorStore** — stores document embeddings
* **QuestionAnswerAdvisor** — retrieves relevant documents and adds them to the prompt
* **Ollama** — runs the models locally
* **Qwen3 1.7B** — chat/LLM model
* **nomic-embed-text** — embedding model

## API

Start the application and query the budget document:

```bash
curl "http://localhost:8080/budget?message=What does the budget say about infrastructure?"
```

The query is embedded, relevant chunks are retrieved from `SimpleVectorStore`, and the retrieved context is passed to Qwen3 to generate the answer.

## Configuration

```properties
spring.application.name=RAG-With-Spring-AI

spring.ai.ollama.base-url=http://localhost:11434

# Chat / LLM
spring.ai.ollama.chat.options.model=qwen3:1.7b

# Embeddings
spring.ai.ollama.embedding.options.model=nomic-embed-text
```

## Running Ollama

Install Ollama and pull the required models:

```bash
ollama pull qwen3:1.7b
ollama pull nomic-embed-text
```

Verify:

```bash
ollama list
```

Start Ollama if it isn't already running:

```bash
ollama serve
```

Then start the Spring Boot application:

```bash
./mvnw spring-boot:run
```