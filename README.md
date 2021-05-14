# Neplog

这是一个基于Springboot + Hibernate + Redis + Vue开发的轻量级前后端分离个人博客项目的后端部分。
同时也是一个个人以学习为主要目的的项目，主要是对以上所有技术的使用层面上的熟悉。

在技术上都有向[Halo]()和[eladmin]()学习

前端仓库见[neplog-web](https://github.com/Uzemiu/neplog-web)

目前项目正在开发中...

## 更新日志

### [0.01] - 2020-11-xx

### [0.1] - 2020-12-13

- 完成文章基本保存/发布功能 

### [0.2] - 2021-01-08

- 增加本地存储实体用于管理上传到本地的文件

### [0.21] - 2021-01-14

- 增加腾讯云存储
- 认证方式从JWT改为Token+Redis

### [0.3] - 2021-5

~~这时，我终于意识到了改写需求文档~~

- 重构小部分代码
    - 配置类Property改为xxxConfig
	- 增加Base实体类，Article都改为继承Base类
- 删除LevelRequiredParam注解
- 以及各种小修小改