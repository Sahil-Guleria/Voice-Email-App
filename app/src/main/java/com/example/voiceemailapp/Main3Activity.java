package com.example.voiceemailapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

public class Main3Activity extends AppCompatActivity {

    private static final int REQUEST_CODE_SPEECH_INPUT = 1000;

     EditText editText;
   // Button btn;

    private SensorManager mSensorManager;

    private float mAbel;
    private float mAbdelCurrent;
    private float mAbdelLast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);


        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Objects.requireNonNull(mSensorManager).registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);

        mAbel = 10f;
        mAbdelCurrent = SensorManager.GRAVITY_EARTH;
        mAbdelLast = SensorManager.GRAVITY_EARTH;

        editText = (EditText) findViewById(R.id.editText);



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_CODE_SPEECH_INPUT: {
                if(resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    editText.setText(result.get(0));
                    String nameable = editText.getText().toString();
                    Intent intent1 = new Intent(this, Main4Activity.class);
                    intent1.putExtra("NAME", nameable);
                    startActivity(intent1);
                }
                break;
            }
        }
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
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Hi speak");
                try {
                    startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT);
                } catch (Exception e) {
                    e.printStackTrace();
                }


                startActivity(intent);





            }
        }




        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };


}

