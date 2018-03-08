package com.xhj.imserver.imserver;

import java.net.InetAddress;
import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.xhj.imserver.bean.IMMessage;
import com.xhj.imserver.bean.User;
import com.xhj.imserver.biz.IMMessageBiz;
import com.xhj.imserver.biz.UserBiz;
import com.xhj.imserver.biz.protocol.IMProtocol;
import com.xhj.imserver.biz.protocol.Request;
import com.xhj.imserver.biz.protocol.Response;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;
import io.netty.channel.SimpleChannelInboundHandler;

/***
 * 面向IM通信操作的业务类
 * @author xhj
 *
 */
public class IMServerHandle extends SimpleChannelInboundHandler<String> {
	/**
	 * user操作业务类
	 */
	private UserBiz userBiz = new UserBiz();
	/***
	 * 消息操作的业务类
	 */
	private IMMessageBiz immessagebiz = new IMMessageBiz();
	
	/***
	 * 处理接受到的String类型的JSON数据
	 */
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
		System.out.println(" get msg >> "+msg);
		//把JSON数据进行反序列化
 		Request req = JSON.parseObject(msg, Request.class);
 		Response respon = new Response();
 		respon.setSendTime(System.currentTimeMillis());
 		//判断是否是合法的请求
 		if(req != null ) {
 			System.out.println("the req method >> "+req.getMethod());
 			//获取操作类型
 			if(req.getMethod() == IMProtocol.LOGIN) {
 				//获取要操作的对象
 				User user = JSON.parseObject(req.getBody(),User.class);
 				//设置返回数据的操作类型
 				respon.setMethod(IMProtocol.LOGIN);
 				//执行业务操作
 				boolean bl = userBiz.login(user);
 				if(bl) {//检验用户有效
 					//设置响应数据
 					respon.setBody("login ok");
 					//设置状态
 					respon.setStatus(0);
 					//登录成功将连接channel保存到Groups里
 					ChannelGroups.add(ctx.channel());
 					//将用户的uname和channelId进行绑定，服务器向指定用户发送消息的时候需要用到uname和channelId
 					ChannelGroups.putUser(user.getUname(), ctx.channel().id());
 					//发送广播通知某人登录成功了
 					userBiz.freshUserLoginStatus(user);
 				} else {//用户密码错误
 					//设置错误描述
 					respon.setErrorStr("pwd-error");
 					//设置状态描述码
 					respon.setStatus(-1);
 				}
 				//将Response序列化为json字符串
 				msg = JSON.toJSONString(respon);
 				//发送josn字符串数据，注意后面一定要加"\r\n"
 				ctx.writeAndFlush(msg+"\r\n");
 			} else if(req.getMethod() == IMProtocol.SEND) {
 				IMMessage immsg = JSON.parseObject(req.getBody(), IMMessage.class);
 				immsg.setSendTime(System.currentTimeMillis());
 				immsg.setReadTiem(-1);
 				immessagebiz.saveMsg(immsg);
 				respon.setStatus(0);
 				respon.setMethod(IMProtocol.SEND);
 				ctx.writeAndFlush(JSON.toJSONString(respon)+"\r\n");
 				if(ChannelGroups.getChannelId(immsg.getTo()) != null) {
 					immessagebiz.transformMessage(immsg);
 				}
 			} else if(req.getMethod() == IMProtocol.REV) {
 				List<IMMessage> msgs = immessagebiz.getUnReadMsg(req.getFrom());
 				Response resp = new Response();
 				resp.setMethod(IMProtocol.REV);
 				resp.setStatus(0);
 				if(msgs != null && msgs.size() > 0) {
 					resp.setBody(JSON.toJSONString(msgs));
 				} else {
 					resp.setBody("");
 				}
 				ctx.writeAndFlush(JSON.toJSONString(resp)+"\r\n");
 			} else if(req.getMethod() == IMProtocol.QUIT) {
 				ChannelId id = ctx.channel().id();
 				String uname = req.getFrom();
 				ChannelGroups.romveUser(uname);
 				ChannelGroups.discard(ctx.channel());	
 				ctx.close();
 				
 				User user = userBiz.findUserByName(uname);
 				userBiz.freshUserLoginoutStatus(user);
 				
 			} else if(req.getMethod() == IMProtocol.READIT) {
 				String[] ids = req.getBody().split(",");
 				immessagebiz.makeRead(ids);
 				respon.setMethod(IMProtocol.READIT);
 				respon.setStatus(0);
 				ctx.writeAndFlush(JSON.toJSONString(respon)+"\r\n");
 				ChannelGroups.romveUser(ctx.channel().id());
 				ChannelGroups.discard(ctx.channel());
 				
 			} else if(req.getMethod() == IMProtocol.REG) {
 				
 				User user = JSON.parseObject(req.getBody(), User.class);
 				boolean bl = userBiz.exitsUserName(user.getUname());
 				respon.setMethod(IMProtocol.REG);
 				if(!bl) {
 					userBiz.reg(user);
 					User nuser = userBiz.findUserByName(user.getUname());
 					nuser.setPwd("");
 					respon.setBody(JSON.toJSONString(nuser));
 					respon.setStatus(0);
 				} else {
 					respon.setStatus(-2);
 					respon.setErrorCode(-2);
 					respon.setErrorStr("uname-exits");
 				}
 				ctx.writeAndFlush(JSON.toJSONString(respon)+"\r\n");
 			}
 		}
		//ctx.flush();
	}
	
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		ctx.write("Welcome to "+InetAddress.getLocalHost().getHostName()+" \r\n ");
		ctx.write("It is "+new Date()+" now \r\n");
		ctx.flush();
	}
	
	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.flush();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}
	
}
