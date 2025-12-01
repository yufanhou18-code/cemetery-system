# ⚰️ 陵园管家智慧墓园管理系统 (Cemetery System)

> 本项目是一个基于 Spring Boot + Vue + 微信小程序的智慧墓园综合管理平台。

---

## 🛠️ 环境准备 (Prerequisites)

在运行项目前，请确保你的电脑已安装以下环境：

* **Java JDK**: 推荐 JDK 17 或 JDK 21 (本项目已做 JDK 21 兼容适配)。
* **Database**: MySQL 8.0+。
* **Cache**: Redis 5.0+ (**必须启动，否则后端无法运行**)。
* **Node.js**: v16+ (用于运行前端管理后台)。
* **IDE**: IntelliJ IDEA (后端) + VS Code (前端) + 微信开发者工具 (小程序)。
* **Git**: 用于版本控制。

---

## 🚀 快速启动指南 (Quick Start)

### 1. 数据库配置 (Database Setup)
**⚠️以此顺序执行 SQL 脚本至关重要，否则会报外键错误！**

1.  创建一个空的数据库，命名为 `cemetery_db`。
2.  在 DataGrip 或 Navicat 中，按以下顺序运行项目 `lygj/sql/` 目录下的脚本：
    * 1️⃣ **`schema.sql`** (初始化核心表结构)
    * 2️⃣ **`fix_missing_data.sql`** (**关键补丁！** 补全缺失的基础用户和订单数据)
    * 3️⃣ **`service_provider_schema.sql`** (服务商模块扩展)
    * 4️⃣ **`digital_memorial_schema.sql`** (数字纪念馆扩展)

### 2. 后端启动 (Backend)
1.  使用 **IntelliJ IDEA** 打开 `lygj` 文件夹（包含 `pom.xml` 的目录）。
2.  等待 Maven 依赖下载完成（建议配置阿里云镜像加速）。
3.  修改配置文件 `cemetery-admin/src/main/resources/application.yml`：
    ```yaml
    spring:
      datasource:
        username: root
        password: 你的MySQL密码  <-- 修改这里
    ```
4.  **确保 Redis 已启动** (运行 `redis-server.exe`)。
5.  运行 `CemeteryAdminApplication.java` 启动项目。
6.  启动成功后，接口文档地址：`http://localhost:8080/doc.html`

### 3. 前端管理后台启动 (Web Admin)
1.  使用 **VS Code** 打开目录：`lygj/cemetery-admin/src/main/frontend`。
2.  打开终端，执行命令：
    ```bash
    npm install   # 安装依赖 (建议配置 npm 淘宝/腾讯镜像)
    npm run dev   # 启动项目
    ```
3.  访问终端显示的本地链接（如 `http://localhost:5173`）即可登录后台。

### 4. 微信小程序启动 (Mini Program)
1.  打开 **微信开发者工具** -> 导入项目。
2.  选择目录：`lygj/cemetery-miniprogram`。
3.  **重要设置**：点击右上角【详情】->【本地设置】，**勾选** ✅“不校验合法域名、web-view...”。
4.  编译即可预览。

---

## 🔧 常见问题与故障排除 (Troubleshooting)

如果在部署过程中遇到问题，请参考以下我们在开发过程中踩过的坑及解决方案：

### 1. 🔴 数据库报错：Foreign key constraint fails
* **现象**：运行扩展脚本时，提示外键约束失败，无法插入数据。
* **原因**：原版脚本 `schema.sql` 只有 3 个用户，但扩展脚本试图引用 ID 为 1000+ 的用户。
* **解决**：务必运行我们新增的 **`sql/fix_missing_data.sql`** 补丁脚本，它会自动补全缺失的用户、订单和逝者数据。

### 2. 🔴 Java 编译报错：java.lang.ExceptionInInitializerError (TypeTag :: UNKNOWN)
* **现象**：IDEA 启动时报错，提示 Lombok 异常。
* **原因**：JDK 21 与旧版 Lombok 不兼容。
* **解决**：已在 `pom.xml` 中将 Lombok 升级至 **1.18.30+** 版本。请重新加载 Maven 依赖。

### 3. 🔴 编译报错：List.of() 找不到符号
* **现象**：提示找不到 `List.of` 方法。
* **原因**：项目语言级别可能被错误识别为 Java 8，而 `List.of` 是 Java 9+ 特性。
* **解决**：代码已统一修改为兼容性更好的 `Arrays.asList(...)` 写法。

### 4. 🔴 启动报错：RedisConnectionException / Connection refused
* **现象**：后端启动失败，控制台大量报错连接拒绝。
* **原因**：本地 Redis 服务未启动。
* **解决**：请下载并运行 Windows 版 Redis，保持 `redis-server.exe` 黑框窗口开启。

### 5. 🔴 Git 推送报错：Failed to connect to github.com port 443
* **现象**：无法 `git push` 代码到 GitHub。
* **原因**：网络代理配置不当。
* **解决**：检查代理软件（如 v2rayN）的端口号，并在终端配置 Git 代理（注意区分 HTTP 和 SOCKS 端口）：
    ```bash
    # 示例（根据实际端口修改）：
    git config --global http.proxy [http://127.0.0.1:10809](http://127.0.0.1:10809)
    git config --global https.proxy [http://127.0.0.1:10809](http://127.0.0.1:10809)
    ```

---

## 👥 贡献说明
提交代码前，请确保已执行 `git pull` 同步最新进度。数据库变更请务必同步更新 SQL 脚本。
