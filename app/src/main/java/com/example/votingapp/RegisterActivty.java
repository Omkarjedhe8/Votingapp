package com.example.votingapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class RegisterActivty extends AppCompatActivity {

    TextView RegisterUserName,RegisterPassword,RegisterEmail,RegisterPhone;
    EditText RUserName,RPassword,REmail,RPhone;

    String  emailPattern = "^[A-Za-z0-9+_.-]+@(.+)$";

    Button RLoginButton,RegisterBtn;

    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register_activty);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        databaseHelper =new DatabaseHelper(this);
        //TextView
        RegisterUserName=findViewById(R.id.RUsernameTextView);
        RegisterPassword=findViewById(R.id.RPasswordTextView);
        RegisterEmail=findViewById(R.id.REmailTextView);
        RegisterPhone=findViewById(R.id.RPhoneTextView);

        //EditText
        RUserName=findViewById(R.id.RUsernameEditText);
        RPassword=findViewById(R.id.RPasswordEditText);
        REmail=findViewById(R.id.REmailEditText);
        RPhone=findViewById(R.id.RPhoneEditText);

        ///button
        RLoginButton=findViewById(R.id.RegisterActivtyLoginBtn);
        RegisterBtn=findViewById(R.id.RegisterBtn);

        RLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),LoginActivty.class);
                startActivity(intent);
                finish();

            }
        });

        RegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String lusername, lpassword, lemail, lphone;
                lusername = RUserName.getText().toString();
                lpassword = RPassword.getText().toString();
                lemail = REmail.getText().toString();
                lphone = RPhone.getText().toString();

                if (TextUtils.isEmpty(lusername) || TextUtils.isEmpty(lpassword) || TextUtils.isEmpty(lemail) || TextUtils.isEmpty(lphone)) {

                    Toast.makeText(RegisterActivty.this, "Enter a Information", Toast.LENGTH_SHORT).show();
                } else if (!lemail.matches(emailPattern)) {
                    Toast.makeText(RegisterActivty.this, "Enter a Valid Email", Toast.LENGTH_SHORT).show();
                } else {
                    // Insert user into the database
                    boolean isInserted = databaseHelper.insertUser(lusername, lemail, lpassword, lphone);
                    if (isInserted) {

                        Toast.makeText(RegisterActivty.this, "Record Successfully Inserted Plz Click On Login Button", Toast.LENGTH_SHORT).show();

                        // Retrieve user ID after insertion
                        int userId = databaseHelper.getUserId(lusername);
                        if (userId != -1) {
                            // Start MainScreen activity and pass user ID
                            Intent intent = new Intent(getApplicationContext(), MainScreen.class);
                            intent.putExtra("USER_ID", userId);
                            startActivity(intent);
                            Toast.makeText(RegisterActivty.this, "userid"+userId, Toast.LENGTH_SHORT).show();

                            Toast.makeText(RegisterActivty.this, "Record successfully inserted", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                      } else {
                        Toast.makeText(RegisterActivty.this, "Record insertion failed", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });


    }
}