plugins {
    id("me.him188.maven-central-publish") version "1.0.0-dev-3"
    val kotlinVersion = "1.6.0"
    kotlin("jvm") version kotlinVersion

    id("net.mamoe.mirai-console") version "2.11.0-M1"
}

group = "org.itxtech"
version = "2.0.0"
description = "在 Mirai Console 中使用MCL管理包和其他高级功能"

kotlin {
    sourceSets {
        all {
            languageSettings.enableLanguageFeature("InlineClasses")
            languageSettings.optIn("kotlin.Experimental")
        }
    }
}

repositories {
    maven("https://maven.aliyun.com/repository/public")
    mavenCentral()
}

dependencies {
    implementation("org.itxtech:mcl:2.0.0-beta.1")
    //Mirai Console Terminal Deps
    implementation("net.mamoe:mirai-console-terminal:2.11.0-M2.1")
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
