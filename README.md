# K-RPC

About A custom RPC framework implemented by Netty+Kyro+Zookeeper（一款基于 Netty+Kyro+Zookeeper 实现的自定义 RPC 框架）

![image](./image/rpc-architure.png)

# 技术栈

- 序列化框架：Hession2、Kyro、Protostuff
- Java 代理机制 
  - 静态代理：代理模式
  - JDK/CGLIB 动态代理
- 线程池：CompletableFuture
- 网络通信：Socket、Netty
- 服务注册中心：Zookeeper 
  - 客户端框架：Curator

# 项目内容

1. 服务注册和服务发现
2. 简单 RPC 框架的实现，包括序列化、反序列化、网络通信等基本机制
3. 封装报文，解析报文，报文的编码器和解码器
4. 负载均衡的实现，包括轮询、随机策略
5. 测试框架，用于编写和运行测试用例，以测试 RPC 框架的正确性
6. 压缩消息体积
7. 引入全局配置

![rpc-architure-detail](./image/rpc-architure-detail.png)

# 通信流程

![rpc-process](./image/rpc-process.png)

RPC 能实现调用远程方法就跟调用本地方法一样（同一个项目中的方法）一样，发起调用请求的那一方叫做调用方，被调用的那一方叫做服务提供方

1. 传输协议：既然 RPC 存在的核心目的是为例实现远程调用，既然是远程调用那就需要提供网络来传输数据，并且 RPC 常用于业务系统之间的数据交互，需要保证其可靠性，所以 RPC 一般默认采用 TCP 来传输。事实上，我们常用的 RPC 协议也是建立在 TCP 之上的。选择 TCP 和核心原因还是因为他的效率要比很多应用层协议高很多
2. 封装一个可用的协议：选择了合适的传输层协议之后，我们需要基于此建立一个我们自己的通用协议，和 HTTP 一样需要封装自己的应用层协议
3. 序列化：网络传输的数据必须是二进制数据，但调用方请求的出入参数都是对象，对象是没法直接在网络中传输的，需要提前把它转换成可传输的二进制，并且要求转换算法是可逆的，这个过程叫做：“序列化”
4. 压缩：序列化后的字节数据体积可能比较大，我们可用对他进行压缩，压缩后的字节数组体积更小，能在传输过程中更加节省带宽和内存

# 教程地址
https://kbws.xyz/docs/rpc
