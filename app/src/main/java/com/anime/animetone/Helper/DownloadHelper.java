package com.anime.animetone.Helper;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;


import androidx.annotation.NonNull;

import com.anime.animetone.MainActivity;
import com.anime.animetone.customprogresdialog;
import com.thecode.aestheticdialogs.AestheticDialog;
import com.thecode.aestheticdialogs.DialogAnimation;
import com.thecode.aestheticdialogs.DialogStyle;
import com.thecode.aestheticdialogs.DialogType;
import com.thecode.aestheticdialogs.OnDialogClickListener;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Locale;

// new DownloadHelper(LoaderActivity.this,"rd3.so").execute("https://terrorvip.in/Download/downloadpvt.php?pvt=hack.so");

public class DownloadHelper extends AsyncTask<String, String, String> {


    Context ctx;
    String Filename;

   File file;
   int type;

    customprogresdialog customprogresdialog;

    public DownloadHelper(Context ctx, String Filename, File file, int type){
        this.ctx = ctx;
        this.Filename = Filename;
        this.file = file;
        this.type = type;
        this.customprogresdialog = new customprogresdialog(ctx);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (ctx == null) {
            return;
        }

        customprogresdialog.show();

    }


    @Override
    protected String doInBackground(String... f_url) {

        if (!isInternetAvailable(ctx)) {
            return "No internet connection";
        }
        if(file.exists()){
            return "";
        }
        int count;
        try {
            URL url = new URL(f_url[0]);
            URLConnection connection = url.openConnection();
            connection.connect();

            // this will be useful so that you can show a tipical 0-100%
            // progress bar
            int lenghtOfFile = connection.getContentLength();

            // download the file
            InputStream input = new BufferedInputStream(url.openStream(),
                    8192);

            // Output stream
            OutputStream output = new FileOutputStream(file);

            byte data[] = new byte[1024];

            long total = 0;

            while ((count = input.read(data)) != -1) {
                total += count;
                // publishing the progress....
                // After this onProgressUpdate will be called
                publishProgress("" + (int) ((total * 100) / lenghtOfFile));

                // writing data to file
                output.write(data, 0, count);
            }

            // flushing output
            output.flush();

            // closing streams
            output.close();
            input.close();

        } catch (Exception e) {
            Log.e("Error: ", e.getMessage());
            return "eror occured";
        }

        return null;
    }


    /**
     * Updating progress bar
     * */
    protected void onProgressUpdate(String... progress) {
        // setting progress percentage

    }


    /**
     * After completing background task Dismiss the progress dialog
     * **/

    @Override
    protected void onPostExecute(String s) {

        if (ctx == null) {
            return ;
        }

        if(file.exists()){
            if(type ==1){
                RingtoneHelper.setRingtone(ctx, file.getAbsolutePath());
            }else if(type ==2){
                RingtoneHelper.setNotificationTone(ctx, file.getAbsolutePath());
            }
           // Toast.makeText(ctx,"type "+type,Toast.LENGTH_SHORT).show();
        }else{

            Toast.makeText(ctx,"file doesn't exist",Toast.LENGTH_SHORT).show();
        }

        customprogresdialog.cancel();
        
        return ;
    }
    private static boolean isInternetAvailable(Context ctx) {
        ConnectivityManager connectivityManager = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
