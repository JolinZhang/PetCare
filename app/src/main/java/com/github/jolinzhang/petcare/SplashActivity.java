package com.github.jolinzhang.petcare;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.github.jolinzhang.model.DataRepository;

import io.realm.ObjectServerError;
import io.realm.SyncUser;

/**
 * Created by Jonelezhang on 11/26/16.
 */

public class SplashActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //hide status bar and action bar
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash);

        final Context context = this;
        DataRepository.getInstance().login(new SyncUser.Callback() {
            @Override
            public void onSuccess(SyncUser user) {
                //start MainActivity
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                //close this activity
                finish();
            }

            @Override
            public void onError(ObjectServerError error) {
                Toast.makeText(context, "Failed to connect to database server.", Toast.LENGTH_LONG).show();
            }
        });
    }
}
