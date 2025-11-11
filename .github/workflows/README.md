# GitHub Actions CI/CD

This project uses GitHub Actions for continuous integration.

## Workflow: CI

**File:** `.github/workflows/ci.yml`

### Purpose
Automatically verify that code compiles and passes tests on every pull request and push to master.

### Triggers
- **Push:** All commits to `master` branch
- **Pull Request:** All PRs (from any branch to any branch)

### Jobs

#### Build & Test
Runs on: `ubuntu-latest`  
Timeout: `10 minutes`  
Java: `21 (Temurin)`

**Steps:**
1. **Checkout code** - Clones repository
2. **Set up JDK 21** - Installs Java and enables Gradle caching
3. **Build and test** - Compiles and tests all modules: `./gradlew build`

### PR Merge Requirements

Before a PR can be merged, ALL of these checks must pass:
- ✅ Code compiles successfully
- ✅ All tests pass

### Local Testing

Run the same checks locally before pushing:

```bash
# Build and test (same as CI)
./gradlew build
```

### GitHub Actions Tab

View workflow execution and logs:
1. Go to repository on GitHub.com
2. Click "Actions" tab
3. Select "CI" workflow
4. View latest run details and logs

### Troubleshooting

**Build fails in CI but passes locally:**
- Clear Gradle cache: `./gradlew clean`
- Rebuild: `./gradlew build`
- Check Java version: `java -version` (should be 21)

### Caching

Gradle dependency cache is automatically handled by the workflow. First run may take longer (~2 minutes), subsequent runs faster (~30 seconds) due to caching.

### Server Verification

Runtime server verification (e.g., checking if server starts and responds) should be part of **integration tests**, not the pipeline.

Add integration tests in `app/src/test/kotlin/` to test runtime behavior:
```kotlin
@Test
fun testServerStartsAndResponds() {
    // Test implementation here
}
```

These tests will be executed as part of `./gradlew build`.

### Future Enhancements

Possible additions to CI (implement when needed):
- Code coverage reports
- Performance benchmarks
- Docker image building
- Static code analysis
- License compliance checks

For now: Keep CI simple and focused on core verification (compile + test).

## Security

- ✅ Gradle wrapper already in repository (trusted)
- ✅ Official GitHub actions used (checkout, setup-java)
- ✅ No secrets stored in workflow
- ✅ Read-only code checkout for security



