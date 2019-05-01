package com.a7f.drawingsound;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.a7f.drawingsound.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends AppCompatActivity {
    Button ButtonSave;
    Button ButtonCancel;
    EditText EditTextPasswd;
    EditText EditTextEmail;
    EditText EditTextName;

    private String email, passwd, name;



    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference myRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        settingDB();
        settingAuth();
        settingHandler();
    }

    private void settingDB(){
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
    }

    private void settingAuth(){
        mAuth = FirebaseAuth.getInstance();
    }

    private void settingHandler(){
        ButtonSave = (Button)findViewById(R.id.ButtonSave);
        ButtonCancel = (Button)findViewById(R.id.ButtonCancel);

        EditTextPasswd = (EditText)findViewById(R.id.EditTextPasswd);
        EditTextPasswd.setTransformationMethod(new PasswordTransformationMethod());

        EditTextEmail = (EditText)findViewById(R.id.EditTextEmail);

        EditTextName = (EditText)findViewById(R.id.EditTextName);

        ButtonSave.setOnClickListener(SaveClick);
        ButtonCancel.setOnClickListener(CancelClick);
    }

    @Override
    public void onStart(){
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(SignupActivity.this,SetActivity.class);
            startActivity(intent);
        }
    }

    private void saveUserInfo(FirebaseUser Fuser){
        String uid;
        uid = Fuser.getUid();
        Log.d("getuserid", uid);
        try {
            User user = new User(name, email);
            myRef.child("users").child(uid).setValue(user);
            Intent intent = new Intent(SignupActivity.this, SigninActivity.class);
            startActivity(intent);
            finish();
        }catch (Exception e){
            Log.d("auth error:",e.toString() );
            //
        }
    }

    private void createAccount(String email, String password) {
        final String TAG = "EmailPassword";
        Log.d(TAG, "createAccount:" + email);
        Log.d(TAG, "createAccount:" + password);

        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        FirebaseUser user = mAuth.getCurrentUser();
                        Log.d("UserID",user.getUid());
                        saveUserInfo(user);
                        Log.d(TAG, "createUserWithEmail:success");
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                        Toast.makeText(SignupActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                    }
                }
        });



    }



    Button.OnClickListener SaveClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            email = EditTextEmail.getText().toString();
            passwd = EditTextPasswd.getText().toString();
            name = EditTextName.getText().toString();

            if(!email.isEmpty() && !passwd.isEmpty()){
                createAccount(email,passwd);


            }
        }
    };

    Button.OnClickListener CancelClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(SignupActivity.this, SigninActivity.class);
            startActivity(intent);
        }
    };
}


