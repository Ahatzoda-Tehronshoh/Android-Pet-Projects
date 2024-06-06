package com.example.somoni;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.view.View;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import androidx.core.content.FileProvider;

public class ViewToIntentAsBitmap {
    private File bitmapToFile(Context context, Bitmap bitmap, String fileNameToSave) {
        File file = null;
        File file1;

        try {
            file = new File(context.getCacheDir().toString() + File.separator + fileNameToSave);
            file.createNewFile();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
            byte[] bitmapData = bos.toByteArray();
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bitmapData);
            fos.flush();
            fos.close();
            file1 = file;
        } catch (Exception e) {
            e.printStackTrace();
            file1 = file;
        }

        return file1;
    }

    private static Bitmap getScreenShot(View view, int widthG, int heightG) {
        Bitmap returnedBitmap = Bitmap.createBitmap(widthG, heightG, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null) {
            Log.d("TAG_TEST", "getScreenShot: if" + bgDrawable);
            bgDrawable.draw(canvas);
        } else {
            Log.d("TAG_TEST", "getScreenShot: else" + bgDrawable);
            canvas.drawColor(Color.WHITE);

        }
        view.draw(canvas);
        return returnedBitmap;
    }

    @SuppressLint("WrongConstant")
    public void outoCreateFile (Context context, View view, String fileName){
        Bitmap bitmap = getScreenShot(view,view.getWidth(),view.getHeight());
        File file = bitmapToFile(context,bitmap,"cardview.png");
        Intent sendIntent = new Intent().
                setAction(Intent.ACTION_SEND ).
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION).setType("image/png");

        Uri imageUri = FileProvider.getUriForFile(context, context.getPackageName() + ".provider", file);

        sendIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
        context.startActivity(Intent.createChooser(sendIntent, "Поделиться"));

    }
}
