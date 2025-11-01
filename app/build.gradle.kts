plugins {
    alias(libs.plugins.kotlin.jvm)
    application
}

dependencies {
    // Testing
    implementation(libs.bundles.testing)
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
