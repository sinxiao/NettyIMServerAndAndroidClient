package im.xu.zhangjiang.panggu.cn.imclient;

import com.xhj.imserver.biz.protocol.Request;
import com.xhj.imserver.biz.protocol.Response;

import im.xu.zhangjiang.panggu.cn.imclient.netty.NettyClient;

/**
 * Created by Deon Xu on 2018/2/28 15:55
 * e-mail：xuhuajie2008@gmail.com
 */

public class MessageSender {

    public static interface MessgeReceivedListener {
        public void onMessageReceived(Response msg);
        public void onMessageDisconnect();
        public void onMessageConnect();
    }

    private MessgeReceivedListener messgeReceivedListener;

//    public void setMessageReceivedListener( MessgeReceivedListener messgeReceivedListener) {
//        this.messgeReceivedListener = messgeReceivedListener;
//    }

    private NettyClient client;
    private volatile static MessageSender INSTANCE = null ;
    private MessageSender() {

    }
    public static MessageSender getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new MessageSender();
        }

        return INSTANCE;
    }

    public void start() {
        INSTANCE.messgeReceivedListener = messgeReceivedListener;
        client =new NettyClient();
        //client.setMessgeReceivedListener(messgeReceivedListener);
        new Thread(client).start();
    }

    public void addMessgeReceivedListener(MessgeReceivedListener messgeReceivedListener) {
        client.addMessgeReceivedListener(messgeReceivedListener);
    }

    public void removeMessageReceivedListener(MessgeReceivedListener messgeReceivedListener) {
        client.remove(messgeReceivedListener);
    }

    public void putRequest(Request request) {
        client.addRequest(request);
    }
}
