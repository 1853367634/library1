# 图书管理系统

一个基于 Spring Boot 的图书管理系统，提供图书借阅、用户管理等功能。

## 功能特点

### 用户功能
- 用户注册和登录
- 浏览图书列表
- 搜索图书
- 借阅和归还图书
- 查看个人借阅历史
- 修改个人信息和密码

### 管理员功能
- 图书管理（添加、编辑、删除）
- 用户管理
- 查看用户借阅历史

## 技术栈

### 后端
- Spring Boot 3.x
- MyBatis-Plus
- MySQL 8.x

### 前端
- Bootstrap 5
- jQuery
- Font Awesome

## 项目结构

```
src/main/java/com/example/library/
├── annotation/        # 自定义注解
├── common/           # 通用类
├── config/           # 配置类
├── controller/       # 控制器
├── dto/             # 数据传输对象
├── entity/          # 实体类
├── enums/           # 枚举类
├── interceptor/     # 拦截器
├── mapper/          # MyBatis 接口
├── service/         # 服务接口
│   └── impl/        # 服务实现
└── vo/              # 视图对象

src/main/resources/
├── mapper/          # MyBatis XML 映射文件
├── static/          # 静态资源
└── templates/       # Thymeleaf 模板
    └── common/      # 公共模板
```

## 快速开始

### 环境要求
- JDK 17 或更高版本
- Maven 3.6 或更高版本
- MySQL 8.0 或更高版本

### 数据库配置
1. 创建数据库：
```sql
CREATE DATABASE library DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

2. 修改 `application.yml` 中的数据库配置：
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/library?serverTimezone=Asia/Shanghai
    username: your_username
    password: your_password
```

### 运行项目
1. 克隆项目：
```bash
git clone https://github.com/yourusername/library.git
```

2. 进入项目目录：
```bash
cd library
```

3. 编译并运行：
```bash
mvn spring-boot:run
```

4. 访问：http://localhost:8080

### 默认账号
- 管理员：admin/123456
- 测试用户：user/123456

## 主要功能说明

### 图书管理
- 支持按书名、作者、出版社、分类搜索
- 图书信息包括：书名、作者、出版社、分类、库存等
- 管理员可以添加、编辑、删除图书

### 借阅管理
- 用户可以借阅和归还图书
- 借阅时自动检查库存
- 防止重复借阅同一本书
- 支持查看借阅历史

### 用户管理
- 分普通用户和管理员两种角色
- 支持用户注册和登录
- 管理员可以查看用户信息和借阅历史
- 用户可以修改个人信息和密码

## 安全特性
- 密码加密存储（MD5）
- 基于拦截器的登录验证
- 基于注解的角色权限控制
- 防止重复提交
- XSS 防护

## 开发规范
- 使用统一的代码格式化配置
- 遵循 RESTful API 设计规范
- 使用统一的异常处理机制
- 使用统一的返回结果封装

## 性能优化
- 使用 MyBatis-Plus 的分页插件
- 合理使用数据库索引
- 使用缓存减少数据库访问

## 部署说明
1. 打包：
```bash
mvn clean package
```

2. 运行：
```bash
java -jar target/library-1.0.0.jar
```

3. 使用 nginx 反向代理（可选）：
```nginx
server {
    listen 80;
    server_name library.example.com;

    location / {
        proxy_pass http://localhost:8080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
    }
}
```

## 常见问题
1. 数据库连接失败
   - 检查数据库配置是否正确
   - 确保 MySQL 服务已启动

2. 端口被占用
   - 修改 `application.yml` 中的端口配置
   ```yaml
   server:
     port: 8081
   ```

## 贡献指南
1. Fork 本仓库
2. 创建特性分支
3. 提交代码
4. 创建 Pull Request

## 版本历史
- v1.0.0 (2024-01-01)
  - 基本功能实现
  - 用户和图书管理
  - 借阅功能

## 许可证
MIT License 