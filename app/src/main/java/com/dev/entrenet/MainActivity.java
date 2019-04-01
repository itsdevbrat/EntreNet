package com.dev.entrenet;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthMethodPickerLayout;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 0;
    private FirebaseAuth auth;
    private DatabaseReference db;
    private EditText nameField,ageField;
    private ImageView dp;
    private String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION};
    private int allPermissionsCode = 1;

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
        dp = (ImageView) findViewById(R.id.dp);

        //Setting Layout for sign in screen
        AuthMethodPickerLayout customLayout = new AuthMethodPickerLayout
                .Builder(R.layout.signin_ui)
                .setGoogleButtonId(R.id.google)
                .setFacebookButtonId(R.id.facebook)
                .build();

        //Authentication Starts
        if (auth.getCurrentUser() != null) {
            Toast.makeText(getApplicationContext(), "Welcome User" + auth.getCurrentUser().getDisplayName(), Toast.LENGTH_SHORT).show();
            Picasso.get().load(auth.getCurrentUser().getPhotoUrl().toString()).into(dp);
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

        managePermissions(permissions);
    }

    //Authentication Finished
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_SIGN_IN){
            if(resultCode == RESULT_OK){
                Toast.makeText(this, "sign In Suceess"+auth.getCurrentUser().getDisplayName(), Toast.LENGTH_SHORT).show();
                Picasso.get().load(auth.getCurrentUser().getPhotoUrl().toString()).into(dp);
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

    private void managePermissions(String[] permissions) {

        ActivityCompat.requestPermissions(this,permissions,allPermissionsCode);

        for (final String permission : permissions){
            if(ContextCompat.checkSelfPermission(this,permission)!= PackageManager.PERMISSION_GRANTED){
                if(ActivityCompat.shouldShowRequestPermissionRationale(this,permission)){
                    new AlertDialog.Builder(this)
                            .setTitle("Please we need the gps permissions to serve you the best content")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    ActivityCompat.requestPermissions(MainActivity.this,new String[]{permission},allPermissionsCode);
                                }
                            }).create().show();
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == allPermissionsCode) {
            if(grantResults.length<=0)
                managePermissions(permissions);

        }
    }
}
