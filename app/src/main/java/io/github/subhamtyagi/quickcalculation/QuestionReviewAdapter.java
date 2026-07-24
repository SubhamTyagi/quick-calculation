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

    public QuestionReviewAdapter() {
            super(DIFF_CALLBACK);
                }

                    // Matches the Kotlin DiffUtil logic to check for identical items
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

                                                                                                                                                                                                                              // Check if the user got it right or wrong
                                                                                                                                                                                                                                      if (result.getUserAnswer().equals(result.getCorrectAnswer())) {
                                                                                                                                                                                                                                                  // THEY GOT IT RIGHT: Make their answer Green and hide the Correct Answer text
                                                                                                                                                                                                                                                              holder.userAnswerText.setTextColor(Color.parseColor("#4CAF50")); 
                                                                                                                                                                                                                                                                          holder.correctAnswerText.setVisibility(View.GONE); 
                                                                                                                                                                                                                                                                                  } else {
                                                                                                                                                                                                                                                                                              // THEY GOT IT WRONG: Make their answer Red and explicitly SHOW the Correct Answer
                                                                                                                                                                                                                                                                                                          holder.userAnswerText.setTextColor(Color.parseColor("#F44336")); 
                                                                                                                                                                                                                                                                                                                      holder.correctAnswerText.setVisibility(View.VISIBLE);
                                                                                                                                                                                                                                                                                                                                  holder.correctAnswerText.setText("Correct Answer: " + result.getCorrectAnswer());
                                                                                                                                                                                                                                                                                                                                              holder.correctAnswerText.setTextColor(Color.parseColor("#4CAF50")); // Make correct answer green
                                                                                                                                                                                                                                                                                                                                                      }
                                                                                                                                                                                                                                                                                                                                                          }
                                                                                                                                                                                                                                                                                                                                                          

                                                                                                                                                                                                                                                                                          public static class ViewHolder extends RecyclerView.ViewHolder {
                                                                                                                                                                                                                                                                                                  TextView questionText;
                                                                                                                                                                                                                                                                                                          TextView userAnswerText;
                                                                                                                                                                                                                                                                                                                  TextView correctAnswerText;

                                                                                                                                                                                                                                                                                                                          public ViewHolder(@NonNull View itemView) {
                                                                                                                                                                                                                                                                                                                                      super(itemView);
                                                                                                                                                                                                                                                                                                                                                  // Verify these IDs match your item_question_review.xml layout
                                                                                                                                                                                                                                                                                                                                                              questionText = itemView.findViewById(R.id.tvQuestion);
                                                                                                                                                                                                                                                                                                                                                                          userAnswerText = itemView.findViewById(R.id.tvUserAnswer);
                                                                                                                                                                                                                                                                                                                                                                                      correctAnswerText = itemView.findViewById(R.id.tvCorrectAnswer);
                                                                                                                                                                                                                                                                                                                                                                                              }
                                                                                                                                                                                                                                                                                                                                                                                                  }
                                                                                                                                                                                                                                                                                                                                                                                                  }
                                                                                                                                                                                                                                                                                                                                                                                                  