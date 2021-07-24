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

package org.itxtech.mcl.addon.logger

import kotlinx.coroutines.launch
import net.mamoe.mirai.console.command.CommandSender
import net.mamoe.mirai.console.command.isUser
import org.itxtech.mcl.addon.PluginMain
import org.itxtech.mcl.addon.PluginMain.logger
import org.itxtech.mcl.component.Logger
import org.itxtech.mcl.impl.DefaultLogger

open class ConsoleLogger : DefaultLogger() {
    val sender: ThreadLocal<CommandSender?> = ThreadLocal()

    override fun log(s: String, lvl: Int) {
        val sender = this.sender.get()
        if (lvl < logLevel) {
            return
        }
        if (sender != null && sender.isUser()) {
            PluginMain.launch {
                try {
                    sender.sendMessage(s)
                } catch (ignored: Exception) {
                }
            }
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

    override fun println(s: String) {
        info(s)
    }

    override fun print(s: String) {
    }
}
