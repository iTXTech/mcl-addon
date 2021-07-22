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
import net.mamoe.mirai.console.plugin.PluginManager.INSTANCE.disable
import net.mamoe.mirai.console.plugin.PluginManager.INSTANCE.enable
import net.mamoe.mirai.console.plugin.PluginManager.INSTANCE.load
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.JvmPluginLoader
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import net.mamoe.mirai.console.terminal.terminal
import net.mamoe.mirai.console.util.ConsoleExperimentalApi
import net.mamoe.mirai.message.data.MessageChain
import net.mamoe.mirai.message.data.content
import org.itxtech.mcl.Loader
import org.itxtech.mcl.component.Config
import org.itxtech.mcl.component.Logger
import org.itxtech.mcl.impl.DefaultLogger

class Addon{
    val mcl: Loader = Loader.getInstance()

    init {
        try{
            Class.forName("net.mamoe.mirai.console.terminal.MiraiConsoleImplementationTerminalKt")
            mcl.logger = MclLoggerTerminal()
        } catch (e: Exception){
            PluginMain.logger.warning("未使用 Mirai Console Terminal 前端，print 不可用")
            mcl.logger = MclLogger()
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
