package com.dev.entrenet;

import android.Manifest;
import android.content.Context;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

public class FeedsFragment extends Fragment {

    RecyclerView postsList;
    private List<Post> posts;
    private DatabaseReference postsRef;
    private Post post;
    RecyclerViewAdapter recyclerViewAdapter;
    FirebaseAuth auth;
    FusedLocationProviderClient fusedLocationProviderClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private double userLat, userLng;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //Location
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());

        //Set a location Request
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(15000);
        locationRequest.setFastestInterval(10000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        //defining location callback which will be called once location is fetched
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult != null) {
                    Toast.makeText(getActivity(), "loc " + locationResult.getLastLocation().getLatitude(), Toast.LENGTH_SHORT).show();
                    userLat = locationResult.getLastLocation().getLatitude();
                    userLng = locationResult.getLastLocation().getLongitude();
                    Log.d("devbrat", "userloc lat" + userLng +" lng "+ userLat);
                }
            }
        };

        LocationManager manager = (LocationManager) getActivity().getSystemService(HomeActivity.LOCATION_SERVICE);
        if (!manager.isProviderEnabled(manager.GPS_PROVIDER)){
            turnGpsOn();
            Log.d("devbrat", "userloc lat" + userLng +" lng "+ userLat);
        }

        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);

        return inflater.inflate(R.layout.fragment_feeds, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Firebase
        auth = FirebaseAuth.getInstance();
        postsRef = FirebaseDatabase.getInstance().getReference().child("Posts");

        //RecyclerView Setup
        posts = new ArrayList<Post>();
        recyclerViewAdapter = new RecyclerViewAdapter(getActivity(), posts);
        postsList = (RecyclerView) view.findViewById(R.id.postsList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        postsList.setLayoutManager(linearLayoutManager);
        postsList.setHasFixedSize(true);
        postsList.setAdapter(recyclerViewAdapter);

        //avoid posts repetition
        final HashSet keys = new HashSet();

        postsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    post = postSnapshot.getValue(Post.class);
                    Log.d("devbrat", "post is " + keys);
                    if (!keys.contains(postSnapshot.getKey()) && !post.userid.equals(auth.getCurrentUser().getUid())) {
                        keys.add(postSnapshot.getKey());
                        posts.add(post);
                    }
                }
                calculateDistance(posts);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //Toast.makeText(getContext(),"Cant Load Posts",Toast.LENGTH_LONG).show();
                Log.d("devbrat", databaseError.toString());
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        //stop updates once activity goes to background
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
    }

    private void turnGpsOn() {

        //Building a dialog to ask the user to turn gps on
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        Task<LocationSettingsResponse> result = LocationServices.getSettingsClient(getActivity()).checkLocationSettings(builder.build());

        //handle callbacks
        result.addOnSuccessListener(new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                Toast.makeText(getActivity(), "GPS ON", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ResolvableApiException) {
                    try {
                        ((ResolvableApiException) e).startResolutionForResult(getActivity(), 1);
                    } catch (IntentSender.SendIntentException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });
    }

    private void calculateDistance(final List<Post> posts) {

            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location userLoc) {
                    for (final Post post : posts) {
                        Location postLoc = new Location("");
                        postLoc.setLatitude(post.latitude);
                        postLoc.setLongitude(post.longitude);
                        post.distance = userLoc.distanceTo(postLoc) / 1000;
                        Toast.makeText(getActivity(), "dis " + post.distance, Toast.LENGTH_SHORT).show();
                        Log.d("devbrat", "userdistance is" + post.distance);
                    }
                    Collections.sort(posts, new Comparator<Post>() {
                        @Override
                        public int compare(Post o1, Post o2) {
                            return Double.compare(o1.distance, o2.distance);
                        }
                    });
                    recyclerViewAdapter.notifyDataSetChanged();
                }
            });

//        Location userLoc = new Location("");
//        userLoc.setLatitude(userLat);
//        userLoc.setLongitude(userLng);
    }
}
