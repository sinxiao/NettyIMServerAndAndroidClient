package im.xu.zhangjiang.panggu.cn.imclient.biz;

import com.alibaba.fastjson.JSON;
import com.xhj.imserver.bean.IMMessage;
import com.xhj.imserver.bean.User;
import com.xhj.imserver.biz.protocol.IMProtocol;
import com.xhj.imserver.biz.protocol.Request;

import im.xu.zhangjiang.panggu.cn.imclient.MessageSender;

/**
 * Created by Deon Xu on 2018/2/28 17:19
 * e-mail：xuhuajie2008@gmail.com
 */

public class IMBiz {

    public static void sendMsg(IMMessage imMessage) {
        Request request = new Request();
        request.setFrom(imMessage.getFrom());
        request.setSendTime(System.currentTimeMillis());
        request.setMethod(IMProtocol.SEND);
        request.setBody(JSON.toJSONString(imMessage));
        MessageSender.getInstance().putRequest(request);
    }

    public static void login(User user) {
        Request request = new Request();
        request.setFrom(user.getUname());
        request.setSendTime(System.currentTimeMillis());
        request.setMethod(IMProtocol.LOGIN);
        request.setBody(JSON.toJSONString(user));
        MessageSender.getInstance().putRequest(request);
    }

    public static void getUnReadMsg(String uname) {
        Request request = new Request();
        request.setFrom(uname);
        request.setSendTime(System.currentTimeMillis());
        request.setMethod(IMProtocol.REV);
        MessageSender.getInstance().putRequest(request);
    }

    public static void reg(User user) {
        Request request = new Request();
        request.setFrom(user.getUname());
        request.setSendTime(System.currentTimeMillis());
        request.setMethod(IMProtocol.REG);
        request.setBody(JSON.toJSONString(user));
        MessageSender.getInstance().putRequest(request);
    }
}
