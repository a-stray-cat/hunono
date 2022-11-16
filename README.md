一款Mirai-Console插件
====================
#### 第一次运行：
  先运行一次mcl，会在config目录下生成插件的目录
  修改config.yml文件中的path和apiKey内容
### 目前已有模块：
- 指令模块

  | 指令            | 功能           |
  |---------------|---------------|
  | 添加指令 触发词 指令内容 | 发送触发词，回复指令内容 |
  | 修改指令 触发词 指令内容 | 修改已有触发词的指令内容 |
  | 查询指令 触发词      | 查询该触发词的指令内容 |
  | 删除指令 触发词      | 删除已有触发词和指令内容 |
  | 所有指令          | 查询所有触发词和指令内容 |
  | 删除数据          | 删除所有指令模块数据 |

- Pixiv模块

  | 指令        | 功能                 |
  |--------------------|-----|
  | 查看作品 作品ID | 发送该作品ID相关信息        |
  | 查找作品 作者ID | 发送该作者的作品ID       |
  | 关注列表      | 查看当前关注列表           |
  | 来点        | 从关注列表随机发送一套作品ID的图片 |
  | 添加关注 作者ID | 添加作者ID到关注列表        |
  | 移除关注 作者ID | 将作者ID从关注列表移除       |
  | 搜索 关键词      | 搜索关键词内容       |
  
### MCL命令
  |指令|功能|
  |-----|-----|
  |/hn path 文件路径|修改config.yml文件中的path内容|
  |/hn aFollow 作者ID |添加作者到关注列表|
  |/hn dFollow 作者ID |删除关注列表中该作者|
  |/hn cleanCache |删除images文件夹|

---------------------------------------------------------------------
#### Pixiv模块说明：
1. 该模块使用acg的API获取图片，非Pixiv.net
2. 部分图片在群聊里会被屏蔽，但私聊并未屏蔽（我也不太懂屏蔽机制是啥）
