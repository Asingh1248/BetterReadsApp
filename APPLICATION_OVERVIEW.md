# BetterReads Application - Overview & Architecture

## 🎯 **What is BetterReads?**

BetterReads is a **book tracking and discovery application** built with **Spring Boot**. It allows users to:
- **Authenticate using GitHub OAuth2**
- **Search for books** using the OpenLibrary API
- **View detailed book information** including covers, descriptions, and metadata
- **Store and manage book data** in a Cassandra database

---

## 🏗️ **High-Level Architecture Diagram**

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                           BetterReads Application                            │
└─────────────────────────────────────────────────────────────────────────────┘

    ┌─────────────┐                    ┌─────────────────────────────────────┐
    │             │                    │                                     │
    │   Browser   │◄──────────────────►│           Spring Boot               │
    │   (User)    │    HTTP Requests   │        Web Application              │
    │             │                    │                                     │
    └─────────────┘                    └─────────────────────────────────────┘
                                                        │
                                                        │
                ┌───────────────────────┬───────────────┼─────────────────┬──────────────────┐
                │                       │               │                 │                  │
                ▼                       ▼               ▼                 ▼                  ▼
    ┌──────────────────────┐ ┌─────────────────┐ ┌────────────────┐ ┌──────────────┐ ┌──────────────┐
    │                      │ │                 │ │                │ │              │ │              │
    │   GitHub OAuth2      │ │  Thymeleaf      │ │   WebFlux      │ │  Cassandra   │ │ OpenLibrary  │
    │   Authentication     │ │  Templates      │ │   WebClient    │ │  Database    │ │     API      │
    │                      │ │                 │ │                │ │ (DataStax    │ │              │
    │  • User Login        │ │  • index.html   │ │ • Reactive     │ │  Astra)      │ │ • Book       │
    │  • OAuth Flow        │ │  • book.html    │ │   HTTP calls   │ │              │ │   Search     │
    │  • Session Mgmt      │ │  • search.html  │ │ • External API │ │ • Book Data  │ │ • Metadata   │
    │                      │ │  • Components   │ │   Integration  │ │ • User Data  │ │ • Covers     │
    └──────────────────────┘ └─────────────────┘ └────────────────┘ └──────────────┘ └──────────────┘
```

---

## 🔧 **Technology Stack**

| Component | Technology | Purpose |
|-----------|------------|---------|
| **Backend Framework** | Spring Boot 2.5.2 | Core application framework |
| **Security** | Spring Security + OAuth2 | Authentication & Authorization |
| **Database** | Apache Cassandra (DataStax Astra) | NoSQL data persistence |
| **Template Engine** | Thymeleaf | Server-side HTML rendering |
| **HTTP Client** | Spring WebFlux WebClient | Reactive HTTP calls to external APIs |
| **Build Tool** | Maven | Dependency management & build |
| **Java Version** | Java 11 | Runtime environment |

---

## 📦 **Application Components**

### 1. **Authentication Layer**
- **GitHub OAuth2 Integration**: Users authenticate using their GitHub accounts
- **Security Configuration**: Handles login/logout flows and session management
- **User Context**: Maintains authenticated user information throughout the session

### 2. **Book Management System**
```java
book/
├── Book.java              // Entity model for books
├── BookController.java    // Web controller for book operations  
└── BookRepository.java    // Data access layer for Cassandra
```

### 3. **Search Functionality**
```java
search/
├── SearchController.java     // Handles search requests
├── SearchResult.java         // Response model from OpenLibrary
└── SearchResultBook.java     // Individual book result model
```

### 4. **Database Layer**
- **Cassandra Integration**: Uses DataStax Astra (cloud Cassandra)
- **Book Storage**: Stores book metadata, covers, authors, publication info
- **Reactive Data Access**: Spring Data Cassandra integration

### 5. **External API Integration**
- **OpenLibrary API**: `http://openlibrary.org/search.json`
- **Book Covers**: `https://covers.openlibrary.org/b/id/`
- **Reactive HTTP Calls**: Using WebFlux WebClient for non-blocking operations

---

## 🔄 **Application Flow**

### **User Journey:**
1. **Landing Page**: User visits the application → sees login prompt
2. **Authentication**: Clicks "Login with GitHub" → OAuth2 flow
3. **Search Books**: After login → can search for books using OpenLibrary API
4. **View Details**: Click on search results → view detailed book information
5. **Book Storage**: Book data is cached/stored in Cassandra database

### **Technical Flow:**
```
User Request → Spring Security (Auth Check) → Controller → Service Logic → 
External API Call (if needed) → Database Operation → Template Rendering → HTML Response
```

---

## 🚀 **Key Features**

- ✅ **Secure Authentication** via GitHub OAuth2
- ✅ **Book Search** integration with OpenLibrary
- ✅ **Book Details** with covers, descriptions, author info
- ✅ **Scalable Database** using Cassandra
- ✅ **Reactive Programming** for efficient API calls
- ✅ **Responsive UI** with Thymeleaf templates

---

## 📊 **Data Models**

### **Book Entity:**
```java
@Table("book_by_id")
class Book {
    @Id String id;
    String name;
    String description;
    LocalDate publishedDate;
    List<String> coversIds;
    List<String> authorNames;
    List<String> authorIds;
}
```

---

## 🌐 **API Endpoints**

| Endpoint | Method | Purpose |
|----------|--------|---------|
| `/` | GET | Home page with login |
| `/search?query={term}` | GET | Search books |
| `/books/{bookId}` | GET | View book details |
| `/oauth2/authorization/github` | GET | GitHub OAuth login |

---

## 🔮 **Future Enhancements** (Based on TODO comments)

- **User Reading Lists**: Track books as "read", "want to read", "currently reading"
- **Book Reviews & Ratings**: Allow users to rate and review books
- **Social Features**: Follow other users, share reading lists
- **Reading Progress**: Track reading progress for books
- **Recommendations**: Suggest books based on reading history

---

This application follows **modern Spring Boot best practices** with reactive programming, cloud-native database integration, and OAuth2 security, making it a solid foundation for a book tracking platform!