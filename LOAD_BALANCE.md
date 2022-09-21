负载均衡方案

**方案一：传统注册中心的客户端负载均衡方案**

![方案一](https://s2.loli.net/2022/04/29/tWgbUN4KQvXfYjy.png)

步骤：

1、tcp-server启动即向tcp-center注册中心上报自己的状态信息(IP、PORT、连接数等)。

2、tcp-center将节点状态信息存储到redis中。

3、Client通过GetTCPIP接口获取tcp-server列表信息（按最小连接数排好序），从第一个开始尝试建立连接。

备注：该方案适用于传统自建机房方案，可以避免SLB/CLB高性能、高可用要求的技术复杂度。

**方案二：基于云厂商SLB/CLB负载均衡方案**

![方案二](https://s2.loli.net/2022/04/29/MGJoRlcNiSrIZjm.png)

说明：

1、客户端直连SLB/CLB，SLB/CLB通过最小连接数策略选择一个后端Server建立连接。

备注：该方案适用于云原生架构，可以节省web-server服务器成本，及客户端复杂度。


** 技术方案选型最终需要根据实际业务情况来决定 **