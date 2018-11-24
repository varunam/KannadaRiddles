package app.kannadariddles.com.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import app.kannadariddles.com.data.model.KannadaRiddle;

/**
 * Created by varun.am on 04/11/18
 */
public class RiddlesTracker {
    
    private static final String TAG = RiddlesTracker.class.getSimpleName();
    private static final String PREF_RIDDLES = "pref_riddles_index";
    private static final String INDEX_KEY = "index_kay";
    private static final String RIDDLE_KEY = "riddle_key";
    private static final String DEFAULT_RIDDLE = "Loading Riddle...";
    private static final int DEFAULT_INDEX = 0;
    private SharedPreferences riddlesPreferences;
    
    public void setRiddlesIndex(Context context, int index) {
        riddlesPreferences = context.getSharedPreferences(PREF_RIDDLES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = riddlesPreferences.edit();
        Log.e(TAG, "Storing riddles index: " + index);
        editor.putInt(INDEX_KEY, index);
        editor.apply();
    }
    
    
    public int getRiddlesIndex(Context context) {
        riddlesPreferences = context.getSharedPreferences(PREF_RIDDLES, Context.MODE_PRIVATE);
        int index = riddlesPreferences.getInt(INDEX_KEY, DEFAULT_INDEX);
        Log.e(TAG, "getting riddles index: " + index);
        return index;
    }
    
    public String getLastRiddleAttended(Context context) {
        riddlesPreferences = context.getSharedPreferences(PREF_RIDDLES, Context.MODE_PRIVATE);
        String riddle = riddlesPreferences.getString(RIDDLE_KEY, DEFAULT_RIDDLE);
        Log.e(TAG, "getting latest riddle: " + riddle);
        return riddle;
    }
    
    public void setLastRiddleAttended(Context context, KannadaRiddle riddle) {
        riddlesPreferences = context.getSharedPreferences(PREF_RIDDLES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = riddlesPreferences.edit();
        Log.e(TAG, "Storing Last attended riddle: " + riddle.getRiddle());
        editor.putString(RIDDLE_KEY, riddle.getRiddle());
        editor.apply();
    }
    
}
