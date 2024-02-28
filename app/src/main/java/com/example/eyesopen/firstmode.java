package com.example.eyesopen;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.eyesopen.ml.ModelUnquant;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class firstmode extends AppCompatActivity{
    Button firstmodebutton2;
    Button secondmodebutton2;
    ImageView imageView10;
    public SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    private Camera camera;
    static TextView textView2;
    TextToSpeech tts;
    ConstraintLayout firstmodelayout;
    public static int cameraID = 0;
    public static boolean isBlack = true; // 배경화면 색깔인듯?


    ImageView imageView;
    Button button100;
    static int imageSize = 224;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.firstmode);

        File sdcard = Environment.getExternalStorageDirectory();


        firstmodelayout = (ConstraintLayout) findViewById(R.id.firstmodelayout);
        firstmodebutton2 = (Button) findViewById(R.id.firstmodebutton2);
        secondmodebutton2 = (Button) findViewById(R.id.secondmodebutton2);
        imageView10 = (ImageView) findViewById(R.id.imageView10);
        surfaceView = (SurfaceView) findViewById(R.id.imageView4);
        textView2 = (TextView) findViewById(R.id.textView2);
        button100 =(Button) findViewById(R.id.button100);
        button100.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    /*Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, 1);*/
                    /*
                    cameraID = 0; // 0은 후면카메라 1은 전면카메라
                    Intent i = new Intent(getApplicationContext(),CameraView.class);
                    startActivityForResult(i, 999);*/
                    surfaceHolder = surfaceView.getHolder();
                    surfaceHolder.addCallback(surfaceListener);
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //surfaceHolder.removeCallback(surfaceListener);
                        }
                    }, 5000);
                } else {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, 100);
                }
            }
        });


        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = tts.setLanguage(Locale.KOREA);
                    // 영어로 설정해도 한글을 읽을 수 있고 영어 발음이 한국어로 설정하는것 보다 낫다.
                    if (result == TextToSpeech.LANG_NOT_SUPPORTED || result == TextToSpeech.LANG_MISSING_DATA) {
                        Log.e("TTS", "Language not supported.");
                    }
                } else {
                    Log.e("TTS", "Initialization failed.");
                }

            }
        });
        Timer timer = new Timer();

        TimerTask TT = new TimerTask() {
            @Override
            public void run() {

                // 반복실행할 구문

            }

        };

        timer.schedule(TT, 0, 3000); //Timer 실행
        timer.cancel();//타이머 종료

        firstmodelayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stt t = new stt();
                final Handler handler = new Handler(){
                    @Override
                    public void handleMessage(@NonNull Message msg) {
                        super.handleMessage(msg);
                        for(int i=0;i<t.matches.size();i++){
                            if(t.matches.get(i).equals("거리")){
                                Intent j = new Intent(firstmode.this,firstmode.class);
                                startActivity(j);
                            }
                            else if (t.matches.get(i).equals("읽기")){
                                Intent k = new Intent(firstmode.this, secondmode.class);
                                startActivity(k);
                            }
                            else if(t.matches.get(i).equals("가이드북")){
                                CharSequence text = "\"Eyes Open\"의 사용법을 알려드리겠습니다이 앱에는 모드는 두가지가 있습니다. 길거리 모드와 글씨 읽기 모드가 있습니다. '길거리 모드'는 길을 걸어 다닐 때 보이는 표지판을 알려주는 모드이고 '글씨 읽기 모드'는 사용자님이 앱에서 촬영한 사진에 있는 글씨를 읽어주는 모드입니다. 촬영을 원하시면 촬영이라고 말하시면 촬영이 됩니다. 모드를 변경을 원하시면 '모드 변경'이라고 말하신 후 원하시는 모드 이름을 말하시면 됩니다.\n";
                                tts.setPitch((float) 1.0); // Sets the speech pitch for the TextToSpeech engine.
                                tts.setSpeechRate((float) 1.0); // Sets the speech rate.

                                tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "uid");
                            }
                            Toast.makeText(firstmode.this, t.matches.get(i), Toast.LENGTH_SHORT).show();
                        }
                    }

                };

                t.callstt(firstmode.this, handler);
            }
        });
        firstmodebutton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(firstmode.this, firstmode.class);
                startActivity(i);
            }
        });

        secondmodebutton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(firstmode.this, secondmode.class);
                startActivity(i);
            }
        });
        imageView10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CharSequence text = "\"Eyes Open\"의 사용법을 알려드리겠습니다이 앱에는 모드는 두가지가 있습니다. 길거리 모드와 글씨 읽기 모드가 있습니다. '길거리 모드'는 길을 걸어 다닐 때 보이는 표지판을 알려주는 모드이고 '글씨 읽기 모드'는 사용자님이 앱에서 촬영한 사진에 있는 글씨를 읽어주는 모드입니다. 촬영을 원하시면 촬영이라고 말하시면 촬영이 됩니다. 모드를 변경을 원하시면 '모드 변경'이라고 말하신 후 원하시는 모드 이름을 말하시면 됩니다.\n";
                tts.setPitch((float) 1.0); // Sets the speech pitch for the TextToSpeech engine.
                tts.setSpeechRate((float) 1.0); // Sets the speech rate.

                tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "uid");
                // QUEUE_ADD - Queue mode where the new entry is added at the end of the playback queue.
                // QUEUE_FLUSH - Queue mode where all entries in the playback queue (media to be played
                // and text to be synthesized) are dropped and replaced by the new entry.
            }
        });
    }
    private SurfaceHolder.Callback surfaceListener = new SurfaceHolder.Callback() {

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            // TODO Auto-generated method stub
            camera.release();

        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            // TODO Auto-generated method stub
            camera = Camera.open();
            try {
                camera.setPreviewDisplay(holder);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            // TODO Auto-generated method stub
            Camera.Parameters parameters = camera.getParameters();
            parameters.setPreviewSize(width, height);
            camera.startPreview();

        }
    };

    public  static void  classifyImage(Context context, @NonNull Bitmap image){
        try {
            ModelUnquant model = ModelUnquant.newInstance(context);

            // Creates inputs for reference.
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 224, 224, 3}, DataType.FLOAT32);
            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * imageSize * imageSize * 3);
            byteBuffer.order(ByteOrder.nativeOrder());

            int[] intValues = new int [imageSize * imageSize];
            image.getPixels(intValues, 0, image.getWidth(),0,0, image.getWidth(), image.getHeight());

            int pixel = 0;
            for(int i=0; i< imageSize; i++){
                for(int j=0; j< imageSize; j++){
                    int val =intValues[pixel++];//RGB
                    byteBuffer.putFloat(((val >> 16) & 0xFF) * (1.f / 255.f));
                    byteBuffer.putFloat(((val >> 8) & 0xFF) * (1.f / 255.f));
                    byteBuffer.putFloat((val & 0xFF) * (1.f / 255.f));

                }
            }

            inputFeature0.loadBuffer(byteBuffer);

            // Runs model inference and gets result.
            ModelUnquant.Outputs outputs = model.process(inputFeature0);
            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

            float[] confidences = outputFeature0.getFloatArray();
            int maxPos =0;
            float maxConfidence = 0;
            for(int i=0; i<confidences.length; i++){
                if(confidences[i] > maxConfidence) {
                    maxConfidence = confidences[i];
                    maxPos = i;
                }
            }
            String[] classes = {"자전거전용도로","보행자전용도로","횡단보도","전방공사중표지판","안전제일 띠","사각출입금지","풍경","남자화장실","여자화장실"};

            TextView result = textView2;
            result.setText(classes[maxPos]);
            System.out.println(classes[maxPos]);


            // Releases model resources if no longer used.
            model.close();
        } catch (IOException e) {
            // TODO Handle the exception
        }
    }
    public static void readyImage(Bitmap image,Context context){

        int dimension = Math.min(image.getWidth(), image.getHeight());
        image = ThumbnailUtils.extractThumbnail(image, dimension, dimension);

        image = Bitmap.createScaledBitmap(image, imageSize, imageSize, false);
        classifyImage(context, image);
    }

    @Override
    protected void onDestroy() {
        if (tts != null) {
            tts.stop();
            // Interrupts the current utterance (whether played or rendered to file) and
            // discards other utterances in the queue.
            tts.shutdown();
            // Releases the resources used by the TextToSpeech engine.
        }
        super.onDestroy();
    }

}
