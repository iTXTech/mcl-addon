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

import net.mamoe.mirai.console.command.CommandManager.INSTANCE.register
import org.itxtech.mcl.Loader
import org.itxtech.mcl.addon.logger.ConsoleLogger
import org.itxtech.mcl.addon.logger.TerminalLogger

class Addon{
    val mcl: Loader = Loader.getInstance()

    init {
        try{
            Class.forName("net.mamoe.mirai.console.terminal.MiraiConsoleImplementationTerminalKt")
            mcl.logger = TerminalLogger()
        } catch (e: Exception){
            PluginMain.logger.warning("未使用 Mirai Console Terminal 前端，print 不可用")
            mcl.logger = ConsoleLogger()
        }

        PluginMain.logger.info("iTXTech MCL Version: ${mcl.version}")

        MclcCommand.register()
        MclCommand.register()
    }

    fun runMclCommand(args: Array<String>) {
        mcl.parseCli(args, true)
        mcl.manager.phaseCli()
    }
}
