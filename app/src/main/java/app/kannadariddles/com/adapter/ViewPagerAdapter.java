package app.kannadariddles.com.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.text.TextUtils;
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
        View view = LayoutInflater.from(context).inflate(R.layout.layout_riddle, container, false);
        TextView riddleTextView = view.findViewById(R.id.riddle_text_id);
        final Button clueButton = view.findViewById(R.id.clue_button_id);
        final TextView clueTextView = view.findViewById(R.id.clue_text_id);
        ImageView speechInput = view.findViewById(R.id.speech_input_id);
        Button answerButton = view.findViewById(R.id.answer_button_id);
        final TextView answerText = view.findViewById(R.id.answer_text_id);
        final EditText answerEditText = view.findViewById(R.id.answer_editText_id);
        
        //finding answers in english and kannada
        final String answer = riddlesList.get(position).getAnswer();
        String[] answers;
        String answerInEnglish = "", answerInKannada = "";
        if (answer.contains("/")) {
            answers = answer.split("/");
            answerInKannada = answers[0].replaceAll(" ", "");
            answerInEnglish = answers[1].toLowerCase().replaceAll(" ", "");
        }
        final String finalAnswerInEnglish = answerInEnglish;
        final String finalAnswerInKannada = answerInKannada;
        
        clueTextView.setVisibility(View.GONE);
        answerText.setVisibility(View.GONE);
        
        speechInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                voiceInputClickCallbacks.onVoiceInputClicked(finalAnswerInKannada);
            }
        });
        
        clueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clueTextView.setVisibility(View.VISIBLE);
                clueButton.setVisibility(View.INVISIBLE);
            }
        });
        
        answerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String submittedAnswer = answerEditText.getText().toString().trim();
                if (TextUtils.isEmpty(submittedAnswer)) {
                    answerEditText.requestFocus();
                    answerEditText.setError(context.getResources().getString(R.string.empty_answer));
                } else if (submittedAnswer.toLowerCase().equals(finalAnswerInEnglish) || submittedAnswer.equals(finalAnswerInKannada)) {
                    answeredCallbacks.answeredCorrect(submittedAnswer);
                } else {
                    answeredCallbacks.answeredIncorrect(answer, submittedAnswer);
                    answerText.setVisibility(View.VISIBLE);
                }
            }
        });
        
        riddleTextView.setText(riddlesList.get(position).getRiddle());
        clueTextView.setText(riddlesList.get(position).getClue());
        String shownAnswer = context.getResources().getString(R.string.answer) + ": " + riddlesList.get(position).getAnswer();
        answerText.setText(shownAnswer);
        
        container.addView(view);
        
        return view;
    }
    
    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((RelativeLayout) object);
    }
}
