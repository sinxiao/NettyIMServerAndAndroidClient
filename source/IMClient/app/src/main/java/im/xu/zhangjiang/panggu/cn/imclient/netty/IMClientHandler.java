package im.xu.zhangjiang.panggu.cn.imclient.netty;

import com.alibaba.fastjson.JSON;
import com.xhj.imserver.biz.protocol.Response;
import com.xhj.imserver.utils.Utils;
import java.util.Vector;
import im.xu.zhangjiang.panggu.cn.imclient.MessageSender;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class IMClientHandler extends SimpleChannelInboundHandler<String> {
	private static Vector<MessageSender.MessgeReceivedListener> messgeReceivedListeners = new Vector<MessageSender.MessgeReceivedListener>();

	public void addMessgeReceivedListener(MessageSender.MessgeReceivedListener messgeReceivedListener) {
		Utils.showLog("IMClientHandler"," rev msg >>> addMessgeReceivedListener ");
		messgeReceivedListeners.add(messgeReceivedListener);

	}

	public void remove(MessageSender.MessgeReceivedListener messgeReceivedListener) {
		messgeReceivedListeners.remove(messgeReceivedListener);
	}
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
		// TODO Auto-generated method stub
		Utils.showLog("IMClientHandler"," rev msg >>> channelRead0 ");
		//System.out.println(" get the string msg >>> "+msg);
		Utils.showLog("IMClientHandler"," rev msg >>> "+msg);
		if(messgeReceivedListeners != null && messgeReceivedListeners.size()>0) {
			msg = msg.replaceAll("\r\n","");
			msg = msg.replaceAll(" ","");
			Response msge = JSON.parseObject(msg, Response.class);
			if(msge == null) {
				Utils.showLog("IMClientHandler"," rev msg >>> parseResponse is null ");
			} else {
				Utils.showLog("IMClientHandler"," rev msg >>> parseResponse is not null ");
			}
			int size = messgeReceivedListeners.size();
			for (int i = 0;i<size;i++) {
				messgeReceivedListeners.get(i).onMessageReceived(msge);
			}
		}
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		super.channelRead(ctx, msg);
		Utils.showLog("IMClientHandler"," rev msg >>> channelRead ");
	}

	/**
	 * Calls {@link ChannelHandlerContext#fireChannelActive()} to forward
	 *
	 * <p>
	 * Sub-classes may override this method to change behavior.
	 *
	 * @param ctx
	 */
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		super.channelActive(ctx);
		Utils.showLog("IMClientHandler"," rev msg >>> channelActive ");
	}

	/**
	 * Calls {@link ChannelHandlerContext#fireChannelInactive()} to forward
	 * to the next {@link } in the {@link }.
	 * <p>
	 * Sub-classes may override this method to change behavior.
	 *
	 * @param ctx
	 */
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		super.channelInactive(ctx);
		Utils.showLog("IMClientHandler"," rev msg >>> channelInactive ");
	}

	/**
	 * Calls {@link ChannelHandlerContext#fireChannelReadComplete()} to forward
	 * to the next {@link } in the {@link }.
	 * <p>
	 * Sub-classes may override this method to change behavior.
	 *
	 * @param ctx
	 */
	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		super.channelReadComplete(ctx);
		Utils.showLog("IMClientHandler"," rev msg >>> channelReadComplete ");
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}



}
