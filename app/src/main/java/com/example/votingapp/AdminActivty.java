
package com.example.votingapp;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AdminActivty extends AppCompatActivity {

    private DatabaseHelper db;
    private TextView textViewCandidateA, textViewCandidateB, textViewCandidateC, textViewCandidateD;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_activty);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        db = new DatabaseHelper(this);
        textViewCandidateA = findViewById(R.id.textViewCandidateA);
        textViewCandidateB = findViewById(R.id.textViewCandidateB);
        textViewCandidateC = findViewById(R.id.textViewCandidateC);
        textViewCandidateD = findViewById(R.id.textViewCandidateD);



        loadVoteCounts();

    }

    private void loadVoteCounts() {
        int countCandidateA = db.getCandidateVoteCount("Candidate A");
        int countCandidateB = db.getCandidateVoteCount("Candidate B");
        int countCandidateC = db.getCandidateVoteCount("Candidate C");
        int countCandidateD = db.getCandidateVoteCount("Candidate D");

        textViewCandidateA.setText("Candidate A: " + countCandidateA);
        textViewCandidateB.setText("Candidate B: " + countCandidateB);
        textViewCandidateC.setText("Candidate C: " + countCandidateC);
        textViewCandidateD.setText("Candidate D: " + countCandidateD);
    }


}