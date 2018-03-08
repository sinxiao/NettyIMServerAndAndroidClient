package com.xhj.imserver.imserver;

import com.xhj.imserver.utils.Log;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class NettyServer {
	
	private static final StringDecoder DECODER = new StringDecoder();
	private static final StringEncoder ENCODER = new StringEncoder();
	public static int PORT = 10000;
	public final int HEART_SYNC_TIME = 300;
	private EventLoopGroup bossGroup;
	private EventLoopGroup workerGroup;

	public void bind(int port) throws InterruptedException {
		 //boss线程监听端口，worker线程负责数据读写
		bossGroup = new NioEventLoopGroup(1);
		workerGroup = new NioEventLoopGroup();
		//辅助启动类
		ServerBootstrap bootstrap = new ServerBootstrap();
		try {
			//设置线程池
			bootstrap.group(bossGroup, workerGroup);
			//设置socket工厂
			bootstrap.channel(NioServerSocketChannel.class);
			bootstrap.handler(new LoggingHandler(LogLevel.INFO));
			//设置管道工厂
			bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
				@Override
				protected void initChannel(SocketChannel socketChannel) throws Exception {
					//获取管道
					ChannelPipeline pipe = socketChannel.pipeline();
					
					// Add the text line codec combination first,
					pipe.addLast(new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
			        // the encoder and decoder are static as these are sharable
					//字符串解码器
					pipe.addLast(DECODER);
					//字符串编码器
					pipe.addLast(ENCODER);
					//业务处理类
					pipe.addLast(new IMServerHandle());
				}
			});
			//绑定端口
			// Bind and start to accept incoming connections.
			ChannelFuture f = bootstrap.bind(port).sync();

			if (f.isSuccess()) {
				Log.debug("server start success... port: " + port + ", main work thread: "
						+ Thread.currentThread().getId());
			}

			////等待服务端监听端口关闭
			// Wait until the server socket is closed.
			f.channel().closeFuture().sync();

		} finally {
			//优雅退出，释放线程池资源
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}

	public synchronized void stop() {
		bossGroup.shutdownGracefully();
		workerGroup.shutdownGracefully();
	}

	public static void main(String[] args) {
		NettyServer server = null;
		try {
			server = new NettyServer();
			if (args != null && args.length > 0 && args[0].length() > 3) {
				PORT = Integer.parseInt(args[0]);
			} else { 
				PORT = 10000;
			}

			// message work thread.
//			new Thread(new Runnable() {
//				@Override
//				public void run() {
//					MessageManager.getInstance().start();
//
//				}
//			}).start();

			server.bind(PORT);
		} catch (InterruptedException e) {
			 MessageManager.getInstance().stop();
			 UserManager.getInstance().clearAll();
			server.stop();
			e.printStackTrace();
		}
	}
}
