package com.dev.entrenet;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<ViewHolderOfPost> {

    List <Post> posts;
    Context context;

    public RecyclerViewAdapter(Context context, List<Post> posts) {
        this.posts = posts;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolderOfPost onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post,parent,false);
        return new ViewHolderOfPost(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderOfPost viewHolderOfPost,final int position) {

        Post post = posts.get(position);

        viewHolderOfPost.username.setText(post.username);
        viewHolderOfPost.title.setText(post.title);
        viewHolderOfPost.desc.setText(post.desc);
        Picasso.get().load(post.userdp).into(viewHolderOfPost.userdp);
        viewHolderOfPost.postCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,"Item Clicked"+position,Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return posts.size();
    }
}
