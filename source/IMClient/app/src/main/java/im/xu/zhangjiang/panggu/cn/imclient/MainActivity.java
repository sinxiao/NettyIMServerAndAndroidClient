package im.xu.zhangjiang.panggu.cn.imclient;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.alibaba.fastjson.JSON;
import com.xhj.imserver.bean.IMMessage;
import com.xhj.imserver.bean.User;
import com.xhj.imserver.biz.protocol.IMProtocol;
import com.xhj.imserver.biz.protocol.Response;
import com.xhj.imserver.utils.Utils;

import java.util.List;

import im.xu.zhangjiang.panggu.cn.imclient.biz.IMBiz;

public class MainActivity extends AppCompatActivity {
    private Button btnLogin;
    private Button btnSend;
    private Button btnRev;
    private Button btnReg ;
    private MessageSender.MessgeReceivedListener messgeReceivedListener;
    private EditText edtTip;
    public static final int APPEND_TEXT = 0;

    private Handler handler = new Handler(){
        public void handleMessage(Message msg) {
            int what = msg.what;
            if(what == APPEND_TEXT) {
                edtTip.append(((String)msg.obj)+"\r\n");
            }
        }
    };
    private void sendTextToEdit(String info) {
        Message msg = handler.obtainMessage(APPEND_TEXT);
        msg.obj = info;
        handler.sendMessage(msg);
    }
    private void regMsgRev() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                try{
                    MessageSender.getInstance().addMessgeReceivedListener(messgeReceivedListener);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    regMsgRev();
                }
            }
        },3000);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startService(new Intent(this,IMService.class));
        messgeReceivedListener = new MessageSender.MessgeReceivedListener() {
            public void onMessageReceived(Response msg) {
                sendTextToEdit("收到消息："+ JSON.toJSONString(msg));
                if(msg.getMethod()== IMProtocol.REG) {
                    String tip= "注册成功";
                    if(msg.getStatus() == 0) {
                        String body = msg.getBody();
                        Utils.showLog("MainActivity","body >>> "+body);
                    } else {
                        tip= "注册失败";
                        if(msg.getStatus() == -2) {
                            tip+=",名字已经使用了。";
                        }
                    }
                    sendTextToEdit(tip);
                } else if(msg.getMethod()== IMProtocol.LOGIN) {
                    String tip= "登录成功";
                    if(msg.getStatus() == 0) {
                        String body = msg.getBody();
                        Utils.showLog("MainActivity","body >>> "+body);
                        sendTextToEdit(tip);
                    } else {
                        tip= "登录失败";
                        if(msg.getStatus() == -2) {
                            //tip+=",名字已经使用了。";
                        }
                        sendTextToEdit(tip);
                    }
                } else if(msg.getMethod()== IMProtocol.SEND) {
                    String tip = "发送成功";
                    if(msg.getStatus() == 0) {
                        String body = msg.getBody();
                        Utils.showLog("MainActivity","body >>> "+body);
                    } else {
                        tip= "发送失败";
                        if(msg.getStatus() == -2) {
                            //tip+=",名字已经使用了。";
                        }
                    }
                    sendTextToEdit(tip);
                }else if(msg.getMethod() == IMProtocol.REV) {
                    String tip = "接受消息成功";
                    if(msg.getStatus() == 0) {
                        String body = msg.getBody();
                        List<IMMessage> msgs = JSON.parseArray(body,IMMessage.class);
                        if(msgs != null) {
                            tip+=" 收到的message大小为："+msgs.size();
                        } else {
                            tip+=" 收到的message大小为 空";
                        }

                        Utils.showLog("MainActivity","body >>> "+body);
                    } else {
                        tip= "接受消息失败";
                        if(msg.getStatus() == -2) {
                            //tip+=",名字已经使用了。";
                        }
                    }
                    sendTextToEdit(tip);
                }
            }

            @Override
            public void onMessageDisconnect() {

            }

            @Override
            public void onMessageConnect() {

            }
        };

        regMsgRev();
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnSend = (Button) findViewById(R.id.btnSend);
        btnRev = (Button) findViewById(R.id.btnRev);
        btnReg = (Button) findViewById(R.id.btnReg);
        edtTip = (EditText) findViewById(R.id.edttip);
        btnReg.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                User user = new User();

                user.setUname("aaa");
                user.setPwd("bbcc");
                user.setNickname("aaa");
                IMBiz.reg(user);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User user = new User();

                user.setUname("aaa");
                user.setPwd("bbcc");
                user.setNickname("aaa");
                IMBiz.login(user);
            }
        });
        btnSend.setOnClickListener(new View.OnClickListener(){

            public void onClick(View view) {
                IMMessage message = new IMMessage();
                message.setBody("hello world");
                message.setFrom("aaa");
                message.setTo("bbb");
                message.setBody("where are you ?");
                IMBiz.sendMsg(message);
            }
        });

        btnRev.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                IMBiz.getUnReadMsg("bbb");
            }
        });

    }
}
