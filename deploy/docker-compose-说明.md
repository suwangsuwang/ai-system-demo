# docker-compose.yml 配置说明

## 整体结构

```yaml
services:
```

`services` 是 Docker Compose 的顶层配置项，下面的每个子项代表一个独立的容器服务。

---

## 1. nginx 服务（反向代理网关）

```yaml
nginx:
  image: nginx:latest
  container_name: nginx-gateway
  ports:
    - "8089:80"
  volumes:
    - ./nginx.conf:/etc/nginx/conf.d/default.conf
  depends_on:
    - app
```

| 参数 | 含义 |
|------|------|
| `image: nginx:latest` | 使用 Docker Hub 上最新的 Nginx 官方镜像 |
| `container_name: nginx-gateway` | 给容器起一个固定名字 `nginx-gateway`，方便管理（否则 Docker 会自动生成随机名） |
| `ports: "8089:80"` | **端口映射**。`8089:80` 表示：将宿主机的 **8089** 端口映射到容器内部的 **80** 端口。用户通过 `http://localhost:8089` 访问，实际请求被转发到 Nginx 容器的 80 端口 |
| `volumes:` | **文件挂载**。将宿主机上 `./nginx.conf`（相对于 docker-compose.yml 所在目录）挂载到容器内的 `/etc/nginx/conf.d/default.conf`，这样 Nginx 会使用你自定义的配置，而不是镜像自带的默认配置 |
| `depends_on: - app` | **启动依赖**。表示 nginx 容器要等 `app` 容器**启动之后**才会启动。注意：这只是控制启动顺序，不保证 app 已经完全就绪 |

---

## 2. app 服务（Spring Boot 应用）

```yaml
app:
  image: springboot-demo:v2
  container_name: springboot-app
  ports:
    - "8082:8081"
```

| 参数 | 含义 |
|------|------|
| `image: springboot-demo:v2` | 使用本地构建的名为 `springboot-demo`、标签为 `v2` 的镜像（不是从 Docker Hub 拉取的，而是你自己 `docker build` 出来的） |
| `container_name: springboot-app` | 容器固定名称为 `springboot-app` |
| `ports: "8082:8081"` | 将宿主机 **8082** 端口映射到容器内 **8081** 端口。也就是说 Spring Boot 应用在容器里监听的是 **8081** 端口，外部通过 `localhost:8082` 访问 |

> **注意**：`8082:8081` 这种写法说明容器内的应用端口（8081）和对外暴露的端口（8082）不同，这是一种常见做法，可以避免宿主机端口冲突。

---

## 3. redis 服务（缓存）

```yaml
redis:
  image: redis:latest
  container_name: redis-demo
  ports:
    - "6379:6379"
```

| 参数 | 含义 |
|------|------|
| `image: redis:latest` | 使用最新的 Redis 官方镜像 |
| `container_name: redis-demo` | 容器固定名称为 `redis-demo` |
| `ports: "6379:6379"` | 将宿主机的 **6379** 端口映射到容器的 **6379** 端口（Redis 默认端口），应用可以通过 `localhost:6379` 或容器网络中的 `redis:6379` 访问 |

---

## 4. mysql 服务（数据库）

```yaml
mysql:
  image: mysql:8.4
  container_name: mysql-demo
  environment:
    MYSQL_ROOT_PASSWORD: 123456
    MYSQL_DATABASE: ai_system
  ports:
    - "3306:3306"
```

| 参数 | 含义 |
|------|------|
| `image: mysql:8.4` | 使用 MySQL **8.4** 版本镜像（指定了具体版本而非 `latest`，这是生产环境的好习惯，避免版本升级导致不兼容） |
| `container_name: mysql-demo` | 容器固定名称为 `mysql-demo` |
| `environment:` | 注入**环境变量**，MySQL 镜像启动时会读取这些变量进行初始化 |
| `MYSQL_ROOT_PASSWORD: 123456` | 设置 MySQL root 用户的密码为 `123456`（**注意**：生产环境请使用强密码） |
| `MYSQL_DATABASE: ai_system` | 容器启动时**自动创建**一个名为 `ai_system` 的数据库，省去了手动建库的步骤 |
| `ports: "3306:3306"` | 将宿主机 3306 端口映射到容器 3306 端口（MySQL 默认端口） |

---

## 整体架构图

```
浏览器 / 客户端
     │
     ▼
┌─────────────────┐
│  Nginx (8089)   │  ← 反向代理，路由请求到 app
│  nginx-gateway  │
└────────┬────────┘
         │ depends_on
         ▼
┌─────────────────┐
│  Spring Boot    │  ← 业务应用 (8082→8081)
│  springboot-app │
└───┬──────┬──────┘
    │      │
    ▼      ▼
┌──────┐ ┌──────┐
│Redis │ │MySQL │  ← 缓存 + 数据库
│6379  │ │3306  │
└──────┘ └──────┘
```

---

## 端口映射规则详解

### 容器内部端口（右侧）— 可以重复

每个容器有**独立的网络命名空间**，互不干涉。所以：

```yaml
nginx:
  ports:
    - "8089:80"    # nginx 容器内部监听 80

app:
  ports:
    - "8082:80"    # 假设 app 容器内部也监听 80，完全没问题 ✅
```

这就好比两台独立的虚拟机，各自内部都可以跑一个监听 80 端口的程序，谁也不影响谁。

### 宿主机端口（左侧）— 不能重复

宿主机的端口是**全局唯一**的资源，一个端口只能被一个进程绑定：

```yaml
# ❌ 错误示范
nginx:
  ports:
    - "8089:80"

app:
  ports:
    - "8089:8081"   # 冲突！8089 已经被 nginx 占用了
```

启动时会直接报错：

```
Error response from daemon: driver failed programming external connectivity:
Bind for 0.0.0.0:8089 failed: port is already allocated
```

### 总结

| | 可以重复？ | 原因 |
|---|---|---|
| 左侧（宿主机端口） | ❌ 不能 | 宿主机端口全局唯一 |
| 右侧（容器内部端口） | ✅ 可以 | 每个容器有独立的网络栈 |

> **打个比方**：宿主机端口就像一栋楼的门牌号，每个房间号（8089、8090…）只能对应一户人家。而容器内部端口是每户人家**自己房间里的编号**，你家的"1号房间"和别人家的"1号房间"互不影响。

---

## 值得注意的点

1. **没有配置 networks**：所有服务默认会在同一个 `default` 网络中，容器之间可以通过 `container_name`（如 `redis-demo`、`mysql-demo`）互相通信。

2. **app 服务没有 `depends_on`**：应用依赖 Redis 和 MySQL，但没有声明依赖关系。这意味着可能出现 app 启动了但数据库还没就绪的情况。Spring Boot 通常在启动时连接数据库，如果连不上会重试或失败。

3. **MySQL 没有持久化存储**：缺少 `volumes` 配置来挂载数据目录，容器删除后数据库数据会丢失。建议加上类似：
   ```yaml
   volumes:
     - ./mysql-data:/var/lib/mysql
   ```

4. **Redis 同样没有持久化**：建议也加上 volumes 或配置 AOF/RDB 持久化。
