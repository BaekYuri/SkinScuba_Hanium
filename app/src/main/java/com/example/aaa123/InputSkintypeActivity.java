package com.example.aaa123;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RadioGroup;

public class InputSkintypeActivity extends AppCompatActivity {
    RadioGroup q4, q5, q6, q7, q8, q9;

    int score=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_skintype);
        final int atype;
        Intent i = getIntent();
        atype = i.getExtras().getInt("type");

        q4 = (RadioGroup) findViewById(R.id.q4);
        q5 = (RadioGroup) findViewById(R.id.q5);
        q6 = (RadioGroup) findViewById(R.id.q6);
        q7 = (RadioGroup) findViewById(R.id.q7);
        q8 = (RadioGroup) findViewById(R.id.q8);
        q9 = (RadioGroup) findViewById(R.id.q9);

        ImageButton skintypeOK2 = (ImageButton)findViewById(R.id.skintypeOK2);

        skintypeOK2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(q4.getCheckedRadioButtonId()==R.id.yes4){
                    score--;
                }else if(q4.getCheckedRadioButtonId()==R.id.no4){
                    score++;
                }
                if(q5.getCheckedRadioButtonId()==R.id.yes5){
                    score++;
                }else if(q5.getCheckedRadioButtonId()==R.id.no5){
                    score--;
                }
                if(q6.getCheckedRadioButtonId()==R.id.yes6){
                    score++;
                }else if(q6.getCheckedRadioButtonId()==R.id.no6){
                    score--;
                }
                if(q7.getCheckedRadioButtonId()==R.id.yes7){
                    score--;
                }else if(q7.getCheckedRadioButtonId()==R.id.no7){
                    score++;
                }
                if(q8.getCheckedRadioButtonId()==R.id.yes8){
                    score--;
                }else if(q8.getCheckedRadioButtonId()==R.id.no8){
                    score++;
                }
                if(q9.getCheckedRadioButtonId()==R.id.yes9){
                    score--;
                }else if(q9.getCheckedRadioButtonId()==R.id.no9){
                    score++;
                }
                Intent intent = new Intent(InputSkintypeActivity.this, DetectSkinActivity.class);
                intent.putExtra("typescore",score);
                intent.putExtra("type", atype);
                startActivity(intent);
            }
        });
    }
}
