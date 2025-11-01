# PureDash - Copilot Instructions

**Project Status:** Fresh start - iterative development phase

## Project Overview
PureDash is a **lightweight server-side rendered (SSR) dashboard** designed to run reliably on legacy browsers (Android 4.2+). It will fetch data from pluggable integrations (starting with Home Assistant), cache results, and render pure HTML with minimal JavaScript. The system prioritizes **simplicity, resilience, extensibility, and minimal dependencies**.

**Technology Stack:**
- **Language:** Kotlin with Java 21
- **Server:** Ktor (lightweight web framework) - to be added
- **Frontend:** Pure HTML/CSS + minimal inline JS (no SPA frameworks)
- **Serialization:** kotlinx.serialization (JSON/YAML) - to be added
- **Build System:** Gradle

## Core Objectives
1. **Compatibility:** Render correctly on Android 4.2 WebKit (limited CSS/JS support)
2. **Simplicity:** Pure HTML SSR; only minimal inline JS when absolutely necessary
3. **Extensibility:** Pluggable modules for integrations and widgets, discoverable at runtime
4. **Reliability:** Server never blocks page rendering on remote API calls; always uses cached snapshots
5. **Minimal Footprint:** Small memory usage, fast startup, minimal third-party libraries
6. **Easy Deployment:** Single JVM process or small Docker image

## Current Project Structure (Minimal)

```
PureDash/
├── app/                             # Main application module
│   ├── src/main/kotlin/app/puredash/
│   │   └── Main.kt                 # Entry point (currently: println "Hello World")
│   ├── src/test/kotlin/            # Test directory (empty)
│   └── build.gradle.kts             # App module config
│
├── build.gradle.kts                 # Root build config
├── settings.gradle.kts              # Module configuration
├── gradle.properties                # Build version & constants
└── llm-agent-architecture-and-principles.md  # Full architectural spec
```

## Current State
- ✅ Gradle structure set up (single app module)
- ✅ Java 21 / Kotlin configuration ready
- ✅ Minimal entry point: `Main.kt` with `println("Hello World")`
- ✅ Project builds successfully
- ✅ App runs and prints "Hello World"
- ⏳ No external dependencies yet (Ktor, serialization to be added iteratively)
- ⏳ No server running yet (Ktor to be added)
- ⏳ No caching, scheduling, or integrations yet

## Development Approach: Iterative Refinement

We will build PureDash incrementally, test at each step, and align with the architecture spec as we progress.

### Iteration 1 (Completed): Minimal Setup
- ✅ Single module Gradle structure
- ✅ Minimal main function
- ✅ Build verified
- ✅ Hello World runs

### Iteration 2 (Next): Add Web Server
- Add Ktor Server dependencies
- Create basic HTTP server on port 8080
- GET `/` returns simple "PureDash" HTML
- GET `/health` returns JSON: `{"status":"UP"}`
- Verify both endpoints work

### Iteration 3: Configuration Loading
- Add YAML configuration support (kaml library)
- Create `config/kiosk.example.yml`
- Load config on startup (environment variable: `CONFIG_PATH`)
- Read server port from config

### Iteration 4: In-Memory Cache & Scheduler
- Implement `Cache<K, V>` class (thread-safe)
- Implement `Scheduler` class for fixed-rate polling
- Define `Integration` interface (contract for data sources)
- Define `Snapshot` interface (immutable data)

### Iteration 5: Rendering & Widgets
- Implement `Renderer` class (SSR from widgets)
- Implement `Widget` interface
- Create Clock widget (server-side rendered)
- Modify GET `/` to use renderer

### Iteration 6: Home Assistant Integration (New Module)
- Create `ha-integration` module
- Implement `HaIntegration` implementing `Integration`
- Create REST API client with timeouts
- Poll weather entity on fixed interval
- Create weather widget

### Iteration 7+: Refinement
- Additional integrations and widgets
- Error handling and graceful degradation
- Logging and observability
- Docker packaging
- Performance optimization

## Key Architectural Concepts (Reference)

### Integrations
- Pluggable components that fetch data from external sources
- Produce **immutable snapshots** on fixed intervals
- Encapsulate own error handling; failures never crash app
- (To be added in later iterations)

### Snapshots
- Immutable, typed data from integration at point-in-time
- Include timestamp, stale flag, error message
- Thread-safe; stored in cache by integration ID
- (To be added in iteration 4)

### Widgets
- Self-contained visual components
- Read snapshots via read-only context (never call integrations)
- Generate HTML fragments server-side
- Display graceful indicators when data is missing/stale
- (To be added in iteration 5)

### Cache
- Thread-safe in-memory store for latest snapshots
- Page rendering reads from cache only; never waits on APIs
- Keeps stale data on integration failures
- (To be added in iteration 4)

### Scheduler
- Fixed-rate polling per integration
- Strict connect/read timeouts on all external calls
- Bounded thread pools; no unbounded queues
- (To be added in iteration 4)

### Renderer
- Composes final HTML page from widgets
- Reads from cache only (no I/O blocking)
- Target: < 30 ms page render time
- (To be added in iteration 5)

## Development Guidelines

### Code Style
- **Immutability First:** Models and configs as `data class`; avoid mutable state
- **Fail Closed:** On errors, keep prior state; inform UI clearly
- **Minimal Logging:** Short structured messages; no secrets or PII
- **Old-Browser Discipline:** Test CSS/JS on Android 4.2; avoid modern features
- **No Hidden Coupling:** Explicit inputs/outputs; clear dependencies

### Testing Strategy
- Unit tests for individual components
- Integration tests for server endpoints
- Contract tests for integrations (using JSON fixtures)
- Performance smoke tests (render latency, memory)

### Building & Running
```bash
./gradlew build                     # Build all modules
./gradlew :app:run                 # Run app (prints "Hello World")
./gradlew test                     # Run all tests
./gradlew :app:distZip             # Create distribution
```

## Key Constraints (DO NOT violate)
- **No WebSockets:** Use server-side polling only
- **No persistent database:** In-memory cache only
- **No SPA frameworks:** Server-side rendering only
- **No modern JS:** ES5 compatible only (Android 4.2 WebKit)
- **Cache-first rendering:** Page requests never block on external APIs
- **No secrets in code:** Use environment variables only

## Endpoints (To Be Implemented)

| Endpoint | Method | Response | Purpose | Status |
|----------|--------|----------|---------|--------|
| `/` | GET | HTML | SSR dashboard page (renders from cache) | Iteration 2 |
| `/health` | GET | JSON | Server status: `{"status":"UP"}` | Iteration 2 |

## Next Immediate Step
Run verified build and app:
```bash
./gradlew build       # Should succeed
./gradlew :app:run   # Should print "Hello World"
```

Then proceed to **Iteration 2: Add Web Server** when ready.

## References
- **Full Architecture:** See `llm-agent-architecture-and-principles.md`
- **Root Build Config:** `build.gradle.kts`
- **App Module Config:** `app/build.gradle.kts`
