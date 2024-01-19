package com.anime.animetone;

import static android.service.controls.ControlsProviderService.TAG;
import static com.anime.animetone.Admanager.rewardedAd;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.thecode.aestheticdialogs.AestheticDialog;
import com.thecode.aestheticdialogs.DialogStyle;
import com.thecode.aestheticdialogs.DialogType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Adapterforallbum extends RecyclerView.Adapter<Adapterforallbum.Viewholder>{

    Context context;
    List<albumModel> modelList;

    customprogresdialog customprogresdialog;
    public Adapterforallbum(Context ctx, List<albumModel> list) {
        this.context = ctx;
        this.modelList =list;
        this.customprogresdialog = new customprogresdialog(ctx);
    }

    @NonNull
    @Override
    public Adapterforallbum.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.albumcard, parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapterforallbum.Viewholder holder, int position) {

        albumModel model = modelList.get(position);
        holder.series.setText(model.getAlbumname());
        Glide.with(context).load("https://animetone.000webhostapp.com/music/uploads/img/"+model.getAlbumimg()).centerCrop().placeholder(R.drawable.loading).into(holder.imageofsong);
        holder.albumbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (rewardedAd != null) {

                    rewardedAd.show((Activity) context, new OnUserEarnedRewardListener() {
                        @Override
                        public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                            // Handle the reward.
                            Log.d(TAG, "AD ::-> The user earned the reward.");
                            Intent intent = new Intent(context, playlists.class);
                            intent.putExtra("key",model.getAlbumname());
                            intent.putExtra("image",model.getAlbumimg());
                            context.startActivity(intent);
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


               // customprogresdialog.cancel();

            }
        });
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        TextView series;
        ImageView imageofsong;
        LinearLayout albumbutton;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            series=itemView.findViewById(R.id.albumsongname);
            imageofsong=itemView.findViewById(R.id.imagesongalbum);
            albumbutton=itemView.findViewById(R.id.albumbutton);
        }
    }
}
