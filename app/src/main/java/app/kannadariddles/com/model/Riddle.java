package app.kannadariddles.com.model;

/**
 * Created by varun.am on 28/10/18
 */
public class Riddle {
    
    private String riddle;
    private String clues;
    private String answer;
    
    public Riddle(){
    
    }
    
    public Riddle(String riddle, String clues, String answer) {
        this.riddle = riddle;
        this.clues = clues;
        this.answer = answer;
    }
    
    public String getAnswer() {
        return answer;
    }
    
    public void setAnswer(String answer) {
        this.answer = answer;
    }
    
    public String getClues() {
        return clues;
    }
    
    public void setClues(String clues) {
        this.clues = clues;
    }
    
    public String getRiddle() {
        return riddle;
    }
    
    public void setRiddle(String riddle) {
        this.riddle = riddle;
    }
}
