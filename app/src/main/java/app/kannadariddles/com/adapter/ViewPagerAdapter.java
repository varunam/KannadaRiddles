package app.kannadariddles.com.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import app.kannadariddles.com.kannadariddles.R;
import app.kannadariddles.com.model.Riddle;

/**
 * Created by varun.am on 23/10/18
 */
public class ViewPagerAdapter extends PagerAdapter {
    
    private static final String TAG = ViewPagerAdapter.class.getSimpleName();
    private Context context;
    private List<Riddle> riddlesList;
    
    public ViewPagerAdapter(Context context, List<Riddle> riddlesList) {
        this.context = context;
        this.riddlesList = riddlesList;
    }
    
    @Override
    public int getCount() {
        return 4;
    }
    
    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == (ConstraintLayout) o;
    }
    
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_riddle, container, false);
        TextView riddleTextView = view.findViewById(R.id.riddle_text_id);
        
        riddleTextView.setText(riddlesList.get(0).getRiddle());
        container.addView(view);
        
        return view;
    }
    
    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ConstraintLayout) object);
    }
}
