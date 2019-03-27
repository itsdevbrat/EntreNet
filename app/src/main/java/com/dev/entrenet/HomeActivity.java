package com.dev.entrenet;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class HomeActivity extends AppCompatActivity {

    TextView profile,feeds,post;
    ViewPager viewPager;
    PagerViewAdapter pagerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //initializing variables
        profile = findViewById(R.id.profile);
        feeds = findViewById(R.id.feeds);
        post = findViewById(R.id.post);
        viewPager = findViewById(R.id.viewPager);
        pagerViewAdapter = new PagerViewAdapter(getSupportFragmentManager());

        //setting adapter to the viewPager same like ecycler View
        viewPager.setAdapter(pagerViewAdapter);
        viewPager.setCurrentItem(1);

        //adding oon click listener to the viewPager
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {

                //just changing the color of textView on top so the users knows where he is
                if (i == 0) {
                    profile.setTextColor(getColor(R.color.colorPrimary));
                    post.setTextColor(getColor(R.color.colorPrimaryDark));
                    feeds.setTextColor(getColor(R.color.colorPrimaryDark));
                }
                if (i == 1) {
                    feeds.setTextColor(getColor(R.color.colorPrimary));
                    profile.setTextColor(getColor(R.color.colorPrimaryDark));
                    post.setTextColor(getColor(R.color.colorPrimaryDark));
                }
                if (i == 2) {
                    post.setTextColor(getColor(R.color.colorPrimary));
                    profile.setTextColor(getColor(R.color.colorPrimaryDark));
                    feeds.setTextColor(getColor(R.color.colorPrimaryDark));
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        //assign on click listeners to textview on the top
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(0);
            }
        });

        feeds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(1);
            }
        });

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(2);
            }
        });

    }


}
