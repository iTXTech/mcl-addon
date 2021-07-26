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

import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin

object PluginMain : KotlinPlugin(
    JvmPluginDescription(
        id = "org.itxtech.mcl.addon",
        name = "MCL Addon",
        version = "1.2.1"
    ) {
        author("PeratX")
        info("MCL Addon 支持在 Mirai Console 中使用 Mirai Console Loader 管理包和其他高级功能")
    }
) {
    lateinit var addon: Addon

    override fun onEnable() {
        try {
            Class.forName("org.itxtech.mcl.Loader")
        } catch (e: Exception) {
            logger.error("Mirai Console 并未通过 iTXTech Mirai Console Loader 加载。")
            logger.error("请访问 https://github.com/iTXTech/mirai-console-loader")
            return
        }
        addon = Addon()
    }
}
