plugins {
    kotlin("jvm") version "1.7.20"
    kotlin("plugin.serialization") version "1.7.20"

    id("net.mamoe.mirai-console") version "2.13.0-RC2"
    id("me.him188.maven-central-publish") version "1.0.0-dev-3"
}

group = "org.itxtech"
version = "2.1.1"
description = "在 Mirai Console 中使用MCL管理包和其他高级功能"

repositories {
    mavenCentral()
}

dependencies {
    compileOnly("org.itxtech:soyuz:1.0.0")
    compileOnly("org.itxtech:mcl:2.1.0")
    compileOnly("com.google.code.gson:gson:2.10")
    //Mirai Console Terminal Deps
    compileOnly("net.mamoe:mirai-console-terminal:2.13.0-RC2")
    compileOnly("org.jline:jline:3.21.0")
}

mavenCentralPublish {
    singleDevGithubProject("iTXTech", "mcl-addon")
    licenseAGplV3()
    useCentralS01()
}

mirai {
    jvmTarget = JavaVersion.VERSION_11
}
