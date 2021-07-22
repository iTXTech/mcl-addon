plugins {
    val kotlinVersion = "1.5.20"
    kotlin("jvm") version kotlinVersion
    kotlin("plugin.serialization") version kotlinVersion

    id("net.mamoe.mirai-console") version "2.7-M2"
}

group = "org.itxtech"
version = "1.1.0"

kotlin {
    sourceSets {
        all {
            languageSettings.enableLanguageFeature("InlineClasses")
            languageSettings.useExperimentalAnnotation("kotlin.Experimental")
        }
    }
}

repositories {
    maven("https://maven.aliyun.com/repository/public")
}

dependencies {
    //compileOnly("org.itxtech:mcl:1.1.0")
    implementation(files("libs/mcl.jar"))
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions.jvmTarget = "11"
}
