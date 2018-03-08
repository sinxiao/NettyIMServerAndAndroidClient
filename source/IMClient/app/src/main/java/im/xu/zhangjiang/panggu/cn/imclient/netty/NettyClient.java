package im.xu.zhangjiang.panggu.cn.imclient.netty;

import com.alibaba.fastjson.JSON;
import com.xhj.imserver.biz.protocol.Request;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import im.xu.zhangjiang.panggu.cn.imclient.MessageSender;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class NettyClient implements Runnable  {

    private BlockingQueue<Request> requests = new LinkedBlockingQueue<>();

    /**
     * String字符串解码器
     */
	private static final StringDecoder DECODER = new StringDecoder();

    /***
     * String字符串编码器
     */
	private static final StringEncoder ENCODER = new StringEncoder();

    /**
     * 客户端业务处理Handler
     */
    private IMClientHandler clientHandler ;

    /**
     * 添加发送请求Request
     * @param request
     */
    public void addRequest(Request request) {
        try {
            requests.put(request);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 是否继续进行运行
     */
    private boolean run = true;

    public void run() {
        //远程IP
        String host = "172.20.10.7";
        //端口号
        int port = 10000;
        //工作线程
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            //辅助启动类
            Bootstrap b = new Bootstrap(); // (1)
            //设置线程池
            b.group(workerGroup); // (2)
            //设置socket工厂 不是ServerSocket而是Socket
            b.channel(NioSocketChannel.class); // (3)
            b.handler(new LoggingHandler(LogLevel.INFO));
            //设置管道工厂
            b.handler(new ChannelInitializer<SocketChannel>() {
                public void initChannel(SocketChannel ch) throws Exception {
                    ChannelPipeline pipe = ch.pipeline();
                    // Add the text line codec combination first,
                    pipe.addLast(new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
                    // the encoder and decoder are static as these are sharable
                    //字符串解码器
                    pipe.addLast(DECODER);
                    //字符串编码器
                    pipe.addLast(ENCODER);
                    clientHandler = new IMClientHandler();
                    //IM业务处理类
                    pipe.addLast(clientHandler);
                }
            });

            // Start the client.
            ChannelFuture f = b.connect(host, port).sync(); // (5)

            Channel ch = f.channel();
            ChannelFuture lastWriteFuture = null;
            while(run) {
                //将要发送的Request转化为JSON String类型
                String line = JSON.toJSONString(requests.take());
                if(line != null && line.length() > 0) {//判断非空
                    // Sends the received line to the server.
                    //发送数据到服务器
                    lastWriteFuture = ch.writeAndFlush(line + "\r\n");
                }
            }
            // Wait until all messages are flushed before closing the channel.
            //关闭写的端口
            if (lastWriteFuture != null) {
                lastWriteFuture.sync();
            }
        } catch(Exception ex){
            ex.printStackTrace();
        } finally {
            //优雅的关闭工作线程
            workerGroup.shutdownGracefully();
        }
    }

    /**
     * 增加消息监听接受接口
     * @param messgeReceivedListener
     */
    public void addMessgeReceivedListener(MessageSender.MessgeReceivedListener messgeReceivedListener) {
        clientHandler.addMessgeReceivedListener(messgeReceivedListener);
    }

    /***
     *  移除消息监听接口
     * @param messgeReceivedListener
     */
    public void remove(MessageSender.MessgeReceivedListener messgeReceivedListener) {
        clientHandler.remove(messgeReceivedListener);
    }
}
