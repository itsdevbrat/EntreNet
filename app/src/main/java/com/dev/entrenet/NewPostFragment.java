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
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;


public class NewPostFragment extends Fragment {

    private EditText posttitle,postdesc;
    private FirebaseAuth auth;
    private DatabaseReference db;
    private Button postbutton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_new_post, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        db = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();
        posttitle = (EditText) getView().findViewById(R.id.posttitle);
        postdesc = (EditText) getView().findViewById(R.id.postdesc);
        postbutton = (Button) getView().findViewById(R.id.postbutton);
        postbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String,String> post = new HashMap<String, String>();
                post.put("title",posttitle.getText().toString());
                post.put("desc",postdesc.getText().toString());
                db.child("Posts").push().setValue(post);
                postbutton.setBackgroundColor(R.color.colorPrimary);
                postbutton.setClickable(false);
            }
        });
    }
}
