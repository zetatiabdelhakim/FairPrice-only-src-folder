package com.example.fairprice_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.fairprice_app.R;

public class MainActivity extends AppCompatActivity {
    Button NextPage1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        NextPage1 = findViewById(R.id.scan);
        Intent i1= new Intent(getApplicationContext(), Page2.class);
        NextPage1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(i1);
            }
        });
    }
}