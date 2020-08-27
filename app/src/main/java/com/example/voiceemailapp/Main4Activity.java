package com.example.voiceemailapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class Main4Activity extends AppCompatActivity {

    TextToSpeech textToSpeech;
    TextView tv;

    private static final int REQUEST_CODE_SPEECH_INPUT = 1000;
    //Declaring EditText
   // private EditText editTextEmail;
    // private EditText editTextSubject;
    private EditText editTextMessage;
    private EditText editRes;

    //Send button
  //  private Button buttonSend;

    private SensorManager mSensorManager;

    private float mAbel;
    private float mAbdelCurrent;
    private float mAbdelLast;
    private int numberOfClicks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);

       // email = findViewById(R.id.textView);

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
                        Toast.makeText(Main4Activity.this, "this language is not supported", Toast.LENGTH_SHORT).show();

                    }
                    else {

                        speak("Tell me the message . please");

                    }
                }
            }


        });

        editTextMessage = (EditText) findViewById(R.id.editTextMessage);

      //  editRes = (EditText) findViewById(R.id.editRes);

        tv = (TextView) findViewById(R.id.textView);



    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_CODE_SPEECH_INPUT: {
                if(resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    editTextMessage.setText(result.get(0));



                    tv.setText(getIntent().getStringExtra("NAME").replaceAll("underscore", "_"));
                    tv.setText(getIntent().getStringExtra("NAME").replaceAll("\\s+","")+"@gmail.com");
                final String email = tv.getText().toString().trim();
			// .replaceAll("\\s+","");
                    //email.setText(getIntent().getStringExtra("NAME") + "face31@gmail.com");
                    // String subject = editTextSubject.getText().toString().trim();
                   // speak("Tell me the message . please");
                    final String message = editTextMessage.getText().toString().trim();
                    //editRes.setText(result.get(0));
                  //  String res = editRes.getText().toString().trim();




                    speak("Please Confirm the mail To : " + tv.getText().toString() + "\nMessage : " + editTextMessage.getText().toString() +"\nyour mail "+Utils.EMAIL+"\nyour password" +Utils.PASSWORD + "\n your mail is send .. thankyou for using this app.");





                    //Creating SendMail object
                    if(true) {
                      //  speak("do you want to send...");

                        // my response

                        // my response

                            /*Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
                            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Hi speak");
                            try {
                                startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT);
                            } catch (Exception e) {
                                e.printStackTrace();
                            } */
                     /*   new Timer().schedule(new TimerTask() {
                            @Override
                            public void run() {
                                boolean running = false;
                                if (!running) {
                                   speak("your mail is sent.");
                                }
                            }
                            },4000); */


                                SendMail sm = new SendMail(this, email, message);
                                sm.execute();
                        new Timer().schedule(new TimerTask() {
                            @Override
                            public void run() {
                                boolean running = false;
                                if (!running) {

                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            exitFromApp();
                                        }
                                    }, 4000);
                                }
                            }
                        },25000);




                    }
                    //Executing sendmail to send email


                }
                break;
            }
        }
    }
    private void exitFromApp()
    {
        this.finishAffinity();
    }
    private void speak(String text) {

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
        else
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }




    /* private void mail() {
        String email = editTextEmail.getText().toString().trim();
        //  String subject = editTextSubject.getText().toString().trim();
        String message = editTextMessage.getText().toString().trim();

        //Creating SendMail object
        SendMail sm = new SendMail(MainActivity.this, email, message);

        //Executing sendmail to send email
        sm.execute();
    } */

  /*  private void sendEmail() {
        //Getting content for email
        String email = editTextEmail.getText().toString().trim();
        String subject = editTextSubject.getText().toString().trim();
        String message = editTextMessage.getText().toString().trim();

        //Creating SendMail object
        SendMail sm = new SendMail(this, email, subject, message);

        //Executing sendmail to send email
        sm.execute();
    } */

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

