package app.kannadariddles.com.view.activities;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import app.kannadariddles.com.kannadariddles.R;
import app.kannadariddles.com.view.adapter.ViewPagerAdapter;
import me.kaelaela.verticalviewpager.transforms.DefaultTransformer;

public class MainActivity extends AppCompatActivity {
    
    private ViewPagerAdapter viewpagerAdapter;
    private ViewPager viewPager;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        viewpagerAdapter = new ViewPagerAdapter(getApplicationContext());
        viewPager = findViewById(R.id.main_viewpager_id);
        viewPager.setAdapter(viewpagerAdapter);
        viewPager.setPageTransformer(false, new DefaultTransformer());
        
    }
}
