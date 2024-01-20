package xyz.kbws.remoting.transport;

import xyz.kbws.remoting.dto.RpcRequest;

/**
 * @author kbws
 * @date 2024/1/20
 * @description: 发生RPC请求
 */
public interface RpcRequestTransport {
    /**
     * 发生 RPC 请求到服务端并获得返回结果
     * @param rpcRequest 消息体
     * @return 服务端的数据
     */
    Object sendRpcRequest(RpcRequest rpcRequest);
}
