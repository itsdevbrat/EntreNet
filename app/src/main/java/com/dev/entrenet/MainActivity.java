package com.dev.entrenet;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.ui.auth.AuthMethodPickerLayout;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 0;
    private FirebaseAuth auth;
    private DatabaseReference db;
    private EditText nameField,ageField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initializing firebase variables
        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance().getReference();

        //Initializing UI Elements
        nameField = (EditText)findViewById(R.id.name);
        ageField = (EditText)findViewById(R.id.age);

        //Setting Layout for sign in screen
        AuthMethodPickerLayout customLayout = new AuthMethodPickerLayout
                .Builder(R.layout.signin_ui)
                .setGoogleButtonId(R.id.google)
                .setFacebookButtonId(R.id.facebook)
                .build();

        //Authentication Starts
        if (auth.getCurrentUser() != null) {
            Toast.makeText(getApplicationContext(), "Welcome User" + auth.getCurrentUser().getDisplayName(), Toast.LENGTH_SHORT).show();
        } else {
            startActivityForResult(AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setIsSmartLockEnabled(false)
                    .setTheme(R.style.AppTheme)
                    .setAuthMethodPickerLayout(customLayout)
                    .setAvailableProviders(Arrays.asList(
                            new AuthUI.IdpConfig.GoogleBuilder().build(),
                            new AuthUI.IdpConfig.FacebookBuilder().build()))
                    .build(), RC_SIGN_IN);
        }

    }

    //Authentication Finished
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_SIGN_IN){
            if(resultCode == RESULT_OK){
                Toast.makeText(this, "sign In Suceess"+auth.getCurrentUser().getDisplayName(), Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, "Please Try Again", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    public void signOut(View v){
        AuthUI.getInstance().signOut(this);
        finish();
    }


    public void saveProfile(View view) {
        Log.d("name", "name is" +nameField.getText().toString()+ageField.getText().toString() );
        User user = new User(auth.getCurrentUser().getUid(),nameField.getText().toString(),ageField.getText().toString());
        db.child("Users").child(auth.getCurrentUser().getUid()).setValue(user);
        startActivity(new Intent(this,HomeActivity.class));
    }
}
