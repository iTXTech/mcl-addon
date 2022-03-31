package org.itxtech.mcl.addon;

import net.mamoe.mirai.console.MiraiConsole;
import net.mamoe.mirai.console.internal.plugin.PluginManagerImpl;
import net.mamoe.mirai.console.plugin.Plugin;

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
@SuppressWarnings("ALL")
public class InternalAccessor {
    public static void addResolvedPlugin(Plugin plugin) {
        ((PluginManagerImpl) MiraiConsole.INSTANCE.getPluginManager()).resolvedPlugins.add(plugin);
    }
}