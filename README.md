# Newswire

A full-stack news aggregation application built with **Spring Boot**, **React**, **TypeScript**, **Vite**, and **PostgreSQL**.

---

# Overview

Newswire aggregates news articles from multiple sources and presents them in a clean, searchable interface.

The project is intended as both a practical application and a learning project for modern Java and React development.

---

# Technology Stack

## Backend

- Java 21
- Spring Boot 3.5.x
- Gradle
- PostgreSQL
- REST API

## Frontend

- React
- TypeScript
- Vite

---

# Current Features

- Search news articles
- Category filtering
- Pagination
- Infinite scrolling
- Manual refresh
- Backend status polling
- Responsive article cards

---

# Project Structure

```
frontend/
└── src
    ├── components
    │   └── NewsCard.tsx
    ├── data
    │   └── sources.ts
    ├── hooks
    ├── services
    │   └── newsApi.ts
    ├── types
    │   └── news.ts
    ├── legacy
    ├── App.tsx
    ├── main.tsx
    └── style.css
```

---

# Backend

Start the backend:

```bash
./gradlew bootRun
```

Application runs on:

```
http://localhost:8080
```

---

# Frontend

Install dependencies:

```bash
cd frontend
npm install
```

Run development server:

```bash
npm run dev
```

Application runs on:

```
http://localhost:5173
```

---

# Production Build

Build the frontend:

```bash
npm run build
```

Output is generated in:

```
frontend/dist/
```

---

# Recent Refactoring

The frontend architecture was reorganized to improve maintainability.

Completed work includes:

- Extracted `NewsCard` into its own reusable component.
- Centralized shared TypeScript models in `types/news.ts`.
- Added a `services` layer for API abstraction.
- Added a `data` folder for curated source metadata.
- Added a `hooks` folder for future custom React hooks.
- Cleaned up `App.tsx`.
- Resolved all TypeScript compilation issues.
- Verified a successful production build.

---

# Roadmap

Planned improvements include:

- Extract `StatusBar` component
- Extract search toolbar
- Complete migration of API calls into `services/newsApi.ts`
- Curated news source dashboard
- Additional dashboard widgets
- Authentication
- User preferences
- Saved searches

---

# Development Status

Current status:

- Backend API operational
- Frontend production build passing
- Project structure refactored
- Ready for continued feature development

---

# Issues starting env Backend
- .\gradlew.bat bootRun --args="--spring.profiles.active=dev"
- $env:SPRING_PROFILES_ACTIVE = "dev"
- .\gradlew.bat bootRun

# Start Frontend
- npm run dev
- check with npm run build

# Check localhost
- Backend:  http://localhost:8080
- Frontend: http://localhost:5173

# License

This project is intended for personal learning and software development.