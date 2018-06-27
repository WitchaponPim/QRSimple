package com.witchapon.qrsimple;

import android.Manifest;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;

public class TakePicActivity extends AppCompatActivity {
    Gallery gallery;
    ImageView slip;
    Button picker,upload;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_pic);
        slip =(ImageView) findViewById(R.id.slip);
        picker = (Button) findViewById(R.id.picker);
        upload = (Button) findViewById(R.id.upload);

        if (Build.VERSION.SDK_INT > 22) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }



    }
    public void pick(View view) {
        // Create intent to Open Image applications like Gallery, Google Photos
        gallery = new Gallery.Builder()
                .setpickPhotoRequestCode(123)
                .resetToCorrectOrientation(true)
                .setDirectory("DCIM/Camera/")
                .setName("Img_naja_eiei")
                .setImageFormat(Gallery.IMAGE_JPG)
                .setCompression(75)
                .setImageHeight(1000)
                .build(this);

        try {
            gallery.pickPicture();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Gallery.REQUEST_PICK_PHOTO && resultCode == RESULT_OK && data != null) {
//            Toast.makeText(getApplicationContext(), "OK", Toast.LENGTH_SHORT).show();
            String respath= gallery.getCameraBitmapPath();
            Uri pickedImage = data.getData();

            String[] filePath = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(pickedImage, filePath, null, null, null);
            cursor.moveToFirst();
            String imagePath = cursor.getString(cursor.getColumnIndex(filePath[0]));

            Bitmap bitmap = Utils.decodeFile(new File(imagePath),1000);
            bitmap = Utils.rotateBitmap(bitmap, Utils.getImageRotation(imagePath));
            Utils.saveBitmap( bitmap,respath,"jpg",75);

            slip.setImageBitmap(bitmap);

            File file = new File(respath);
            cursor.close();
        }
    }

}
