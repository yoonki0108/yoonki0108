package com.example.eyesopen;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class guidebookpage extends AppCompatActivity {

    Button button1;
    Button button3;
    TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guidebookpage);


        button1 = (Button) findViewById(R.id.button1);
        button3 = (Button) findViewById(R.id.button3);




        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(guidebookpage.this,main.class);
                startActivity(i);
            }
        });
    }
}
