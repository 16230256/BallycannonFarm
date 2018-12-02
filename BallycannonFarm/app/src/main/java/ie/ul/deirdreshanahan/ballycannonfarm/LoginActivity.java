package ie.ul.deirdreshanahan.ballycannonfarm;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText mEmailEditText;
    private EditText mPasswordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mEmailEditText = findViewById(R.id.email_edittext);
        mPasswordEditText = findViewById(R.id.password_edittext);
        mAuth = FirebaseAuth.getInstance();
        //*if you have already signed in new need to sign in again
        if (mAuth.getCurrentUser() != null) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);

        }
    }
    public void handleSignIn(View view) {
        //    Toast.makeText(this, "Sing in", Toast.LENGTH_LONG).show();
        String email = mEmailEditText.getText().toString();
        String password = mPasswordEditText.getText().toString();
        if (email.length() < 5 || !email.contains("@")) {
            mEmailEditText.setError(getString(R.string.invalid_email));
        } else if (password.length() < 6) {
            mPasswordEditText.setError(getString(R.string.invalid_password));
        } else {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_LONG).show();
                            }

                        }
                    });
        }
    }
    public void handleSignUp(View view){
        //     Toast.makeText(this, "Sing Up", Toast.LENGTH_LONG).show();
        String email = mEmailEditText.getText().toString();
        String password = mPasswordEditText.getText().toString();
        if (email.length() < 5 || !email.contains("@")) {
            mEmailEditText.setError(getString(R.string.invalid_email));
        } else if (password.length() < 6) {
            mPasswordEditText.setError(getString(R.string.invalid_password));
        } else {
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                            }else{
                                Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }
    }
}
