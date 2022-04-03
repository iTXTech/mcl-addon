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

package org.itxtech.mcl.addon.logger

import kotlinx.coroutines.runBlocking
import net.mamoe.mirai.console.command.CommandSender
import net.mamoe.mirai.console.command.isNotConsole
import net.mamoe.mirai.console.util.sendAnsiMessage
import org.itxtech.mcl.addon.PluginMain.logger
import org.itxtech.mcl.component.Logger
import org.itxtech.mcl.impl.DefaultLogger

open class ConsoleLogger : DefaultLogger() {
    val sender: ThreadLocal<CommandSender?> = ThreadLocal()

    override fun log(s: Any, lvl: Int) {
        if (lvl < logLevel) {
            return
        }
        val sender = this.sender.get()
        val str = when (s) {
            !is String -> {
                s.toString()
            }
            else -> s
        }
        if (sender != null && sender.isNotConsole()) {
            runBlocking {
                try {
                    sender.sendAnsiMessage(str)
                } catch (ignored: Exception) {
                }
            }
            return
        }
        when (lvl) {
            Logger.LOG_DEBUG -> logger.debug(str)
            Logger.LOG_INFO -> logger.info(str)
            Logger.LOG_WARNING -> logger.warning(str)
            Logger.LOG_ERROR -> logger.error(str)
            else -> logger.info(str)
        }
    }

    override fun println(s: Any) {
        info(s)
    }

    override fun print(s: Any) {
    }
}
