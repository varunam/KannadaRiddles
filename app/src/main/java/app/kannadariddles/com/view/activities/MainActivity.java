package app.kannadariddles.com.view.activities;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
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
import com.lib.riddlesprovider.RiddlesLoadedCallbacks;
import com.lib.riddlesprovider.RiddlesProvider;
import com.lib.riddlesprovider.model.Riddle;

import java.util.ArrayList;
import java.util.List;

import app.kannadariddles.RiddlesWidget;
import app.kannadariddles.com.adapter.ViewPagerAdapter;
import app.kannadariddles.com.data.KannadaRiddlesDatabase;
import app.kannadariddles.com.data.model.KannadaRiddle;
import app.kannadariddles.com.interfaces.AnsweredCallbacks;
import app.kannadariddles.com.interfaces.VoiceInputClickCallbacks;
import app.kannadariddles.com.kannadariddles.R;
import app.kannadariddles.com.utils.RiddlesTracker;
import app.kannadariddles.com.utils.SpeechUtils;
import me.kaelaela.verticalviewpager.transforms.DefaultTransformer;

public class MainActivity extends AppCompatActivity implements AnsweredCallbacks, RiddlesLoadedCallbacks, VoiceInputClickCallbacks, ViewPager.OnPageChangeListener {
    
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int REQ_CODE_K2E_SPEECH_INPUT = 1001;
    private static final int NEXT_PAGE_INTERVAL = 2000;
    private ViewPagerAdapter viewpagerAdapter;
    private ViewPager viewPager;
    private List<KannadaRiddle> riddlesList;
    private ImageView loader;
    private TextView loaderText;
    private RelativeLayout mainLayout;
    
    private String expectedAnswer;
    private KannadaRiddle currentRiddle;
    private RiddlesProvider riddlesProvider;
    
    private KannadaRiddlesDatabase kannadaRiddlesDatabase;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        init();
        
        riddlesProvider = new RiddlesProvider();
        riddlesProvider.loadRiddles(this);
    }
    
    private void init() {
        kannadaRiddlesDatabase = KannadaRiddlesDatabase.getInstance(this);
        viewPager = findViewById(R.id.main_viewpager_id);
        loader = findViewById(R.id.loader_id);
        mainLayout = findViewById(R.id.main_layout_id);
        loaderText = findViewById(R.id.loader_text_id);
        Glide.with(getApplicationContext())
                .load(R.drawable.ic_loader)
                .into(loader);
        
        viewpagerAdapter = new ViewPagerAdapter(MainActivity.this, MainActivity.this, MainActivity.this);
        viewPager.setPageTransformer(false, new DefaultTransformer());
        viewPager.setAdapter(viewpagerAdapter);
        viewPager.setOnPageChangeListener(this);
        
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
    public void answeredCorrect(KannadaRiddle kannadaRiddle, String submittedAnswer) {
        kannadaRiddlesDatabase.kannadaRiddlesDao().updateKannadaRiddle(kannadaRiddle);
        Toast.makeText(getApplicationContext(), getResources().getString(R.string.correct_answer), Toast.LENGTH_LONG).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                viewPager.setCurrentItem(viewPager.getCurrentItem() + 1, true);
            }
        }, NEXT_PAGE_INTERVAL);
    }
    
    @Override
    public void answeredIncorrect(KannadaRiddle kannadaRiddle, String correctAnswer, String submittedAnswer) {
        Toast.makeText(getApplicationContext(), getResources().getString(R.string.wrong_answer), Toast.LENGTH_LONG).show();
        
    }
    
    @Override
    public void onRiddlesLoaded(List<Riddle> riddles) {
        if (riddles != null) {
            hideLoader();
            setMainLayoutColor(getResources().getColor(R.color.colorPrimaryDark));
            Log.e(TAG, "Riddles received: " + riddles.size());
            
            loadRiddlesToDb(riddles);
            
        } else
            Log.e(TAG, "Riddles received NULL");
    }
    
    private void loadRiddlesToDb(List<Riddle> riddles) {
        for (int i = 0; i < riddles.size(); i++) {
            if (kannadaRiddlesDatabase.kannadaRiddlesDao().hasRiddle(riddles.get(i).getRiddle()).getCount() == 0) {
                KannadaRiddle kannadaRiddle = new KannadaRiddle(
                        riddles.get(i).getRiddle(),
                        riddles.get(i).getClues(),
                        riddles.get(i).getAnswer(),
                        false,
                        false
                );
                kannadaRiddlesDatabase.kannadaRiddlesDao().insertRiddle(kannadaRiddle);
            } else {
                Log.d(TAG, "Riddle exists in table. Skipping...");
            }
        }
        if (riddlesList.size() < 1)
            onResume();
    }
    
    @Override
    public void onVoiceInputClicked(KannadaRiddle kannadaRiddle, String expectedAnswer) {
        this.currentRiddle = kannadaRiddle;
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
                        currentRiddle.setAnsweredCorrect(true);
                        answeredCorrect(currentRiddle, result.get(0));
                    } else {
                        currentRiddle.setAnsweredCorrect(false);
                        answeredIncorrect(currentRiddle, expectedAnswer, result.get(0));
                    }
                }
                break;
            default:
                break;
        }
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        riddlesList = kannadaRiddlesDatabase.kannadaRiddlesDao().loadAllKannadaRiddles();
        viewpagerAdapter.setRiddlesList(riddlesList);
        viewPager.setCurrentItem(new RiddlesTracker().getRiddlesIndex(getApplicationContext()), true);
    }
    
    @Override
    protected void onStop() {
        super.onStop();
        
        int[] ids = AppWidgetManager.getInstance(getApplication()).getAppWidgetIds(new ComponentName(getApplication(), RiddlesWidget.class));
        new RiddlesWidget().onUpdate(getApplicationContext(), AppWidgetManager.getInstance(getApplicationContext()), ids);
    }
    
    @Override
    public void onPageScrolled(int i, float v, int i1) {
        new RiddlesTracker().setRiddlesIndex(getApplicationContext(), i);
        new RiddlesTracker().setLastRiddleAttended(getApplicationContext(), riddlesList.get(i));
    }
    
    @Override
    public void onPageSelected(int i) {
    }
    
    @Override
    public void onPageScrollStateChanged(int i) {
    }
}
