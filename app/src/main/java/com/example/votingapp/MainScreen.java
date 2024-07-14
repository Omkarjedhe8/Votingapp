package com.example.votingapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.w3c.dom.Text;

public class MainScreen extends AppCompatActivity {

    RadioGroup radiogroup;
    RadioButton candidate1,candidate2,candidate3,candidate4;

    TextView selectedOptionTextView;

    Button selectedRadioButton;
    Button button,logout;

    private int userId;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        db =new DatabaseHelper(this);

        userId = getIntent().getIntExtra("USER_ID", -1);
        Log.d("MainScreen", "Retrieved user ID: " + userId);

        // Validate user ID or take appropriate action
        if (userId == -1) {
            // Handle case where user ID is not set or invalid
            Log.e("MainScreen", "Invalid user ID retrieved");
        }

        candidate1=findViewById(R.id.radioButton);
        candidate2=findViewById(R.id.radioButton2);
        candidate3=findViewById(R.id.radioButton3);
        candidate4=findViewById(R.id.radioButton4);

        selectedOptionTextView=findViewById(R.id.textView);
        String optionS=selectedOptionTextView.toString();

        logout=findViewById(R.id.logout);
        button=findViewById(R.id.SubmitBtn);

        radiogroup=findViewById(R.id.radioGroup);

        if (db.hasUserVoted(userId)) {
            button.setEnabled(false);
            Toast.makeText(this, "You have already voted", Toast.LENGTH_SHORT).show();
        }



       /* logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),RegisterActivty.class);
                startActivity(intent);
                finish();
            }
        });*/

        radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId != -1) {
                    RadioButton selectedRadioButton = findViewById(checkedId);
                    selectedOptionTextView.setText(selectedRadioButton.getText().toString());
                    Toast.makeText(MainScreen.this, selectedRadioButton.getText().toString(), Toast.LENGTH_SHORT).show();
                    Log.d("MainScreen", "Radio button selected: " + selectedRadioButton.getText().toString());
                } else {
                    selectedOptionTextView.setText("");
                    Toast.makeText(MainScreen.this, "No radio button selected", Toast.LENGTH_SHORT).show();
                    Log.d("MainScreen", "No radio button selected");
                }
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getApplicationContext(),RegisterActivty.class);
                startActivity(intent);

            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String selectedOption = selectedOptionTextView.getText().toString();
                if (!selectedOption.isEmpty()) {
                    if (!db.hasUserVoted(userId)) { // Check again to ensure user hasn't voted since UI could change
                        if (db.recordVote(userId, selectedOption)) {
                            button.setEnabled(false);
                            Toast.makeText(MainScreen.this, "Vote Cast Successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainScreen.this, "Vote Failed", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(MainScreen.this, "You have already voted", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainScreen.this, "Please select an option", Toast.LENGTH_SHORT).show();
                }
            }
        });    }
}