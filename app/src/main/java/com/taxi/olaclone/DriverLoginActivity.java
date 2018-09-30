package com.taxi.olaclone;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DriverLoginActivity extends AppCompatActivity implements View.OnClickListener {

    private static String TAG = DriverLoginActivity.class.getSimpleName();

    private DatabaseReference mDatabaseReference;
    private FirebaseAuth mAuth;
    private EditText edEmail, edPass;
    private Button btnLogin, btnRegister;

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_login);

        mAuth = FirebaseAuth.getInstance();

        edEmail = findViewById(R.id.edEmail);
        edPass = findViewById(R.id.edPass);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);

        btnLogin.setOnClickListener(this);
        btnRegister.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnLogin:

                String email = edEmail.getText().toString().trim();
                String pass = edPass.getText().toString().trim();

                if (!validation(email, pass)) return;

                loginAccount(email, pass);


                break;
            case R.id.btnRegister:

                email = edEmail.getText().toString().trim();
                pass = edPass.getText().toString().trim();

                if (!validation(email, pass)) return;

                createAccount(email, pass);

                break;
        }
    }

    private void createAccount(String email, String pass) {

        mAuth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.i(TAG, "onComplete: account create success");
                            onAccountCreateSuccess();
                            updateUI(mAuth.getCurrentUser());
                        } else {
                            Log.i(TAG, "onComplete: account create failure" + task.getException());
                            updateUI(null);
                        }
                    }
                });


    }

    private void onAccountCreateSuccess() {
        String userId = mAuth.getCurrentUser().getUid();
        mDatabaseReference = FirebaseDatabase
                .getInstance()
                .getReference()
                .child("Users")
                .child("Drivers")
                .child(userId);
        mDatabaseReference.setValue(true);
    }

    private void loginAccount(String email, String pass) {

        mAuth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.i(TAG, "onComplete: login success");
                            updateUI(mAuth.getCurrentUser());
                        } else {
                            Log.i(TAG, "onComplete: login failure" + task.getException());
                            updateUI(null);
                        }
                    }
                });

    }

    private boolean validation(String email, String pass) {
        boolean validate = true;
        if (TextUtils.isEmpty(email)) {
            validate = false;
            edEmail.setError("Required");
        } else {
            edEmail.setError(null);
        }

        if (TextUtils.isEmpty(pass)) {
            validate = false;
            edPass.setError("Required");
        } else {
            edPass.setError(null);
        }
        return validate;
    }

    private void updateUI(FirebaseUser currentUser) {
        if (currentUser != null) {
            startActivity(new Intent(this, MapActivity.class));
            finish();
            return;
        }
    }

}
