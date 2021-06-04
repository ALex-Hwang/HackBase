# Region Server
---
负责人：hsy, zy

---
## 模块划分
1. minisql(相当于文件系统，所有的读写磁盘的操作都由该模块负责)
2. region server

---
## 数据结构定义
1. region：一张表算一个region。
2. 表的列不限定数据类型，统一按照字符来存。