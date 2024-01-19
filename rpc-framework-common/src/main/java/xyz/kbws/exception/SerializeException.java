package xyz.kbws.exception;

/**
 * @author kbws
 * @date 2024/1/19
 * @description: 序列化异常
 */
public class SerializeException extends RuntimeException{
    public SerializeException(String message) {
        super(message);
    }
}
