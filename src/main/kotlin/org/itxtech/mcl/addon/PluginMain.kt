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
import net.mamoe.mirai.console.command.RawCommand
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import net.mamoe.mirai.message.data.MessageChain
import net.mamoe.mirai.message.data.content
import org.itxtech.mcl.Loader
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
        try{
            Class.forName("org.itxtech.mcl.Loader")
        } catch (e: Exception){
            logger.error("Mirai Console was not loaded by iTXTech Mirai Console Loader")
            logger.error("Please check https://github.com/iTXTech/mirai-console-loader")
            return
        }
        mcl = Loader.getInstance()
        logger.info("iTXTech MCL Version: ${mcl.version}")

        MclCommand.register()
        mcl.logger = MclLogger
    }

    object MclCommand : RawCommand(
        this,
        primaryName = "mcl",
        usage = "mcl <args>",
        description = "Access iTXTech MCL from Mirai Console"
    ) {
        override suspend fun CommandSender.onCommand(args: MessageChain) {
            mcl.parseCli(args.map { it.content }.toTypedArray(), true)
            mcl.manager.phaseCli()
        }
    }

    object MclLogger : Logger{
        private var logLevel = Logger.LOG_DEBUG

        override fun setLogLevel(lvl: Int) {
            logLevel = lvl
        }

        override fun log(s: String, lvl: Int) {
            if (lvl < logLevel) {
                return
            }
            when(lvl){
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
            info(s)
        }

        override fun logException(e: Throwable) {
            error(DefaultLogger.getExceptionMessage(e))
        }
    }
}
