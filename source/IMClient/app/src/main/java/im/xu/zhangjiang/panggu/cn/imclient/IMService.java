package im.xu.zhangjiang.panggu.cn.imclient;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by Deon Xu on 2018/2/28 15:40
 * e-mail：xuhuajie2008@gmail.com
 */
public class IMService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        MessageSender.getInstance().start();
    }

}
