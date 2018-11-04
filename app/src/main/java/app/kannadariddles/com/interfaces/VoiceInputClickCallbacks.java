package app.kannadariddles.com.interfaces;

import app.kannadariddles.com.data.model.KannadaRiddle;

/**
 * Created by varun.am on 04/11/18
 */
public interface VoiceInputClickCallbacks {
    void onVoiceInputClicked(KannadaRiddle kannadaRiddle, String expectedAnswer);
}
