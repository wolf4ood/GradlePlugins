plugins {
    // Apply the Java Gradle plugin development plugin to add support for developing Gradle plugins
    id("com.gradle.plugin-publish") version "1.0.0" apply false
    checkstyle
    // for publishing to nexus/ossrh/mavencentral
    id("org.gradle.crypto.checksum") version "1.4.0"
    id("io.github.gradle-nexus.publish-plugin") version "1.1.0"
    `maven-publish`
    signing
}

val groupId: String by project;
val projectVersion: String by project;
var deployUrl = "https://oss.sonatype.org/service/local/staging/deploy/maven2/"

// cannot do snapshots for gradle plugins
//if (projectVersion.contains("SNAPSHOT")) {
//    deployUrl = "https://oss.sonatype.org/content/repositories/snapshots/"
//}

subprojects {
    apply(plugin = "checkstyle")
    version = projectVersion
    group = groupId

    // for all gradle plugins:
    pluginManager.withPlugin("java-gradle-plugin") {
        apply(plugin = "maven-publish")
        apply(plugin = "com.gradle.plugin-publish")
    }

    // for all java libs:
    pluginManager.withPlugin("java-library") {
        apply(plugin = "maven-publish")
        if (!project.hasProperty("skip.signing")) {

            apply(plugin = "signing")
            publishing {
                repositories {
                    maven {
                        name = "OSSRH"
                        setUrl(deployUrl)
                        credentials {
                            username = System.getenv("OSSRH_USER") ?: return@credentials
                            password = System.getenv("OSSRH_PASSWORD") ?: return@credentials
                        }
                    }
                }

                signing {
                    useGpgCmd()
                    sign(publishing.publications)
                }
            }
        }
    }

    // configure checkstyle version
    checkstyle {
        toolVersion = "10.0"
        maxErrors = 0 // does not tolerate errors
    }

    repositories {
        mavenCentral()
    }

    // let's not generate any reports because that is done from within the Github Actions workflow
    tasks.withType<Checkstyle> {
        reports {
            html.required.set(false)
            xml.required.set(true)
        }
    }


}


repositories {
    // Use Maven Central for resolving dependencies
    mavenCentral()
}

nexusPublishing {
    repositories {
        sonatype {
            nexusUrl.set(uri("https://oss.sonatype.org/service/local/"))
            snapshotRepositoryUrl.set(uri("https://oss.sonatype.org/content/repositories/snapshots/"))
            username.set(System.getenv("OSSRH_USER") ?: return@sonatype)
            password.set(System.getenv("OSSRH_PASSWORD") ?: return@sonatype)
        }
    }
}