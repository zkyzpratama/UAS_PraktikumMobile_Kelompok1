package com.kelompok1.newsapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {

    EditText emailEditText, passwordEditText;
    Button loginBtn;
    ProgressBar progressBar;
    TextView createAccountBtnTextView;

    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEditText = findViewById(R.id.email_edit_text);
        passwordEditText = findViewById(R.id.password_edit_text);
        loginBtn = findViewById(R.id.login_btn);
        progressBar = findViewById(R.id.progress_bar);
        createAccountBtnTextView = findViewById(R.id.create_account_text_view_btn);

        firestore = FirebaseFirestore.getInstance();

        loginBtn.setOnClickListener((v) -> loginUser());
        createAccountBtnTextView.setOnClickListener((v) -> startActivity(new Intent(LoginActivity.this, CreateAccountActivity.class)));
    }

    void loginUser() {
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        boolean isValidated = validateData(email, password);
        if (!isValidated) {
            return;
        }

        // Check if the user exists in Firestore
        checkUserInFirestore(email, password);
    }

    private void checkUserInFirestore(String email, String password) {
        firestore.collection("users")
                .document(email)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            // Pengguna ada di Firestore
                            String storedPassword = document.getString("password");
                            if (password.equals(storedPassword)) {
                                // Password sesuai, lanjutkan ke MainActivity
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                finish();
                            } else {
                                // Password tidak sesuai
                                Utility.showToast(LoginActivity.this, "Incorrect password");
                            }
                        } else {
                            // Pengguna tidak ada di Firestore
                            Utility.showToast(LoginActivity.this, "User not found");
                        }
                    } else {
                        // Gagal memeriksa pengguna di Firestore
                        Utility.showToast(LoginActivity.this, "Failed to check user in Database: " + task.getException().getMessage());
                    }
                });
    }

    void changeInProgress(boolean inProgress){
        if(inProgress){
            progressBar.setVisibility(View.VISIBLE);
            loginBtn.setVisibility(View.GONE);
        }else{
            progressBar.setVisibility(View.GONE);
            loginBtn.setVisibility(View.VISIBLE);
        }
    }

    boolean validateData(String email,String password){
        //validate the data that are input by user.

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailEditText.setError("Email is invalid");
            return false;
        }
        if(password.length()<6){
            passwordEditText.setError("Password length is invalid");
            return false;
        }
        return true;
    }

}