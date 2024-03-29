package xyz.kbws.registry.zookeeper;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import xyz.kbws.enums.RpcErrorMessageEnum;
import xyz.kbws.exception.RpcException;
import xyz.kbws.loadBalancer.LoadBalance;
import xyz.kbws.loadBalancer.balancer.RandomLoadBalance;
import xyz.kbws.registry.ServiceDiscovery;
import xyz.kbws.remoting.dto.RpcRequest;
import xyz.kbws.utils.CuratorUtils;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * @author kbws
 * @date 2024/1/21
 * @description: 基于 Zookeeper 的服务发现
 */
@Slf4j
public class ZkServiceDiscoveryImpl implements ServiceDiscovery {

    private final LoadBalance loadBalance;

    public ZkServiceDiscoveryImpl() {
        this.loadBalance = new RandomLoadBalance();
    }

    @Override
    public InetSocketAddress lookupService(RpcRequest rpcRequest) {
        String rpcServiceName = rpcRequest.getRpcServiceName();
        CuratorFramework zkClient = CuratorUtils.getZkClient();
        List<String> serviceUrlList = CuratorUtils.getChildrenNodes(zkClient, rpcServiceName);
        if (serviceUrlList.isEmpty()) {
            throw new RpcException(RpcErrorMessageEnum.SERVICE_CAN_NOT_BE_FOUND, rpcServiceName);
        }
        // 负载均衡
        String targetServiceUrl = loadBalance.selectServiceAddress(serviceUrlList, rpcRequest);
        log.info("Successfully found the service address:[{}]", targetServiceUrl);
        String[] socketAddressArray = targetServiceUrl.split(":");
        String host = socketAddressArray[0];
        int port = Integer.parseInt(socketAddressArray[1]);
        return new InetSocketAddress(host, port);
    }
}
