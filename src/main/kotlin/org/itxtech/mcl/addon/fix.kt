package org.itxtech.mcl.addon

import net.mamoe.mirai.console.ConsoleFrontEndImplementation
import net.mamoe.mirai.console.MiraiConsoleImplementation
import net.mamoe.mirai.console.data.Value
import net.mamoe.mirai.console.data.findBackingFieldValue
import net.mamoe.mirai.console.internal.data.builtins.*
import net.mamoe.mirai.console.terminal.consoleLogger
import net.mamoe.mirai.console.util.ConsoleExperimentalApi
import org.itxtech.mcl.Loader
import java.net.URL
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.*
import kotlin.system.exitProcess

fun fixMiraiConsoleLoader() {
    val loader = Loader.getInstance()
    var change = false
    if (loader.config.miraiRepo == "https://repo.itxtech.org") {
        loader.config.miraiRepo = "https://repo.mirai.mamoe.net/keep/mcl"
        change = true
    }
    val mavenRepo = arrayListOf(
        "https://maven.aliyun.com/repository/public",
        "https://repo.huaweicloud.com/repository/maven",
        "https://repo1.maven.org/maven2"
    )
    if (loader.config.mavenRepo.singleOrNull() == "https://maven.aliyun.com/repository/public") {
        loader.config.mavenRepo = mavenRepo
        change = true
    }
    if (loader.config.packages["net.mamoe:mirai-console"]?.channel == "stable") {
        loader.config.packages["net.mamoe:mirai-console"]?.channel = "maven-stable"
        change = true
    }
    if (loader.config.packages["net.mamoe:mirai-console-terminal"]?.channel == "stable") {
        loader.config.packages["net.mamoe:mirai-console-terminal"]?.channel = "maven-stable"
        change = true
    }
    if (loader.config.packages["net.mamoe:mirai-core-all"]?.channel == "stable") {
        loader.config.packages["net.mamoe:mirai-core-all"]?.channel = "maven-stable"
        change = true
    }
    if (loader.config.packages["org.itxtech:mcl-addon"]?.channel != "maven-stable") {
        loader.config.packages["org.itxtech:mcl-addon"]?.channel = "maven-stable"
        change = true
    }
    if (change) {
        loader.saveConfig()
    }

    var updated = false
    val current = loader.version.substringBefore('-')
    for (maven in mavenRepo) {
        try {
            updated = updateMiraiConsoleLoader(maven, current)
            if (updated) break
        } catch (_: Throwable) {
            continue
        }
    }
    if (change || updated) {
        consoleLogger.warning("mcl 已升级请重新启动")
        exitProcess(0)
    }
}

private fun updateMiraiConsoleLoader(maven: String, version: String): Boolean {
    val xml = URL("$maven/org/itxtech/mcl/maven-metadata.xml").readText()
    val release = """<release>(.+)<""".toRegex().find(xml)?.groupValues?.get(1).orEmpty()
    if (release != version) {
        consoleLogger.warning("mcl 将尝试升级")
        val temp = Files.createTempFile("mcl", ".jar")
        val bytes = URL("$maven/org/itxtech/mcl/${release}/mcl-${release}-all.jar").readBytes()
        val filename = "mcl-${release}-all.jar"
        temp.writeBytes(bytes)
        temp.moveTo(Path.of(filename))

        val sh = Path.of("mcl")
        if (sh.isWritable()) {
            sh.writeText(sh.readText().replace("""mcl(.*)\.jar""".toRegex(), filename))
        }

        val cmd = Path.of("mcl.cmd")
        if (cmd.isWritable()) {
            cmd.writeText(cmd.readText().replace("""mcl(.*)\.jar""".toRegex(), filename))
        }

        return true
    }
    return false
}

@OptIn(ConsoleExperimentalApi::class, ConsoleFrontEndImplementation::class)
@Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE", "UNCHECKED_CAST")
fun fixMiraiConsole() {
    with(MiraiConsoleImplementation.getInstance()) {
        val config = consoleDataScope.get(PluginDependenciesConfig::class)
        if (config.repoLoc.singleOrNull().orEmpty().startsWith("https://maven.aliyun.com")) {
            val field = config.findBackingFieldValue(config::repoLoc) as Value<List<String>>
            field.value += "https://repo.huaweicloud.com/repository/maven"
            field.value += "https://repo1.maven.org/maven2"
        }
        configStorageForBuiltIns.store(consoleDataScope.configHolder, config)
    }
}