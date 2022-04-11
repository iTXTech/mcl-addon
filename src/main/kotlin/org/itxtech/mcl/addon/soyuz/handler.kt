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

package org.itxtech.mcl.addon.soyuz

import com.google.gson.Gson
import kotlinx.serialization.Serializable
import org.itxtech.mcl.addon.PluginMain.addon
import org.itxtech.mcl.component.Repository
import org.itxtech.mcl.pkg.MclPackage
import org.itxtech.soyuz.ReplyMessage
import org.itxtech.soyuz.Soyuz
import org.itxtech.soyuz.SoyuzWebSocketSession
import org.itxtech.soyuz.handler.SoyuzHandler

fun Any.toJson(): String = Gson().toJson(this)

class MclUpdatePackageHandler : SoyuzHandler("mcl-update-package") {
    @Serializable
    data class Info(
        val id: String,
        val type: String? = null,
        val channel: String? = null,
        val version: String? = null,
        val lock: Boolean? = null
    )

    override suspend fun handle(session: SoyuzWebSocketSession, data: String) {
        val info = Soyuz.json.decodeFromString(Info.serializer(), data)
        val pkg = if (addon.mcl.packageManager.hasPackage(info.id)) {
            addon.mcl.packageManager.getPackage(info.id)
        } else {
            MclPackage(info.id).apply { addon.mcl.packageManager.addPackage(this) }
        }
        pkg.apply {
            if (info.type != null) {
                type = info.type
            }
            if (info.channel != null) {
                channel = info.channel
            }
            if (info.version != null) {
                version = info.version
            }
            if (info.lock != null) {
                versionLocked = info.lock
            }
        }
        addon.mcl.saveConfig()
        session.sendText(ReplyMessage(key, "success").toJson())
    }
}

class MclRemovePackageHandler : SoyuzHandler("mcl-remove-package") {
    @Serializable
    data class Info(
        val id: String
    )

    override suspend fun handle(session: SoyuzWebSocketSession, data: String) {
        val info = Soyuz.json.decodeFromString(Info.serializer(), data)
        if (addon.mcl.packageManager.hasPackage(info.id)) {
            addon.mcl.packageManager.getPackage(info.id)?.apply {
                removeFiles()
                addon.mcl.packageManager.removePackage(info.id)
            }
            session.sendText(ReplyMessage(key, "success").toJson())
        } else {
            session.sendText(ReplyMessage(key, "failed to find package ${info.id}").toJson())
        }
    }
}

class MclListPackageHandler : SoyuzHandler("mcl-list-package") {
    data class Packages(
        val key: String,
        val packages: Collection<MclPackage>
    )

    override suspend fun handle(session: SoyuzWebSocketSession, data: String) {
        session.sendText(Packages(key, addon.mcl.packageManager.packages).toJson())
    }
}

class MclFetchPackage : SoyuzHandler("mcl-fetch-package") {
    @Serializable
    data class Package(
        val id: String
    )

    data class PackageInfo(
        val key: String,
        val id: String,
        val data: Repository.PackageInfo?
    )

    override suspend fun handle(session: SoyuzWebSocketSession, data: String) {
        val id = Soyuz.json.decodeFromString(Package.serializer(), data).id
        session.sendText(
            PackageInfo(
                key, id, try {
                    addon.mcl.repo.fetchPackage(id)
                } catch (e: Exception) {
                    null
                }
            ).toJson()
        )
    }
}

class MclFetchPackageIndex : SoyuzHandler("mcl-fetch-package-index") {
    data class Index(
        val key: String,
        val data: Repository.MclPackageIndex?
    )

    override suspend fun handle(session: SoyuzWebSocketSession, data: String) {
        session.sendText(
            Index(
                key, try {
                    addon.mcl.repo.fetchPackageIndex()
                } catch (e: Exception) {
                    null
                }
            ).toJson()
        )
    }
}
