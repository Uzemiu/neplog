# Neplog

这是一个基于Springboot + Hibernate + Redis + Vue开发的轻量级前后端分离个人博客项目的后端部分。
前端仓库见[neplog-web](https://github.com/Uzemiu/neplog-web) 。

同时也是一个个人以学习为主要目的的项目，主要是对以上所有技术的使用层面上的熟悉（其实就是做来玩玩）。

目前项目基本功能开发完毕，正在测试阶段，可能存在许多BUG。
如果发现BUG或者有更好的建议欢迎交流以及PR（不过好像除了我也不会有人用）。

顺便也欢迎各位dalao来审计代码😀

👉 [预览](https://neplog.cn)（暂时不开放后台）

## 食用方法

👉 [食用方法](https://www.neptu.cn/article/2)

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

### [0.3.1] - 2021-05-26

- 完善评论功能
- 完成点赞功能

### [0.3.5] - 2021-05-31

- 完成站点地图生成

### [0.4] - 2021-06

- 删除COS配置
- 邮件配置改为yaml配置