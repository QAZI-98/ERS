package com.rapidbox.emergencyresponseapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class howitworks extends AppCompatActivity {
    TextToSpeech tts;
    TextView intro;
    TextView step1;
    TextView titleStep1;
    TextView infoStep;

    TextView step2T;
    TextView step2B;

    TextView step3T;
    TextView step3B;

    TextView step4T;
    TextView step4B;

    TextView step5T;
    TextView step5B;
    TextView step6T;
    TextView step6B;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_howitworks);
    intro = (TextView)findViewById(R.id.introTXT);
        step1 = (TextView)findViewById(R.id.step1TXT);
        titleStep1 = (TextView)findViewById(R.id.titleStep1);
        infoStep = (TextView)findViewById(R.id.infoStep1);
        step2T =(TextView)findViewById(R.id.step2Title);
        step2B =(TextView)findViewById(R.id.step2Body);

        step3T =(TextView)findViewById(R.id.step3Title);
        step3B =(TextView)findViewById(R.id.step3Body);

        step4T =(TextView)findViewById(R.id.step4Title);
        step4B =(TextView)findViewById(R.id.step4Body);

        step5T =(TextView)findViewById(R.id.step5Title);
        step5B =(TextView)findViewById(R.id.step5Body);

        step6T =(TextView)findViewById(R.id.step6Title);
        step6B =(TextView)findViewById(R.id.step6Body);


    }
    public void txt(View view){
        String txt = intro.getText().toString();

        texttoSpeech(txt);

    }

    public void txtStep(View view){
        String txt = step1.getText().toString();
        String txt1 = titleStep1.getText().toString();
        String txt2 = infoStep.getText().toString();
        String total = txt+txt1+txt2;
        texttoSpeech(total);

    }
    public void txtStep2(View view){
        String txt = step2T.getText().toString();
        String txt1 = step2B.getText().toString();
        String total = txt+txt1;
        texttoSpeech(total);

    }
    public void txtStep3(View view){
        String txt = step3T.getText().toString();
        String txt1 = step3B.getText().toString();
        String total = txt+txt1;
        texttoSpeech(total);

    }
    public void txtStep4(View view){
        String txt = step4T.getText().toString();
        String txt1 = step4B.getText().toString();
        String total = txt+txt1;
        texttoSpeech(total);

    }
    public void txtStep5(View view){
        String txt = step5T.getText().toString();
        String txt1 = step5B.getText().toString();
        String total = txt+txt1;
        texttoSpeech(total);

    }
    public void txtStep6(View view){
        String txt = step6T.getText().toString();
        String txt1 = step6B.getText().toString();
        String total = txt+txt1;
        texttoSpeech(total);

    }

    public void texttoSpeech(final String txt){
        tts=new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status==TextToSpeech.SUCCESS)
                {
                    int check= tts.setLanguage(Locale.ENGLISH);
                    if (check==TextToSpeech.LANG_MISSING_DATA || check==TextToSpeech.LANG_NOT_SUPPORTED)
                    {
                        Toast.makeText(howitworks.this, "languge not supported", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        float speed=1f;
                        tts.setPitch(1);
                        tts.setPitch(speed);
                        tts.speak(txt,TextToSpeech.QUEUE_FLUSH,null);
                    }
                }
                else{
                    Toast.makeText(howitworks.this, "Text to sppech failed", Toast.LENGTH_SHORT).show();
                }

            }

        });


    }
}
