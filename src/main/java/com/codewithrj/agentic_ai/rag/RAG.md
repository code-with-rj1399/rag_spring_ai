# RAG With Spring AI

A simple hands-on **Retrieval-Augmented Generation (RAG)** project using **Spring AI and Ollama**.

The project uses `Budget_Speech_2024-2025.txt` as the knowledge source and demonstrates document chunking, embeddings, vector storage, retrieval, and LLM-based question answering.

## Architecture

```text
Budget_Speech.txt
       ↓
   TextReader
       ↓
TokenTextSplitter
       ↓
nomic-embed-text
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

## Models

The project uses Ollama to run both models locally.

| Purpose    | Model              |
| ---------- | ------------------ |
| Chat / LLM | `qwen3:1.7b`       |
| Embeddings | `nomic-embed-text` |

**Qwen3 1.7B** is responsible for generating the final answer.

**nomic-embed-text** converts the document chunks and user queries into vector embeddings, which are used for semantic similarity search.

## Dependencies

The important Spring AI dependencies in `../../../../../../../pom.xml` are:

```xml
<dependency>
    <groupId>org.springframework.ai</groupId>
    <artifactId>spring-ai-starter-model-ollama</artifactId>
</dependency>

<dependency>
    <groupId>org.springframework.ai</groupId>
    <artifactId>spring-ai-vector-store</artifactId>
</dependency>

<dependency>
    <groupId>org.springframework.ai</groupId>
    <artifactId>spring-ai-vector-store-advisor</artifactId>
</dependency>
```

The project uses:

```xml
<properties>
    <java.version>21</java.version>
    <spring-ai.version>2.0.0</spring-ai.version>
</properties>
```

The Spring AI BOM should be imported in `dependencyManagement` so that the Spring AI dependencies use the same version.

## Configuration

```properties
spring.application.name=RAG-With-Spring-AI

spring.ai.ollama.base-url=http://localhost:11434

# Chat / LLM
spring.ai.ollama.chat.options.model=qwen3:1.7b

# Embedding model
spring.ai.ollama.embedding.options.model=nomic-embed-text
```

## Setup

Install Ollama, then pull both models:

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


### Elasticsearch

Run Elasticsearch locally using Docker:

```bash
docker pull docker.elastic.co/elasticsearch/elasticsearch:9.1.0

docker run --name elasticsearch \
  -p 9200:9200 \
  -e discovery.type=single-node \
  -e xpack.security.enabled=false \
  docker.elastic.co/elasticsearch/elasticsearch:9.1.0
```

Verify:

```bash
curl http://localhost:9200
```

Elasticsearch will be available at `http://localhost:9200`.


Then start the Spring Boot application:

```bash
./mvnw spring-boot:run
```

## API

Once the application is running:

```bash
curl "http://localhost:8080/budget?message=What does the budget say about infrastructure?"
```

The application retrieves relevant chunks from `Budget_Speech_2024-2025.txt` using `nomic-embed-text` and `SimpleVectorStore`, then passes the retrieved context to `Qwen3 1.7B` through Spring AI's `QuestionAnswerAdvisor`.

Everything runs **locally through Ollama**, so no OpenAI or Groq API key is required.
