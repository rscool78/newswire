# 📰 Newswire – Intelligent News Aggregation Platform

## 📌 Project Overview
Newswire is a full-stack news aggregation platform designed to ingest, process, and deliver curated news content from multiple RSS sources. The system emphasizes **data freshness, deduplication, and intelligent ranking**, providing users with a real-time, prioritized news feed.

This project serves as a **portfolio-grade application** demonstrating:
- Backend data pipelines (Spring Boot)
- Frontend user experience (React + TypeScript)
- Data modeling and persistence (JPA/Hibernate)
- Intelligent scoring and ranking logic
- API design and integration

---

## 🎯 Project Objectives

### Primary Goals
- Aggregate news from multiple RSS feeds
- Store and manage articles in a relational database
- Eliminate duplicate content using fingerprinting
- Provide paginated and filtered API endpoints
- Deliver a responsive frontend experience

### Advanced Goals (In Progress / Planned)
- Trending score algorithm (content prioritization)
- User personalization and saved articles
- Authentication and role-based access
- ML-based recommendation engine
- Cloud deployment (Azure / containerization)

---

## 🏗️ Architecture Overview
RSS Feeds → Spring Boot Backend → Database → REST API → React Frontend


### Key Components
- **Ingestion Layer**: Pulls RSS feeds and parses content
- **Processing Layer**: Deduplication + scoring
- **Persistence Layer**: Stores structured articles
- **API Layer**: Serves paginated and filtered data
- **Frontend Layer**: Displays articles with UX enhancements

---

## 📂 Project Structure

### Backend (Spring Boot)

src/main/java/com/newswire/
│
├── article/
│ ├── ArticleEntity.java # JPA entity for articles
│ ├── ArticleRepository.java # Database access layer
│ ├── Category.java # Enum for article categories
│
├── controller/
│ └── NewsController.java # REST API endpoints
│
├── service/
│ ├── RssNewsService.java # RSS feed ingestion
│ ├── ArticleStoreService.java # Business logic for saving articles
│ ├── NewsRefreshJob.java # Scheduled ingestion job
│ └── RefreshStatus.java # Tracks ingestion status
│
├── source/
│ ├── FeedProperties.java # Configurable RSS sources
│ └── FeedSource.java
│
└── dto/
└── NewsItem.java # Data transfer object


---

### Frontend (React + Vite + TypeScript)
frontend/
│
├── src/
│ ├── App.tsx # Main UI component
│ ├── components/ # Reusable UI components (planned)
│ ├── services/ # API calls (planned)
│ ├── types/ # TypeScript interfaces
│ └── styles/ # Styling (planned)
│
├── index.html
├── package.json
└── vite.config.ts


---

## 🚀 Features Implemented (Today’s Progress)

### ✅ Backend
- RSS ingestion using Rome library
- Scheduled background job for automatic refresh
- Article persistence with JPA/Hibernate
- Category-based filtering
- Pagination support via Spring Data
- REST API endpoints:
  - `GET /api/news` (paginated, optional category filter)
  - `GET /api/news/status` (refresh status)
  - `POST /api/news/refresh` (manual trigger)

---

### ✅ Data Enhancements
- Fingerprinting logic for deduplication (SHA-256)
- Structured article storage:
  - Title
  - URL
  - Summary
  - Source
  - Category
  - Published date

---

### ✅ Frontend
- React + TypeScript application initialized
- API integration with backend
- Infinite scroll / pagination support
- Category filtering UI
- Status polling (refresh monitoring)

---

## ⚙️ In Progress Enhancements

### 🔄 Trending Score Algorithm
- Ranking articles based on:
  - Recency
  - Category weighting
  - Keyword detection (e.g., “AI”)
- Sorting results dynamically

---

### 🎨 UI Improvements
- “Trending” indicators
- Time-ago formatting for articles
- Cleaner layout and component structure

---

## 🔧 Configuration

### Backend (application-dev.properties)

spring.jpa.hibernate.ddl-auto=update

newswire.refresh.enabled=true
newswire.refresh.fixedDelayMs=600000
newswire.refresh.initialDelayMs=10000
newswire.refresh.fetchLimit=200
newswire.maxItems=40


### Sample RSS Feeds
- BBC News
- New York Times
- NPR

---

## ▶️ Running the Application

### Backend
./gradlew bootRun --args="--spring.profiles.active=dev"


### Frontend
cd frontend
npm install
npm run dev


---

## 📈 Future Roadmap

### Phase 3 – Product Maturity
- User accounts (JWT authentication)
- Saved articles / bookmarks
- Personalized news feeds

### Phase 4 – Intelligence Layer
- Machine learning recommendations
- Sentiment analysis
- Topic clustering

### Phase 5 – Deployment
- Docker containerization
- Azure hosting
- CI/CD pipeline

---

## 🧠 Strategic Value

This project demonstrates:
- Full-stack engineering capability
- Data pipeline design
- Real-world problem solving (deduplication, ranking)
- Product thinking and scalability

It is intended to serve as a **flagship portfolio project** supporting progression toward:
- Senior Engineer roles
- Director of Engineering
- CIO / CTO track positions

---

## 📬 Author

Roger Steele  
System Administrator | AI Graduate | MBA  
Focused on building enterprise-grade systems and transitioning into senior leadership roles in technology.
