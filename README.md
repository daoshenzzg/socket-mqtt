# socket-core: Netty4.x + MQTT

这是一个基于[Netty4.x](https://netty.io/) + [MQTT](http://mqtt.org/)实现的Push推送基础框架。相比于原生Netty，
socket-core会帮助上层服务做以下工作：

* 为C/S模式开发封装简单统一的编程模式
* 简单高性能的代码
* 统一的连接管理方案
* 统一的线程管理方案
* 网络基础问题的解决与支持：如心跳保持、压缩解压缩、编码与解码、加密与解密等
* 各种网络参数、连接池实现、监听器实现等可配置可替换
* 可实现对等集群
* 提供数据统计/监控组件
* 支持普通socket、MQTT、MQTT web socket协议

# 项目结构
<img src="doc/project.png" height="400px;"/>

* codec: 封装编码与解码
* compression: 封装压缩与解压缩
* count: 封装统计信息
* database: 基于hsql的内存数据库
* encrypt: 封装加密与解密
* future: 封装同步和异步调用
* listener: 封装事件监听，包括消息、通道、异常三类事件监听器
* service: 封装C/S模型、通道、心跳管理、消息分发等核心模块

# 压测报告

单Broker8核16G，支持44万连接；1万客户端 单消息1024B 下行tps: 16万+；
4000客户端 Publish 单消息1024B 上行tps: 17万+，千兆网卡流量基本打满。
备注：Mqtt Server启动内存只分配了5G，如果分配到10G，理论上可以支持百万连接。还有，测试开启了心跳上报。

#### 消息下行能力
<table>
    <tr>
        <th>1万Clients订阅的消息下行能力</th>
        <th>对应下行负载情况</th>
    <tr>
        <td width="50%">
            <img src="doc/sub.png">
        </td>
        <td width="50%">
            <img src="doc/dstat_sub.png">
        </td>
    </tr>
</table>

#### 消息上行能力
<table>
    <tr>
        <th>4000Clients订阅消息上行能力</th>
        <th>对应上行负载情况</th>
    <tr>
        <td width="50%">
            <img src="doc/pub.png">
        </td>
        <td width="50%">
            <img src="doc/dstat_pub.png">
        </td>
    </tr>
</table>

#### 查看连接数情况
<table>
    <tr>
        <th>查看连接数(telnet 10.43.204.61 8001; get status)</th>
        <th>查看连接数(ss -l)</th>
    <tr>
        <td width="50%">
            <img src="doc/status.png">
        </td>
        <td width="50%">
            <img src="doc/ss.png">
        </td>
    </tr>
</table>

# 使用说明

各种测试类的源码在src/test/java/com/yb/socket包路径下: 
包括:
* 普通socket Server/Client
* MQTT socket Server/Client
* 带注册中心的普通socket/MQTT socket
* 基于内存数据库的模拟订阅推送

## 服务启动配置选项
```java
Server server = new Server();
// 设置Broker端口
server.setPort(8000); 
// 设置启动信息统计。默认true
server.setOpenCount(true);
// 设置启用心跳功能。默认true
server.setCheckHeartbeat(true);
// 设置启动服务状态，默认端口8001。通过telnet server_ip 8001; get status查看服务信息
server.setOpenStatus(true);
// 服务状态端口。默认8001
server.setStatusPort(8001);
// 设置服务名称
server.setServiceName("Demo");
// 设置工作线程数量。默认CPU个数+1
server.setWorkerCount(64);
// 是否开户业务处理线程池。默认false
server.setOpenExecutor(true);
// 设置tcp no delay。默认true
server.setTcpNoDelay(true);
// 是否启用keepAlive。默认true
server.setKeepAlive(true);
// 自定义监听器，可处理相关事件
server.addEventListener(new EchoMessageEventListener());
// 设置Broker启动协议。SocketType.MQTT - MQTT协议； SocketType.NORMAL - 普通Socket协议；SocketType.MQTT_WS - MQTT web socket协议；
server.setSocketType(SocketType.MQTT);
// 绑定端口启动服务
server.bind();
```

# 后续规划

TODO

# 压测工具

* https://github.com/daoshenzzg/mqtt-mock

# 参考项目

* https://github.com/netty/netty
* https://github.com/singgel/mqtt_iot_push
* https://github.com/Wizzercn/MqttWk

# 特别鸣谢

在此特别感谢神一般的**春哥**。架构、解决问题的思路以及管理水平都让人钦佩不已，是我学习的榜样。此框架也是基于他很早之前封装netty3.x框架升级改造而来。
