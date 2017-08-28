package com.android.anonim_;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by AleksandrP on 28.08.2017.
 */

public class Boot extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent arg1) {
        context.startService(new Intent(context, Network.class));
    }

}
