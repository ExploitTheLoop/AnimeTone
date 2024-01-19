package com.anime.animetone;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;

public class SplashScreen extends AppCompatActivity {

    ImageView img;
    private static final int SPLASH_SCREEN_TIME_OUT = 5000;
    private final int MY_REQ_CODE=100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.statusbarcolor));
        getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.transparentblue));
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);

        img = findViewById(R.id.splashgif);
        Glide.with(this).load(R.drawable.loadinggifsplash)
                .into(img);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        checkforupdate();

        //Admanager.loadRedAd(this);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Intent is used to switch from one activity to another.
                Intent i = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(i); // invoke the SecondActivity.
                finish(); // the current activity will get finished.
            }
        }, SPLASH_SCREEN_TIME_OUT);
    }

         private void checkforupdate(){
             AppUpdateManager appUpdateManager = AppUpdateManagerFactory.create(this);

             // Returns an intent object that you use to check for an update.
             Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();

             // Checks that the platform will allow the specified type of update.
             appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
                 if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                         // This example applies an immediate update. To apply a flexible update
                         // instead, pass in AppUpdateType.FLEXIBLE
                         && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                     // Request the update.

                     try {
                         appUpdateManager.startUpdateFlowForResult(
                                 appUpdateInfo,
                                 AppUpdateType.IMMEDIATE,
                                 SplashScreen.this,
                                 MY_REQ_CODE);
                     } catch (IntentSender.SendIntentException e) {
                         throw new RuntimeException(e);
                     }
                 }
             });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode,Intent data) {
        // handle callback
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MY_REQ_CODE) {
            if (resultCode != RESULT_OK) {

                Log.w("UPDATE","Update failed :"+resultCode);
            }

        }
    }
}