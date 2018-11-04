package app.kannadariddles.com.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.FirebaseDatabase;
import com.lib.riddlesprovider.RiddlesLoadedCallbacks;
import com.lib.riddlesprovider.RiddlesProvider;
import com.lib.riddlesprovider.model.Riddle;

import java.util.ArrayList;
import java.util.List;

import app.kannadariddles.com.adapter.ViewPagerAdapter;
import app.kannadariddles.com.interfaces.AnsweredCallbacks;
import app.kannadariddles.com.interfaces.VoiceInputClickCallbacks;
import app.kannadariddles.com.kannadariddles.R;
import app.kannadariddles.com.utils.SpeechUtils;
import me.kaelaela.verticalviewpager.transforms.DefaultTransformer;

public class MainActivity extends AppCompatActivity implements AnsweredCallbacks, RiddlesLoadedCallbacks, VoiceInputClickCallbacks {
    
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int REQ_CODE_K2E_SPEECH_INPUT = 1001;
    private static final int NEXT_PAGE_INTERVAL = 2000;
    private ViewPagerAdapter viewpagerAdapter;
    private ViewPager viewPager;
    private List<Riddle> riddlesList;
    private ImageView loader;
    private TextView loaderText;
    private RelativeLayout mainLayout;
    private String expectedAnswer;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        init();
        
        new RiddlesProvider(this).init();
    }
    
    private void init() {
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        viewPager = findViewById(R.id.main_viewpager_id);
        loader = findViewById(R.id.loader_id);
        mainLayout = findViewById(R.id.main_layout_id);
        loaderText = findViewById(R.id.loader_text_id);
        Glide.with(getApplicationContext())
                .load(R.drawable.ic_loader)
                .into(loader);
        showLoader();
    }
    
    private void showLoader() {
        loader.setVisibility(View.VISIBLE);
        loaderText.setVisibility(View.VISIBLE);
    }
    
    private void hideLoader() {
        loader.setVisibility(View.GONE);
        loaderText.setVisibility(View.GONE);
    }
    
    private void setMainLayoutColor(int colorId) {
        mainLayout.setBackgroundColor(colorId);
    }
    
    @Override
    public void answeredCorrect(String submittedAnswer) {
        Toast.makeText(getApplicationContext(), getResources().getString(R.string.correct_answer), Toast.LENGTH_LONG).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                viewPager.setCurrentItem(viewPager.getCurrentItem() + 1, true);
            }
        }, NEXT_PAGE_INTERVAL);
    }
    
    @Override
    public void answeredIncorrect(String correctAnswer, String submittedAnswer) {
        Toast.makeText(getApplicationContext(), getResources().getString(R.string.wrong_answer), Toast.LENGTH_LONG).show();
        
    }
    
    @Override
    public void onRiddlesLoaded(List<Riddle> riddles) {
        if (riddles != null) {
            hideLoader();
            setMainLayoutColor(getResources().getColor(R.color.colorPrimaryDark));
            Log.e(TAG, "Riddles received: " + riddles.size());
            riddlesList = riddles;
            viewpagerAdapter = new ViewPagerAdapter(MainActivity.this, riddlesList, MainActivity.this, MainActivity.this);
            //setting adapter
            viewPager.setAdapter(viewpagerAdapter);
            viewPager.setPageTransformer(false, new DefaultTransformer());
        } else
            Log.e(TAG, "Riddles received NULL");
    }
    
    @Override
    public void onVoiceInputClicked(String expectedAnswer) {
        this.expectedAnswer = expectedAnswer;
        new SpeechUtils(this).showAudioInputPromptKannada(REQ_CODE_K2E_SPEECH_INPUT);
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        switch (requestCode) {
            case REQ_CODE_K2E_SPEECH_INPUT:
                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    if (expectedAnswer.replaceAll(" ", "").equals(result.get(0).replaceAll(" ", ""))) {
                        answeredCorrect(result.get(0));
                    } else
                        answeredIncorrect(expectedAnswer, result.get(0));
                }
                break;
            default:
                break;
        }
    }
}
