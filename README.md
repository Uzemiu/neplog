# Neplog

这是一个基于Springboot + Hibernate + Redis + Vue开发的轻量级前后端分离个人博客项目的后端部分。
前端仓库见[neplog-web](https://github.com/Uzemiu/neplog-web) 。

同时也是一个个人以学习为主要目的的项目，主要是对以上所有技术的使用层面上的熟悉。

开发过程中都有向 
[Halo](https://github.com/halo-dev/halo) 和 
[eladmin](https://github.com/elunez/eladmin) 学习

目前项目正在开发中...




## 食用方法

### 本地测试

### 服务器部署




## 更新日志

### [0.1] - 2020-12-13

- 完成分类，标签功能
- 完成文章基本保存/发布功能 

### [0.2] - 2021-01-08

- 增加评论功能
- 增加本地存储实体用于管理上传到本地的文件
- 增加访问量统计

### [0.2.1] - 2021-01-14

- 增加腾讯云存储
- 认证方式从JWT改为Token+Redis

### [0.3] - 2021-05-12

- 重构小部分代码
    - 配置类Property改为xxxConfig
    - Property则用于用户自定义等动态属性
	- 增加Base实体类，Article都改为继承Base类
- 删除LevelRequiredParam注解
- 以及各种小修小改

### [0.31] - 2021-05-26

- 完善评论功能

## TODO

- [ ] 点赞功能
- [ ] 站点地图
- [ ] 日记