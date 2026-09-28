plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

group = "dev.elysia"
version = "0.1.0"

kotlin {
    jvmToolchain(25)

    jvm()

    sourceSets {
        commonMain.dependencies {
            compileOnly(compose.runtime)
            compileOnly(compose.foundation)
            compileOnly(compose.material3)
            compileOnly(compose.ui)
        }
    }
}