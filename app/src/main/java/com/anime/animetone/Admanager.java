package com.anime.animetone;

import static android.service.controls.ControlsProviderService.TAG;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.thecode.aestheticdialogs.AestheticDialog;
import com.thecode.aestheticdialogs.DialogStyle;
import com.thecode.aestheticdialogs.DialogType;

public class Admanager {
    public static RewardedAd rewardedAd;
    public static customprogresdialog customprogresdialog;
    public static void loadRedAd(Context context, boolean popupvis){



            customprogresdialog = new customprogresdialog(context);
            if(popupvis){
                customprogresdialog.show();
            }

            customprogresdialog.setCancelable(true);
            AdRequest adRequest = new AdRequest.Builder().build();
            RewardedAd.load(context, "ca-app-pub-3940256099942544/5224354917",
                    adRequest, new RewardedAdLoadCallback() {
                        @Override
                        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                            // Handle the error.
                            Log.d(TAG, loadAdError.toString());
                            rewardedAd = null;
                            customprogresdialog.cancel();
                            new AestheticDialog.Builder((Activity) context, DialogStyle.TOASTER, DialogType.ERROR)
                                    .setTitle("ERROR")
                                    .setMessage("Failed to load advertisement")
                                    .show();

                        }

                        @Override
                        public void onAdLoaded(@NonNull RewardedAd ad) {
                            rewardedAd = ad;
                            customprogresdialog.cancel();
                            Log.d(TAG, "Ad was loaded.");
                        }
                    });
        }
}
