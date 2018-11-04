package app.kannadariddles.com.utils;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.widget.Toast;

import java.util.Locale;

import app.kannadariddles.com.kannadariddles.R;

/**
 * Created by varun.am on 04/11/18
 */
public class SpeechUtils {
    
    private static final String TAG = SpeechUtils.class.getSimpleName();
    private static final String LANG_KANNADA = "kn";
    public static final String LANG_KANNADA_IN = "kn_IN";
    private Activity activityContext;
    
    public SpeechUtils(Activity activityContext){
        this.activityContext = activityContext;
    }
    
    public void showAudioInputPromptKannada(int RESULT_KEY) {
        Locale locale = new Locale(LANG_KANNADA);
        Log.e(TAG, "Locale " + locale.getLanguage() + " present: " + isLocalePresent(locale));
        if (isLocalePresent(locale)) {
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, LANG_KANNADA_IN);
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "ನಿಮ್ಮ ಉತ್ತರವನ್ನು ತಿಳಿಸಿ");
            try {
                activityContext.startActivityForResult(intent, RESULT_KEY);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(activityContext, R.string.device_not_supported, Toast.LENGTH_SHORT).show();
            }
        } else
            Toast.makeText(activityContext, "Device not supported or \nLanguage not available", Toast.LENGTH_LONG).show();
    }
    
    private boolean isLocalePresent(Locale newLocale) {
        Locale[] locales = Locale.getAvailableLocales();
        for (Locale locale : locales) {
            //Logger.e(TAG, "Found Locale: " + locale.getDisplayName());
            if (locale.getLanguage().equals(newLocale.getLanguage())) {
                return true;
            }
        }
        return false;
    }
    
}
