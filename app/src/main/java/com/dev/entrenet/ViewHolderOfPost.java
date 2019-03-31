package com.dev.entrenet;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;



public class ViewHolderOfPost extends RecyclerView.ViewHolder {

    public TextView username,title,desc;
    public ImageView userdp;
    public CardView postCardView;

    public ViewHolderOfPost(@NonNull View itemView) {
        super(itemView);

        username = (TextView)itemView.findViewById(R.id.username);
        title = (TextView)itemView.findViewById(R.id.title);
        desc = (TextView)itemView.findViewById(R.id.desc);
        userdp = (ImageView) itemView.findViewById(R.id.userdp);
        postCardView = (CardView) itemView.findViewById(R.id.postcardview);
    }
}
