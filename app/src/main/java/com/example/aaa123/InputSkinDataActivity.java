package com.example.aaa123;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;
import com.google.android.gms.vision.face.Landmark;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static java.lang.Double.parseDouble;

public class InputSkinDataActivity extends AppCompatActivity {


    private Executor executor = Executors.newSingleThreadExecutor();

    int[] scorecount= new int[3];
    int[] moisscore= new int[3];
    int[] resultscore=new int[3];
    int[] partType=new int[3];
    String[] captureType=new String[3];
    int realtype,moisscorep;
    int[][] faceXY = new int[3][2];
    final int INPUT_SIZE=224;
    //float x1, x2, y1, y2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_skin_data);
        ImageView captureImage = (ImageView)findViewById(R.id.detectImage);
//        ImageView leftFace=(ImageView)findViewById(R.id.leftcheek);
//        ImageView rightFace = (ImageView)findViewById(R.id.rightcheek);
//        ImageView head = (ImageView)findViewById(R.id.forehead);
        TextView typeId = (TextView) findViewById(R.id.typeid);
        Button nextButton2 = (Button) findViewById(R.id.but);
        final int atype, btype;
        Intent i = getIntent();
        atype = i.getExtras().getInt("type2");
        btype = i.getExtras().getInt("typescore2");
        captureType[0]=i.getExtras().getString("lefttype");
        captureType[1]=i.getExtras().getString("righttype");
        captureType[2]=i.getExtras().getString("foreheadtype");
        faceXY[0][0]=i.getExtras().getInt("leftcheekx");
        faceXY[0][1]=i.getExtras().getInt("leftcheeky");
        faceXY[1][0]=i.getExtras().getInt("rightcheekx");
        faceXY[1][1]=i.getExtras().getInt("rightcheeky");
        faceXY[2][0]=i.getExtras().getInt("foreheadx");
        faceXY[2][1]=i.getExtras().getInt("foreheady");

        byte[] byteArray=i.getByteArrayExtra("captureimage");
        Bitmap myBitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        captureImage.setImageBitmap(myBitmap);

        Paint PaintType1 = new Paint();
        PaintType1.setStrokeWidth(3);
        PaintType1.setColor(Color.BLUE);
        PaintType1.setStyle(Paint.Style.STROKE);

        Paint PaintType2 = new Paint();
        PaintType2.setStrokeWidth(3);
        PaintType2.setColor(Color.GREEN);
        PaintType2.setStyle(Paint.Style.STROKE);

        Paint PaintType3 = new Paint();
        PaintType3.setStrokeWidth(3);
        PaintType3.setColor(Color.RED);
        PaintType3.setStyle(Paint.Style.STROKE);

        Bitmap tempBitmap = Bitmap.createBitmap(myBitmap.getWidth(), myBitmap.getHeight(), Bitmap.Config.RGB_565);
        Canvas tempCanvas = new Canvas(tempBitmap);
        tempCanvas.drawBitmap(myBitmap, 0, 0, null);
        double scale1=(double)myBitmap.getWidth()/INPUT_SIZE;
        double scale2=(double)myBitmap.getHeight()/INPUT_SIZE;

        faceXY[0][0]=(int)(scale1*faceXY[0][0]);
        faceXY[0][1]=(int)(scale2*faceXY[0][1]);
        faceXY[1][0]=(int)(scale1*faceXY[1][0]);
        faceXY[1][1]=(int)(scale2*faceXY[1][1]);
        faceXY[2][0]=(int)(scale1*faceXY[2][0]);
        faceXY[2][1]=(int)(scale2*faceXY[2][1]);


        for(int x=0;x<3;x++) {
            if (captureType[x].contains("[0")) {
                resultscore[x] = 7;
            } else if (captureType[x].contains("[1")) {
                resultscore[x] = 12;
            } else if (captureType[x].contains("[2")) {
                resultscore[x] = 17;
            }
            moisscore[x] = resultscore[x] + btype;
        }
        Log.d("확인확인확인",Integer.toString(resultscore[0])+" "+Integer.toString(resultscore[1])+" "+Integer.toString((resultscore[2])));
        for(int x=0;x<3;x++) {
            if (moisscore[x] >= 16) {
                partType[x] = 3;
                scorecount[2]++;
                tempCanvas.drawCircle(faceXY[x][0], faceXY[x][1], 20, PaintType3);
            } else if (moisscore[x] <= 8) {
                partType[x] = 1;
                scorecount[0]++;
                tempCanvas.drawCircle(faceXY[x][0], faceXY[x][1], 20, PaintType1);
            } else {
                partType[x] = 2;
                scorecount[1]++;
                tempCanvas.drawCircle(faceXY[x][0], faceXY[x][1], 20, PaintType2);
            }
        }
        captureImage.setImageDrawable(new BitmapDrawable(getResources(),tempBitmap));

        moisscorep=(moisscore[0]+moisscore[1]+moisscore[2])/3;
        if(scorecount[0]!=0 &&scorecount[2]!=0){
            realtype=4;
        }else{

            if(moisscorep >=18){
                realtype=3;
            }else if(moisscorep <=9){
                realtype=1;
            }else{
                realtype=2;
            }
        }
        nextButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InputSkinDataActivity.this, SelectDataActivity.class);
                intent.putExtra("selectType",realtype);//건성 중성 지성 복합성 번호
                intent.putExtra("typeA", atype);//여드름 민감성 등등 번호
                intent.putExtra("moisp", moisscorep);//수분 포인트
                startActivity(intent);
            }
        });

    }

}
