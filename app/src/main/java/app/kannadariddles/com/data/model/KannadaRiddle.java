package app.kannadariddles.com.data.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by varun.am on 04/11/18
 */
@Entity(tableName = "KannadaRiddles")
public class KannadaRiddle {
    
    private static final int scoreWithoutClue = 10;
    private static final int scoreWithClue = 5;
    private static final int answeredWrong = 0;
    
    @PrimaryKey(autoGenerate = true)
    private int id;
    
    private String riddle;
    private String clue;
    private String answer;
    private boolean clueChecked;
    private boolean answeredCorrect;
    private int score;
    
    public KannadaRiddle(int id, String riddle, String clue, String answer, boolean clueChecked, boolean answeredCorrect) {
        this.id = id;
        this.riddle = riddle;
        this.clue = clue;
        this.answer = answer;
        this.clueChecked = clueChecked;
        this.answeredCorrect = answeredCorrect;
        if (!answeredCorrect)
            this.score = answeredWrong;
        else if (!clueChecked)
            this.score = scoreWithoutClue;
        else this.score = scoreWithClue;
        
    }
    
    @Ignore
    public KannadaRiddle(String riddle, String clue, String answer, boolean clueChecked, boolean answeredCorrect) {
        this.riddle = riddle;
        this.clue = clue;
        this.answer = answer;
        this.clueChecked = clueChecked;
        this.answeredCorrect = answeredCorrect;
        if (!answeredCorrect)
            this.score = answeredWrong;
        else if (!clueChecked)
            this.score = scoreWithoutClue;
        else this.score = scoreWithClue;
    }
    
    public String getRiddle() {
        return riddle;
    }
    
    public int getId() {
        return id;
    }
    
    public String getClue() {
        return clue;
    }
    
    public String getAnswer() {
        return answer;
    }
    
    public boolean isClueChecked() {
        return clueChecked;
    }
    
    public boolean isAnsweredCorrect() {
        return answeredCorrect;
    }
    
    public int getScore() {
        return score;
    }
    
    public void setScore(int score) {
        this.score = score;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public void setRiddle(String riddle) {
        this.riddle = riddle;
    }
    
    public void setClue(String clue) {
        this.clue = clue;
    }
    
    public void setAnswer(String answer) {
        this.answer = answer;
    }
    
    public void setClueChecked(boolean clueChecked) {
        this.clueChecked = clueChecked;
        if (answeredCorrect && clueChecked)
            this.score = scoreWithClue;
        else if (answeredCorrect)
            this.score = scoreWithoutClue;
    }
    
    public void setAnsweredCorrect(boolean answeredCorrect) {
        this.answeredCorrect = answeredCorrect;
        if (clueChecked)
            this.score = scoreWithClue;
        else
            this.score = scoreWithoutClue;
    }
}
