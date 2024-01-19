package com.anime.animetone;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.anime.animetone.Helper.DownloadHelper;
import com.anime.animetone.Helper.RingtoneHelper;
import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.thecode.aestheticdialogs.AestheticDialog;
import com.thecode.aestheticdialogs.DialogAnimation;
import com.thecode.aestheticdialogs.DialogStyle;
import com.thecode.aestheticdialogs.DialogType;
import com.thecode.aestheticdialogs.OnDialogClickListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MusicPlayer extends AppCompatActivity {

    private MediaPlayer mediaPlayer;
    private ImageView playPauseButton;
    private ImageView forwardButton;
    private ImageView backwardButton;
    private SeekBar seekBar;
    private TextView durationTextView;
    private Handler handler = new Handler();

    TextView songname;

    TextView songnameview;
    LinearLayout backbutton;

    ImageView imgpl,setringtonebt,setNotificationbt;

    String url ="https://animetone.000webhostapp.com/music/uploads/";



    private static final int REQUEST_WRITE_EXTERNAL_STORAGE = 1;
    private static final int REQUEST_WRITE_SETTINGS = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);
        getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.statusbarcolor));
        getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.transparentblue));

        playPauseButton = findViewById(R.id.playPauseButton);
        forwardButton = findViewById(R.id.forwardButton);
        backwardButton = findViewById(R.id.backwardButton);
        seekBar = findViewById(R.id.seekbar);
        durationTextView = findViewById(R.id.durationTextView);
        songnameview = findViewById(R.id.songnamemusic);
        backbutton = findViewById(R.id.backbuttonmusicplayer);
        imgpl = findViewById(R.id.imgmusicpl);
        setringtonebt = findViewById(R.id.btnSetRingtone);
        setNotificationbt =  findViewById(R.id.btnSetNotification);

        String filename = getIntent().getStringExtra("url");
        String songname = getIntent().getStringExtra("name");
        String series = getIntent().getStringExtra("series");
        String imagefile = getIntent().getStringExtra("image");


        //Admanager.loadRedAd(this,true);

        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        Glide.with(this).load("https://animetone.000webhostapp.com/music/uploads/img/"+imagefile)
                .centerCrop()
                .placeholder(R.drawable.loading)
                .into(imgpl);

        songnameview.setText(songname);

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        try {
            mediaPlayer.setDataSource(url+filename);
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }


      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
          // Check if the permission is already granted
          if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                  != PackageManager.PERMISSION_GRANTED) {
              // Permission not granted, request it
              requestWriteExternalStoragePermission();
          } else {
              // Permission already granted, proceed with your logic
          }
      } else {
          // For versions below Android 6.0, permission is granted at installation time
      }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Check if the permission is already granted
            if (!Settings.System.canWrite(this)) {
                // Permission not granted, request it
                requestWriteSettingsPermission();
            } else {
                // Permission already granted, proceed with your logic
            }
        } else {
            // For versions below Android 6.0, permission is granted at installation time
        }


        playPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mediaPlayer.isPlaying()) {
                    pauseMediaPlayer();
                } else {
                    startMediaPlayer();
                }
            }
        });

        forwardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                forwardMediaPlayer(5000); // Forward by 5 seconds (adjust as needed)
            }
        });

        backwardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backwardMediaPlayer(5000); // Backward by 5 seconds (adjust as needed)
            }
        });


        // Setup the seekbar
        seekBar.setMax(mediaPlayer.getDuration());
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {


                seekBar.setProgress(0);
                seekBar.setEnabled(true);
                playPauseButton.setImageResource(R.drawable.baseline_play_arrow_24);

            }
        });

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
               // startActivity(new Intent(MusicPlayer.this,MainActivity.class));
               // finish();
            }
        });

        setringtonebt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_RINGTONES +"/AnimeTone/");
                File file = new File(directory, filename);
                if(!directory.exists()){
                    directory.mkdir();
                    directory.setReadable(true);
                    directory.setWritable(true);
                }
                new DownloadHelper(MusicPlayer.this,filename,file,1).execute(url+filename);
            }

        });

        setNotificationbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_NOTIFICATIONS +"/AnimeTone/");
                File file = new File(directory, filename);
                if(!directory.exists()){
                    directory.mkdir();
                    directory.setReadable(true);
                    directory.setWritable(true);
                }
                new DownloadHelper(MusicPlayer.this, filename, file,2).execute(url+filename);
            }
        });


        updateSeekBar();

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestWriteSettingsPermission() {
        // Request the permission
        Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
        intent.setData(Uri.parse("package:" + getPackageName()));
        startActivityForResult(intent, REQUEST_WRITE_SETTINGS);
    }

    // Override onActivityResult to handle the result of the permission request
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_WRITE_SETTINGS) {
            // Check if the permission is granted
            if (Settings.System.canWrite(this)) {
                // Permission granted, proceed with your logic
            } else {
                new AestheticDialog.Builder(this, DialogStyle.TOASTER, DialogType.WARNING)
                        .setTitle("Write Permission Denied")
                        .setMessage("Application lacks necessary permissions.")
                        .show();
                // Permission denied, handle accordingly (e.g., show a message, disable functionality)
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestWriteExternalStoragePermission() {
        // Request the permission
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                REQUEST_WRITE_EXTERNAL_STORAGE);
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_WRITE_EXTERNAL_STORAGE) {
            // Check if the permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed with your logic
            } else {

                new AestheticDialog.Builder(this, DialogStyle.TOASTER, DialogType.WARNING)
                        .setTitle("Storage Permission Denied")
                        .setMessage("Application lacks necessary permissions.")
                        .show();
                // Permission denied, handle accordingly (e.g., show a message, disable functionality)
            }
        }
    }



    private void startMediaPlayer() {
        mediaPlayer.start();
        //isPlaying = true;
        playPauseButton.setImageResource(R.drawable.baseline_pause_24);
        updateSeekBar();
    }

    private void pauseMediaPlayer() {
        mediaPlayer.pause();
       // isPlaying = false;
        playPauseButton.setImageResource(R.drawable.baseline_play_arrow_24);
        updateSeekBar();
    }

    private void forwardMediaPlayer(int milliseconds) {
        int currentPosition = mediaPlayer.getCurrentPosition();
        int duration = mediaPlayer.getDuration();
        if (currentPosition + milliseconds < duration) {
            mediaPlayer.seekTo(currentPosition + milliseconds);
            seekBar.setProgress(currentPosition + milliseconds);
        } else {
            mediaPlayer.seekTo(duration);
            seekBar.setProgress(duration);
        }
    }

    private void backwardMediaPlayer(int milliseconds) {
        int currentPosition = mediaPlayer.getCurrentPosition();
        if (currentPosition - milliseconds > 0) {
            mediaPlayer.seekTo(currentPosition - milliseconds);
            seekBar.setProgress(currentPosition - milliseconds);
        } else {
            mediaPlayer.seekTo(0);
            seekBar.setProgress(0);
        }
    }

    private void updateSeekBar() {
        seekBar.setProgress(mediaPlayer.getCurrentPosition());
        durationTextView.setText(formatDuration(mediaPlayer.getCurrentPosition()) +
                " / " + formatDuration(mediaPlayer.getDuration()));

        if (mediaPlayer.isPlaying()) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    updateSeekBar();
                }
            }, 100);
        }
    }

    private String formatDuration(int duration) {
        int seconds = duration / 1000;
        int minutes = seconds / 60;
        seconds = seconds % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
    }

    @Override
    public void onBackPressed() {
        // Disable back button press by leaving this method empty
        // If you want to perform some action on back press, you can add your logic here
        super.onBackPressed();

       // new AestheticDialog.Builder(MusicPlayer.this, DialogStyle.FLAT, DialogType.WARNING)
       //         .setTitle("Warning")
       //         .setMessage("Are you sure, You want to exit?")
       //         .setCancelable(true)
       //         .setDarkMode(true)
       //         .setGravity(Gravity.CENTER)
       //         .setAnimation(DialogAnimation.SHRINK)
       //         .setOnClickListener(new OnDialogClickListener() {
       //             @Override
       //             public void onClick(@NonNull AestheticDialog.Builder builder) {
       //                 finishAffinity();
       //             }
       //         }).show();

    }
}