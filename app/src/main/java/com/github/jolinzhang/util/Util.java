package com.github.jolinzhang.util;

import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.github.jolinzhang.petcare.ThisApplication;

import java.io.File;

import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;


/**
 * Created by Shadow on 11/23/16.
 */

public class Util {

    private static Util instance = new Util();
    private Util() {}

    public static Util getInstance() { return instance; }

    public void uploadPicture(Uri URI, String id, Callback callback) {

        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = ThisApplication.instance.getContentResolver().query(URI, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String s =cursor.getString(column_index);
        cursor.close();
        File file = new File(s);

        MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");

        OkHttpClient client = new OkHttpClient();

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("name", "pt=" + id,
                        RequestBody.create(MEDIA_TYPE_PNG, file))
                .build();

        Request request = new Request.Builder()
                .url("http://54.191.156.153/avatar")
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(callback);
    }
}
