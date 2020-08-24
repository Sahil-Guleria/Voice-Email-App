package com.example.voiceemailapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Locale;

public class Main6Activity extends AppCompatActivity {

    private TextToSpeech tts;
    private TextView status;
    private TextView To,Subject,Message;
    private int numberOfClicks;
    private boolean IsInitialVoiceFinshed;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main6);

        IsInitialVoiceFinshed = false ;
        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = tts.setLanguage(Locale.US);
                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("TTS", "This Language is not supported");
                    }
                    speak("Welcome to voice mail... This is a login session.. Please enter your name .");

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            IsInitialVoiceFinshed=true;
                        }
                    }, 2000);
                } else {
                    Log.e("TTS", "Initilization Failed!");
                }
            }
        });

        //status = (TextView)findViewById(R.id.status);
        To = (TextView) findViewById(R.id.to);
        // Subject  =(TextView)findViewById(R.id.subject);
        Message = (TextView) findViewById(R.id.message);
        numberOfClicks = 0;
    }

    private void speak(String text){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
        }else{
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        }
    }

    @Override
    public void onDestroy() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }

    public void layoutClicked(View view)
    {
        if(IsInitialVoiceFinshed) {
            numberOfClicks++;
            listen();
        }
    }

    private void listen(){
        Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        i.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say something");

        try {
            startActivityForResult(i, 100);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(Main6Activity.this, "Your device doesn't support Speech Recognition", Toast.LENGTH_SHORT).show();
        }
    }

    private void sendEmail() {
        //Getting content for email
        String email = To.getText().toString().trim();
        // String subject = Subject.getText().toString().trim();
        // String message = Message.getText().toString().trim();

        //Creating SendMail object

    }

    private void exitFromApp()
    {
        this.finishAffinity();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 100&& IsInitialVoiceFinshed){
            IsInitialVoiceFinshed = false;
            if (resultCode == RESULT_OK && null != data) {
                ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                if(result.get(0).equals("cancel"))
                {
                    speak("Cancelled!");
                    exitFromApp();
                }
                else {

                    switch (numberOfClicks) {
                        case 1:
                            String to;
                            to = result.get(0).replaceAll("underscore", "_");
                            to = to.replaceAll("\\s+", "");
                            To.setText(to);
                            if (to.equals("Sahil")) {
                                speak("Hello. Sahil. Welcome.");

                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        Intent intent = new Intent(Main6Activity.this, Main5Activity.class);
                                        startActivity(intent);
                                    }
                                }, 3000);
                            } else
                            {
                                speak("Your identity do not match..  bye .. ..");


                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        exitFromApp();
                                    }
                                }, 4000);
                            }
                            // status.setText("Subject?");
                            // speak("What should be the subject?");

                            break;
                       /* case 1:

                            Subject.setText(result.get(0));
                            status.setText("Message?");
                            speak("\\nSubject : \" + Subject.getText().toString() + \"\\nMess");

                            if (Subject.equals("please"))
                            {
                                speak("hello hello yes yes yees yes yes");
                            }
                           /*  else
                            {
                                status.setText("Restarting");
                                speak("Please Restart the app to reset");
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        exitFromApp();
                                    }
                                }, 4000);

                            } */

                        //  break;
                        /* case 3:
                            Message.setText(result.get(0));
                            status.setText("ur mail");
                            speak("Give ur mail address");
                            break; */


                     /*   default:
                            if(result.get(0).equals("yes"))
                            {
                                status.setText("Sending");
                                speak("Sending the mail");
                                sendEmail();
                            }else
                            {
                                status.setText("Restarting");
                                speak("Please Restart the app to reset");
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        exitFromApp();
                                    }
                                }, 4000);
                            }*/
                    }

                }
            }
            else {
                switch (numberOfClicks) {
                    // case 1:
                    //   speak(" whom you want to send mail?");
                    // break;
                   /* case 1:
                        speak("What should be the subject?");
                        break;
                    case 3:
                        speak("Give me message");
                        break; */

                   /* default:
                        speak("say yes or no");
                        break; */
                }
                numberOfClicks--;
            }
        }
        IsInitialVoiceFinshed=true;
    }
}

