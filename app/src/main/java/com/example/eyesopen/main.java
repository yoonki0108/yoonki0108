package com.example.eyesopen;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.Locale;

public class main extends AppCompatActivity {
    Button firstmodebutton;
    Button secondmodebutton;
    ImageView imageView9;
    Button button10;
    TextToSpeech tts;
    ConstraintLayout mainlayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ConstraintLayout mainlayout = (ConstraintLayout) findViewById(R.id.mainlayout);
        firstmodebutton = (Button) findViewById(R.id.firstmodebutton);
        secondmodebutton = (Button) findViewById(R.id.secondmodebutton);
        imageView9 = (ImageView) findViewById(R.id.imageView9);
        button10 = (Button) findViewById((R.id.button10));
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
        mainlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stt t = new stt();
                final Handler handler = new Handler(){
                    @Override
                    public void handleMessage(@NonNull Message msg) {
                        super.handleMessage(msg);
                        for(int i=0;i<t.matches.size();i++){
                            if(t.matches.get(i).equals("거리")){
                                Intent j = new Intent(main.this,firstmode.class);
                                startActivity(j);
                            }
                            else if (t.matches.get(i).equals("읽기")){
                                Intent k = new Intent(main.this, secondmode.class);
                                startActivity(k);
                            }
                            else if(t.matches.get(i).equals("가이드북")){
                                CharSequence text = "\"Eyes Open\"의 사용법을 알려드리겠습니다이 앱에는 모드는 두가지가 있습니다. 길거리 모드와 글씨 읽기 모드가 있습니다. '길거리 모드'는 길을 걸어 다닐 때 보이는 표지판을 알려주는 모드이고 '글씨 읽기 모드'는 사용자님이 앱에서 촬영한 사진에 있는 글씨를 읽어주는 모드입니다. 촬영을 원하시면 촬영이라고 말하시면 촬영이 됩니다. 모드를 변경을 원하시면 '모드 변경'이라고 말하신 후 원하시는 모드 이름을 말하시면 됩니다.\n";
                                tts.setPitch((float) 1.0); // Sets the speech pitch for the TextToSpeech engine.
                                tts.setSpeechRate((float) 1.0); // Sets the speech rate.

                                tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "uid");
                            }
                            Toast.makeText(main.this, t.matches.get(i), Toast.LENGTH_SHORT).show();
                        }
                    }

                };

                t.callstt(main.this, handler);
            }
        });


        firstmodebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(main.this, firstmode.class);
                startActivity(i);
            }
        });

        secondmodebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(main.this, secondmode.class);
                startActivity(i);
            }
        });



        imageView9.setOnClickListener(new View.OnClickListener() {
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

