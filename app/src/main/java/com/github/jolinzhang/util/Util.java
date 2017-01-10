package com.github.jolinzhang.util;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.ImageView;

import com.github.jolinzhang.petcare.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * Created by Shadow on 11/23/16.
 */

public class Util {

    private static Util instance = new Util();
    private Util() {}

    public static Util getInstance() { return instance; }

    public static void init(Context context) { instance.context = context; }

    private Context context;

    /**
     *  Zengtai Qi- zxq150130
     */
    public void uploadPicture(Uri URI, String id, Callback callback) {

        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = context.getContentResolver().query(URI, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String imagePath =cursor.getString(column_index);
        cursor.close();
        File file = new File(imagePath);

        MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");

        OkHttpClient client = new OkHttpClient();

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("name", "pt=" + id,
                        RequestBody.create(MEDIA_TYPE_PNG, file))
                .build();

        Request request = new Request.Builder()
                .url("https://server.shaneqi.com/upload")
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(callback);
    }

    /**
     *  Zengtai Qi- zxq150130
     */
    //load circle image
    public void loadImage(String id, ImageView imageView, boolean isCircle, final com.squareup.picasso.Callback callback) {
        RequestCreator rc = Picasso.with(context)
                .load("https://server.shaneqi.com/upload/pt="+id) // Your image source.
                .placeholder(R.drawable.ic_setting_account);
        if (isCircle) {
            int width = imageView.getMeasuredWidth();
            rc = rc.transform(new RoundedTransformation(width, 0));
        }
        //get call back
        rc.fit().centerCrop().into(imageView, new com.squareup.picasso.Callback(){
            @Override
            public void onSuccess() {
                callback.onSuccess();
            }

            @Override
            public void onError() {
                callback.onError();

            }
        });
    }

    /**
     *  Zengtai Qi- zxq150130
     */
    //load image
    public void loadImage(Uri uri, ImageView imageView) {
        Picasso.with(context)
                .load(uri)
                .fit()
                .centerCrop()
                .into(imageView);
    }

    /**
     *  Zengtai Qi- zxq150130
     */
    public SimpleDateFormat dateFormatter() {
        return new SimpleDateFormat("MMM. dd, yyyy", Locale.US);
    }

    /**
     *  Zengtai Qi- zxq150130
     */
    //compare data
    public List<List<Date>> getThisDay(Date since, Date today) {
        return null;
    }

    public String getShortMonth(Date date) {
        return new SimpleDateFormat("MMM", Locale.US).format(date);
    }

    /**
     *  Zengtai Qi- zxq150130
     */
    public String getNumberDay(Date date) {
        return new SimpleDateFormat("dd", Locale.US).format(date);
    }

}
