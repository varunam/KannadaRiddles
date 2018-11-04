package app.kannadariddles.com.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by varun.am on 04/11/18
 */
public class RiddlesTracker {
    
    private static final String TAG = RiddlesTracker.class.getSimpleName();
    private static final String PREF_RIDDLES_INDEX = "pref_riddles_index";
    private static final String INDEX_KEY = "index_kay";
    private static final int DEFAULT_INDEX = 0;
    private SharedPreferences riddlesPreferences;
    
    public void setRiddlesIndex(Context context, int index) {
        riddlesPreferences = context.getSharedPreferences(PREF_RIDDLES_INDEX, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = riddlesPreferences.edit();
        Log.e(TAG,"Storing riddles index: " + index);
        editor.putInt(INDEX_KEY, index);
        editor.apply();
    }
    
    
    public int getRiddlesIndex(Context context) {
        riddlesPreferences = context.getSharedPreferences(PREF_RIDDLES_INDEX, Context.MODE_PRIVATE);
        int index = riddlesPreferences.getInt(INDEX_KEY, DEFAULT_INDEX);
        Log.e(TAG,"getting riddles index: " + index);
        return index;
    }
    
}
