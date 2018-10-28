package app.kannadariddles.com.view.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.List;

import app.kannadariddles.com.adapter.ViewPagerAdapter;
import app.kannadariddles.com.data.firebasedatabase.FirebaseDB;
import app.kannadariddles.com.kannadariddles.R;
import app.kannadariddles.com.model.Riddle;
import app.kannadariddles.com.viewmodels.MainViewModel;
import me.kaelaela.verticalviewpager.transforms.DefaultTransformer;

public class MainActivity extends AppCompatActivity {
    
    private static final String TAG = MainActivity.class.getSimpleName();
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
        
        firebaseDB.init();
    
        mainViewModel.riddles.observe(this, new Observer<List<Riddle>>() {
            @Override
            public void onChanged(@Nullable List<Riddle> riddles) {
                if (riddles != null) {
                    Log.e(TAG, "Riddles received: " + riddles.size());
                    riddlesList = riddles;
                    viewpagerAdapter = new ViewPagerAdapter(getApplicationContext(), riddlesList);
                    //setting adapter
                    viewPager.setAdapter(viewpagerAdapter);
                    viewPager.setPageTransformer(false, new DefaultTransformer());
                } else
                    Log.e(TAG, "Riddles received NULL");
            }
        });
    }
    
    private void init() {
        viewPager = findViewById(R.id.main_viewpager_id);
        firebaseDB = new FirebaseDB(this);
        this.mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
    }
}
