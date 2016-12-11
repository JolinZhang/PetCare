package com.github.jolinzhang.petcare;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
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

    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //hide status bar and action bar
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash);
        context = this;

        //ask for permission
        isStoragePermissionGranted();

        //if permission granted, connect to main activity
        if(isStoragePermissionGranted() == true){
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

    public boolean isStoragePermissionGranted(){
        //check version >23
        if (Build.VERSION.SDK_INT >= 23){
            if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED
                    && checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
               //permission granted do nothing
                return true;
            }else{
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_FINE_LOCATION},1);
                return false;
            }
        }else{
            //permission is automatically granted on sdk<23 upon installation
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 1:
                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Handler mainHandler = new Handler(context.getMainLooper());
                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {

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
                    };
                    mainHandler.post(runnable);
                }else{

                }
                break;

        }
        return;
    }
}
