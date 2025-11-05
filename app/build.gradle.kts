plugins {
    alias(libs.plugins.kotlin.jvm)
    application
}

dependencies {
    // Plugin API
    implementation(project(":plugin-api"))
    
    // Logging
    implementation(libs.logback.classic)
    
    // HTML building
    implementation(libs.kotlinx.html)
    
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
