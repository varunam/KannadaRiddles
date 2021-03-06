package app.kannadariddles.com.view.activities;

import android.appwidget.AppWidgetManager;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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

import app.kannadariddles.com.services.RiddlesLoadingScheduler;
import app.kannadariddles.com.widget.RiddlesWidget;
import app.kannadariddles.com.adapter.ViewPagerAdapter;
import app.kannadariddles.com.data.KannadaRiddlesDatabase;
import app.kannadariddles.com.data.model.KannadaRiddle;
import app.kannadariddles.com.executors.AppExecutors;
import app.kannadariddles.com.interfaces.AnsweredCallbacks;
import app.kannadariddles.com.interfaces.VoiceInputClickCallbacks;
import app.kannadariddles.com.kannadariddles.R;
import app.kannadariddles.com.utils.RiddlesTracker;
import app.kannadariddles.com.utils.SpeechUtils;
import app.kannadariddles.com.viewmodels.MainViewModel;
import me.kaelaela.verticalviewpager.transforms.DefaultTransformer;

public class MainActivity extends AppCompatActivity implements AnsweredCallbacks, RiddlesLoadedCallbacks, VoiceInputClickCallbacks, ViewPager.OnPageChangeListener {
    
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int REQ_CODE_K2E_SPEECH_INPUT = 1001;
    private static final int NEXT_PAGE_INTERVAL = 500;
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
    
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        
        init();
        
        riddlesProvider = new RiddlesProvider();
        riddlesProvider.loadRiddles(this);
    }
    
    /**
     * initializations
     */
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
        RiddlesLoadingScheduler.scheduleRiddlesLoading(this);
    }
    
    /**
     * show loader and indicate user that the app is waiting for some task to be finished
     */
    private void showLoader() {
        loader.setVisibility(View.VISIBLE);
        loaderText.setVisibility(View.VISIBLE);
    }
    
    /**
     * hide loader once required task is completed
     */
    private void hideLoader() {
        loader.setVisibility(View.GONE);
        loaderText.setVisibility(View.GONE);
    }
    
    /**
     * to change main layout color
     * @param colorId - color to which main layout should change to
     */
    private void setMainLayoutColor(int colorId) {
        mainLayout.setBackgroundColor(colorId);
    }
    
    @Override
    public void answeredCorrect(final KannadaRiddle kannadaRiddle, String submittedAnswer) {
        AppExecutors.getInstance(getApplicationContext()).diskIO().execute(new Runnable() {
            @Override
            public void run() {
                kannadaRiddlesDatabase.kannadaRiddlesDao().updateKannadaRiddle(kannadaRiddle);
            }
        });
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
    
    /**
     * loading riddles to local database
     * @param riddles - riddles to be loaded to local database
     */
    private void loadRiddlesToDb(final List<Riddle> riddles) {
        for (int i = 0; i < riddles.size(); i++) {
            final int finalI = i;
            AppExecutors.getInstance(getApplicationContext()).diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    if (kannadaRiddlesDatabase.kannadaRiddlesDao().hasRiddle(riddles.get(finalI).getRiddle()).getCount() == 0) {
                        KannadaRiddle kannadaRiddle = new KannadaRiddle(
                                riddles.get(finalI).getRiddle(),
                                riddles.get(finalI).getClues(),
                                riddles.get(finalI).getAnswer(),
                                false,
                                false
                        );
                        kannadaRiddlesDatabase.kannadaRiddlesDao().insertRiddle(kannadaRiddle);
                    } else {
                        Log.d(TAG, "Riddle exists in table. Skipping...");
                    }
                }
            });
        }
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
        MainViewModel mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        mainViewModel.getRiddles().observe(this, new Observer<List<KannadaRiddle>>() {
            @Override
            public void onChanged(@Nullable List<KannadaRiddle> kannadaRiddles) {
                riddlesList = kannadaRiddles;
                viewpagerAdapter.setRiddlesList(riddlesList);
                Log.e(TAG,"setRiddlesList");
                viewPager.setCurrentItem(new RiddlesTracker().getRiddlesIndex(getApplicationContext()), true);
                Log.e(TAG,"setCurrentItem " + new RiddlesTracker().getRiddlesIndex(getApplicationContext()));
            }
        });
    }
    
    @Override
    protected void onStop() {
        super.onStop();
        
        int[] ids = AppWidgetManager.getInstance(getApplication()).getAppWidgetIds(new ComponentName(getApplication(), RiddlesWidget.class));
        new RiddlesWidget().onUpdate(getApplicationContext(), AppWidgetManager.getInstance(getApplicationContext()), ids);
    }
    
    @Override
    public void onPageScrolled(int i, float v, int i1) {
    }
    
    @Override
    public void onPageSelected(int i) {
        new RiddlesTracker().setRiddlesIndex(getApplicationContext(), i);
        new RiddlesTracker().setLastRiddleAttended(getApplicationContext(), riddlesList.get(i));
    }
    
    @Override
    public void onPageScrollStateChanged(int i) {
    }
}
