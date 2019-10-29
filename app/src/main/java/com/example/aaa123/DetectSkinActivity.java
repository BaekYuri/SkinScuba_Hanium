package com.example.aaa123;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Landmark;
import com.wonderkiln.camerakit.CameraKitError;
import com.wonderkiln.camerakit.CameraKitEvent;
import com.wonderkiln.camerakit.CameraKitEventListener;
import com.wonderkiln.camerakit.CameraKitImage;
import com.wonderkiln.camerakit.CameraKitVideo;
import com.wonderkiln.camerakit.CameraView;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


import com.google.android.gms.vision.face.Face;

import com.google.android.gms.vision.face.FaceDetector;
public class DetectSkinActivity extends AppCompatActivity {

    private static final String MODEL_PATH = "model_v3.tflite";
    private static final boolean QUANT = false;
    private static final String LABEL_PATH = "labels.txt";
    private static final int INPUT_SIZE = 224;

    private Classifier classifier;

    private Executor executor = Executors.newSingleThreadExecutor();
    private TextView textViewResult;
    private ImageButton btnDetectObject, btnToggleCamera;
//    private ImageView imageViewResult;
    private CameraView cameraView;
    int lefteye_y,righteye_y,nosebase_x,leftcheek_x, leftcheek_y, rightcheek_x, rightcheek_y;
    float x1, x2, y1, y2;
    String r,lr,rr,fr;
    Bitmap bitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detect_skin);
        final int atype, btype;
        Intent i = getIntent();
        atype = i.getExtras().getInt("type");
        btype = i.getExtras().getInt("typescore");
        cameraView = (CameraView) findViewById(R.id.cameraView);
//        imageViewResult = findViewById(R.id.imageViewResult);
//        textViewResult = findViewById(R.id.textViewResult);
//        textViewResult.setMovementMethod(new ScrollingMovementMethod());

        btnToggleCamera = findViewById(R.id.btnToggleCamera);
        btnDetectObject = findViewById(R.id.btnDetectObject);
        cameraView.addCameraKitListener(new CameraKitEventListener() {
            @Override
            public void onEvent(CameraKitEvent cameraKitEvent) {

            }

            @Override
            public void onError(CameraKitError cameraKitError) {

            }

            @Override
            public void onImage(CameraKitImage cameraKitImage) {

                Bitmap origin = cameraKitImage.getBitmap();
                double widthSize = 370;
                double scalee = widthSize/origin.getWidth();
                final Bitmap abit = Bitmap.createScaledBitmap(origin,(int)widthSize,(int)(origin.getHeight()*scalee),false);
//                final Bitmap abit = Bitmap.createScaledBitmap(origin,370,586,false);
                bitmap = Bitmap.createScaledBitmap(origin, INPUT_SIZE, INPUT_SIZE, false);

                //imageViewResult.setImageBitmap(bitmap);

                //final List<Classifier.Recognition> results = classifier.recognizeImage(bitmap);
                //r=results.toString();
                //textViewResult.setText(results.toString());

                Canvas tempCanvas = new Canvas(bitmap);
                tempCanvas.drawBitmap(bitmap, 0, 0, null);

                double viewWidth = tempCanvas.getWidth();
                double viewHeight = tempCanvas.getHeight();
                double imageWidth = bitmap.getWidth();
                double imageHeight = bitmap.getHeight();
                final double scale = Math.min(viewWidth / imageWidth, viewHeight / imageHeight);

                FaceDetector faceDetector = new
                        FaceDetector.Builder(getApplicationContext()).setTrackingEnabled(false).setLandmarkType(FaceDetector.ALL_LANDMARKS).setProminentFaceOnly(true)
                        .build();
                if(!faceDetector.isOperational()){
                    Toast.makeText(DetectSkinActivity.this,"Could not set up the face detector!",Toast.LENGTH_SHORT).show();
                    return;
                }
                    Frame frame = new Frame.Builder().setBitmap(bitmap).build();
                    final SparseArray<Face> faces = faceDetector.detect(frame);
                    AlertDialog.Builder builder = new AlertDialog.Builder(DetectSkinActivity.this);
                    builder.setTitle("캡쳐 성공");
                    builder.setMessage("이 사진을 사용하겠습니까? 얼굴 인식이 안된 경우에는 이전화면으로 돌아갑니다.");
                    builder.setPositiveButton("다시 찍기", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    builder.setNegativeButton("네", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            for (int a = 0; a < faces.size(); ++a) {
                                Face thisFace = faces.valueAt(a);
                                for (Landmark landmark : thisFace.getLandmarks()) {
                                    int factype = landmark.getType();
                                    if (factype == landmark.LEFT_EYE) {
                                        lefteye_y = (int) (landmark.getPosition().y * scale);
                                    } else if (factype == landmark.RIGHT_EYE) {
                                        righteye_y = (int) (landmark.getPosition().y * scale);
                                    } else if (factype == landmark.NOSE_BASE) {
                                        nosebase_x = (int) (landmark.getPosition().x * scale);
                                    }else if (factype == landmark.LEFT_CHEEK){
                                        leftcheek_x=(int)(landmark.getPosition().x*scale);
                                        leftcheek_y=(int)(landmark.getPosition().y*scale);
                                    }else if(factype == landmark.RIGHT_CHEEK){
                                        rightcheek_x=(int)(landmark.getPosition().x*scale);
                                        rightcheek_y=(int)(landmark.getPosition().y*scale);
                                    }
                                }
                                x1 = thisFace.getPosition().x;
                                y1 = thisFace.getPosition().y;
                                x2 = x1 + thisFace.getWidth();
                                y2 = y1 + thisFace.getHeight();
                            }
                            Bitmap leftCheek = Bitmap.createBitmap(bitmap, (int) x1, lefteye_y, (int) (nosebase_x - x1), (int) (y2 - lefteye_y));
                            leftCheek = Bitmap.createScaledBitmap(leftCheek, INPUT_SIZE, INPUT_SIZE, false);
                            Bitmap rightCheek = Bitmap.createBitmap(bitmap, (nosebase_x), righteye_y, (int) (x2 - nosebase_x), (int) (y2 - righteye_y));
                            rightCheek = Bitmap.createScaledBitmap(rightCheek, INPUT_SIZE, INPUT_SIZE, false);
                            Bitmap forehead = Bitmap.createBitmap(bitmap, (int) x1, (int) y1, (int) (x2 - x1), (int) (righteye_y - y1));
                            forehead = Bitmap.createScaledBitmap(forehead, INPUT_SIZE, INPUT_SIZE, false);
                            List<Classifier.Recognition> leftResults = classifier.recognizeImage(leftCheek);
                            List<Classifier.Recognition> rightResults = classifier.recognizeImage(rightCheek);
                            List<Classifier.Recognition> foreheadResults = classifier.recognizeImage(forehead);
                            lr = leftResults.toString();
                            rr = rightResults.toString();
                            fr = foreheadResults.toString();
                            int foh=(int)(righteye_y+y1)/2;
                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                            abit.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                            byte[] byteArray = stream.toByteArray();

                                Intent intent = new Intent(DetectSkinActivity.this, InputSkinDataActivity.class);

                                intent.putExtra("typescore2", btype);
                                intent.putExtra("type2", atype);
                                intent.putExtra("lefttype", lr);
                                intent.putExtra("righttype", rr);
                                intent.putExtra("foreheadtype", fr);
                                intent.putExtra("captureimage", byteArray);
                                intent.putExtra("leftcheekx", leftcheek_x);
                                intent.putExtra("leftcheeky", leftcheek_y);
                                intent.putExtra("rightcheekx", rightcheek_x);
                                intent.putExtra("rightcheeky", rightcheek_y);
                                intent.putExtra("foreheadx", nosebase_x);
                                intent.putExtra("foreheady",foh);

                                startActivity(intent);

                        }
                    });
                    builder.show();
            }

            @Override
            public void onVideo(CameraKitVideo cameraKitVideo) {

            }
        });

        btnToggleCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraView.toggleFacing();
            }
        });

        btnDetectObject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraView.captureImage();
            }
        });

        initTensorFlowAndLoadModel();

    }

    @Override
    protected void onResume() {
        super.onResume();
        cameraView.start();
    }

    @Override
    protected void onPause() {
        cameraView.stop();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                classifier.close();
            }
        });
    }

    private void initTensorFlowAndLoadModel() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    classifier = TensorFlowImageClassifier.create(
                            getAssets(),
                            MODEL_PATH,
                            LABEL_PATH,
                            INPUT_SIZE,
                            QUANT);
                    makeButtonVisible();
                } catch (final Exception e) {
                    throw new RuntimeException("Error initializing TensorFlow!", e);
                }
            }
        });
    }

    private void makeButtonVisible() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                btnDetectObject.setVisibility(View.VISIBLE);
            }
        });
    }
    private void show(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("얼굴인식 성공");
        builder.setMessage("얼굴인식에 성공했습니다. 이 사진을 사용하겠습니까?");
        builder.setPositiveButton("다시 찍기", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setNegativeButton("네", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }

}
