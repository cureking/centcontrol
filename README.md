#  ICentControl

[![AUR](https://img.shields.io/apm/l/vim-mode.svg)](https://github.com/cureking/centcontrol)
[![](https://img.shields.io/badge/Author-Jarry-orange.svg)](https://www.cnblogs.com/Tiancheng-Duan/)
[![](https://img.shields.io/badge/version-0.2-brightgreen.svg)](https://github.com/cureking/centcontrol)
[![GitHub stars](https://img.shields.io/github/forks/cureking/centcontrol.svg?style=social&label=Stars)](https://github.com/cureking/centcontrol)
[![GitHub forks](https://img.shields.io/github/forks/cureking/centcontrol.svg?style=social&label=Fork)](https://github.com/cureking/centcontrol)


该项目是瑞纽宝科技有限公司“工业物联网”的组成部分之一，也是“工业物联网”系统的第二个子系统。

该系统顾名思义，位于企业中控室，也是企业级项目信息系统的顶层系统（尤其是部分不对外连接云平台的企业），故又被称为企业服务器。

## 工业物联网项目
“工业物联网”系统，作为一个以云平台未信息交换中心，以底层AIOT SDK，硬件网关，终控计算展示平台，企业综合中控平台为依托，主要实现数据的实时传入与高效处理，数据多维度展示与查询，多维度数据挖掘与分析等功能的系统，主要面向工业领域，以2B为中心，致力于降低工业成本，提升工业效率，优化工业流程。

>物联网总体架构设计参考了[百度物联网](https://cloud.baidu.com/doc/IOT/ProductDescription.html#.E4.BA.A7.E5.93.81.E6.A6.82.E8.BF.B0)，亚马逊的[Greengrass](https://docs.aws.amazon.com/greengrass/latest/developerguide/what-is-gg.html)(推荐[AWS前沿技术分享](https://www.imooc.com/learn/925))等。

## [IGateway项目](https://github.com/cureking/gateway)
作为其中的软件底层-IGateway，一方面解决了底层硬件技术异构性带来的兼容问题，避免不同平台，不同技术所带来的技术差异性，另一方面，对数据进行初步的清洗与计算，提高数据价值，降低成本。

## [ICentControl项目](https://github.com/cureking/centcontrol)
在整个工业物联网综合系统中，位于中层位置，起到了重要的承接作用。其一方面需要将终端服务器的数据收集到中控服务器，作出企业级数据纵向对比，提供数据概览，并提供预警，配置统一等工作。另一方面在允许的前提下，将数据上传至云平台，并在该过程中保证数据安全性。

## 功能

### 数据处理
+ 数据接收：通过消息队列，由下级终端服务器建立对应Exchange，上传至指定Queue（便于日后扩展）。
+ 数据存储：数据于MySQL中进行持久化操作。
+ 数据展示：配合前端，建立对应接口，由前端负责调用对应数据。
+ 数据上传：1.通过消息队列（以云平台消息队列服务器为中心），实现数据上传；2.通过消息队列跨数据中心的消息共享（或者说消息队列的双活）实现企业消息队列服务器与云平台消息队列服务器数据共享。两个方法各有利弊，具体后面研究。
（补充：不要使用多主互联化数据方式，可以使用跨数据中心进行数据分发的内置工具，毕竟企业服务器在上层并不是主，而且多个企业的企业服务器添加麻烦）
 
### 集成管理
+ 产品管理：按照产品，对数据进行归类。
+ 终端管理：按照终端服务器，对数据进行管理。

### 配置管理
+ 分类：系统纵向，功能横向
+ 控制模型：
+ 消息模式：pull模式或push模式
+ 参考：消息队列，以及配置中心（分布式）

### 通知管理
+ 监控切入：一方面，中控服务器可以根据从消息队列获取的数据进行判断。另一方面，在终端服务器发现异常时，直接通过特定Queue发送通知（可以利用路由分级，扩展性好）
+ 通知方式：日后可以进行分级分等。目前主要集中于外部报警设备（报警声，报警灯），网页（弹窗，页面色彩等）等。



## 开发文档
[Wiki](https://github.com/cureking/centcontrol/wiki)

## 进度文档
进度表