package app.kannadariddles.com.view.activities;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.FirebaseDatabase;
import com.lib.riddlesprovider.RiddlesLoadedCallbacks;
import com.lib.riddlesprovider.RiddlesProvider;
import com.lib.riddlesprovider.model.Riddle;

import java.util.List;

import app.kannadariddles.com.adapter.ViewPagerAdapter;
import app.kannadariddles.com.data.firebasedatabase.FirebaseDB;
import app.kannadariddles.com.interfaces.AnsweredCallbacks;
import app.kannadariddles.com.kannadariddles.R;
import app.kannadariddles.com.viewmodels.MainViewModel;
import me.kaelaela.verticalviewpager.transforms.DefaultTransformer;

public class MainActivity extends AppCompatActivity implements AnsweredCallbacks, RiddlesLoadedCallbacks {
    
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int NEXT_PAGE_INTERVAL = 2000;
    private ViewPagerAdapter viewpagerAdapter;
    private ViewPager viewPager;
    private FirebaseDB firebaseDB;
    private MainViewModel mainViewModel;
    private List<Riddle> riddlesList;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        init();
        
        //firebaseDB.init();
        
        /*mainViewModel.riddles.observe(this, new Observer<List<Riddle>>() {
            @Override
            public void onChanged(@Nullable List<Riddle> riddles) {
                if (riddles != null) {
                    Log.e(TAG, "Riddles received: " + riddles.size());
                    riddlesList = riddles;
                    viewpagerAdapter = new ViewPagerAdapter(MainActivity.this, riddlesList, MainActivity.this);
                    //setting adapter
                    viewPager.setAdapter(viewpagerAdapter);
                    viewPager.setPageTransformer(false, new DefaultTransformer());
                } else
                    Log.e(TAG, "Riddles received NULL");
            }
        });*/
        
        new RiddlesProvider(this).init();
    }
    
    private void init() {
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        viewPager = findViewById(R.id.main_viewpager_id);
        this.mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
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
            Log.e(TAG, "Riddles received: " + riddles.size());
            riddlesList = riddles;
            viewpagerAdapter = new ViewPagerAdapter(MainActivity.this, riddlesList, MainActivity.this);
            //setting adapter
            viewPager.setAdapter(viewpagerAdapter);
            viewPager.setPageTransformer(false, new DefaultTransformer());
        } else
            Log.e(TAG, "Riddles received NULL");
    }
}
