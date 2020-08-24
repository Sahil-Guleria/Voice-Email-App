
package com.example.voiceemailapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Locale;
import java.util.Objects;

public class Main2Activity extends AppCompatActivity {

    Button btnSpeak;
    EditText edtSpeak;

    TextToSpeech textToSpeech;

    private SensorManager mSensorManager;

    private float mAbel;
    private float mAbdelCurrent;
    private float mAbdelLast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Objects.requireNonNull(mSensorManager).registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);

        mAbel = 10f;
        mAbdelCurrent = SensorManager.GRAVITY_EARTH;
        mAbdelLast = SensorManager.GRAVITY_EARTH;

       textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status == TextToSpeech.SUCCESS) {
                    int result = textToSpeech.setLanguage(Locale.US);
                    if(result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Toast.makeText(Main2Activity.this, "this language is not supported", Toast.LENGTH_SHORT).show();

                    }
                    else {
                        textToSpeech.setSpeechRate(1.0f);
                        speak();
                    }
                }
            }


        });




    }
     private void speak() {
        String text = "Hello .. Welcome to Voice Mail Bot.. this is your personl AI bot.. please shake your phone to move forward.";
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
        else
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }

    private final SensorEventListener mSensorListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];
            mAbdelLast = mAbdelCurrent;
            mAbdelCurrent = (float) Math.sqrt((double) (x * x + y * y + z * z));
            float delta = mAbdelCurrent - mAbdelLast;
            mAbel = mAbel * 0.9f + delta;
            if (mAbel > 12) {
                String text = "Enter the mail  .. whom you want to send";
               if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                    textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
                else
                    textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);


                    Intent intent = new Intent(Main2Activity.this, Main3Activity.class);
                    startActivity(intent);


            }


        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };

    @Override
    protected void onResume() {
        mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
        SensorManager.SENSOR_DELAY_NORMAL);
        super.onResume();
    }

    @Override
    protected void onPause() {
        mSensorManager.unregisterListener(mSensorListener);
        super.onPause();
    }



  /*  @Override
    protected void onDestroy() {
        if(textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onDestroy();
    } */
}
