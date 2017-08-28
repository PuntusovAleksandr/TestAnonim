package com.android.check_anonymous;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by AleksandrP on 28.08.2017.
 */

public class Network extends Service {

    private Handler mHandler;

    @Override
    public void onCreate() {
        super.onCreate();

        MyTimerTask myTask = new MyTimerTask();
        Timer myTimer = new Timer();
        myTimer.schedule(myTask, 10000, 10000);

    }

    class MyTimerTask extends TimerTask {
        public void run() {
            mHandler = new Handler(Looper.getMainLooper()) {
                @Override
                public void handleMessage(Message message) {
                    // This is where you do your work in the UI thread.
                    // Your worker tells you in the message what to do.
                    Toast.makeText(Network.this, (String) message.obj, Toast.LENGTH_SHORT).show();
                }
            };

            String result = "";
            result = GET("http://site.ru/?q=");
// Обработка result
            Message message = mHandler.obtainMessage(1, result);
            message.sendToTarget();

            Log.d("MESS_0", result);

        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return Service.START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("");
    }


    public String GET(String url) {
//        InputStream inputStream = null;
//        String result = "";
//        try {
//            HttpClient httpclient = new DefaultHttpClient();
//            HttpResponse httpResponse = httpclient.execute(new HttpGet(url));
//            inputStream = httpResponse.getEntity().getContent();
//            if(inputStream != null)
//                result = convertInputStreamToString(inputStream);
//            else
//                result = "";
//        } catch (Exception e) {
//        }
//        return result;
        return new Date().getTime() + "";
    }

}
