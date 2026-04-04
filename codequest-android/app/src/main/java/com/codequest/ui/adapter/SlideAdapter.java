package com.codequest.ui.adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.codequest.R;
import com.codequest.model.LessonSlide;
import com.codequest.model.QuizQuestion;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
public class SlideAdapter extends RecyclerView.Adapter<SlideAdapter.SlideViewHolder> {
    private final List<LessonSlide> slides;
    public SlideAdapter(List<LessonSlide> slides) {
        this.slides = slides;
    }
    @NonNull
    @Override
    public SlideViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_slide, parent, false);
        return new SlideViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull SlideViewHolder holder, int position) {
        LessonSlide slide = slides.get(position);
        holder.bind(slide);
    }
    @Override
    public int getItemCount() { return slides.size(); }
    static class SlideViewHolder extends RecyclerView.ViewHolder {
        TextView tvSlideTitle, tvSlideContent, tvCodeContent, tvCodeLang, tvConceptText;
        TextView tvQuizQuestion, tvFeedbackText;
        ImageView tvFeedbackIcon;
        LinearLayout codeBlockContainer, conceptBoxContainer, quizContainer, feedbackContainer, optionsContainer;
        SlideViewHolder(View v) {
            super(v);
            tvSlideTitle = v.findViewById(R.id.tvSlideTitle);
            tvSlideContent = v.findViewById(R.id.tvSlideContent);
            tvCodeContent = v.findViewById(R.id.tvCodeContent);
            tvCodeLang = v.findViewById(R.id.tvCodeLang);
            tvConceptText = v.findViewById(R.id.tvConceptText);
            tvQuizQuestion = v.findViewById(R.id.tvQuizQuestion);
            tvFeedbackIcon = v.findViewById(R.id.tvFeedbackIcon);
            tvFeedbackText = v.findViewById(R.id.tvFeedbackText);
            codeBlockContainer = v.findViewById(R.id.codeBlockContainer);
            conceptBoxContainer = v.findViewById(R.id.conceptBoxContainer);
            quizContainer = v.findViewById(R.id.quizContainer);
            feedbackContainer = v.findViewById(R.id.feedbackContainer);
            optionsContainer = v.findViewById(R.id.optionsContainer);
        }
        void bind(LessonSlide slide) {
            codeBlockContainer.setVisibility(View.GONE);
            conceptBoxContainer.setVisibility(View.GONE);
            quizContainer.setVisibility(View.GONE);
            String type = slide.getContentType();
            if ("TEXT".equals(type)) {
                tvSlideContent.setVisibility(View.VISIBLE);
                tvSlideContent.setText(slide.getContentText());
            } else if ("CODE".equals(type)) {
                tvSlideContent.setVisibility(View.VISIBLE);
                tvSlideContent.setText(slide.getContentText());
                codeBlockContainer.setVisibility(View.VISIBLE);
                tvCodeContent.setText(slide.getCodeSnippet());
                tvCodeLang.setText(slide.getCodeLanguage());
                if (slide.getExplanation() != null) {
                    conceptBoxContainer.setVisibility(View.VISIBLE);
                    tvConceptText.setText(slide.getExplanation());
                }
            } else if ("QCM".equals(type)) {
                tvSlideContent.setVisibility(View.GONE);
                quizContainer.setVisibility(View.VISIBLE);
                bindQCM(slide.getQuizQuestion());
            }
        }
        void bindQCM(QuizQuestion q) {
            if (q == null) return;
            tvQuizQuestion.setText(q.getQuestion());
            optionsContainer.removeAllViews();
            List<String> answers = new ArrayList<>();
            answers.add(q.getCorrectAnswer());
            answers.add(q.getWrongAnswer1());
            answers.add(q.getWrongAnswer2());
            answers.add(q.getWrongAnswer3());
            Collections.shuffle(answers);
            for (String answer : answers) {
                Button btn = new Button(itemView.getContext());
                btn.setText(answer);
                btn.setAllCaps(false);
                btn.setBackgroundResource(R.drawable.btn_outline);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                params.bottomMargin = 8;
                btn.setLayoutParams(params);
                btn.setOnClickListener(v -> {
                    boolean correct = answer.equals(q.getCorrectAnswer());
                    feedbackContainer.setVisibility(View.VISIBLE);
                    if (correct) {
                        tvFeedbackIcon.setImageResource(R.drawable.ic_check);
                        tvFeedbackIcon.setColorFilter(v.getContext().getColor(R.color.success));
                        tvFeedbackText.setText("Correct ! " + q.getExplanation());
                    } else {
                        tvFeedbackIcon.setImageResource(R.drawable.ic_close);
                        tvFeedbackIcon.setColorFilter(v.getContext().getColor(R.color.error));
                        tvFeedbackText.setText("Incorrect. La bonne réponse est : " +
                                q.getCorrectAnswer() + "\n" + q.getExplanation());
                    }
                    for (int i = 0; i < optionsContainer.getChildCount(); i++) {
                        optionsContainer.getChildAt(i).setEnabled(false);
                    }
                });
                optionsContainer.addView(btn);
            }
        }
    }
}
