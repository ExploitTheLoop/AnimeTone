package com.anime.animetone;

import static android.service.controls.ControlsProviderService.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.snackbar.Snackbar;
import com.thecode.aestheticdialogs.AestheticDialog;
import com.thecode.aestheticdialogs.DialogAnimation;
import com.thecode.aestheticdialogs.DialogStyle;
import com.thecode.aestheticdialogs.DialogType;
import com.thecode.aestheticdialogs.OnDialogClickListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    public ArrayList<Model> list;
    private ArrayList<Model> searchlist;
    private List<albumModel> albumlist;
    private Adapter adapter;
    private Adapterforallbum albumadapter;

    private RecyclerView recyclerView1,recyclerView2;
    TextView songstxtxheader, albumtxtheader;

    SearchView searchView;

    customprogresdialog customprogresdialog;
    AdView mAdView;

    ImageView discoserver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       // getSupportActionBar().hide();
        getWindow().setStatusBarColor(ContextCompat.getColor(MainActivity.this,R.color.statusbarcolor));
        getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.transparentblue));
       // getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);


      //  Admanager.loadRedAd(this,true);

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        recyclerView1 = findViewById(R.id.recycle1);
        recyclerView2 = findViewById(R.id.recycle2);
        songstxtxheader =findViewById(R.id.songstxtxheader);
        albumtxtheader = findViewById(R.id.albumtxtheader);
        searchView = findViewById(R.id.searchview);
        discoserver = findViewById(R.id.discoserver);
        Glide.with(this).load(R.drawable.discord)
                .into(discoserver);

        searchView.setIconified(false);
        searchView.clearFocus();

        list = new ArrayList<>();
        albumlist = new ArrayList<>();
        adapter = new Adapter(this, list);
        albumadapter = new Adapterforallbum(this,albumlist);

        recyclerView1.setHasFixedSize(true);
        recyclerView1.setAdapter(albumadapter);
        recyclerView1.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
      //  recyclerView1.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL));


        recyclerView2.setHasFixedSize(true);
        recyclerView2.setAdapter(adapter);
        recyclerView2.setLayoutManager(new LinearLayoutManager(this));
       // recyclerView2.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        volleyProcess();


       discoserver.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

               Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://discord.gg/6JJpq3wZ2Q"));
               startActivity(browserIntent);
           }
       });



        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                searchlist = new ArrayList<>();
                if(query.length() > 0){

                    for(int i = 0; i < list.size();i++){
                        if(list.get(i).getAudio_name().toUpperCase().contains(query.toUpperCase()) || list.get(i).audio_series().toUpperCase().contains(query.toUpperCase())){
                            searchlist.add(new Model(list.get(i).getId(),
                                    list.get(i).getAudio_name(),
                                    list.get(i).getAudio_url(),
                                    list.get(i).audio_category(),
                                    list.get(i).audio_series(),
                                    list.get(i).audio_series_img_url()));
                        }

                    }


                    adapter = new Adapter(MainActivity.this, searchlist);
                    recyclerView2.setAdapter(adapter);
                }else{
                    adapter = new Adapter(MainActivity.this, list);
                    recyclerView2.setAdapter(adapter);
                }
                return false;
            }
        });

    }



    public void volleyProcess(){

        customprogresdialog = new customprogresdialog(MainActivity.this);
        customprogresdialog.show();

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        //then receive data from php file, then response can be success or failure

        final StringRequest request = new StringRequest("https://animetone.000webhostapp.com/music/app/index.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //      dialog.dismiss();

//make sure database exceptional are handled

                try {
                    JSONArray array = new JSONArray(response);

                    //now loop your incoming data

                    list.clear();
                    albumlist.clear();

                    for (int loop = array.length() - 1; loop >= 0; loop--) {
                        JSONObject object = array.getJSONObject(loop);


                            list.add(new Model(object.getInt("id"),
                                    object.getString("audio_name"),
                                    object.getString("audio_url"),
                                    object.getString("audio_category"),
                                    object.getString("audio_series"),
                                    object.getString("audio_series_img")

                            ));

                            if(!iscontain(object.getString("audio_series"))){
                                albumlist.add(new albumModel(object.getString("audio_series"),
                                                             object.getString("audio_series_img")));
                            }

                    }



                    MySingleton<Model> modelSingleton = MySingleton.getInstance();
                    modelSingleton.clearDataList();
                    modelSingleton.addDataList(list);

                    songstxtxheader.setText("RECENT UPDATES");
                    albumtxtheader.setText("RECENT ALBUMS");
                    adapter.notifyDataSetChanged();
                    albumadapter.notifyDataSetChanged();

                    customprogresdialog.cancel();


                }

//whatever Error happens, then throw here in catch block

                catch (Exception e) {
                    customprogresdialog.cancel();
                    new AestheticDialog.Builder(MainActivity.this, DialogStyle.FLAT, DialogType.ERROR)
                            .setTitle("Technical Issue")
                            .setMessage(e.getMessage())
                            .setCancelable(false)
                            .setDarkMode(true)
                            .setGravity(Gravity.CENTER)
                            .setAnimation(DialogAnimation.SHRINK)
                            .setOnClickListener(new OnDialogClickListener() {
                                @Override
                                public void onClick(@NonNull AestheticDialog.Builder builder) {
                                    finishAffinity();
                                }
                            }).show();
                }
                refreshAllContent(3000);
            }

            //if response is failure give user the message

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                customprogresdialog.cancel();
                new AestheticDialog.Builder(MainActivity.this, DialogStyle.FLAT, DialogType.ERROR)
                        .setTitle("Server connection issue")
                        .setMessage("Failed to connect to server")
                        .setCancelable(false)
                        .setDarkMode(true)
                        .setGravity(Gravity.CENTER)
                        .setAnimation(DialogAnimation.SHRINK)
                        .setOnClickListener(new OnDialogClickListener() {
                            @Override
                            public void onClick(@NonNull AestheticDialog.Builder builder) {
                                finishAffinity();
                            }
                        }).show();

            }
        });
        requestQueue.add(request);
    }

    //finally, put error message


    public boolean iscontain(String s){
        for(int i = 0; i < albumlist.size();i++) {
            if (albumlist.get(i).getAlbumname().toUpperCase().contains(s.toUpperCase())) {
                return true;
            }
        }

        return false;
    }

    public  void refreshAllContent(final long timetoupdate) {
        new CountDownTimer(timetoupdate, 1000) {
            public void onTick(long millisUntilFinished) {
            }
            public void onFinish() {
                Log.i("SCROLLS ", "UPDATE CONTENT HERE ");
               // volleyProcess();
            }
        }.start();
    }

    @Override
    public void onBackPressed() {
        // Disable back button press by leaving this method empty
        // If you want to perform some action on back press, you can add your logic here
       // super.onBackPressed();
        new AestheticDialog.Builder(MainActivity.this, DialogStyle.FLAT, DialogType.WARNING)
                .setTitle("Warning")
                .setMessage("Are you sure, You want to exit?")
                .setCancelable(true)
                .setDarkMode(true)
                .setGravity(Gravity.CENTER)
                .setAnimation(DialogAnimation.SHRINK)
                .setOnClickListener(new OnDialogClickListener() {
                    @Override
                    public void onClick(@NonNull AestheticDialog.Builder builder) {
                        finishAffinity();
                    }
                }).show();
    }
}