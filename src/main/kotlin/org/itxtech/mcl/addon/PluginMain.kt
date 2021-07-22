/*
 *
 * MCL Addon
 *
 * Copyright (C) 2021 iTX Technologies
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

import net.mamoe.mirai.console.command.CommandManager.INSTANCE.register
import net.mamoe.mirai.console.command.CommandSender
import net.mamoe.mirai.console.command.CompositeCommand
import net.mamoe.mirai.console.command.RawCommand
import net.mamoe.mirai.console.plugin.PluginManager.INSTANCE.enable
import net.mamoe.mirai.console.plugin.PluginManager.INSTANCE.load
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.JvmPluginLoader
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import net.mamoe.mirai.console.util.ConsoleExperimentalApi
import net.mamoe.mirai.message.data.MessageChain
import net.mamoe.mirai.message.data.content
import org.itxtech.mcl.Loader
import org.itxtech.mcl.component.Config
import org.itxtech.mcl.component.Logger
import org.itxtech.mcl.impl.DefaultLogger

object PluginMain : KotlinPlugin(
    JvmPluginDescription(
        id = "org.itxtech.mcl.addon",
        name = "MCL Addon",
        version = "1.1.0"
    ) {
        author("PeratX")
        info("iTXTech MCL 插件助手")
    }
) {
    private lateinit var mcl: Loader

    override fun onEnable() {
        try {
            Class.forName("org.itxtech.mcl.Loader")
        } catch (e: Exception) {
            logger.error("Mirai Console was not loaded by iTXTech Mirai Console Loader")
            logger.error("Please check https://github.com/iTXTech/mirai-console-loader")
            return
        }
        mcl = Loader.getInstance()
        logger.info("iTXTech MCL Version: ${mcl.version}")

        MclcCommand.register()
        MclCommand.register()
        mcl.logger = MclLogger
    }

    fun runMclCommand(args: Array<String>) {
        mcl.parseCli(args, true)
        mcl.manager.phaseCli()
    }

    object MclcCommand : RawCommand(
        this,
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
        this,
        primaryName = "mcl",
        description = "用于调用 MCL 的高级命令"
    ) {
        @SubCommand
        @Description("安装包")
        suspend fun CommandSender.install(
            @Name("package") pkg: String, channel: String = "stable",
            type: String = Config.Package.TYPE_PLUGIN, version: String = "",
            @Name("lock or unlock") lock: String = "unlock"
        ) {
            runMclCommand(arrayListOf("--update-package", pkg, "--channel", channel, "--type", type).apply {
                if (version != "") {
                    add("--version")
                    add(version)
                }
                add(if (lock == "lock") "--lock" else "--unlock")
            }.toTypedArray())
        }

        @SubCommand
        @Description("移除包")
        suspend fun CommandSender.remove(@Name("package") pkg: String, @Name("delete") delete: String = "") {
            runMclCommand(arrayListOf("--remove-package", pkg).apply {
                if (delete != "") add("--delete")
            }.toTypedArray())
        }

        @SubCommand
        @Description("列出已安装的包")
        suspend fun CommandSender.list() {
            runMclCommand(arrayOf("--list-packages"))
        }

        @SubCommand
        @Description("执行脚本load阶段")
        suspend fun CommandSender.run(script: String) {
            mcl.manager.getScript(script)?.apply {
                phase.load?.run()
                return
            }
            logger.error("未找到脚本：${script}")
        }

        @SubCommand
        @Description("执行updater脚本")
        suspend fun CommandSender.update() {
            run("updater")
            JvmPluginLoader.listPlugins().forEach {
                it.load()
                it.enable()
            }
            logger.warning("插件热加载可能导致致命错误，强烈建议重启MCL。")
        }

        @SubCommand
        @Description("获取包信息")
        suspend fun CommandSender.info(@Name("package") pkg: String) {
            runMclCommand(arrayOf("--package-info", pkg))
        }
    }

    object MclLogger : Logger {
        private var logLevel = Logger.LOG_DEBUG

        override fun setLogLevel(lvl: Int) {
            logLevel = lvl
        }

        override fun log(s: String, lvl: Int) {
            if (lvl < logLevel) {
                return
            }
            when (lvl) {
                Logger.LOG_DEBUG -> logger.debug(s)
                Logger.LOG_INFO -> logger.info(s)
                Logger.LOG_WARNING -> logger.warning(s)
                Logger.LOG_ERROR -> logger.error(s)
                else -> logger.info(s)
            }
        }

        override fun debug(s: String) {
            log(s, Logger.LOG_DEBUG)
        }

        override fun info(s: String) {
            log(s, Logger.LOG_INFO)
        }

        override fun warning(s: String) {
            log(s, Logger.LOG_WARNING)
        }

        override fun error(s: String) {
            log(s, Logger.LOG_ERROR)
        }

        override fun println(s: String) {
            info(s)
        }

        override fun print(s: String) {
            //NOP
        }

        override fun logException(e: Throwable) {
            error(DefaultLogger.getExceptionMessage(e))
        }
    }
}
