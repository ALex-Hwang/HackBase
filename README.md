# HackBase

> zju大规模系统构建课程作业。

---

## 6.4 更新

> RIP 就連香港都禁止悼念了

人员分工：
1. lc, lxw: master, zookeeper
2. hsy, zy: client, region server, HackDFS(这部分打算照搬minisql)


==从今天之后，更新都在对应的文件夹中分开进行。==
==同时更新了参考文件：lwm关于分布式minisql的pdf，具体请移步至refs==


## 6.1更新

### todo

- [x] [Thrift]([Thrift: The Missing Guide (diwakergupta.github.io)](https://diwakergupta.github.io/thrift-missing-guide/#_versioning_compatibility)

目前需要考虑的问题：

- [x] 底层文件系统按照传统方式（行储存）还是按照`HDFS`的列式储存
- [x] `Region Server`这部分使用`C++`还是`java`

---


## 架构

<img src="./assets/1.png">

<img src="./assets/5.png">

### 组件

1. Client
2. Master: 管理维护表的分区信息、维护`Region Server`列表、分配`Region`和负载均衡
3. Region Server：存储和维护分配给自己的`Region`、处理来自`Client`的读写请求
4. Related to Zookeeper：实现`Region`的定位和心跳检测等
5. HackDFS（the file system)

### Region 和 Region Server

<img src="./assets/2.png">

### Region的定位

<img src="./assets/3.png">

<img src="./assets/4.png">



---

## 参考：

1. lwm课件（见`refs`中）
