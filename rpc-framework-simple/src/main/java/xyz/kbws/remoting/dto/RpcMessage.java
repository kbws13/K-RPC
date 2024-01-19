package xyz.kbws.remoting.dto;

import lombok.*;

/**
 * @author kbws
 * @date 2024/1/19
 * @description:
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class RpcMessage {
    /**
     * rpc 消息类型
     */
    private byte messageType;
    /**
     * 序列化方式
     */
    private byte codec;
    /**
     * 压缩方式
     */
    private byte compress;
    /**
     * 请求 id
     */
    private int requestId;
    /**
     * 请求体
     */
    private Object data;

}
