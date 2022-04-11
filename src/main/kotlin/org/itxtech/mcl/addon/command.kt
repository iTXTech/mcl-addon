/*
 *
 * MCL Addon
 *
 * Copyright (C) 2021-2022 iTX Technologies
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * @author PeratX
 * @website https://github.com/iTXTech/mcl-addon
 *
 */

package org.itxtech.mcl.addon

import net.mamoe.mirai.console.MiraiConsole
import net.mamoe.mirai.console.command.CommandSender
import net.mamoe.mirai.console.command.CompositeCommand
import net.mamoe.mirai.console.command.RawCommand
import net.mamoe.mirai.console.plugin.Plugin
import net.mamoe.mirai.console.plugin.PluginManager.INSTANCE.enable
import net.mamoe.mirai.console.plugin.PluginManager.INSTANCE.load
import net.mamoe.mirai.console.plugin.jvm.JvmPluginLoader
import net.mamoe.mirai.console.util.ConsoleExperimentalApi
import net.mamoe.mirai.message.data.MessageChain
import net.mamoe.mirai.message.data.content
import org.itxtech.mcl.addon.PluginMain.addon
import org.itxtech.mcl.addon.logger.ConsoleLogger

fun CommandSender.installLogger() {
    val logger = addon.mcl.logger
    if (logger is ConsoleLogger) {
        logger.sender.set(this)
    }
}

fun uninstallLogger() {
    val logger = addon.mcl.logger
    if (logger is ConsoleLogger) {
        logger.sender.set(null)
    }
}

fun CommandSender.runMclCommand(args: Array<String>) {
    installLogger()
    try {
        addon.runMclCommand(args)
    } catch (e: Exception) {
        addon.mcl.logger.logException(e)
    } finally {
        uninstallLogger()
    }
}

object MclcCommand : RawCommand(
    PluginMain,
    primaryName = "mclc",
    usage = "/mclc <MCL命令行参数>",
    description = "调用 MCL 命令行"
) {
    override suspend fun CommandSender.onCommand(args: MessageChain) {
        val a = args.map { it.content }.toTypedArray()
        runMclCommand(if (a.isEmpty()) arrayOf("-h") else a)
    }
}

@OptIn(ConsoleExperimentalApi::class)
object MclCommand : CompositeCommand(
    PluginMain,
    primaryName = "mcl",
    description = "用于调用 MCL 的高级命令"
) {
    @SubCommand
    @Description("安装包")
    suspend fun CommandSender.install(
        @Name("package") pkg: String, channel: String = "",
        type: String = "", version: String = "",
        @Name("lock or unlock") lock: String = "unlock"
    ) {
        runMclCommand(arrayListOf("--update-package", pkg).apply {
            if (channel != "") {
                add("--channel")
                add(channel)
            }
            if (type != "") {
                add("--type")
                add(type)
            }
            if (version != "") {
                add("--version")
                add(version)
            }
            add(if (lock == "lock") "--lock" else "--unlock")
        }.toTypedArray())
    }

    @SubCommand
    @Description("移除包")
    suspend fun CommandSender.remove(@Name("package") pkg: String) {
        runMclCommand(arrayListOf("--remove-package", pkg).toTypedArray())
    }

    @SubCommand
    @Description("列出已安装的包")
    suspend fun CommandSender.list() {
        runMclCommand(arrayOf("--list-packages"))
    }

    @SubCommand
    @Description("执行模块load阶段")
    suspend fun CommandSender.run(script: String) {
        installLogger()
        with(addon.mcl) {
            manager.getModule(script)?.apply {
                load()
                return
            }
            logger.error("未找到模块：${script}")
        }
        uninstallLogger()
    }

    @SubCommand
    @Description("执行updater模块")
    suspend fun CommandSender.update() {
        run("updater")
        JvmPluginLoader.listPlugins().forEach {
            try {
                it.load()
                it.enable()
                addResolvedPlugin(it)
            } catch (ignored: IllegalStateException) {
            } catch (e: Exception) {
                addon.mcl.logger.logException(e)
            }
        }
        addon.mcl.logger.warning("插件热加载可能导致致命错误，强烈建议重启MCL。")
    }

    @SubCommand
    @Description("获取包信息")
    suspend fun CommandSender.info(@Name("package") pkg: String) {
        addon.runMclCommand(arrayOf("--package-info", pkg))
    }
}

@Suppress("INVISIBLE_REFERENCE", "INVISIBLE_MEMBER")
fun addResolvedPlugin(p: Plugin) {
    (MiraiConsole.INSTANCE.pluginManager as net.mamoe.mirai.console.internal.plugin.PluginManagerImpl)
        .resolvedPlugins.add(p)
}
