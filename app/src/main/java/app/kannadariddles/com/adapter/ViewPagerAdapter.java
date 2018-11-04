package app.kannadariddles.com.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import app.kannadariddles.com.data.model.KannadaRiddle;
import app.kannadariddles.com.interfaces.AnsweredCallbacks;
import app.kannadariddles.com.interfaces.VoiceInputClickCallbacks;
import app.kannadariddles.com.kannadariddles.R;

/**
 * Created by varun.am on 23/10/18
 */
public class ViewPagerAdapter extends PagerAdapter {
    
    private static final String TAG = ViewPagerAdapter.class.getSimpleName();
    private Context context;
    private List<KannadaRiddle> riddlesList;
    private AnsweredCallbacks answeredCallbacks;
    private VoiceInputClickCallbacks voiceInputClickCallbacks;
    
    public ViewPagerAdapter(Context context, AnsweredCallbacks answeredCallbacks, VoiceInputClickCallbacks voiceInputClickCallbacks) {
        this.context = context;
        this.riddlesList = new ArrayList<>();
        this.answeredCallbacks = answeredCallbacks;
        this.voiceInputClickCallbacks = voiceInputClickCallbacks;
    }
    
    @Override
    public int getCount() {
        return riddlesList.size();
    }
    
    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == (RelativeLayout) o;
    }
    
    public void setRiddlesList(List<KannadaRiddle> riddlesList) {
        this.riddlesList = riddlesList;
        notifyDataSetChanged();
    }
    
    @NonNull
    @Override
    public Object instantiateItem(@NonNull final ViewGroup container, int position) {
        
        final KannadaRiddle kannadaRiddle = riddlesList.get(position);
        
        Log.e(TAG, "Current Riddle: " + "\n" +
                "riddle: " + kannadaRiddle.getRiddle() + "\n" +
                "clue: " + kannadaRiddle.getClue() + "\n" +
                "answer: " + kannadaRiddle.getAnswer() + "\n" +
                "clueChecked" + kannadaRiddle.isClueChecked() + "\n" +
                "answeredCorrect: " + kannadaRiddle.isAnsweredCorrect() + "\n" +
                "score: " + kannadaRiddle.getScore());
        
        final View riddleView = LayoutInflater.from(context).inflate(R.layout.layout_riddle, container, false);
        TextView riddleTextView = riddleView.findViewById(R.id.riddle_text_id);
        final Button clueButton = riddleView.findViewById(R.id.clue_button_id);
        final TextView clueTextView = riddleView.findViewById(R.id.clue_text_id);
        ImageView speechInput = riddleView.findViewById(R.id.speech_input_id);
        Button answerButton = riddleView.findViewById(R.id.answer_button_id);
        final TextView answerText = riddleView.findViewById(R.id.answer_text_id);
        final EditText answerEditText = riddleView.findViewById(R.id.answer_editText_id);
        
        //finding answers in english and kannada
        final String answer = kannadaRiddle.getAnswer();
        String[] answers;
        String answerInEnglish = "", answerInKannada = "";
        if (answer.contains("/")) {
            answers = answer.split("/");
            answerInKannada = answers[0].replaceAll(" ", "");
            answerInEnglish = answers[1].toLowerCase().replaceAll(" ", "");
        }
        final String finalAnswerInEnglish = answerInEnglish;
        final String finalAnswerInKannada = answerInKannada;
        
        if (kannadaRiddle.isClueChecked()) {
            showClue(riddleView);
        } else {
            hideClue(riddleView);
        }
        
        if (kannadaRiddle.isAnsweredCorrect()) {
            disableAnswerInput(riddleView);
        } else {
            enableAnswerInput(riddleView);
        }
        
        speechInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                voiceInputClickCallbacks.onVoiceInputClicked(kannadaRiddle, finalAnswerInKannada);
            }
        });
        
        clueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                kannadaRiddle.setClueChecked(true);
                showClue(riddleView);
            }
        });
        
        answerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String submittedAnswer = answerEditText.getText().toString().trim();
                if (TextUtils.isEmpty(submittedAnswer)) {
                    answerEditText.requestFocus();
                    answerEditText.setError(context.getResources().getString(R.string.empty_answer));
                } else if (submittedAnswer.toLowerCase().replaceAll(" ", "").equals(finalAnswerInEnglish) || submittedAnswer.replaceAll(" ", "").equals(finalAnswerInKannada)) {
                    kannadaRiddle.setAnsweredCorrect(true);
                    answeredCallbacks.answeredCorrect(kannadaRiddle, submittedAnswer);
                    disableAnswerInput(riddleView);
                } else {
                    kannadaRiddle.setAnsweredCorrect(false);
                    answeredCallbacks.answeredIncorrect(kannadaRiddle, answer, submittedAnswer);
                    enableAnswerInput(riddleView);
                }
            }
        });
        
        riddleTextView.setText(riddlesList.get(position).getRiddle());
        clueTextView.setText(riddlesList.get(position).getClue());
        String shownAnswer = context.getResources().getString(R.string.answer) + ": " + riddlesList.get(position).getAnswer();
        answerText.setText(shownAnswer);
        
        container.addView(riddleView);
        
        return riddleView;
    }
    
    private void showAnswer(View view) {
        Button answerButton = view.findViewById(R.id.answer_button_id);
        TextView answerText = view.findViewById(R.id.answer_text_id);
        
        answerButton.setVisibility(View.VISIBLE);
        answerText.setVisibility(View.GONE);
    }
    
    private void disableAnswerInput(View view){
        RelativeLayout inputLayout = view.findViewById(R.id.anwer_input_layout_id);
        TextView answered = view.findViewById(R.id.already_answered_id);
    
        hideAnswer(view);
    
        inputLayout.setVisibility(View.GONE);
        answered.setVisibility(View.VISIBLE);
    }
    
    private void enableAnswerInput(View view){
        RelativeLayout inputLayout = view.findViewById(R.id.anwer_input_layout_id);
        TextView answered = view.findViewById(R.id.already_answered_id);
    
        showAnswer(view);
        inputLayout.setVisibility(View.VISIBLE);
        answered.setVisibility(View.GONE);
    }
    
    private void hideAnswer(View view) {
        Button answerButton = view.findViewById(R.id.answer_button_id);
        TextView answerText = view.findViewById(R.id.answer_text_id);
        
        answerButton.setVisibility(View.GONE);
        answerText.setVisibility(View.VISIBLE);
    }
    
    private void showClue(View view) {
        Button clueButton = view.findViewById(R.id.clue_button_id);
        TextView clueTextView = view.findViewById(R.id.clue_text_id);
        
        clueButton.setVisibility(View.GONE);
        clueTextView.setVisibility(View.VISIBLE);
    }
    
    private void hideClue(View view) {
        Button clueButton = view.findViewById(R.id.clue_button_id);
        TextView clueTextView = view.findViewById(R.id.clue_text_id);
        
        clueButton.setVisibility(View.VISIBLE);
        clueTextView.setVisibility(View.GONE);
    }
    
    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((RelativeLayout) object);
    }
}
