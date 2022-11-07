# MCL Addon

`MCL Addon` 支持在 [Mirai Console](https://github.com/mamoe/mirai-console)
中使用 [Mirai Console Loader](https://github.com/iTXTech/mirai-console-loader) 管理包和其他高级功能。

通过 [iTXTech Soyuz](https://github.com/iTXTech/soyuz) 提供 `Websocket API` 可用于 `WebUI` 或远程管理 `MCL`。

# 安装

1. 使用MCL命令行，建议一同安装 `iTXTech Soyuz`

```
.\mcl --update-package org.itxtech:mcl-addon
.\mcl --update-package org.itxtech:soyuz
```

2. 从 [Release](https://github.com/iTXTech/mcl-addon/releases) 下载

# 命令

从 `2.1.0` 开始将原来的 `mclc` 替换为 `mcl`, `mcl` 替换为 `mclx`

## `/mcl` - 传统 `MCL` 命令行

```
> mcl --list-packages //同命令行使用，支持大部分参数
> mcl --enable/disable/list-script(s) --dry-run 等特殊指令不可用
```

## `/mclx` - 更现代化的 `MCL` 命令行

```
/mclx info <package>    # 获取包信息
/mclx install <package> [channel] [type] [version] [lock or unlock]    # 安装包
/mclx list    # 列出已安装的包
/mclx remove <package> [delete]    # 移除包
/mclx run <script>    # 执行模块load阶段
/mclx update    # 执行updater模块

列出mirai-console包的信息
> mclx info net.mamoe:mirai-console

锁定2.0.0版本的mirai-native
> mclx install org.itxtech:mirai-native stable plugin 2.0.0 lock

解除版本锁定（此时版本可以随意填写）
> mclx install org.itxtech:mirai-native stable plugin ? unlock

安装mirai-api-http（默认频道为stable，类型为plugin）
> mclx install net.mamoe:mirai-api-http

移除mirai-api-http
> mclx remove net.mamoe:mirai-api-http

移除mirai-api-http，并删除其文件（保留配置文件）
> mclx remove net.mamoe:mirai-api-http delete

执行announcement模块抓取MCL公告
> mclx run announcement

执行updater模块（将应用包的修改)
> mclx update
```

## 开源许可证

    iTXTech MCL Addon
    Copyright (C) 2021-2022 iTX Technologies

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as
    published by the Free Software Foundation, either version 3 of the
    License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
