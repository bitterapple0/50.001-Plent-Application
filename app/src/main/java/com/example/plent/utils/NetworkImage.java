package com.example.plent.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;

public class NetworkImage extends AsyncTask<String, Void, Bitmap> {
    ImageView bmImage;
    Integer imageHeight;
    Integer imageWidth;

    public NetworkImage(ImageView bmImage) {
        this.bmImage = bmImage;
    }

    public NetworkImage(ImageView bmImage, int imageHeight, int imageWidth) {
        this.bmImage = bmImage;
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
    }

    protected Bitmap doInBackground(String... urls) {
        String urldisplay = urls[0];
        Log.i("NETWORK IMAGE", urldisplay);
        Bitmap mIcon11 = null;
        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            mIcon11 = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return mIcon11;
    }

    protected void onPostExecute(Bitmap result) {
        if (imageWidth == null || imageHeight == null) {
            bmImage.setImageBitmap(result);
        } else {
            Bitmap scaledImage = Bitmap.createScaledBitmap(result, imageWidth, imageHeight, false);
            bmImage.setImageBitmap(scaledImage);
        }

    }
}
