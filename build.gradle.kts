import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    kotlin("jvm") version "2.1.0"
    id("io.gitlab.arturbosch.detekt") version "1.23.7"
    id("com.adarshr.test-logger") version "4.0.0"
    jacoco

    `java-library`
    `maven-publish`
    signing
}

group = "io.github.chriskn"
version = "0.13.0"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
    mavenCentral()
}

val structurizrVersion = "3.2.1"
val structurizrExportVersion = "3.2.1"
val junitVersion = "5.11.4"
val assertJVersion = "3.27.1"
val detektVersion = "1.23.7"
val kotlinLoggingVersion = "3.0.5"

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("io.github.microutils:kotlin-logging:$kotlinLoggingVersion")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.18.2")

    api("com.structurizr:structurizr-core:$structurizrVersion")
    api("com.structurizr:structurizr-export:$structurizrExportVersion")

    detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:$detektVersion")

    testImplementation(platform("org.junit:junit-bom:$junitVersion"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.assertj:assertj-core:$assertJVersion")
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_17)
    }
}

tasks {
    test {
        useJUnitPlatform()
    }
    javadoc {
        if (JavaVersion.current().isJava9Compatible) {
            (options as StandardJavadocDocletOptions).addBooleanOption("html5", true)
        }
    }
}

detekt {
    buildUponDefaultConfig = true
    config.setFrom(files("$projectDir/config/detekt.yml"))
    parallel = true
    autoCorrect = true
}

// Force detekt to use a compatible Kotlin version.
// See https://github.com/detekt/detekt/issues/7304
configurations.matching { it.name == "detekt" }.all {
    resolutionStrategy.eachDependency {
        if (requested.group == "org.jetbrains.kotlin") {
            useVersion(io.gitlab.arturbosch.detekt.getSupportedKotlinVersion())
        }
    }
}

java {
    withJavadocJar()
    withSourcesJar()
}

publishing {
    val uri = "github.com/chriskn/structurizr-c4puml-extension"
    publications {
        create<MavenPublication>("mavenJava") {
            artifactId = "structurizr-c4puml-extension"
            from(components["java"])
            versionMapping {
                usage("java-api") {
                    fromResolutionOf("runtimeClasspath")
                }
                usage("java-runtime") {
                    fromResolutionResult()
                }
            }
            pom {
                name.set("Structurizr C4Puml Extension")
                description.set(
                    "Kotlin based extension to the Structurizr java library in order to use extended C4Puml features"
                )
                url.set("https//:$uri")
                licenses {
                    license {
                        name.set("The Apache License, Version 2.0")
                        url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
                    }
                }
                developers {
                    developer {
                        id.set("chriskn")
                        name.set("Christoph Knauf")
                        email.set("knauf.christoph@gmail.com")
                    }
                }
                scm {
                    connection.set("scm:https//:$uri.git")
                    developerConnection.set("scm:git://$uri.git")
                    url.set("https//:$uri")
                }
            }
        }
    }
    repositories {
        maven {
            val releasesRepoUrl = uri("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
            val snapshotsRepoUrl = uri("https://s01.oss.sonatype.org/content/repositories/snapshots/")
            url = if (version.toString().endsWith("SNAPSHOT")) snapshotsRepoUrl else releasesRepoUrl
            credentials {
                username = project.properties["ossrhUsername"].toString()
                password = project.properties["ossrhPassword"].toString()
            }
        }
    }
}

signing {
    sign(publishing.publications["mavenJava"])
}
