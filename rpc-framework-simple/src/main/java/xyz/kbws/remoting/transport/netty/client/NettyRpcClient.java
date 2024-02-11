package xyz.kbws.remoting.transport.netty.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.handler.logging.LoggingHandler;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import xyz.kbws.enums.CompressTypeEnum;
import xyz.kbws.enums.SerializationTypeEnum;
import xyz.kbws.factory.SingletonFactory;
import xyz.kbws.registry.ServiceDiscovery;
import xyz.kbws.registry.zookeeper.ZkServiceDiscoveryImpl;
import xyz.kbws.remoting.constants.RpcConstants;
import xyz.kbws.remoting.dto.RpcMessage;
import xyz.kbws.remoting.dto.RpcRequest;
import xyz.kbws.remoting.dto.RpcResponse;
import xyz.kbws.remoting.transport.RpcRequestTransport;
import xyz.kbws.remoting.transport.netty.codec.RpcMessageDecoder;
import xyz.kbws.remoting.transport.netty.codec.RpcMessageEncoder;

import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * @author kbws
 * @date 2024/2/3
 * @description: 初始化并关闭引导对象
 */
@Slf4j
public final class NettyRpcClient implements RpcRequestTransport {

    private final ServiceDiscovery serviceDiscovery;
    private final UnprocessedRequests unprocessedRequests;
    private final ChannelProvider channelProvider;
    private final Bootstrap bootstrap;
    private final EventLoopGroup eventLoopGroup;

    public NettyRpcClient() {
        // 初始化资源，如EventLoopGroup、Bootstrap
        eventLoopGroup = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .handler(new LoggingHandler(LogLevel.INFO))
                // 连接的超时时间，如果超过此时间或无法建立连接，则连接将失败
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) {
                        ChannelPipeline p = ch.pipeline();
                        // 如果在15秒内没有向服务器发送数据，则会发送心跳请求
                        p.addLast(new IdleStateHandler(0, 15, 0, TimeUnit.SECONDS));
                        p.addLast(new RpcMessageEncoder());
                        p.addLast(new RpcMessageDecoder());
                        p.addLast(new NettyRpcClientHandler());
                    }
                });
        this.serviceDiscovery = new ZkServiceDiscoveryImpl();
        this.unprocessedRequests = SingletonFactory.getInstance(UnprocessedRequests.class);
        this.channelProvider = SingletonFactory.getInstance(ChannelProvider.class);
    }

    /**
     * 连接服务器并获取通道，这样就可以向服务器发送RPC消息
     *
     * @param inetSocketAddress 服务地址
     * @return channel
     */
    @SneakyThrows
    public Channel doConnect(InetSocketAddress inetSocketAddress) {
        CompletableFuture<Channel> completableFuture = new CompletableFuture<>();
        bootstrap.connect(inetSocketAddress).addListener((ChannelFutureListener) future -> {
            if (future.isSuccess()) {
                log.info("The client has connected [{}] successful!", inetSocketAddress.toString());
                completableFuture.complete(future.channel());
            } else {
                throw new IllegalStateException();
            }
        });
        return completableFuture.get();
    }

    @Override
    public Object sendRpcRequest(RpcRequest rpcRequest) {
        // 构建返回值
        CompletableFuture<RpcResponse<Object>> resultFuture = new CompletableFuture<>();
        // 获取服务地址
        InetSocketAddress inetSocketAddress = serviceDiscovery.lookupService(rpcRequest);
        // 获取服务地址相关通道
        Channel channel = getChannel(inetSocketAddress);
        if (channel.isActive()) {
            // 放入未处理的请求
            unprocessedRequests.put(rpcRequest.getRequestId(), resultFuture);
            RpcMessage rpcMessage = RpcMessage.builder().data(rpcRequest)
                    .codec(SerializationTypeEnum.KYRO.getCode())
                    .compress(CompressTypeEnum.GZIP.getCode())
                    .messageType(RpcConstants.REQUEST_TYPE).build();
            channel.writeAndFlush(rpcMessage).addListener((ChannelFutureListener) future -> {
                if (future.isSuccess()) {
                    log.info("client send message: [{}]", rpcMessage);
                } else {
                    future.channel().close();
                    resultFuture.completeExceptionally(future.cause());
                    log.error("Send failed:", future.cause());
                }
            });
        } else {
            throw new IllegalStateException();
        }

        return resultFuture;
    }

    public Channel getChannel(InetSocketAddress inetSocketAddress) {
        Channel channel = channelProvider.get(inetSocketAddress);
        if (channel == null) {
            channel = doConnect(inetSocketAddress);
            channelProvider.set(inetSocketAddress, channel);
        }
        return channel;
    }

    public void close() {
        eventLoopGroup.shutdownGracefully();
    }
}
