package xyz.kbws.remoting.transport.socket;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * @author kbws
 * @date 2024/1/20
 * @description: 发送消息的实体类
 */
@Data
@AllArgsConstructor
public class Message implements Serializable {
    private String content;
}
