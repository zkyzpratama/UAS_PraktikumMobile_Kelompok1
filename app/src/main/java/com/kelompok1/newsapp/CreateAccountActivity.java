package com.kelompok1.newsapp;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class CreateAccountActivity extends AppCompatActivity {

    EditText emailEditText, passwordEditText, confirmPasswordEditText;
    Button createAccountBtn;
    ProgressBar progressBar;
    TextView loginBtnTextView;

    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        emailEditText = findViewById(R.id.email_edit_text);
        passwordEditText = findViewById(R.id.password_edit_text);
        confirmPasswordEditText = findViewById(R.id.confirm_password_edit_text);
        createAccountBtn = findViewById(R.id.create_account_btn);
        progressBar = findViewById(R.id.progress_bar);
        loginBtnTextView = findViewById(R.id.login_text_view_btn);

        firestore = FirebaseFirestore.getInstance();

        createAccountBtn.setOnClickListener(v -> createAccount());
        loginBtnTextView.setOnClickListener(v -> finish());
    }

    void createAccount() {
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String confirmPassword = confirmPasswordEditText.getText().toString();

        boolean isValidated = validateData(email, password, confirmPassword);
        if (!isValidated) {
            return;
        }

        // You can remove Firebase Authentication code here

        // Save user data to Firestore
        saveUserDataToFirestore(email, password);

        finish();
    }

    private void saveUserDataToFirestore(String email, String password) {
        Map<String, Object> user = new HashMap<>();
        user.put("email", email);
        user.put("password", password); // tambahkan password ke Firestore

        firestore.collection("users")
                .document(email)
                .set(user)
                .addOnSuccessListener(aVoid -> {
                    // Berhasil menyimpan data pengguna ke Firestore
                    Utility.showToast(CreateAccountActivity.this, "User data saved");
                })
                .addOnFailureListener(e -> {
                    // Gagal menyimpan data pengguna ke Firestore
                    Utility.showToast(CreateAccountActivity.this, "Failed to save user data: " + e.getMessage());
                });
    }

    void changeInProgress(boolean inProgress){
        if(inProgress){
            progressBar.setVisibility(View.VISIBLE);
            createAccountBtn.setVisibility(View.GONE);
        }else{
            progressBar.setVisibility(View.GONE);
            createAccountBtn.setVisibility(View.VISIBLE);
        }
    }

    boolean validateData(String email,String password,String confirmPassword){
        //validate the data that are input by user.

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailEditText.setError("Email is invalid");
            return false;
        }
        if(password.length()<6){
            passwordEditText.setError("Password length is invalid");
            return false;
        }
        if(!password.equals(confirmPassword)){
            confirmPasswordEditText.setError("Password not matched");
            return false;
        }
        return true;
    }

}