package greenpashtet.com.langmaestro;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

@SuppressWarnings("FieldCanBeLocal")
public class MainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {
    private SecureRandom rand = new SecureRandom();
    private TextToSpeech tts;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    private String word;
    private boolean res = false;
    private boolean launch = false;

    private TextView resultText;
    private ArrayList<String> result;

    private final String[] words = {
            "Uganda", "Sponge", "Spoon", "Square", "Milk", "Cow", "Bottle", "Bear", "Cat",
            "Car", "Socket", "Football", "Table", "Teacher", "People", "Male", "Female",
            "Mail", "Glass", "Air", "Water", "Fire", "Finger", "Way", "Can", "Four", "August",
            "Apple", "Pineapple", "Under", "Chair", "Dog", "Aye", "Say", "Talk", "Think",
            "Police", "Metro", "Princess", "Number", "Word", "Science", "Clock", "Rocket",
            "Monkey", "Pig", "Man", "Lamp", "Squirrel", "Pen", "Carpet", "Floor", "Window",
            "Member", "Tea", "Electricity", "Coffee", "Bad", "Bread", "Fantasy", "Fairy-tail",
            "Train", "Plane", "Work", "Lip", "Foot", "Food", "Flood", "Thunderstorm", "Wave",
            "Wolf", "Tiger", "Bandit", "Kidnap", "Program", "Warrior", "Archer", "War", "Peace",
            "Bank"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        resultText = findViewById(R.id.resultText);
        tts = new TextToSpeech(this, this);
    }

    private void ttsSay(String text) {
        HashMap<String, String> myHashAlarm = new HashMap<>();
        myHashAlarm.put(TextToSpeech.Engine.KEY_PARAM_STREAM, String.valueOf(AudioManager.STREAM_ALARM));
        myHashAlarm.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "READY");
        tts.setLanguage(Locale.ENGLISH);
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }

    @SuppressWarnings("SameParameterValue")
    private void sttInput(String text) {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.ENGLISH);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, text);
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException ignored) {
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {
                    result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    for (String s : result) {
                        if (s.equals(word)) {
                            res = true;
                            resultText.setText("Слово \"" + word + "\" произнесено правильно!");
                        }
                    }
                    if (!res) {
                        resultText.setText("Ваше произношение не верно! Попробуйте снова.");
                    }
                }
                break;
            }
        }
        launch = false;
    }

    @Override
    public void onInit(int i) {
    }

    public void audioBtnOnClick(View view) {
        if (launch) return;
        res = false;
        launch = true;
        word = words[rand.nextInt(words.length)];
        ttsSay(word);
        try {
            Thread.sleep(1200);
            sttInput("Скажите то, что услышали\nПодсказка: \"" + word + "\"" );
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
