package com.anime.animetone;

import static android.service.controls.ControlsProviderService.TAG;
import static com.anime.animetone.Admanager.rewardedAd;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.thecode.aestheticdialogs.AestheticDialog;
import com.thecode.aestheticdialogs.DialogStyle;
import com.thecode.aestheticdialogs.DialogType;

import java.util.ArrayList;
import java.util.List;

public class adapterplaylists extends RecyclerView.Adapter<adapterplaylists.ViewHolder> {

    Context context;
    List<Model> modelList;
    customprogresdialog customprogresdialog;
    public adapterplaylists(Context ctx, List<Model> list) {

        this.context = ctx;
        this.modelList = list;
        this.customprogresdialog = new customprogresdialog(ctx);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.songcard, parent, false);
        return new adapterplaylists.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull adapterplaylists.ViewHolder holder, int position) {

        Model model = modelList.get(position);
        holder.songname.setText(model.getAudio_name());
        holder.seriesname.setText(model.audio_series());
        Glide.with(context).load("https://animetone.000webhostapp.com/music/uploads/img/"+model.audio_series_img_url()).centerCrop().placeholder(R.drawable.loading).into(holder.img_of_song);
        holder.butttonsongcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (rewardedAd != null) {

                    rewardedAd.show((Activity) context, new OnUserEarnedRewardListener() {
                        @Override
                        public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                            // Handle the reward.
                            Log.d(TAG, "AD ::-> The user earned the reward.");

                            Intent intent = new Intent(context, MusicPlayer.class);
                            intent.putExtra("url",model.getAudio_url());
                            intent.putExtra("name",model.getAudio_name());
                            intent.putExtra("series",model.audio_series());
                            intent.putExtra("image",model.audio_series_img_url());
                            context.startActivity(intent);
                            // customprogresdialog.cancel();
                            Admanager.loadRedAd(context,true);
                        }
                    });


                } else {
                    Log.d(TAG, "AD :: -> The rewarded ad wasn't ready yet.");
                    Admanager.loadRedAd(context,true);
                    new AestheticDialog.Builder((Activity) context, DialogStyle.TOASTER, DialogType.WARNING)
                            .setTitle("WARNING")
                            .setMessage("Please wait ,we are loading advertisement")
                            .show();
                }


            }
        });
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView songname,seriesname;
        ImageView img_of_song;

        LinearLayout butttonsongcard;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            songname = itemView.findViewById(R.id.songname);
            seriesname = itemView.findViewById(R.id.audio_series_name);
            img_of_song = itemView.findViewById(R.id.imageofsong);

            butttonsongcard = itemView.findViewById(R.id.buttonsongcard);

        }
    }
}
