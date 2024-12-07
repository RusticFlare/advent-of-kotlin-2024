plugins {
    kotlin("jvm") version "2.1.0"
}

sourceSets {
    main {
        kotlin.srcDir("src")
    }
}

dependencies {
    implementation("com.github.h0tk3y.betterParse:better-parse:0.4.4")
}

tasks {
    wrapper {
        gradleVersion = "8.11.1"
    }
}
