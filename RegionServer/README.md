# Region Server

负责人：hsy, zy

---
## 6.6 更新
### 1 Minisql
找到的某个[minisql](https://github.com/callMeName/MiniSql)

### 2 任务（截止日期6月9日24点）
#### zy：
- [ ] 找一个比较好的minisql的repo（使用上面的也可以）
- [ ] 设计好Log的格式
- [ ] 修改minisql做到操作前记录Log
- [ ] minisql从Log恢复（执行Log中的操作）

#### hsy：
- [ ] 找到比较合适的、基于c++实现的`zkClient`，并实现和`zookeeper`的通信（这是`Region Server`启动时在`zookeeper`注册）
- [ ] 找到一个`rpc`框架，实现`java`和`c++`代码的调用，实现`Client`和`Region Server`之间的通讯
---
## Region Server需要负责的功能
1. 对minisql的操纵（这部分简单，修改接口即可）
2. Log文件的记录（日志格式，如何存储等）
3. 根据Log文件重启回复（容错容灾步骤）
4. 查看自己的性能指标并向？汇报
5. 与`Client`通讯的接口
6. 和`zookeeper`通讯（第一次启动的注册）
---
## 模块划分
1. minisql(相当于文件系统，所有的读写磁盘的操作都由该模块负责)
2. region server

---
## 数据结构定义
1. region：一张表算一个region。
2. 数据库部分使用GitHub上fork下来的代码。