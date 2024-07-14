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

public class LoginActivty extends AppCompatActivity {

    TextView LoginUserName,LoginPassword;
    EditText LUserName,LPassword;

    DatabaseHelper databaseHelper;


    Button Login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login_activty);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        databaseHelper= new DatabaseHelper(this);

        LoginUserName=findViewById(R.id.LUsernameTextView);
        LoginPassword=findViewById(R.id.LPasswordTextView);
        LUserName=findViewById(R.id.LUsernameEditTextText);
        LPassword=findViewById(R.id.LPasswordEditTextText);
        Login=findViewById(R.id.loginActivtyLoginBtn);
        databaseHelper.logAllUsers();


        Login = findViewById(R.id.loginActivtyLoginBtn);

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = LUserName.getText().toString().trim(); // Assuming LUserName contains email
                String password = LPassword.getText().toString().trim();

                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                    Toast.makeText(LoginActivty.this, "Enter the Information", Toast.LENGTH_SHORT).show();

                }
                if (email.equals("admin") || password.equals("admin")) {
                    Intent intent = new Intent(getApplicationContext(), AdminActivty.class);
                    startActivity(intent);
                    finish();
                }else {
                    boolean isUserExists = databaseHelper.checkUser(email, password);
                    if (isUserExists) {
                        int userId = databaseHelper.getUserId(email);
                        if (userId != -1) {
                       //     Toast.makeText(LoginActivty.this, userId, Toast.LENGTH_SHORT).show();
                            Toast.makeText(LoginActivty.this, "Login Successful", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivty.this, MainScreen.class);
                            intent.putExtra("USER_ID", userId);
                            startActivity(intent);
                            finish();
                        }
                        else {
                            Toast.makeText(LoginActivty.this, "Failed to retrieve user ID", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(LoginActivty.this, "Invalid Username or Password", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }




}