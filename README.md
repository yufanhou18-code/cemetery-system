# ⚰️ 陵园管家智慧墓园管理系统 (Cemetery System)  
# 🏁 团队极速启动指南（必读）

> **写在前面 (致组员)：**  
> 本项目包含 **Java 后端、Vue 前端、微信小程序、MySQL、Redis**。  
> 因组员开发环境不同，经常出现下载失败、外键约束失败、依赖冲突等问题。  
> **请务必严格按照本指南步骤执行。**  
> 遇到任何报错，先检查自己的版本和查看文末【🚑 故障排除】。如果依然不能解决，结合一下AI

---

---

# 🏗️ 第一阶段：软件安装与环境准备（必做）

下列软件必须全部安装，缺一不可。未安装请点击链接下载（一路下一步即可）。

| 软件名称 | 版本要求 | 说明 |
|--------|---------|------|
| **Git** | 最新版 | https://git-scm.com/download/win |
| **JDK** | **21** | 项目已统一适配 JDK 21（不要用 1.8/17） |
| **IntelliJ IDEA** | 2023+ | 后端开发必备 |
| **MySQL** | 8.0+ | 推荐与 DataGrip / Navicat 搭配使用 |
| **Redis** | 5.0+ | Windows 下载：https://github.com/tporadowski/redis/releases/download/v5.0.14.1/Redis-x64-5.0.14.1.zip |
| **Node.js** | LTS 最新版 | 前端后台依赖 Node 环境 |
| **微信开发者工具** | 最新版 | 小程序调试 |

---

---

# 📥 第二阶段：获取代码 / 解决 GitHub 连接失败

## 🔧 如果 GitHub 克隆失败（网络问题）

克隆时报错：

```
Failed to connect to github.com port 443
```
说明 GitHub 被墙。

---

### ✅ 情况 1：你使用加速器（v2rayN / clash 等）

查看加速器底部显示的 **HTTP 代理端口**（一般是 10809）。  
在终端设置 Git 代理：

```bash
git config --global http.proxy http://127.0.0.1:10809
git config --global https.proxy http://127.0.0.1:10809
```

---

### ✅ 情况 2：你没用加速器

下载并开启 **Watt Toolkit（原 Steam++） → GitHub 加速**。

---

## 🔽 克隆项目

在你希望放置项目的目录右键 → **Open Git Bash Here**

```bash
git clone https://github.com/yufanhou18-code/cemetery-system.git
```

---

---

# 🗄️ 第三阶段：数据库初始化（最容易出错！）

⚠️ 必须严格按顺序执行 SQL 脚本！否则一定会报外键约束错误。

## 1. 创建数据库
打开 DataGrip / Navicat → 新建数据库：（不确定MySQLworkbench是否可以，我用的是datagrip）

```
cemetery_db
```

## 2. 按顺序执行 lygj/sql 目录下的脚本：

### 1️⃣ schema.sql  
创建基础表结构。

### 2️⃣ fix_missing_data.sql（最关键）  
补全缺失的用户、订单等必需数据。  
❗ **漏执行这一项会导致所有后续步骤报错！**

### 3️⃣ service_provider_schema.sql  
导入服务商模块表。

### 4️⃣ digital_memorial_schema.sql  
导入数字纪念馆模块表。

---

---

# ☕ 第四阶段：后端启动（IntelliJ IDEA）

## 1. IDEA 打开项目
打开目录：

```
lygj/
```

右下角若提示 **Load Maven Project** → 点击加载。

---

## 2. Maven 下载太慢 → 配置阿里云镜像（这一步和网络有关，可能一会儿成功一会儿不成功）
编辑文件：

```
C:\Users\你的用户名\.m2\settings.xml
```

---

## 3. 修改数据库密码

编辑文件：
```
cemetery-admin/src/main/resources/application.yml
```

找到：
```
spring.datasource.password: 你的数据库密码
```
改成自己的 MySQL 密码。

---

## 4. 启动 Redis

进入 Redis 文件夹，双击运行：

```
redis-server.exe
```

⚠️ 黑色窗口不能关！关掉后端会报：

```
RedisConnectionException: Connection refused
```

---

## 5. 启动后端

在 IDEA 打开：

```
CemeteryAdminApplication.java
```

点击绿色三角启动。

看到：

```
Started CemeteryAdminApplication in ... seconds
```

即可。

后端接口文档地址：

```
http://localhost:8080/doc.html
```

---

---

# 🖥️ 第五阶段：前端启动

---

## 1. 后台管理系统（Vue 前端）

工具：VS Code  
打开目录：

```
lygj/cemetery-admin/src/main/frontend
```

安装依赖：
```bash
npm install
```

启动：
```bash
npm run dev
```

访问：
```
http://localhost:5173
```

---

## 2. 微信小程序前端

这步不归我，我没测试到这儿

---

---

# 🚑 故障排除 (Cheat Sheet)

---

## 🔴 1. Foreign key constraint fails  
**原因：** 未执行 `fix_missing_data.sql`  
**解决：** 执行该脚本。

---

## 🔴 2. IDEA 报错  
```
java.lang.ExceptionInInitializerError
TypeTag :: UNKNOWN
```
**原因：** JDK 21 与旧版 Lombok 冲突。

**解决：** IDEA 右侧 Maven 面板 → 点击 **Reload** 刷新依赖。

---

## 🔴 3. 报错：List.of 找不到符号  
**原因：JDK 版本识别错误**  
**解决：** 拉取最新代码（已统一改为 Arrays.asList）

---

## 🔴 4. RedisConnectionException / Connection refused  
**原因：Redis 未启动**  
**解决：** 再次运行 `redis-server.exe`（窗口不要关闭）

---

## 🔴 5. Access denied for user 'root'  
**原因：application.yml 中的数据库密码不对**  
**解决：** 改为正确密码。

---

---

# 🎉 恭喜你，环境配置完成！  
如果你需要我做：

- 小白版配图安装指南  
- 视频脚本版教程  
- 适配你们团队的 README 主题风格  
- 自动检查脚本（bat / shell）

我也可以继续帮你整理。
