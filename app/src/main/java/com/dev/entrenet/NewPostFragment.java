package com.dev.entrenet;


import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;



public class NewPostFragment extends Fragment {

  private EditText posttitle,postdesc;
  private FirebaseAuth auth;
  private DatabaseReference db;
  private Button postbutton;
  private FusedLocationProviderClient fusedLocationClient;
  private double latitude;
  private double longitude;
  private AlphaAnimation animation1;

    @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_new_post, null);
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
    fusedLocationClient.getLastLocation()
            .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
              @Override
              public void onSuccess(Location location) {
                  if(location!= null){
                      latitude = location.getLatitude();
                      longitude = location.getLongitude();
                  }
              }
            });

    db = FirebaseDatabase.getInstance().getReference();
    auth = FirebaseAuth.getInstance();
    posttitle = (EditText) getView().findViewById(R.id.posttitle);
    postdesc = (EditText) getView().findViewById(R.id.postdesc);
    postbutton = (Button) getView().findViewById(R.id.postbutton);

      animation1 = new AlphaAnimation(0.2f, 1.0f);
      animation1.setDuration(1000);
      animation1.setStartOffset(5000);
      animation1.setFillAfter(true);

    postbutton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
          postbutton.startAnimation(animation1);
        Post post = new Post(auth.getCurrentUser().getDisplayName(),posttitle.getText().toString(),postdesc.getText().toString(),auth.getCurrentUser().getPhotoUrl().toString(),latitude,longitude,auth.getCurrentUser().getEmail(),auth.getCurrentUser().getUid());
        db.child("Posts").push().setValue(post);
        Fragment feedsFragment = new FeedsFragment();
        getFragmentManager().beginTransaction().replace(R.id.feedsfragment,feedsFragment).commit();
        posttitle.setText("");
        postdesc.setText("");
      }
    });
  }
}