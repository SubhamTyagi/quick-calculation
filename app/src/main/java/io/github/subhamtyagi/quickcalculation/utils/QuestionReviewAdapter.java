package io.github.subhamtyagi.quickcalculation;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

public class QuestionReviewAdapter extends ListAdapter<QuestionResult, QuestionReviewAdapter.ViewHolder> {

    private static final int COLOR_CORRECT = Color.parseColor("#4CAF50");
    private static final int COLOR_WRONG = Color.parseColor("#F44336");
    private static final int COLOR_SKIPPED = Color.parseColor("#FF9800");

    public QuestionReviewAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<QuestionResult> DIFF_CALLBACK = new DiffUtil.ItemCallback<QuestionResult>() {
        @Override
        public boolean areItemsTheSame(@NonNull QuestionResult oldItem, @NonNull QuestionResult newItem) {
            return oldItem.getQuestion().equals(newItem.getQuestion());
        }

        @Override
        public boolean areContentsTheSame(@NonNull QuestionResult oldItem, @NonNull QuestionResult newItem) {
            return oldItem.getQuestion().equals(newItem.getQuestion()) &&
                   oldItem.getUserAnswer().equals(newItem.getUserAnswer()) &&
                   oldItem.getCorrectAnswer().equals(newItem.getCorrectAnswer());
        }
    };

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_question_review, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        QuestionResult result = getItem(position);
        
        holder.questionText.setText(result.getQuestion());
        holder.userAnswerText.setText("Your Answer: " + result.getUserAnswer());

        String skipText = holder.itemView.getContext().getString(R.string.skip);

        if (result.getUserAnswer().equals(result.getCorrectAnswer())) {
            holder.userAnswerText.setTextColor(COLOR_CORRECT); 
            holder.correctAnswerText.setVisibility(View.GONE); 
        } else if (skipText.equals(result.getUserAnswer()) || "Skipped".equals(result.getUserAnswer())) {
            holder.userAnswerText.setTextColor(COLOR_SKIPPED); 
            holder.correctAnswerText.setVisibility(View.VISIBLE);
            holder.correctAnswerText.setText("Correct Answer: " + result.getCorrectAnswer());
            holder.correctAnswerText.setTextColor(COLOR_CORRECT);
        } else {
            holder.userAnswerText.setTextColor(COLOR_WRONG); 
            holder.correctAnswerText.setVisibility(View.VISIBLE);
            holder.correctAnswerText.setText("Correct Answer: " + result.getCorrectAnswer());
            holder.correctAnswerText.setTextColor(COLOR_CORRECT);
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView questionText, userAnswerText, correctAnswerText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            questionText = itemView.findViewById(R.id.tvQuestion);
            userAnswerText = itemView.findViewById(R.id.tvUserAnswer);
            correctAnswerText = itemView.findViewById(R.id.tvCorrectAnswer);
        }
    }
}
