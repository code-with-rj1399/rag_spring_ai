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



# RAG With Spring AI — Quick Reference

## 1. What is RAG?

**RAG (Retrieval-Augmented Generation)** combines a knowledge source with an LLM.

Instead of asking the LLM to answer purely from its training data:

```text
User Question
     ↓
Retrieve relevant information
     ↓
Add information to prompt
     ↓
LLM generates answer
```

---

## 2. RAG Pipeline

### Document Ingestion

```text
TXT / PDF
   ↓
DocumentReader
   ↓
Document
   ↓
TextSplitter
   ↓
Chunks
   ↓
EmbeddingModel
   ↓
VectorStore
   ↓
Elasticsearch
```

### Question Answering

```text
User Question
      ↓
EmbeddingModel
      ↓
Vector Search
      ↓
Relevant Chunks
      ↓
Question + Retrieved Context
      ↓
ChatClient
      ↓
Chat Model / LLM
      ↓
Answer
```

---

## 3. Important Spring AI Classes

| Class                   | What it does                           | When we need it              |
| ----------------------- | -------------------------------------- | ---------------------------- |
| `DocumentReader`        | Reads source documents                 | At ingestion                 |
| `TextReader`            | Reads TXT files                        | When source is TXT           |
| `PagePdfDocumentReader` | Reads PDF pages                        | When source is PDF           |
| `Document`              | Represents document content + metadata | Throughout ingestion         |
| `TextSplitter`          | Splits documents into chunks           | Before embeddings            |
| `TokenTextSplitter`     | Token-based chunking implementation    | When chunking text           |
| `EmbeddingModel`        | Converts text → vector                 | During ingestion + query     |
| `VectorStore`           | Stores and searches embeddings         | During ingestion + retrieval |
| `SearchRequest`         | Configures vector search               | When customizing retrieval   |
| `QuestionAnswerAdvisor` | Connects retrieval with LLM            | During RAG query             |
| `ChatClient`            | Communicates with the LLM              | During generation            |

---

## 4. What Each Stage Means

### READ

`TextReader` / `PagePdfDocumentReader`

Converts files into Spring AI `Document` objects.

```text
PDF / TXT
   ↓
Document
```

A `Document` contains:

```text
Content
Metadata
```

---

### CHUNK

`TextSplitter` / `TokenTextSplitter`

Breaks large documents into smaller pieces.

```text
Large Document
      ↓
Chunk 1
Chunk 2
Chunk 3
...
```

**Why?**

Smaller chunks allow vector search to retrieve the specific information relevant to a question.

---

### EMBED

`EmbeddingModel`

Converts text into a numerical vector.

```text
"Fiscal deficit target"
          ↓
    EmbeddingModel
          ↓
[0.12, -0.45, 0.78, ...]
```

The same embedding model is generally used for:

```text
Document chunks → embeddings
User question  → embedding
```

This allows semantic similarity search.

---

### STORE

`VectorStore`

Stores:

```text
Chunk
Embedding
Metadata
```

In this project:

```text
VectorStore
     ↓
Elasticsearch
```

`VectorStore` is an abstraction, so the underlying vector database can be changed without changing the application logic significantly.

---

### RETRIEVE

`VectorStore` + `SearchRequest`

When the user asks a question:

```text
Question
   ↓
EmbeddingModel
   ↓
Query Vector
   ↓
VectorStore
   ↓
Similarity Search
   ↓
Top K relevant chunks
```

Example:

```text
Question
   ↓
Search top 5 chunks
   ↓
Chunk 17
Chunk 42
Chunk 103
Chunk 205
Chunk 311
```

---

### AUGMENT

`QuestionAnswerAdvisor`

Takes:

```text
User Question
+
Retrieved Chunks
```

and adds the retrieved information to the LLM request.

Conceptually:

```text
Question
   +
Context
   ↓
Augmented Prompt
```

---

### GENERATE

`ChatClient` → Chat Model

`ChatClient` communicates with the LLM.

```text
Augmented Prompt
       ↓
   ChatClient
       ↓
     Qwen3
       ↓
     Answer
```

---

## 5. Current Project

```text
Source:
Budget_Speech_2024-2025.txt

Embedding Model:
nomic-embed-text

Vector Store:
Elasticsearch

Chat Model:
qwen3:1.7b

Framework:
Spring AI

Runtime:
Ollama
```

---

## 6. The Most Important Mental Model

### Ingestion

```text
READ → CHUNK → EMBED → STORE
```

### Query

```text
ASK → EMBED → SEARCH → AUGMENT → GENERATE
```

### In Spring AI

```text
Reader
  ↓
Document
  ↓
TextSplitter
  ↓
EmbeddingModel
  ↓
VectorStore
  ↓
QuestionAnswerAdvisor
  ↓
ChatClient
  ↓
Chat Model
```

**Simple rule to remember:**

> **`EmbeddingModel` understands similarity. `VectorStore` finds similar content. `ChatClient` talks to the LLM. `QuestionAnswerAdvisor` connects retrieval with generation.**


Everything runs **locally through Ollama**, so no OpenAI or Groq API key is required.
