plugins {
    `java-library`
}

val jupiterVersion: String by project
val assertj: String by project

dependencies {
    // Use JUnit test framework for unit tests
    testImplementation("org.junit.jupiter:junit-jupiter-api:${jupiterVersion}")
    testImplementation("org.junit.jupiter:junit-jupiter-params:${jupiterVersion}")
    testImplementation("org.junit.jupiter:junit-jupiter-params:${jupiterVersion}")
    testImplementation("org.assertj:assertj-core:${assertj}")
    implementation("org.assertj:assertj-core:${assertj}")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:${jupiterVersion}")
}

tasks.test {
    useJUnitPlatform()
    testLogging {
        showStandardStreams = true
    }
}

publishing {
    publications {
        create<MavenPublication>("autodoc-model") {
            artifactId = "autodoc-model"
            from(components["java"])
        }
    }
}