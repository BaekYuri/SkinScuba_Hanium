package com.example.aaa123;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class InputResearchActivity extends AppCompatActivity {
    RadioGroup sensitive, acne, atopy;
    ImageButton skintypeOK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_research);

        sensitive=(RadioGroup) findViewById(R.id.min);
        acne =(RadioGroup) findViewById(R.id.yod);
        atopy = (RadioGroup) findViewById(R.id.ah);
        skintypeOK = (ImageButton)findViewById(R.id.skintypeOK);
        skintypeOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(atopy.getCheckedRadioButtonId()==R.id.ahYes){
                    Intent intent = new Intent(InputResearchActivity.this, InputSkintypeActivity.class);
                    intent.putExtra("type",6);//여드름 5, 아토피 6, 민감성 7, 해당없음 0
                    startActivity(intent);
                }else if(atopy.getCheckedRadioButtonId()==R.id.ahNo){
                    if(acne.getCheckedRadioButtonId()==R.id.yodYes){
                        Intent intent = new Intent(InputResearchActivity.this, InputSkintypeActivity.class);
                        intent.putExtra("type",5);
                        startActivity(intent);
                    }else if(acne.getCheckedRadioButtonId()==R.id.yodNo){
                        if(sensitive.getCheckedRadioButtonId()==R.id.minYes){
                            Intent intent = new Intent(InputResearchActivity.this, InputSkintypeActivity.class);
                            intent.putExtra("type",7);
                            startActivity(intent);
                        }else if(sensitive.getCheckedRadioButtonId()==R.id.minNo){
                            Intent intent = new Intent(InputResearchActivity.this, InputSkintypeActivity.class);
                            intent.putExtra("type",0);
                            startActivity(intent);
                        }else{
                            Toast.makeText(InputResearchActivity.this,"모든 문항을 선택해주세요.",Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(InputResearchActivity.this,"모든 문항을 선택해주세요.",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(InputResearchActivity.this,"모든 문항을 선택해주세요.",Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);

        LayoutInflater inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        View actionbar = inflater.inflate(R.layout.custom_bar,null);

        return true;

    }*/
}