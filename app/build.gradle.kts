plugins {
    alias(libs.plugins.kotlin.jvm)
    application
}

dependencies {
    // Logging
    implementation(libs.logback.classic)
    
    // Testing
    testImplementation(libs.bundles.testing)
}

application {
    mainClass.set("app.puredash.MainKt")
}

kotlin {
    jvmToolchain(21)
}

tasks.test {
    useJUnitPlatform()
}
