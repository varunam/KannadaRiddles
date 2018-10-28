package app.kannadariddles.com.interfaces;

/**
 * Created by varun.am on 28/10/18
 */
public interface AnsweredCallbacks {
    public void answeredCorrect(String submittedAnswer);
    public void answeredIncorrect(String correctAnswer, String submittedAnswer);
}
