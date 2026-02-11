plugins {
    idea
    id("java-library")
    id("maven-publish")
    id("net.neoforged.moddev") version "2.0.80"
}

tasks.named<Wrapper>("wrapper") {
    distributionType = Wrapper.DistributionType.BIN
}

version = "${project.property("minecraft_version")}-${project.property("mod_version")}"
group = "com.cmhh.halovecs"

repositories {
    mavenLocal()
    maven {
        url = uri("https://maven.theillusivec4.top/")
        content {
            includeGroup("top.theillusivec4.curios")
        }
    }
    maven {
        name = "GeckoLib"
        url = uri("https://dl.cloudsmith.io/public/geckolib3/geckolib/maven/")
        content {
            includeGroupByRegex("software\\.bernie.*")
            includeGroup("com.eliotlash.mclib")
        }
    }
    maven {
        name = "Jared's maven"
        url = uri("https://maven.blamejared.com/")
        content {
            includeGroup("mezz.jei")
            includeGroup("vazkii.patchouli")
        }
    }
    maven {
        url = uri("https://maven.shedaniel.me/")
        content {
            includeGroup("me.shedaniel.cloth")
        }
    }
    maven {
        url = uri("https://cursemaven.com")
        content {
            includeGroup("curse.maven")
        }
    }
}

base {
    archivesName.set(project.property("mod_id") as String)
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
    withSourcesJar()
}

neoForge {
    version = project.property("neo_version") as String

    parchment {
        mappingsVersion = project.property("parchment_mappings_version") as String
        minecraftVersion = project.property("parchment_minecraft_version") as String
    }

    runs {
        create("client") {
            client()
            systemProperty("neoforge.enabledGameTestNamespaces", project.property("mod_id") as String)
        }

        create("server") {
            server()
            programArgument("--nogui")
            systemProperty("neoforge.enabledGameTestNamespaces", project.property("mod_id") as String)
        }

        create("gameTestServer") {
            type = "gameTestServer"
            systemProperty("neoforge.enabledGameTestNamespaces", project.property("mod_id") as String)
        }

        create("data") {
            data()
            programArguments.addAll(
                "--mod",
                project.property("mod_id") as String,
                "--all",
                "--output",
                file("src/generated/resources/").absolutePath,
                "--existing",
                file("src/main/resources/").absolutePath
            )
        }

        configureEach {
            jvmArguments = listOf(
                "-XX:+IgnoreUnrecognizedVMOptions",
                "-XX:+AllowEnhancedClassRedefinition"
            )
            systemProperty("forge.logging.markers", "REGISTRIES")
            logLevel = org.slf4j.event.Level.DEBUG
        }
    }

    mods {
        create(project.property("mod_id") as String) {
            sourceSet(sourceSets.main.get())
        }
    }
}

sourceSets.main.get().resources {
    srcDir("src/generated/resources")
}

configurations {
    create("localRuntime")
    getByName("runtimeClasspath").extendsFrom(getByName("localRuntime"))
}

dependencies {
    // NeoForge
    implementation("net.neoforged:neoforge:${project.property("neo_version")}")

    // Superb Warfare dependency - IMPORTANT: This needs to be provided as a compiled JAR
    // For development, you need to obtain the SuperbWarfare JAR and place it in the libs/ folder
    // Then uncomment the line below:
    implementation(files("libs/superbwarfare-${project.property("minecraft_version")}.jar"))
    
    // Temporary: Comment out the project dependency to allow building
    // implementation(project(":SuperbWarfare-0.8.8-1.21"))

    // GeckoLib for animations
    implementation("software.bernie.geckolib:geckolib-neoforge-1.21.1:4.7.5")

    // Curios API
    runtimeOnly("top.theillusivec4.curios:curios-neoforge:9.2.0+1.21.1")
    compileOnly("top.theillusivec4.curios:curios-neoforge:9.2.0+1.21.1:api")

    // Optional mod dependencies
    compileOnly("mezz.jei:jei-1.21.1-common-api:${project.property("jei_version")}")
    compileOnly("mezz.jei:jei-1.21.1-neoforge-api:${project.property("jei_version")}")
    runtimeOnly("mezz.jei:jei-${project.property("minecraft_version")}-neoforge:${project.property("jei_version")}")
}

val generateModMetadata = tasks.register<ProcessResources>("generateModMetadata") {
    val replaceProperties = mapOf(
        "minecraft_version" to project.property("minecraft_version"),
        "minecraft_version_range" to project.property("minecraft_version_range"),
        "neo_version" to project.property("neo_version"),
        "neo_version_range" to project.property("neo_version_range"),
        "loader_version_range" to project.property("loader_version_range"),
        "mod_id" to project.property("mod_id"),
        "mod_name" to project.property("mod_name"),
        "mod_license" to project.property("mod_license"),
        "mod_version" to project.property("mod_version"),
        "mod_authors" to project.property("mod_authors"),
        "mod_description" to project.property("mod_description")
    )
    inputs.properties(replaceProperties)
    expand(replaceProperties)
    from("src/main/templates")
    into("build/generated/sources/modMetadata")
}

sourceSets.main.get().resources.srcDir(generateModMetadata)
neoForge.ideSyncTask(generateModMetadata)

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks.named("createMinecraftArtifacts") {
    dependsOn(tasks.named("generateModMetadata"))
}

idea {
    module {
        isDownloadSources = true
        isDownloadJavadoc = true
    }
}
