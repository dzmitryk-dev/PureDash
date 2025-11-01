# Architecture & Development Principles

## 1) Project Overview
A lightweight, server-side rendered (SSR) web dashboard designed to run reliably on a very old Android 4.2 tablet/browser. The backend fetches data from integrations (starting with Home Assistant), caches results, and renders a pure HTML page with zero or near-zero JavaScript. The system prioritizes simplicity, resilience, and extensibility with minimal dependencies and a pluggable architecture.
---

## 2) Core Objectives
- **Compatibility:** Must render correctly on Android 4.2 WebKit (limited CSS/JS support).
- **Simplicity:** Pure HTML SSR; only minimal inline JS when absolutely necessary (for example, a clock).
- **Extensibility:** Pluggable modules for integrations and widgets, discoverable at runtime.
- **Reliability:** Server never blocks page rendering on remote calls; uses cached snapshots.
- **Minimal Footprint:** Small memory usage, fast startup, minimal third-party libraries.
- **Easy Deployment:** Single JVM process and small Docker image.

---

## 3) Key Constraints
- No SPA frameworks or complex client-side logic.
- No WebSockets or server-sent events required for the initial version (server-side polling instead).
- No persistent database for the initial version (in-memory cache only).
- Prefer standard JDK facilities and a small set of Kotlin libraries.

---

## 4) High-Level Architecture
- **Single process** containing:
  - Embedded HTTP server for serving HTML and static assets.
  - Scheduler that periodically polls external integrations.
  - In-memory cache storing the latest snapshots from each integration.
  - Rendering pipeline that produces the dashboard HTML from cached data.
  - Plugin loader that discovers integrations and widgets at runtime.

**Data flow:**
1. Scheduler triggers each integration to collect data on a fixed interval.
2. Integrations return typed snapshots; the system stores them in cache.
3. An HTTP request for the dashboard renders the page using cached snapshots only.
4. The page can auto-refresh with a meta refresh header; an optional inline clock script may be used.

---

## 5) Architectural Building Blocks
- **Integrations:** Components that fetch data from a specific source (for example, Home Assistant). Each defines its polling cadence and produces a snapshot.
- **Snapshots:** Immutable point-in-time data produced by an integration and stored in cache.
- **Widgets:** Small, independent visual blocks on the page. Each reads from one or more snapshots and contributes an HTML fragment (server-side).
- **Renderer:** Composes the page shell and injects widget fragments in a configured order and layout.
- **Cache:** Thread-safe in-memory store keyed by integration identifiers; holds the most recent snapshot for each integration.
- **Scheduler:** Fixed-rate tasks per integration with strict timeouts and simple failure handling.
- **Plugin System:** Runtime discovery of integrations and widgets via a stable contract and service discovery mechanism.

---

## 6) Modular Project Structure (Conceptual)
- **App:** Runtime wiring (configuration, server, scheduler, cache), endpoints, and process lifecycle.
- **Core:** Contracts and abstractions (integration, snapshot, widget, plugin, rendering context).
- **Render:** Page layout, theming, shared formatting utilities, and rules for HTML assembly.
- **Integrations:** One submodule per data source (for example, Home Assistant).
- **Plugins (Local Widgets):** Modules for widgets that don’t require external data (for example, a clock).
- **Assets:** Static CSS and images designed for old browsers.
- **Docs:** Architectural and operational documentation.

---

## 7) Rendering & Front-End Principles
- **SSR-only:** Generate complete HTML on the server; do not rely on client-side rendering.
- **Minimal JS:** If used at all, keep it extremely small and compatible with very old browsers.
- **Compatibility-first CSS:** Favor simple layout techniques that work on old WebKit; avoid modern features that may misbehave.
- **Graceful Degradation:** When data is missing or stale, show clear indicators such as “Updated X minutes ago.”
- **Accessibility & Readability:** High contrast, large fonts, simple structure; avoid webfont downloads.

---

## 8) Plugin & Extensibility Principles
- **Clear Contracts:** Small, stable interfaces for integrations and widgets.
- **Isolation:** Each integration encapsulates its configuration and error handling; failures never crash the app.
- **Discoverability:** New plugins become available by being present on the classpath with a standard registration mechanism.
- **No Cross-Talk:** Widgets do not call integrations directly; they read through the cache via a read-only rendering context.
- **Backward Compatibility:** Keep contracts backward compatible; document any breaking change.

---

## 9) Configuration & Secrets
- **Configuration Source:** A simple properties file with environment variable overrides.
- **Environment First:** Sensitive values such as tokens are provided via environment variables.
- **Declarative Widget Order:** Widget composition and ordering are configured, for example via a comma-separated list.
- **Per-Integration Settings:** Base URLs, tokens, entity IDs, and polling intervals are defined in configuration.

---

## 10) Reliability & Performance
- **Cache-first Rendering:** Page requests never wait on remote APIs; they read from the cache only.
- **Timeouts Everywhere:** Strict connect and read timeouts for external calls.
- **Failure Handling:** On repeated failures, keep prior snapshots and mark them as stale; avoid escalating to process failure.
- **Bounded Resources:** Fixed-size thread pools; avoid unbounded queues and memory growth.
- **Fast Paths:** Keep page rendering quick, with a goal of sub-30 ms when serving from cache.

---

## 11) Security Posture
- **Network Scope:** Bind to local interfaces or trusted LAN; optionally front with a reverse proxy.
- **Simple Auth (Optional):** Basic authentication gate for the dashboard endpoint if desired.
- **Secret Hygiene:** Never log secrets; read tokens only from environment variables; avoid writing secrets to disk.
- **Headers & Caching:** Apply headers that prevent unintended caching if the page is sensitive.

---

## 12) Operational Endpoints
- **Dashboard (slash):** Serves the SSR HTML page.
- **Static Assets (under /assets):** Serves CSS and icons; long-lived caching if safe.
- **Health (under /health):** Minimal status response such as up status, version, and last poll timestamps.

---

## 13) Home Assistant Integration (Behavioral Description)
- **Authentication:** Use a long-lived access token provided through environment variables.
- **Protocol:** Interact with REST endpoints; avoid WebSockets for the initial version.
- **Scope:** Poll selected entities such as weather and temperature sensors at a configured interval.
- **Resilience:** On any error, keep prior snapshot and record a concise error; avoid log spam.

---

## 14) Theming & Layout
- **Themes:** Light and dark variants defined by simple CSS variables or classes; prefer system fonts.
- **Layout:** Conservative multi-column or stacked layout that renders consistently on old WebKit.
- **Icons:** Prefer static raster icons or very small inline SVGs known to work on old browsers.

---

## 15) Testing Strategy
- **Unit Tests (Widgets):** Provide synthetic snapshots and assert that resulting HTML fragments contain expected content.
- **Integration Tests (Server):** Start the server on a random port and assert endpoint responses and headers.
- **Contract Tests (Integrations):** Validate parsing and mapping from recorded JSON fixtures; verify timeout behavior.
- **Non-Functional Checks:** Smoke tests for rendering performance and memory footprint on startup.

### Test Guidelines
- **Use Mockito** (`org.mockito.kotlin:mockito-kotlin`) to mock external dependencies like HttpExchange
- **Follow Arrange-Act-Assert (AAA) pattern:**
  - Arrange: Set up test data and mocks
  - Act: Execute the code being tested
  - Assert: Verify the results using assertions and mock verifications
- **One test, one behavior:** Each test should verify a single piece of functionality
- **Use Java `HttpClient`** for HTTP integration tests (modern, built-in, cleaner API)
- Server startup is synchronous; `server.start()` ensures it's ready immediately
- Verify tests pass with `./gradlew build` only (no manual server startup or curl testing)

---

## 16) Observability & Logging
- **Minimal Logging:** Short, structured messages; no PII or secrets; warnings for repeated failures.
- **Health Signals:** Expose last successful poll time per integration and uptime via the health endpoint.
- **Failure Counters:** Maintain simple counters for fetch errors and timeouts to aid troubleshooting.

---

## 17) Deployment & Packaging
- **Single Executable JAR:** Fat jar with all dependencies for simple execution.
- **Container Image:** Small JRE base, non-root execution, port exposure via environment variables.
- **Configuration via Environment:** Provide tokens and configuration through container environment variables.
- **Reverse Proxy (Optional):** Front with Traefik or similar for TLS and network policy.

---

## 18) Governance & Coding Guidelines
- **Keep Contracts Small:** Explicit inputs and outputs, no hidden coupling.
- **Immutability at the Edges:** Snapshots are immutable; avoid shared mutable state.
- **Fail Closed:** In case of uncertainty (missing config or parse errors), keep prior state and inform via UI.
- **Backward Compatibility:** Evolve plugin contracts carefully and document changes.
- **Old-Browser Discipline:** Avoid modern CSS or JS features unless verified on the target device.

---

## 19) Roadmap (Incremental)
1. **MVP:** Core app, cache, scheduler, basic renderer, single Home Assistant weather widget, clock, meta refresh.
2. **Config & Theming:** Environment overrides, widget ordering, light/dark themes.
3. **Additional Widgets:** Temperature sensors, next calendar event, simple network pings.
4. **Quality:** Enhanced health endpoint, clear error banners, refined logging.
5. **Nice-to-Haves:** Per-widget refresh intervals, multiple pages, basic auth, partial refresh endpoints.

---

## 20) Acceptance Criteria (Per Release)
- **Compatibility:** Page renders legibly on the Android 4.2 tablet without console errors.
- **Resilience:** Temporary integration outages do not prevent page rendering.
- **Performance:** Initial render and refresh complete promptly on modest hardware.
- **Security:** Secrets are only sourced from environment variables; no leakage in logs.
- **Simplicity:** No frameworks beyond the minimal agreed set.

---

## 21) Glossary
- **Integration:** A polling component that gathers data from an external system and produces a snapshot.
- **Snapshot:** Immutable, typed data representing the state from an integration at a point in time.
- **Widget:** A self-contained visual component that renders a portion of the dashboard using snapshots.
- **Renderer:** The server-side composer of the page shell and widget fragments.
- **Plugin:** A package that provides integrations and/or widgets, discovered at runtime.
