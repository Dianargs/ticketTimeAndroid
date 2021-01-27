package com.example.movie_app;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class MapsFragment extends Fragment {

    GoogleMap googleMap;
    FusedLocationProviderClient client ;
    SupportMapFragment supportMapFragment;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    List<MarkerOptions> markerList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Initializate view
        View view = inflater.inflate(R.layout.fragment_maps, container, false);

        try {
            //set time in mili
            //to do firebase stuff before running
            initMarkers();
            Thread.sleep(2000);

        }catch (Exception e){
            e.printStackTrace();
        }


        //initializate map frag
        supportMapFragment = (SupportMapFragment)
                getChildFragmentManager().findFragmentById(R.id.google_map);
        //zoom buttons

        //Initializate location client
        client = LocationServices.getFusedLocationProviderClient(getActivity());

        markerList = new ArrayList<>();

        //check permission
        if(ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED ){
            //when permission is granted
            getCurrentLocation();
        }else{
            //when permission denied
            //Requeest permission
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION
                    ,Manifest.permission.ACCESS_COARSE_LOCATION},100);
        }

        return view;
    }

     @Override
   public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //check condition
        if(requestCode == 100 && (grantResults.length>0) && (grantResults[0] + grantResults[1]== PackageManager.PERMISSION_GRANTED)){
            //when perssion are granted
            //call method
           getCurrentLocation();

        }else{
            //when permission are denied
            //display toast
            Toast.makeText(getContext(),"Permission denied",Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("MissingPermission")
    private void getCurrentLocation(){

        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
            //when location service is enable
            //get Last
            client.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    Location location = task.getResult();
                    if(location != null){
                        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                            @Override
                            public void onMapReady(GoogleMap googleMap) {
                                googleMap.getUiSettings().setZoomControlsEnabled(true);
                                LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());
                                //initializate marker option
                                MarkerOptions markerOptions = new MarkerOptions();

                               Log.d("ESTOU" , markerList.toString());
                                //set position of marker
                                markerOptions.position(latLng).title("I am here");
                                //set camera
                                //animating to zoom the marker
                                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                                        latLng ,10
                                ));
                                //Add marker on map
                                googleMap.addMarker(markerOptions);
                            }
                        });
                    }else{
                        LocationRequest locationRequest = new LocationRequest()
                                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                                .setInterval(10000)
                                .setFastestInterval(1000)
                                .setNumUpdates(1);

                        LocationCallback locationCallback = new LocationCallback(){

                           @Override
                            public void onLocationResult(LocationResult locationResult){
                           }
                        };
                        client.requestLocationUpdates(locationRequest,locationCallback, Looper.myLooper());
                    }
                }
            });
        }
    }
    private void initMarkers(){

        db.collection("cinemas_location").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for (QueryDocumentSnapshot doc : task.getResult()){
                        GeoPoint latLng = doc.getGeoPoint("location");
                        String name = doc.getString("name");
                        Log.d("Aquiii",String.valueOf(latLng.getLatitude()));
                        Log.d("AquiNome",name);
                        LatLng tmp =  new LatLng(latLng.getLatitude(),latLng.getLongitude());
                        //googleMap.addMarker(new MarkerOptions().position(tmp).title(name));
                        markerList.add(new MarkerOptions().position(new LatLng( latLng.getLatitude(),latLng.getLongitude())).title(name));
                    }
                    //mMap.addMarker(markerList);

                }
            }
        });
    }


}
