package com.example.eyesopen;

import android.hardware.Camera;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

public class TestActivity extends AppCompatActivity {
    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    private Camera camera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_test);
        surfaceView = (SurfaceView)findViewById(R.id.surfaceView);

        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(surfaceListener);
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
            parameters.setPreviewSize(width, height);//여기 고쳐함 .ㅏㅇ러ㅣㅏㅓㅁ리ㅓㅣㅁㄹㄴㅇㅎㅁㅇㅎ
            camera.startPreview();

        }
    };

}