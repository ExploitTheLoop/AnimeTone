package com.anime.animetone.Helper;
import static android.service.controls.ControlsProviderService.TAG;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.anime.animetone.MainActivity;
import com.anime.animetone.MusicPlayer;
import com.anime.animetone.R;
import com.anime.animetone.customprogresdialog;
import com.bumptech.glide.Glide;
import com.thecode.aestheticdialogs.AestheticDialog;
import com.thecode.aestheticdialogs.DialogAnimation;
import com.thecode.aestheticdialogs.DialogStyle;
import com.thecode.aestheticdialogs.DialogType;
import com.thecode.aestheticdialogs.OnDialogClickListener;

import java.io.File;

public class RingtoneHelper {
    public static void setRingtone(Context context, String path) {
        File NotificationTone = new File(path);
        ContentValues values = new ContentValues();
        values.put(MediaStore.MediaColumns.DATA, NotificationTone.getAbsolutePath());
        values.put(MediaStore.MediaColumns.TITLE, NotificationTone.getName());
        values.put(MediaStore.MediaColumns.MIME_TYPE, "audio/mp3");
        values.put(MediaStore.Audio.Media.ARTIST, "Animetone");
        values.put(MediaStore.Audio.Media.IS_RINGTONE, true);
        values.put(MediaStore.Audio.Media.IS_NOTIFICATION, true);
        values.put(MediaStore.Audio.Media.IS_ALARM, true);
        values.put(MediaStore.Audio.Media.IS_MUSIC, false);
        values.put(MediaStore.Audio.Media.IS_PODCAST, false);

        final Uri baseUri = MediaStore.Audio.Media.getContentUriForPath(NotificationTone.getAbsolutePath());
        Uri toneUri = getUriForExistingTone(context, baseUri, NotificationTone.getAbsolutePath());

        if (toneUri == null) {
            toneUri = context.getContentResolver().insert(baseUri, values);
            Log.i(TAG,"toneuri  :"+toneUri.toString());
        }else{
             Log.i(TAG,"toneuri  :"+toneUri.toString());
        }
        try
        {

            RingtoneManager.setActualDefaultRingtoneUri(context, RingtoneManager.TYPE_RINGTONE, toneUri);

            new AestheticDialog.Builder((Activity) context, DialogStyle.TOASTER, DialogType.SUCCESS)
                    .setTitle("Ringtone")
                    .setMessage("Ringtone has been changed successfully")
                    .show();

        } catch (Throwable t) {

            Log.i(TAG,"error  :"+t.getMessage().toString());
        }
    }

    public static void setNotificationTone(Context context, String path) {
        File NotificationTone = new File(path);
        ContentValues values = new ContentValues();
        values.put(MediaStore.MediaColumns.DATA, NotificationTone.getAbsolutePath());
        values.put(MediaStore.MediaColumns.TITLE, NotificationTone.getName());
        values.put(MediaStore.MediaColumns.MIME_TYPE, "audio/mp3");
        values.put(MediaStore.Audio.Media.ARTIST, "Animetone");
        values.put(MediaStore.Audio.Media.IS_RINGTONE, true);
        values.put(MediaStore.Audio.Media.IS_NOTIFICATION, true);
        values.put(MediaStore.Audio.Media.IS_ALARM, true);
        values.put(MediaStore.Audio.Media.IS_MUSIC, false);
        values.put(MediaStore.Audio.Media.IS_PODCAST, false);

        final Uri baseUri = MediaStore.Audio.Media.getContentUriForPath(NotificationTone.getAbsolutePath());
        Uri toneUri = getUriForExistingTone(context, baseUri, NotificationTone.getAbsolutePath());


        if (toneUri == null) {
            toneUri = context.getContentResolver().insert(baseUri, values);
            Log.i(TAG,"toneuri  :"+toneUri.toString());
        }else{
             Log.i(TAG,"toneuri  :"+toneUri.toString());
        }
        try
        {

        RingtoneManager.setActualDefaultRingtoneUri(context, RingtoneManager.TYPE_NOTIFICATION, toneUri);

            new AestheticDialog.Builder((Activity) context, DialogStyle.TOASTER, DialogType.SUCCESS)
                    .setTitle("Notification Tone")
                    .setMessage("Notification Tone has been changed successfully")
                    .show();

        } catch (Throwable t) {

            Log.i(TAG,"error  :"+t.getMessage().toString());
        }

    }


    private static Uri getUriForExistingTone(Context context, Uri uri, String filePath) {

        Cursor cursor = null;
        try {

            cursor = context.getContentResolver()
                    .query(uri,
                            new String[] {MediaStore.MediaColumns._ID, MediaStore.MediaColumns.DATA},
                            MediaStore.MediaColumns.DATA + " = ?",
                            new String[] {filePath},
                            null, null);

            if (cursor != null && cursor.getCount() != 0) {

                cursor.moveToFirst();
                int mediaPos = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
                return Uri.parse(uri.toString() + "/" + mediaPos);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return null;
    }



    // url = file path or whatever suitable URL you want.
    public static String getMimeType(String url) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return type;
    }

}


