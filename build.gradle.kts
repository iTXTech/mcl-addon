plugins {
    id("net.mamoe.maven-central-publish") version "0.6.0-dev-2"
    val kotlinVersion = "1.5.20"
    kotlin("jvm") version kotlinVersion
    kotlin("plugin.serialization") version kotlinVersion

    id("net.mamoe.mirai-console") version "2.7-M2"
}

group = "org.itxtech"
version = "1.2.1"
description = "在 Mirai Console 中使用MCL管理包和其他高级功能"

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
    mavenCentral()
}

dependencies {
    implementation("org.itxtech:mirai-console-loader:1.2.1")
    //Mirai Console Terminal Deps
    implementation("net.mamoe:mirai-console-terminal:2.7-M2")
    implementation("org.jline:jline:3.15.0")
}

mavenCentralPublish {
    singleDevGithubProject("iTXTech", "mcl-addon")
    licenseAGplV3()
    useCentralS01()
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions.jvmTarget = "11"
}

tasks.withType<JavaCompile> {
    sourceCompatibility = "11"
    targetCompatibility = "11"
}
