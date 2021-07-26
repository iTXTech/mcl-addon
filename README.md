# MCL Addon

`MCL Addon` 支持在 [Mirai Console](https://github.com/mamoe/mirai-console)
中使用 [Mirai Console Loader](https://github.com/iTXTech/mirai-console-loader) 管理包和其他高级功能。

# 安装

1. 使用MCL命令行

```
.\mcl --update-package org.itxtech:mcl-addon --channel stable --type plugin
```

1. 从 [Release](https://github.com/iTXTech/mcl-addon/releases) 下载

# 命令

## `/mclc` - 传统 `MCL` 命令行

```
> mclc --list-packages //同命令行使用，支持大部分参数
> mclc --enable/disable/list-script(s) --dry-run 等特殊指令不可用
```

## `/mcl` - 更现代化的 `MCL` 命令行

```
/mcl info <package>    # 获取包信息
/mcl install <package> [channel] [type] [version] [lock or unlock]    # 安装包
/mcl list    # 列出已安装的包
/mcl remove <package> [delete]    # 移除包
/mcl run <script>    # 执行脚本load阶段
/mcl update    # 执行updater脚本

列出mirai-console包的信息
> mcl info net.mamoe:mirai-console

锁定2.0.0版本的mirai-native
> mcl install org.itxtech:mirai-native stable plugin 2.0.0 lock

解除版本锁定（此时版本可以随意填写）
> mcl install org.itxtech:mirai-native stable plugin ? unlock

安装mirai-api-http（默认频道为stable，类型为plugin）
> mcl install net.mamoe:mirai-api-http

移除mirai-api-http
> mcl remove net.mamoe:mirai-api-http

移除mirai-api-http，并删除其文件（保留配置文件）
> mcl remove net.mamoe:mirai-api-http delete

执行announcement脚本抓取MCL公告
> mcl run announcement

执行updater脚本（将应用包的修改)
> mcl update
```

## 开源许可证

    Copyright (C) 2021 iTX Technologies

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
