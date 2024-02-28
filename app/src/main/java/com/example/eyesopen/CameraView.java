package com.example.eyesopen;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class CameraView extends Activity implements SurfaceHolder.Callback, View.OnClickListener {
    private static final String TAG = "CameraTest";
    Camera mCamera;
    boolean mPreviewRunning = false;
    int num = 5;

    @SuppressWarnings("deprecation")
    public void onCreate(Bundle icicle){
        super.onCreate(icicle);
        Log.e(TAG, "onCreate");


        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.cameraview);
        //ImageView img = (ImageView) findViewById(R.id.blankImage);
        /*
        if(firstmode.isBlack)
            img.setBackgroundResource(android.R.color.black);
        else
            img.setBackgroundResource(android.R.color.white);

         */

        mSurfaceView = (SurfaceView) findViewById(R.id.surface_camera);
        mSurfaceView.setOnClickListener(this);
        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.addCallback(this);
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);
    }


    Camera.PictureCallback mPictureCallback = new Camera.PictureCallback() {

        public void onPictureTaken(byte[] data, Camera camera) {
            // TODO Auto-generated method stub
            if (data != null){
                //Intent mIntent = new Intent();
                //mIntent.putExtra("image",imageData);



                try{
                    BitmapFactory.Options opts = new BitmapFactory.Options();
                    Bitmap bitmap= BitmapFactory.decodeByteArray(data, 0, data.length,opts);
                    bitmap = Bitmap.createScaledBitmap(bitmap, 300, 300, false);
                    int width = bitmap.getWidth();
                    int height = bitmap.getHeight();
                    int newWidth = 300;
                    int newHeight = 300;

                    // calculate the scale - in this case = 0.4f
                    float scaleWidth = ((float) newWidth) / width;
                    float scaleHeight = ((float) newHeight) / height;

                    // createa matrix for the manipulation
                    Matrix matrix = new Matrix();
                    // resize the bit map
                    matrix.postScale(scaleWidth, scaleHeight);
                    // rotate the Bitmap
                    matrix.postRotate(-90);
                    Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
                            width, height, matrix, true);
                    //firstmode.imageView4.setImageBitmap(resizedBitmap);
                    //firstmode.readyImage(resizedBitmap, getApplicationContext());

                }catch(Exception e){
                    e.printStackTrace();
                }
                num -= 1;

                //StoreByteImage(mContext, imageData, 50,"ImageName");
                //setResult(FOTO_MODE, mIntent);
                if(num == 0){
                    mCamera.stopPreview();
                    mPreviewRunning = false;
                    setResult(585);
                    mCamera.release();
                    finish();
                } else {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            mCamera.takePicture(null, mPictureCallback, mPictureCallback);//Error
                        }
                    }, 200);
                }

            }
        }
    };

    protected void onResume(){
        Log.e(TAG, "onResume");
        super.onResume();
    }

    protected void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
    }

    protected void onStop(){
        Log.e(TAG, "onStop");
        super.onStop();
    }

    @TargetApi(9)
    public void surfaceCreated(SurfaceHolder holder){
        Log.e(TAG, "surfaceCreated");
        mCamera = Camera.open(firstmode.cameraID);
        mCamera.startPreview();
        mPreviewRunning = true;
        mCamera.takePicture(null, mPictureCallback, mPictureCallback);
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        Log.e(TAG, "surfaceChanged");

        // XXX stopPreview() will crash if preview is not running
        if (mPreviewRunning){
            mCamera.stopPreview();
        }

        Camera.Parameters p = mCamera.getParameters();

        Camera.Size s = p.getSupportedPreviewSizes().get(0);
        p.setPreviewSize(s.width, s.height);

        if(firstmode.cameraID == 0){
            String stringFlashMode = p.getFlashMode();
            if(stringFlashMode != null)
            if (stringFlashMode.equals(Camera.Parameters.FLASH_MODE_TORCH))
                p.setFlashMode("on"); // Light is set off, flash is set to normal 'on' mode
            else
                p.setFlashMode("torch");
        }

        mCamera.setParameters(p);
        try{
            mCamera.setPreviewDisplay(holder);
        }catch (Exception e){
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.e(TAG, "surfaceDestroyed");
        //mCamera.stopPreview();
        //mPreviewRunning = false;
        //mCamera.release();
    }

    private SurfaceView mSurfaceView;
    private SurfaceHolder mSurfaceHolder;

    public void onClick(View v) {
        // TODO Auto-generated method stub
        mCamera.takePicture(null, mPictureCallback, mPictureCallback);
    }
}
