package com.example.adnan.imagefilter;

import android.content.ActivityNotFoundException;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.content.Intent;
import android.graphics.Bitmap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;

public class FilterActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener {
    final static int CAMERA_CAPTURE = 1;
    //keep track of cropping intent
    final int PIC_CROP = 2;
    private Uri picUri;
    ImageView imv,InvertImv,ContrastImv,RoundedCornerImv,SaturationImv,HueFilterImv,GrayScaleImv;
    SeekBar contrastbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
        imv = (ImageView) findViewById(R.id.ResultingImage);
        InvertImv = (ImageView) findViewById(R.id.InvertImv);
        ContrastImv = (ImageView) findViewById(R.id.ContrastImv);
        RoundedCornerImv = (ImageView) findViewById(R.id.RoundedCornerImv);
        SaturationImv = (ImageView) findViewById(R.id.SaturationImv);
        HueFilterImv = (ImageView) findViewById(R.id.HueFilterImv);
        GrayScaleImv = (ImageView) findViewById(R.id.GrayScaleImv);

        contrastbar=(SeekBar)findViewById(R.id.contrastBar);
        contrastbar.setVisibility(View.GONE);
        contrastbar.setMax(50);
        contrastbar.setProgress(25);
        contrastbar.setOnSeekBarChangeListener(this);

        Intent capture_intent = new Intent("android.media.action.IMAGE_CAPTURE");
        startActivityForResult(capture_intent, CAMERA_CAPTURE);

        /*Button invert_button= (Button)findViewById(R.id.Invert_btn);
        invert_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Bitmap bitmap = ((BitmapDrawable) imv.getDrawable()).getBitmap();
                bitmap = Filter.doInvert(bitmap);
                imv.setImageBitmap(bitmap);
            }
        });
        Button contrast_button= (Button)findViewById(R.id.Contrast_btn);
        contrast_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                contrastbar.setVisibility(View.VISIBLE);
            }
        });*/
        InvertImv.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Bitmap bitmap = ((BitmapDrawable) imv.getDrawable()).getBitmap();
                bitmap = Filter.doInvert(bitmap);
                imv.setImageBitmap(bitmap);
            }
        });
        ContrastImv.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                contrastbar.setVisibility(View.VISIBLE);
            }
        });
        RoundedCornerImv.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Bitmap bitmap = ((BitmapDrawable) imv.getDrawable()).getBitmap();
                bitmap = Filter.roundCorner((bitmap), 10);
                imv.setImageBitmap(bitmap);
            }
        });
        SaturationImv.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Bitmap bitmap = ((BitmapDrawable) imv.getDrawable()).getBitmap();
                bitmap = Filter.applySaturationFilter((bitmap),3);
                imv.setImageBitmap(bitmap);
            }
        });
        HueFilterImv.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Bitmap bitmap = ((BitmapDrawable) imv.getDrawable()).getBitmap();
                bitmap = Filter.applyHueFilter((bitmap),3);
                imv.setImageBitmap(bitmap);
            }
        });
        GrayScaleImv.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Bitmap bitmap = ((BitmapDrawable) imv.getDrawable()).getBitmap();
                bitmap = Filter.doGreyscale(bitmap);
                imv.setImageBitmap(bitmap);
            }
        });
    }
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser)
    {
        Bitmap bitmap = ((BitmapDrawable) imv.getDrawable()).getBitmap();
        Log.e("progress","progress="+(progress-25));
        bitmap = Filter.createContrast(bitmap, (progress-25));
        imv.setImageBitmap(bitmap);
    }
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        contrastbar.setVisibility(View.GONE);
    }
    /*protected void onActivityResult(int requestCode, int resultCode, Intent intent)
    {
        super.onActivityResult(requestCode, resultCode, intent);

        if (resultCode == RESULT_OK)
        {
            Bundle extras = intent.getExtras();
            Bitmap bmp = (Bitmap) extras.getParcelable("data");
            imv = (ImageView) findViewById(R.id.ResultingImage);
            imv.setImageBitmap(bmp);
            }
        }
    }*/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if(CAMERA_CAPTURE == requestCode) {
                //get the Uri for the captured image
                picUri = data.getData();
                //carry out the crop operation
                performCrop();
            }
            else if(requestCode == PIC_CROP){
                //get the returned data
                Bundle extras = data.getExtras();
                //get the cropped bitmap
                Bitmap bitmap = extras.getParcelable("data");
                imv.setImageBitmap(bitmap);
                InvertImv.setImageBitmap(Filter.doInvert(bitmap));
                ContrastImv.setImageBitmap(Filter.createContrast((bitmap), 0));
                RoundedCornerImv.setImageBitmap(Filter.roundCorner((bitmap), 10));
                SaturationImv.setImageBitmap(Filter.applySaturationFilter((bitmap), 3));
                HueFilterImv.setImageBitmap(Filter.applyHueFilter((bitmap), 3));
                GrayScaleImv.setImageBitmap(Filter.doGreyscale(bitmap));
            }
        }
    }
    private void performCrop(){
        try {
            //call the standard crop action intent (the user device may not support it)
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            //indicate image type and Uri
            cropIntent.setDataAndType(picUri, "image/*");
            //set crop properties
            cropIntent.putExtra("crop", "true");
            //indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            //indicate output X and Y
            cropIntent.putExtra("outputX", 256);
            cropIntent.putExtra("outputY", 256);
            //retrieve data on return
            cropIntent.putExtra("return-data", true);
            //start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, PIC_CROP);
        }
        catch(ActivityNotFoundException anfe){
            //display an error message
            String errorMessage = "Whoops - your device doesn't support the crop action!";
            //Snackbar.make(this, errorMessage, Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }
    }
}
