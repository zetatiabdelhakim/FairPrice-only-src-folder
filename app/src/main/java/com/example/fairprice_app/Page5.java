package com.example.fairprice_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.IOException;

public class Page5 extends AppCompatActivity {
    private TensorFlowLiteModel tfliteModel;
    private TfBalgha tfBalghaModel;
    private TfJellaba tfJellabaModel;
    private TfJersey tfJerseyModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page5);

        // Initialize TensorFlow Lite model
        try {
            tfliteModel = new TensorFlowLiteModel(this, "model.tflite");
            tfBalghaModel = new TfBalgha(this, "balgha.tflite");
            tfJellabaModel=new TfJellaba(this,"jellaba.tflite");
            tfJerseyModel=new TfJersey(this,"jerseys.tflite");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Get references to UI elements
        ImageView imageView = findViewById(R.id.product);
        ProgressBar loader = findViewById(R.id.loader);
        loader.setProgressTintList(getResources().getColorStateList(R.color.purple));

        // Get the image bitmap passed from previous activity
        Bitmap imageBitmap = getIntent().getParcelableExtra("image");
        if (imageBitmap != null) {
            // Display the image in ImageView
            imageView.setImageBitmap(imageBitmap);

            // Perform classification on the image using TensorFlow Lite model
            String predictedClass = tfliteModel.classifyImage(imageBitmap);
            System.out.println(predictedClass);
            if(predictedClass.equals("belgha")){
                String originalPrice = tfBalghaModel.classifyImage(imageBitmap);

                String[] parts = originalPrice.split("-");
                float firstValue = Float.parseFloat(parts[0]);
                float secondValue = Float.parseFloat(parts[1]);
                firstValue = (float) (firstValue*0.35);
                secondValue = (float) (secondValue*0.35);
                String imitationPrice = String.valueOf(firstValue) + "-" + String.valueOf(secondValue);

                long delayMillis = 3000;

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Create an Intent to start the new activity
                        Intent intent = new Intent(Page5.this, Result.class);
                        intent.putExtra("image",imageBitmap);
                        intent.putExtra("categ",predictedClass);
                        intent.putExtra("originalPrice",originalPrice);
                        intent.putExtra("imitationPrice",imitationPrice);
                        startActivity(intent);
                    }
                }, delayMillis);
            } else if (predictedClass.equals("jellaba")) {
                String originalPrice = tfJellabaModel.classifyImage(imageBitmap);

                String[] parts = originalPrice.split("-");
                float firstValue = Float.parseFloat(parts[0]);
                float secondValue = Float.parseFloat(parts[1]);
                firstValue = (float) (firstValue*0.35);
                secondValue = (float) (secondValue*0.35);
                String imitationPrice = String.valueOf(firstValue) + "-" + String.valueOf(secondValue);

                long delayMillis = 3000;

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Create an Intent to start the new activity
                        Intent intent = new Intent(Page5.this, Result.class);
                        intent.putExtra("image",imageBitmap);
                        intent.putExtra("categ",predictedClass);
                        intent.putExtra("originalPrice",originalPrice);
                        intent.putExtra("imitationPrice",imitationPrice);
                        startActivity(intent);
                    }
                }, delayMillis);
            }else {
                String originalPrice = tfJerseyModel.classifyImage(imageBitmap);

                String[] parts = originalPrice.split("-");
                float firstValue = Float.parseFloat(parts[0]);
                float secondValue = Float.parseFloat(parts[1]);
                firstValue = (float) (firstValue*0.35);
                secondValue = (float) (secondValue*0.35);
                String imitationPrice = String.valueOf(firstValue) + "-" + String.valueOf(secondValue);

                long delayMillis = 3000;

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Create an Intent to start the new activity
                        Intent intent = new Intent(Page5.this, Result.class);
                        intent.putExtra("image",imageBitmap);
                        intent.putExtra("categ",predictedClass);
                        intent.putExtra("originalPrice",originalPrice);
                        intent.putExtra("imitationPrice",imitationPrice);
                        startActivity(intent);
                    }
                }, delayMillis);
            }


            // Example: You can display the predicted class in a toast message
        }
    }
}