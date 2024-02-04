package xyz.kbws.remoting.transport.netty.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import lombok.extern.slf4j.Slf4j;
import xyz.kbws.compress.Compress;
import xyz.kbws.compress.gzip.GzipCompress;
import xyz.kbws.enums.CompressTypeEnum;
import xyz.kbws.enums.SerializationTypeEnum;
import xyz.kbws.remoting.constants.RpcConstants;
import xyz.kbws.remoting.dto.RpcMessage;
import xyz.kbws.remoting.dto.RpcRequest;
import xyz.kbws.remoting.dto.RpcResponse;
import xyz.kbws.serialize.Serializer;
import xyz.kbws.serialize.kryo.KryoSerializer;

import java.util.Arrays;

/**
 * @author kbws
 * @date 2024/2/3
 * @description:
 */
@Slf4j
public class RpcMessageDecoder extends LengthFieldBasedFrameDecoder {

    public RpcMessageDecoder() {
        // lengthFieldOffset：魔术码是4B，版本是1B，然后是全长。所以值是5
        // lengthFieldLength：全长为4B。所以值是4
        // lengthAdjustment：全长包括之前读取的所有数据和9个字节，所以左边的长度是（fullLength-9）
        // initialBytesToStrip：我们将手动检查魔术代码和版本，所以不要剥离任何字节。因此值为0
        this(RpcConstants.MAX_FRAME_LENGTH, 5, 4, -9, 0);
    }

    /**
     * @param maxFrameLength      最大帧长度。它决定了可以接收的数据的最大长度。如果超过，数据将被丢弃
     * @param lengthFieldOffset   长度字段偏移。长度字段是跳过指定字节长度的字段
     * @param lengthFieldLength   长度字段中的字节数
     * @param lengthAdjustment    要添加到长度字段值的补偿值
     * @param initialBytesToStrip 跳过的字节数。如果需要接收所有标头+正文数据，则此值为0。如果只想接收正文数据，那么需要跳过标头消耗的字节数
     */
    public RpcMessageDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength,
                             int lengthAdjustment, int initialBytesToStrip) {
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip);
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        Object decoded = super.decode(ctx, in);
        if (decoded instanceof ByteBuf) {
            ByteBuf frame = (ByteBuf) decoded;
            if (frame.readableBytes() >= RpcConstants.TOTAL_LENGTH) {
                try {
                    return decodeFrame(frame);
                } catch (Exception e) {
                    log.error("Decode frame error!", e);
                    throw e;
                } finally {
                    frame.release();
                }
            }

        }
        return decoded;
    }

    private Object decodeFrame(ByteBuf in) {
        // 注意：必须按顺序读取ByteBuf
        checkMagicNumber(in);
        checkVersion(in);
        int fullLength = in.readInt();
        // 生成RpcMessage对象
        byte messageType = in.readByte();
        byte codecType = in.readByte();
        byte compressType = in.readByte();
        int requestId = in.readInt();
        RpcMessage rpcMessage = RpcMessage.builder()
                .codec(codecType)
                .requestId(requestId)
                .messageType(messageType).build();
        if (messageType == RpcConstants.HEARTBEAT_REQUEST_TYPE) {
            rpcMessage.setData(RpcConstants.PING);
            return rpcMessage;
        }
        if (messageType == RpcConstants.HEARTBEAT_RESPONSE_TYPE) {
            rpcMessage.setData(RpcConstants.PONG);
            return rpcMessage;
        }
        int bodyLength = fullLength - RpcConstants.HEAD_LENGTH;
        if (bodyLength > 0) {
            byte[] bs = new byte[bodyLength];
            in.readBytes(bs);
            // 解压字节
            String compressName = CompressTypeEnum.getName(compressType);
            Compress compress = new GzipCompress();
            bs = compress.decompress(bs);
            // 反序列化对象
            String codecName = SerializationTypeEnum.getName(rpcMessage.getCodec());
            log.info("codec name: [{}] ", codecName);
            Serializer serializer = new KryoSerializer();
            if (messageType == RpcConstants.REQUEST_TYPE) {
                RpcRequest tmpValue = serializer.deserializer(bs, RpcRequest.class);
                rpcMessage.setData(tmpValue);
            } else {
                RpcResponse tmpValue = serializer.deserializer(bs, RpcResponse.class);
                rpcMessage.setData(tmpValue);
            }
        }
        return rpcMessage;

    }

    private void checkVersion(ByteBuf in) {
        // 获取版本并进行比较
        byte version = in.readByte();
        if (version != RpcConstants.VERSION) {
            throw new RuntimeException("version isn't compatible" + version);
        }
    }

    private void checkMagicNumber(ByteBuf in) {
        // 读取前4位，即魔数，并进行比较
        int len = RpcConstants.MAGIC_NUMBER.length;
        byte[] tmp = new byte[len];
        in.readBytes(tmp);
        for (int i = 0; i < len; i++) {
            if (tmp[i] != RpcConstants.MAGIC_NUMBER[i]) {
                throw new IllegalArgumentException("Unknown magic code: " + Arrays.toString(tmp));
            }
        }
    }
}
