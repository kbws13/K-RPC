package xyz.kbws.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author kbws
 * @date 2024/1/21
 * @description:
 */
@AllArgsConstructor
@Getter
public enum RpcConfigEnum {
    RPC_CONFIG_PATH("rpc.properties"),
    ZK_ADDRESS("rpc.zookeeper.address");

    private final String propertyValue;
}
