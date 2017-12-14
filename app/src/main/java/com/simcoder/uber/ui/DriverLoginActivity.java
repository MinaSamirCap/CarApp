package com.simcoder.uber.ui;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.simcoder.uber.R;
import com.simcoder.uber.network.NetworkingUtils;
import com.victor.loading.rotate.RotateLoading;

public class DriverLoginActivity extends BaseActivity {
    private MaterialEditText emailEditText, passwordEditText;
    private Button loginButton, registerButton;
    private RotateLoading rotateLoading;
    private ConstraintLayout rootLayout;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_login);

        mAuth = FirebaseAuth.getInstance();

        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    Intent intent = new Intent(DriverLoginActivity.this, DriverMapActivity2.class);
                    startActivity(intent);
                    finish();
                }
            }
        };

        emailEditText = findViewById(R.id.email_edit_text);
        passwordEditText = findViewById(R.id.password_edit_text);
        rootLayout = findViewById(R.id.root_layout);

        loginButton = findViewById(R.id.login_button);
        registerButton = findViewById(R.id.register_button);
        rotateLoading = findViewById(R.id.rotate_loading);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validation()) {
                    rotateLoading.start();
                    final String email = emailEditText.getText().toString();
                    final String password = passwordEditText.getText().toString();
                    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(DriverLoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            rotateLoading.stop();
                            if (!task.isSuccessful()) {
                                Snackbar.make(rootLayout, getString(R.string.register_error), Snackbar.LENGTH_SHORT).show();
                            } else {
                                String user_id = mAuth.getCurrentUser().getUid();
                                DatabaseReference current_user_db = FirebaseDatabase.getInstance().getReference().child("Users").child("Drivers").child(user_id).child("name");
                                current_user_db.setValue(email);
                            }
                        }
                    });
                }
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validation()) {
                    rotateLoading.start();
                    final String email = emailEditText.getText().toString();
                    final String password = passwordEditText.getText().toString();
                    mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(DriverLoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            rotateLoading.stop();
                            if (!task.isSuccessful()) {
                                Snackbar.make(rootLayout, getString(R.string.sign_in_error), Snackbar.LENGTH_SHORT).show();
                            }
                        }
                    });
                }


            }
        });
    }

    private boolean validation() {
        if (TextUtils.isEmpty(emailEditText.getText().toString())) {
            Snackbar.make(rootLayout, getString(R.string.email_validation), Snackbar.LENGTH_SHORT).show();
            return false;
        } else if (TextUtils.isEmpty(passwordEditText.getText().toString())) {
            Snackbar.make(rootLayout, getString(R.string.password_validation), Snackbar.LENGTH_SHORT).show();
            return false;
        } else if (!NetworkingUtils.isNetworkConnected()) {
            Snackbar.make(rootLayout, getString(R.string.network_validation), Snackbar.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(firebaseAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(firebaseAuthListener);
    }
}
