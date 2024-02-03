package xyz.kbws.loadBalancer;

import xyz.kbws.remoting.dto.RpcRequest;

import java.util.List;

/**
 * @author kbws
 * @date 2024/1/23
 * @description: 负载平衡策略的接口
 */
public interface LoadBalance {
    /**
     * 从现有服务地址列表中选择一个
     *
     * @param serviceUrlList 服务地址列表
     * @param rpcRequest
     * @return 目标服务地址
     */
    String selectServiceAddress(List<String> serviceUrlList, RpcRequest rpcRequest);
}
