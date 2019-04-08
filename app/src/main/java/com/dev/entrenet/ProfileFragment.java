package com.dev.entrenet;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;


public class ProfileFragment extends Fragment {

    private FirebaseAuth auth;
    private DatabaseReference db;
    private TextView nameField,ageField;
    private ImageView dp;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Initializing firebase variables
        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance().getReference();

        //Initializing UI Elements
        nameField = (TextView)getView().findViewById(R.id.name);
        ageField = (TextView)getView().findViewById(R.id.age);
        dp = (ImageView) getView().findViewById(R.id.dp);

        Picasso.get().load(auth.getCurrentUser().getPhotoUrl().toString().replace("s96-c/photo.jpg","s400-c/photo.jpg")).into(dp);

        db.child("Users").child(auth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                nameField.setText("Name :    "+dataSnapshot.getValue(User.class).username.toString());
                ageField.setText("Age :    "+dataSnapshot.getValue(User.class).age.toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
