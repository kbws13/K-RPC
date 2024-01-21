package xyz.kbws.remoting.transport.socket.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * @author kbws
 * @date 2024/1/20
 * @description: Socket客户端
 */
public class HelloClient {
    private static final Logger logger = LoggerFactory.getLogger(HelloClient.class);

    public Object send(Message message, String host, int port) {
        // 1.创建 Socket 对象并且指定服务器的地址和端口号
        try(Socket socket = new Socket(host,port)) {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            // 2.通过输出流向服务器端发送请求消息
            objectOutputStream.writeObject(message);
            // 3.通过输入流获取服务器端响应的信息
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            return objectInputStream.readObject();
        }catch (IOException | ClassNotFoundException e) {
            logger.error("occur exception:",e);
        }
        return null;
    }

    public static void main(String[] args) {
        HelloClient helloClient = new HelloClient();
        Message message = (Message) helloClient.send(new Message("content from client"),"127.0.0.1",6666);
        System.out.println("client receive message:" + message.getContent());
    }
}
