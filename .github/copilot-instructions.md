# PureDash - Copilot Instructions

**Project Status:** Iteration 2 - Web Server Implementation  
**Guiding Principle:** Minimal implementation, minimal changes

## Core Development Principle

> **Implement the minimum required to achieve the goal. Add only what is needed. No more.**

- Add dependencies ONLY when actually used
- Implement only what's needed for current iteration
- Avoid speculative/future-proofing code
- Keep codebase simple, understandable, maintainable
- Clean up unused code immediately
- Prefer built-in Java over external frameworks when possible

## Project Overview
PureDash is a **lightweight server-side rendered (SSR) dashboard** designed to run reliably on legacy browsers (Android 4.2+). It fetches data from integrations, caches results, and renders pure HTML with minimal JavaScript. The system prioritizes **simplicity, resilience, extensibility, and minimal dependencies**.

**Technology Stack:**
- **Language:** Kotlin with Java 21
- **Server:** Java built-in HttpServer (zero external web framework)
- **Build System:** Gradle with version catalog
- **Minimal Dependencies:** Only what's actually used

## Current Project Structure

```
PureDash/
├── app/                                # Main application module
│   ├── src/main/kotlin/app/puredash/
│   │   └── Main.kt                    # HTTP server entry point
│   ├── src/test/kotlin/               # Test directory (empty)
│   └── build.gradle.kts               # App module config
│
├── build.gradle.kts                    # Root build config
├── settings.gradle.kts                 # Module configuration
├── gradle/libs.versions.toml          # Version catalog (MINIMAL - only used)
├── gradle.properties                   # Build constants
└── llm-agent-architecture-and-principles.md  # Architectural spec
```

## Current State (Iteration 2)

### ✅ Completed
- ✅ HTTP server on port 8080 (Java HttpServer)
- ✅ Responds with "OK" to all requests
- ✅ Logging configured (logback-classic)
- ✅ Project builds successfully
- ✅ **Version catalog cleaned - only essential dependencies**

### Current Dependencies
```toml
# gradle/libs.versions.toml
[versions]
- kotlin = "2.2.20"
- junit = "5.10.0"
- logback = "1.5.6"

[libraries]
- kotlin-test (Kotlin test library)
- junit-jupiter (JUnit 5)
- logback-classic (Logging)

[bundles]
- testing (kotlin-test + junit-jupiter)
```

**That's it. No Ktor, no serialization, no HTML builders. Minimal and clean.**

## Implementation Details

### Main.kt - HTTP Server (23 lines)
```kotlin
package app.puredash

import com.sun.net.httpserver.HttpServer
import org.slf4j.LoggerFactory
import java.net.InetSocketAddress

fun main() {
    val logger = LoggerFactory.getLogger("app.puredash.Main")
    
    val port = 8080
    val server = HttpServer.create(InetSocketAddress("0.0.0.0", port), 0)
    
    server.createContext("/") { exchange ->
        exchange.responseHeaders["Content-Type"] = "text/plain"
        val response = "OK"
        exchange.sendResponseHeaders(200, response.length.toLong())
        exchange.responseBody.write(response.toByteArray())
        exchange.responseBody.close()
    }
    
    server.executor = null
    server.start()
    logger.info("PureDash server started on port $port")
}
```

### Why Java HttpServer?
- ✅ Built into Java (no external dependency)
- ✅ Simple to understand and maintain
- ✅ Fast startup
- ✅ Sufficient for SSR dashboard
- ✅ Aligns with minimal implementation principle

When we need a framework (for complex routing, serialization, etc.), we can add Ktor. For now: **YAGNI** (You Aren't Gonna Need It).

## Core Objectives
1. **Compatibility:** Render on Android 4.2 WebKit
2. **Simplicity:** Pure HTML SSR; minimal JS
3. **Extensibility:** Pluggable integrations/widgets
4. **Reliability:** Never block on remote APIs
5. **Minimal Footprint:** Small memory, fast startup
6. **Easy Deployment:** Single JVM process

## Development Practices

### Minimal Implementation Principle
- **Add dependencies incrementally:** Only when needed, not speculatively
- **Simplest solution first:** Use Java stdlib before external libs
- **Clean up immediately:** Remove unused code/dependencies
- **Document trade-offs:** Explain why we're using a library
- **Avoid premature optimization:** Solve problems when they exist

### Code Guidelines
- Use `val` not `var` (immutability)
- Keep functions small (< 30 lines)
- Explicit imports (no wildcard imports)
- Minimal logging (info on startup, warn on issues, error on failures)
- Comments only when non-obvious
- No magic numbers (use named constants)

### Dependency Policy
**Add a dependency ONLY when:**
1. ✅ It's needed for current iteration
2. ✅ We've evaluated simpler alternatives
3. ✅ Benefits clearly outweigh complexity
4. ✅ It's maintained and stable

**Never add for:**
- ❌ Potential future features
- ❌ "Nice to have" functionality
- ❌ Framework fashion
- ❌ Multiple similar libraries

### Testing Strategy
- Unit tests for business logic (use Mockito for mocking)
- Integration tests for endpoints (use Java HttpClient)
- Run tests on each change: `./gradlew build`
- Keep tests simple and focused
- One test should check only one behavior

### Test Guidelines
- **Use Mockito** (`org.mockito.kotlin:mockito-kotlin`) to mock HttpExchange and other dependencies
- **Follow Arrange-Act-Assert (AAA) pattern:**
  - Arrange: Set up test data and mocks
  - Act: Execute the code being tested
  - Assert: Verify the results
- **Use Java `HttpClient`** for HTTP integration tests (modern, built-in, cleaner API)
- Server startup is synchronous, so `server.start()` ensures it's ready immediately
- **Verify with `./gradlew build` only** - no manual server startup or curl testing needed for unit tests

### Git Workflow
- **NEVER commit changes automatically** – only commit when explicitly requested
- Always verify changes work (build + tests pass) before asking for commit
- User controls all git commits – no automatic pushes or commits
- When asked to commit, provide clear commit message following conventional commits

## Build & Run

### Build
```bash
./gradlew build
```

### Run Server
```bash
./gradlew :app:run
```
Server starts on http://localhost:8080 and logs: "PureDash server started on port 8080"

### Test Endpoint
```bash
curl http://localhost:8080/
# Output: OK
```

## Iteration Roadmap

### ✅ Iteration 1: Minimal Setup
- Basic Gradle structure
- Hello World entry point

### ✅ Iteration 2: HTTP Server (Current)
- Java HttpServer on port 8080
- Responds with "OK" to all requests
- Minimal dependencies

### → Iteration 3: Configuration Loading
- Read config from YAML (config/kiosk.yml)
- Load server port from config (default 8080)
- Environment variable overrides
- Graceful error handling

### Iteration 4: Cache & Scheduler
- In-memory cache for snapshots
- Fixed-rate scheduler interface
- Integration contract definition

### Iteration 5: Rendering
- Renderer class (SSR from templates)
- Widget interface
- Basic widgets (clock, static content)

### Iteration 6: Home Assistant Integration
- Create ha-integration module
- REST API client
- Weather entity polling
- Weather widget

### Iteration 7+: Polish & Production
- Additional integrations
- Error handling refinement
- Performance optimization
- Docker support

## Key Constraints
- **No external web frameworks** (using Java HttpServer)
- **No persistent DB** (in-memory only)
- **No SPA frameworks** (SSR only)
- **Cache-first rendering** (never block on APIs)
- **Minimal dependencies** (only what's used)
- **No secrets in code** (env vars only)

## Current Endpoints

| Path | Status |
|------|--------|
| `/` (all paths) | Returns "OK" ✅ |

## When to Add Dependencies

### Example: "Should we add Ktor?"
- Current: Java HttpServer works fine
- Need: Complex routing? Template rendering? JSON serialization?
- Decision: Add Ktor when we need at least 2-3 of these features
- Action: Update libs.versions.toml, update build.gradle.kts, minimal code changes

### Example: "Should we add kotlinx.html for templates?"
- Current: Can use string builders
- Need: HTML generation? Complex nested structures?
- Decision: Add when we have 10+ lines of template logic
- Action: Add to catalog, use in Iteration 5+

## Quick Reference

### Minimal Dependencies Currently
```
Kotlin: 2.2.20 (language)
Java 21 (built-in HttpServer)
Logback: 1.5.6 (logging)
JUnit: 5.10.0 (testing)
```

### Zero External Dependencies For
- Web server (Java built-in)
- HTTP handling (Java built-in)
- Core logic (plain Kotlin)
- Configuration parsing (will add kaml only when needed)

## Files Reference
- `gradle/libs.versions.toml` → Dependency versions (MINIMAL)
- `build.gradle.kts` (root) → Root Gradle config
- `app/build.gradle.kts` → App module config
- `app/src/main/kotlin/app/puredash/Main.kt` → Entry point

## Next Steps

1. ✅ Verify current build/run works
2. → Plan Iteration 3 (Configuration)
3. → Add YAML support (kaml library) when needed
4. → Implement config loading from file + env vars

## Philosophy

> "Perfection is achieved, not when there is nothing more to add, but when there is nothing left to take away."  
> — Often attributed to Saint-Exupéry

Keep PureDash simple. Implement only what's needed. Remove unused code. Clean up immediately.


## Continuous Integration

### GitHub Actions Workflow
- **File:** `.github/workflows/ci.yml`
- **Triggers:** Push to `master` branch, all pull requests (any branch)
- **Purpose:** Verify code compiles and tests pass before merge

### CI Pipeline (3 steps)
1. **Checkout code:** Get repository
2. **Set up JDK 21:** Install Java with caching
3. **Build and test:** `./gradlew build` (includes compile + tests)

### PR Merge Requirements
✅ All CI checks must pass before merge:
- Code compiles successfully
- All tests pass

### Running CI Locally
```bash
./gradlew build          # Same as CI: compiles + tests
```

### Runtime Verification
Server startup and runtime verification should be implemented as **integration tests**, not pipeline steps.

Add tests in `app/src/test/kotlin/`:
```kotlin
@Test
fun serverStartsAndResponds() {
    // Test server startup and responses
}
```

These tests run automatically as part of `./gradlew build`.

### Adding New Tests
When implementing features:
1. Add unit/integration tests in `src/test/kotlin/`
2. CI will auto-run tests as part of build
3. PR will show test results
4. Keep pipeline simple: compile + test
