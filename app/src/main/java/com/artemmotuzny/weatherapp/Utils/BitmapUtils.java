package com.artemmotuzny.weatherapp.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by tema_ on 13.10.2016.
 */

public class BitmapUtils {

    public static String encodeToBase64(Bitmap image, Bitmap.CompressFormat compressFormat, int quality){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        image.compress(compressFormat,quality,byteArrayOutputStream);
        return Base64.encodeToString(byteArrayOutputStream.toByteArray(),Base64.DEFAULT);
    }

    public static Bitmap decodeBase64(String input){
        byte[] decode = Base64.decode(input,0);
        return BitmapFactory.decodeByteArray(decode,0,decode.length);
    }

    public static Bitmap getBitmapFromUrl(String src){
        try {
            String baseUrl = "http://openweathermap.org/img/w/";
            URL url = new URL(baseUrl+src+".png");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);
        } catch (IOException e) {
            // Log exception
            return null;
        }
    }
}
