package com.example.aaa123;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{
            Thread.sleep(4000);
        }catch(InterruptedException e){
            e.printStackTrace();
        }
        startActivity(new Intent(this, InputResearchActivity.class));
        finish();
    }
}
