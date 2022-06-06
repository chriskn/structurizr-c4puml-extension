[![PR pipeline](https://github.com/chriskn/structurizr-c4puml-extension/actions/workflows/pr-pipeline.yml/badge.svg?branch=main)](https://github.com/chriskn/structurizr-c4puml-extension/actions/workflows/pr-pipeline.yml)

# Structurizr C4-PlantUML extension

Structurizr C4-PlantUML extension aims to bridge the gab between to [structurizr java library](https://github.com/structurizr/java) and [C4-PlantUML](https://github.com/plantuml-stdlib/C4-PlantUML) by extending the structurizr model and providing an extended C4-PlantUML writer.   

## Example diagram

The following example container diagram shows the additional features the Structurizr C4-PlantUML extension provides: 

* links and properties for elements and relationships
* icons for elements and relationships
* external containers and system boundaries
* database and queue shapes
* differentiation between synchronous and asynchronous relationships
* advanced layout configuration for C4-PlantUML diagrams

![Example container diagram](./docs/container_example.svg)

The related code, together with more examples, can be found under `src/test/kotlin`

## How to use it 

Structurizr C4-PlantUML extension is available in maven central. `structurizr-core` and `structurizr-plantuml` dependencies are required. Example using gradle kotlin:

```kotlin
repositories {
    mavenCentral()
}

dependencies {
    implementation("com.structurizr:structurizr-core:1.12.2")
    implementation("com.structurizr:structurizr-plantuml:1.6.3")
    implementation("io.github.chriskn:structurizr-c4puml-extension:$currentVersion")
} 
```
