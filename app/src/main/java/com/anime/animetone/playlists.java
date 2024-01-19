package com.anime.animetone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.thecode.aestheticdialogs.AestheticDialog;
import com.thecode.aestheticdialogs.DialogAnimation;
import com.thecode.aestheticdialogs.DialogStyle;
import com.thecode.aestheticdialogs.DialogType;
import com.thecode.aestheticdialogs.OnDialogClickListener;

import java.util.ArrayList;
import java.util.List;

public class playlists extends AppCompatActivity {
    TextView albumname;

    String texttoshow = "";

    RecyclerView recyclerView;
    private adapterplaylists adapterplaylists;
    List<Model> songslist;
    ImageView img;
    LinearLayout backbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlists);

        getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.statusbarcolor));
        getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.transparentblue));

      //  Admanager.loadRedAd(this,true);

        albumname = findViewById(R.id.currentalbumname);
        recyclerView = findViewById(R.id.recycleviewplaylists);
        img = findViewById(R.id.imgplaylists);
        backbutton = findViewById(R.id.backbuttonolaylists);

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
               // startActivity(new Intent(playlists.this,MainActivity.class));
            }
        });

        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);



        MySingleton<Model> modelSingleton = MySingleton.getInstance();
        List<Model> list = modelSingleton.getDataList();
        String message = getIntent().getStringExtra("key");
        String imagefile = getIntent().getStringExtra("image");

        Glide.with(this).load("https://animetone.000webhostapp.com/music/uploads/img/"+imagefile)
                .centerCrop()
                .placeholder(R.drawable.loading)
                .into(img);


        albumname.setText(message.toUpperCase());


        songslist = new ArrayList<>();
        adapterplaylists = new adapterplaylists(this,songslist);

        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapterplaylists);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
       // recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        songslist.clear();
        for(int i = 0; i < list.size();i++){
            if(list.get(i).audio_series().toUpperCase().contains(message.toUpperCase())){

             songslist.add(new Model(list.get(i).getId(),
                                     list.get(i).getAudio_name(),
                                     list.get(i).getAudio_url(),
                                     list.get(i).audio_category(),
                                     list.get(i).audio_series(),
                                     list.get(i).audio_series_img_url()));

            }

        }
        adapterplaylists.notifyDataSetChanged();



    }


    @Override
    public void onBackPressed() {
        // Disable back button press by leaving this method empty
        // If you want to perform some action on back press, you can add your logic here
        super.onBackPressed();


    //   new AestheticDialog.Builder(playlists.this, DialogStyle.FLAT, DialogType.WARNING)
    //           .setTitle("Warning")
    //           .setMessage("Are you sure, You want to exit?")
    //           .setCancelable(true)
    //           .setDarkMode(true)
    //           .setGravity(Gravity.CENTER)
    //           .setAnimation(DialogAnimation.SHRINK)
    //           .setOnClickListener(new OnDialogClickListener() {
    //               @Override
    //               public void onClick(@NonNull AestheticDialog.Builder builder) {
    //                   finishAffinity();
    //               }
    //           }).show();

    }
}