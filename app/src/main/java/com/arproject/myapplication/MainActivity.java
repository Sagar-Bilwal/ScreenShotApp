package com.arproject.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity {

    Button sendButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sendButton=findViewById(R.id.send_button);
        final RelativeLayout relativeLayout=findViewById(R.id.relative);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Bitmap bitmap = takeScreenshot(relativeLayout);
                saveBitmap(bitmap);
                shareScreenshot();
            }
        });
    }

    private Bitmap takeScreenshot(View view) {
        View screenView =view.getRootView().findViewById(R.id.relative);
        screenView.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(screenView.getDrawingCache());
        screenView.setDrawingCacheEnabled(false);
        return bitmap;
    }

    File imagePath;
    private void saveBitmap(Bitmap bitmap)
    {
        //image path is static
        String str = Environment.getExternalStorageDirectory()+ "/Projects/Screenshots";
        File file=new File(str);
        if(!file.exists())
        {
            file.mkdirs();
        }
        imagePath = new File(str+"/"+"project"+".jpg");
        OutputStream fos;
        try {
            fos = new FileOutputStream(imagePath);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            Log.e("GREC", e.getMessage(), e);
        } catch (IOException e) {
            Log.e("GREC", e.getMessage(), e);
        }
    }


    private void shareScreenshot()
    {
        try {
            Uri uri = FileProvider.getUriForFile(this,getApplicationContext().getPackageName() + ".fileprovider",imagePath);
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_SEND);
            intent.setType("image/*");

//            intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Share Photo");
            intent.putExtra(Intent.EXTRA_STREAM, uri);

            startActivity(Intent.createChooser(intent, "Share Photo"));
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "No App Available", Toast.LENGTH_SHORT).show();
        }
        catch(Exception ex)
        {
            Log.d("ShareScreenshot>>>>>> :",ex.getMessage());
        }

    }
}
