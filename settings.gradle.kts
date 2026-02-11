pluginManagement {
    repositories {
        mavenLocal()
        gradlePluginPortal()
        maven {
            name = "NeoForged"
            url = uri("https://maven.neoforged.net/releases")
        }
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

rootProject.name = "halovecs"

// SuperbWarfare should be obtained as a compiled JAR dependency, not included as a subproject
// Remove the include statements below
// include(":SuperbWarfare-0.8.8-1.21")
// project(":SuperbWarfare-0.8.8-1.21").projectDir = file("../SuperbWarfare-0.8.8-1.21")
