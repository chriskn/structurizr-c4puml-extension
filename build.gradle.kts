plugins {
    kotlin("jvm") version "1.6.10"
    id("io.gitlab.arturbosch.detekt") version "1.19.0"
    jacoco
}

group = "com.github.chriskn"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
    mavenCentral()
}

val structurizrVersion = "1.12.1"
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
    withSourcesJar()
}

//tasks.jar {
//    val dependencies = configurations
//        .api
//        .get()
//     val mapped = dependencies.map(::zipTree)
//    from(mapped)
//    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
//}