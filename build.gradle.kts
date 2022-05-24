plugins {
    kotlin("jvm") version "1.6.10"
    id("io.gitlab.arturbosch.detekt") version "1.19.0"
    jacoco

    `java-library`
    `maven-publish`
     signing
}

group = "io.github.chriskn"
version = "0.3.1"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
    mavenCentral()
}

val structurizrVersion = "1.12.2"
val structurizrPlantUmlVersion = "1.6.3"
val junitVersion = "5.8.2"
val assertJVersion = "3.22.0"
val detektVersion = "1.19.0"

dependencies {
    implementation(kotlin("stdlib-jdk8"))

    implementation("com.structurizr:structurizr-core:$structurizrVersion")
    implementation("com.structurizr:structurizr-plantuml:$structurizrPlantUmlVersion")

    detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:$detektVersion")

    testImplementation(platform("org.junit:junit-bom:$junitVersion"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.assertj:assertj-core:$assertJVersion")
    implementation(kotlin("script-runtime"))
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "11"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "11"
    }
    test {
        useJUnitPlatform()
    }
}

detekt {
    buildUponDefaultConfig = true
    config = files("$projectDir/config/detekt.yml")
    parallel = true
    autoCorrect = true
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

tasks.javadoc {
    if (JavaVersion.current().isJava9Compatible) {
        (options as StandardJavadocDocletOptions).addBooleanOption("html5", true)
    }
}
