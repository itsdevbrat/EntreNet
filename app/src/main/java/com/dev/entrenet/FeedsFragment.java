package com.dev.entrenet;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class FeedsFragment extends Fragment {

    RecyclerView postsList;
    private List<Post> posts;
    private DatabaseReference postsRef;
    private Post post;
    RecyclerViewAdapter recyclerViewAdapter;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_feeds, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        posts = new ArrayList<Post>();
        recyclerViewAdapter = new RecyclerViewAdapter(getActivity(),posts);
        //posts.add(new Post("dev","devd","devdd","devddd"));

        postsList = (RecyclerView)view.findViewById(R.id.postsList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        postsList.setLayoutManager(linearLayoutManager);
        postsList.setHasFixedSize(true);

        postsList.setAdapter(recyclerViewAdapter);


        postsRef = FirebaseDatabase.getInstance().getReference().child("Posts");

        final HashSet keys = new HashSet();

        postsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    post = postSnapshot.getValue(Post.class);
                    Log.d("Post","post is "+keys);
                    if (!keys.contains(postSnapshot.getKey())){
                        keys.add(postSnapshot.getKey());
                        posts.add(post);
                    }
                }
                recyclerViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //Toast.makeText(getContext(),"Cant Load Posts",Toast.LENGTH_LONG).show();
                Log.d("Firebase",databaseError.toString());
            }
        });



    }
}
