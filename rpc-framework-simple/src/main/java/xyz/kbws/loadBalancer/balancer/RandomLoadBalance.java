package xyz.kbws.loadBalancer.balancer;

import xyz.kbws.loadBalancer.AbstractLoadBalance;
import xyz.kbws.remoting.dto.RpcRequest;

import java.util.List;
import java.util.Random;

/**
 * @author kbws
 * @date 2024/1/23
 * @description: 随机负载平衡策略
 */
public class RandomLoadBalance extends AbstractLoadBalance {
    @Override
    protected String doSelect(List<String> serviceAddresses, RpcRequest rpcRequest) {
        Random random = new Random();
        return serviceAddresses.get(random.nextInt(serviceAddresses.size()));
    }
}
