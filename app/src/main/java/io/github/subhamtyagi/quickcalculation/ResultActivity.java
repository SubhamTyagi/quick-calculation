package io.github.subhamtyagi.quickcalculation;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class ResultActivity extends AppCompatActivity {

    @Override
        @SuppressWarnings("unchecked")
            protected void onCreate(Bundle savedInstanceState) {
                    super.onCreate(savedInstanceState);
                            setContentView(R.layout.activity_result);

                                    // Retrieve the list passed from QuizActivity
                                            ArrayList<QuestionResult> resultsList = (ArrayList<QuestionResult>) getIntent().getSerializableExtra("QUESTION_RESULTS");
                                                    if (resultsList == null) {
                                                                resultsList = new ArrayList<>();
                                                                        }

                                                                                // Setup RecyclerView
                                                                                        RecyclerView recyclerView = findViewById(R.id.recyclerView); 
                                                                                                if (recyclerView != null) {
                                                                                                            recyclerView.setLayoutManager(new LinearLayoutManager(this));
                                                                                                                        
                                                                                                                                    // Initialize Adapter and submit the data
                                                                                                                                                QuestionReviewAdapter adapter = new QuestionReviewAdapter();
                                                                                                                                                            recyclerView.setAdapter(adapter);
                                                                                                                                                            adapter.submitList(resultsList);
                                                                                                                                                            android.widget.Button doneButton = findViewById(R.id.btnDone);
                                                                                                                                                            if (doneButton != null) {
                                                                                                                                                                  doneButton.setOnClickListener(new android.view.View.OnClickListener() {
                                                                                                                                                                  @Override
                                                                                                                                                            public void onClick(android.view.View v) {
                                                                                                                                                                  android.content.Intent intent = new android.content.Intent(ResultActivity.this, LaunchActivity.class);
                                                                                                                                                                  intent.addFlags(android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                                                                                                                                  startActivity(intent);
                                                                                                                                                                                                               

                                                                                                                                                                  }
                                                                                                                                                                  });
                                                                                                                                                                  } 
                                                                                                                                                            
                                                                                                                                                                                }
                                                                                                                                                                                }
                                                                                                                                                                                    }
                                                                                                                                                                                    