package app.kannadariddles.com.interfaces;

import app.kannadariddles.com.data.model.KannadaRiddle;

/**
 * Created by varun.am on 28/10/18
 */
public interface AnsweredCallbacks {
    public void answeredCorrect(KannadaRiddle kannadaRiddle, String submittedAnswer);
    public void answeredIncorrect(KannadaRiddle kannadaRiddle, String correctAnswer, String submittedAnswer);
}
