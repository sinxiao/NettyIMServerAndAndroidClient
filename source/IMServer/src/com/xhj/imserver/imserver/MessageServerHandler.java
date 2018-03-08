package com.xhj.imserver.imserver;

import com.xhj.imserver.utils.Log;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.SocketChannel;

public class MessageServerHandler extends ChannelHandlerAdapter {
	public static final int PING = 0 ;
	public static final int LOGIN = 1 ;
	public static final int MESSAGE = 2;
	public static final int LOGOUT = 3;
	
	public void channelInactive(ChannelHandlerContext cxt) throws Exception {
		Log.debug("channelInactive()...");
		UserManager.getInstance().removeChannel((SocketChannel) cxt.channel());
	}

	
	public void channelRead(ChannelHandlerContext cxt, Object obj) throws Exception {
		Log.debug("channelRead()...  threadId: " + Thread.currentThread().getId());
//		MessageProto.Message message = (MessageProto.Message) obj;
//		switch (message.getMsgType()) {
//		case PING:
//			Log.debug("received ping..." + message.getClientID());
//			cxt.channel().writeAndFlush(createResponseMsg(message, MessageType.PING, null));
//			break;
//		case LOGIN:
//			UserManager.getInstance().addUser(message.getGroupID(), message.getClientID(),
//					(SocketChannel) cxt.channel());
//			int count = UserManager.getInstance().getGroupSize(message.getGroupID());
//			Log.debug("received login..." + message.getClientID() + ", count: " + count);
//			cxt.channel().writeAndFlush(createResponseMsg(message, MessageType.LOGIN, "userCount:" + count));
//			Log.debug("received login sended..." + message.getClientID());
//			MessageManager.getInstance().putMessage((createResponseMsg(message, MessageType.MESSAGE, "大家好! 我来了...")));
//			Log.debug("received login sended 222..." + message.getClientID());
//			cxt.channel()
//					.writeAndFlush(createResponseMsg(
//							MessageProto.Message.newBuilder().setClientID("管理员").setGroupID(message.getGroupID())
//									.setMsgType(MessageType.MESSAGE).build(),
//							MessageType.MESSAGE, "欢迎 " + message.getClientID() + " 加入本群..."));
//			break;
//		case MESSAGE:
//			Log.debug("received message..." + message.getClientID() + "， " + message.getBody());
//			MessageManager.getInstance().putMessage(createResponseMsg(message, MessageType.MESSAGE, null));
//			break;
//		case LOGOUT:
//			UserManager.getInstance().removeUser(message.getGroupID(), message.getClientID());
//			Log.debug("received logout..." + message.getClientID() + ", count: "
//					+ UserManager.getInstance().getGroupSize(message.getGroupID()));
//			MessageManager.getInstance().putMessage(createResponseMsg(message, MessageType.MESSAGE, "大家聊! 我走了...."));
//			break;
//		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext cxt, Throwable cause) {
		Log.debug("exceptionCaught()...");
		UserManager.getInstance().removeChannel((SocketChannel) cxt.channel());
		cause.printStackTrace();
		cxt.close();
	}

//	private Message createResponseMsg(Message receivedMsg, MessageType type, String body) {
//		MessageProto.Message.Builder builder = MessageProto.Message.newBuilder();
//		builder.setClientID(receivedMsg.getClientID());
//		builder.setMsgType(type);
//		builder.setGroupID(receivedMsg.getGroupID());
//		if (body != null) {
//			builder.setBody(body);
//		} else {
//			builder.setBody(receivedMsg.getBody());
//		}
//		return builder.build();
//	}
}
