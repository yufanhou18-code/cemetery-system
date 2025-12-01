# 陵园管家智慧墓园管理系统

## 项目简介

陵园管家是一款基于 Spring Boot 2.7.18 和 Vue 3.x 开发的智慧墓园管理系统，旨在为墓园管理方和家属提供便捷、高效的数字化服务。

## 技术栈

### 后端技术
- Spring Boot 2.7.18
- MyBatis Plus 3.5.3.1
- MySQL 8.0
- Redis
- Druid 数据库连接池
- JWT 认证
- Knife4j API文档

### 前端技术
- Vue 3.x
- Element Plus
- Vite
- Pinia
- Axios

### 小程序
- 微信小程序原生开发

## 项目结构

```
lygj/
├── cemetery-parent/          # 父POM模块
├── cemetery-common/          # 通用工具模块
│   ├── constant/            # 常量定义
│   ├── result/              # 统一返回结果
│   ├── exception/           # 异常处理
│   └── utils/               # 工具类
├── cemetery-domain/         # 领域模型模块
│   ├── entity/              # 实体类
│   ├── dto/                 # 数据传输对象
│   ├── vo/                  # 视图对象
│   └── mapper/              # 数据访问层
├── cemetery-service/        # 业务服务模块
│   ├── service/             # 服务接口
│   └── impl/                # 服务实现
├── cemetery-web/            # Web接口模块
│   ├── controller/          # 控制器
│   ├── config/              # 配置类
│   └── resources/           # 配置文件
├── cemetery-admin/          # 管理前端模块（Vue 3.x）
│   └── src/main/frontend/   # 前端源码
└── cemetery-miniprogram/    # 微信小程序模块
    └── src/main/miniprogram/ # 小程序源码
```

## 核心功能

### 管理后台
- ✅ 用户管理：管理员、员工、家属账号管理
- 🔄 墓位管理：墓位信息录入、查询、状态管理
- 🔄 预约管理：祭扫预约审核、时间安排
- 🔄 财务管理：墓位销售、服务收费管理
- 🔄 公告管理：系统公告发布与管理

### 微信小程序
- 🔄 墓位查询：按编号、区域查询墓位信息
- 🔄 预约祭扫：在线预约祭扫时间
- 🔄 在线祭扫：网上献花、留言祭奠
- 🔄 墓园导航：地图导航到具体墓位
- 🔄 祭扫记录：查看历史祭扫记录

## 快速开始

### 环境要求
- JDK 1.8+
- Maven 3.6+
- MySQL 8.0+
- Redis 5.0+
- Node.js 16+（前端开发）

### 数据库初始化
1. 创建数据库
```bash
mysql -u root -p < cemetery-domain/src/main/resources/db/schema.sql
```

2. 默认管理员账号
- 用户名：admin
- 密码：123456

### 后端启动
```bash
# 编译项目
mvn clean install

# 启动后端服务
cd cemetery-web
mvn spring-boot:run
```

访问接口文档：http://localhost:8080/doc.html

### 前端启动
```bash
cd cemetery-admin/src/main/frontend
npm install
npm run dev
```

访问管理后台：http://localhost:3000

### 小程序开发
1. 使用微信开发者工具打开 `cemetery-miniprogram/src/main/miniprogram` 目录
2. 配置小程序AppID
3. 点击编译即可预览

## 配置说明

### 后端配置
修改 `cemetery-web/src/main/resources/application.yml` 文件：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/cemetery_db
    username: root
    password: your_password
  redis:
    host: localhost
    port: 6379
```

### 前端配置
修改 `cemetery-admin/src/main/frontend/src/api/request.js` 中的API地址

### 小程序配置
修改 `cemetery-miniprogram/src/main/miniprogram/app.js` 中的baseUrl

## 开发规范

### 代码规范
- 使用统一的代码格式化工具
- 遵循阿里巴巴Java开发手册
- Vue组件使用组合式API（Composition API）

### 命名规范
- 实体类：使用名词，如User、Grave
- 服务类：以Service结尾，如UserService
- 控制器：以Controller结尾，如UserController
- 接口路径：RESTful风格，如/api/users

## 技术支持

如有问题，请联系开发团队。

## 许可证

本项目采用 MIT 许可证。
