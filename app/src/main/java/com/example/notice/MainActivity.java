package com.example.notice;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.notice.activity.NoteActivity;
import com.example.notice.activity.RegisterActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private EditText emailEditText;
    private EditText passwordEditText;
    private Button logInButton;
    private Button registerButton;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        logInButton = findViewById(R.id.logInButton);
        registerButton = findViewById(R.id.registerButton);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        if (firebaseUser != null) {
            finish();
            startActivity(new Intent(MainActivity.this, NoteActivity.class));
        }

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, RegisterActivity.class));
            }
        });

        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "B???n ph???i ??i???n ?????y ????? th??ng tin", Toast.LENGTH_SHORT).show();
                } else if (password.length() < 8) {
                    Toast.makeText(getApplicationContext(), "M???t kh???u d??i t???i thi???u 8 k?? t???", Toast.LENGTH_SHORT).show();
                } else {
                    //gui thong tin dang nhap toi Firebase
                    firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                //lay thong tin tu Firebase
                                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                                if (firebaseUser.isEmailVerified() == true) {
                                    //neu da xac thuc email
                                    Toast.makeText(getApplicationContext(), "????ng nh???p th??nh c??ng", Toast.LENGTH_SHORT).show();
                                    finish();
                                    startActivity(new Intent(MainActivity.this, NoteActivity.class));
                                } else {
                                    //chua xac thuc email
                                    Toast.makeText(getApplicationContext(), "C???n x??c th???c email tr?????c", Toast.LENGTH_SHORT).show();
                                    firebaseAuth.signOut();
                                }
                            } else {
                                //khong co thong tin tu Firebase
                                Toast.makeText(getApplicationContext(), "T??i kho???n kh??ng t???n t???i", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }
}