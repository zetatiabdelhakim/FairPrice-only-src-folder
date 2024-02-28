package com.example.fairprice_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class Result extends AppCompatActivity {
    private static final int pic_id = 99;
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        Button report= findViewById(R.id.report);
        Button rescan=findViewById(R.id.rescan);
        Bitmap imageBitmap = getIntent().getParcelableExtra("image");
        String text =getIntent().getStringExtra("categ");
        String originalPrice = getIntent().getStringExtra("originalPrice");
        String imitationPrice = getIntent().getStringExtra("imitationPrice");
        ImageView image=findViewById(R.id.found_product);
        image.setImageBitmap(imageBitmap);
        TextView categ=findViewById(R.id.productLabel);
        TextView or_price=findViewById(R.id.range);
        TextView im_price=findViewById(R.id.range2);
        or_price.setText("Range : "+originalPrice+" DHS");
        im_price.setText("Range : "+imitationPrice+" DHS");
        categ.setText(text);
        rescan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCamera();
            }
        });
        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = "+212123456789"; // Replace with your desired phone number

                // Check if the app has the necessary permission before making the call
                if (ContextCompat.checkSelfPermission(Result.this, Manifest.permission.CALL_PHONE) !=
                        PackageManager.PERMISSION_GRANTED) {
                    // Request the permission
                    ActivityCompat.requestPermissions(Result.this,
                            new String[]{Manifest.permission.CALL_PHONE},
                            MY_PERMISSIONS_REQUEST_CALL_PHONE);
                } else {
                    // Permission already granted, proceed with making the call
                    Intent dialIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber));
                    if (dialIntent.resolveActivity(getPackageManager()) != null) {
                        startActivity(dialIntent);
                    }
                }
            }
        });
    }
    private void openCamera() {
        Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(camera_intent, pic_id);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == pic_id) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            Intent intent = new Intent(getApplicationContext(), Page5.class);
            intent.putExtra("image",photo);
            startActivity(intent);
        }
    }
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == MY_PERMISSIONS_REQUEST_CALL_PHONE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed with making the call
                String phoneNumber = "+212123456789"; // Replace with your desired phone number
                Intent dialIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber));
                if (dialIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(dialIntent);
                }
            }
        }
    }
}