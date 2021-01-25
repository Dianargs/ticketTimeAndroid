package com.example.movie_app;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
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

import java.util.List;
import java.util.Map;

import static kotlin.jvm.internal.Reflection.function;

public class MapsFragment extends Fragment {

    GoogleMap mMap;
    FusedLocationProviderClient client ;
    SupportMapFragment supportMapFragment;
    FirebaseFirestore tmp =  FirebaseFirestore.getInstance();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Initializate view
        View view = inflater.inflate(R.layout.fragment_maps, container, false);

        //initializate map frag
        supportMapFragment = (SupportMapFragment)
                getChildFragmentManager().findFragmentById(R.id.google_map);
        //zoom buttons

        //Initializate location client
        client = LocationServices.getFusedLocationProviderClient(getActivity());

        //check permission
        if(ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getActivity(),
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

        //Async map
      /*  supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                //when map is loaded
                googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {
                        //when clicked on map
                        //initializate marker option
                        MarkerOptions markerOptions = new MarkerOptions();
                        //set position of marker
                        markerOptions.position(latLng);
                        //set title of marker
                        markerOptions.title(latLng.latitude + " : "+latLng.longitude);
                        //remove all marker
                        googleMap.clear();
                        //animating to zoom the marker
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                                latLng ,10
                        ));
                        //Add marker on map
                        googleMap.addMarker(markerOptions);
                    }
                });
            }
        });*/
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
            Toast.makeText(getActivity(),"Permission denied",Toast.LENGTH_SHORT).show();
        }
    }

    private void getCurrentLocation(){
        @SuppressLint("MissingPermission")
        Task<Location> task = client.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                //when sucess
                if(location != null){
                    //sync Map
                    supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(GoogleMap googleMap) {
                            googleMap.getUiSettings().setZoomControlsEnabled(true);
                            LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());

                            //Create Marker options
                           // location_cinemas();
                            //initializate marker option
                            MarkerOptions markerOptions = new MarkerOptions();
                            //set position of marker
                            markerOptions.position(latLng).title("I am here");
                            //set camera
                            //animating to zoom the marker
                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                                    latLng ,10
                            ));
                            //Add marker on map 
                            googleMap.addMarker(markerOptions);
                            //set a marker where you press ?
                          /*  googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                                @Override
                                public void onMapClick(LatLng latLng) {
                                    //when clicked on map
                                    //initializate marker option
                                    MarkerOptions markerOptions = new MarkerOptions();
                                    //set position of marker
                                    markerOptions.position(latLng);
                                    //set title of marker
                                    markerOptions.title(latLng.latitude + " : "+latLng.longitude);
                                    //remove all marker
                                    googleMap.clear();
                                    //animating to zoom the marker
                                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                                            latLng ,10
                                    ));
                                    //Add marker on map
                                    googleMap.addMarker(markerOptions);
                                }
                            });*/
                        }
                    });
                }
            }
        });
    }
    List<Marker> allMarkers;
    public void location_cinemas() {
        // [START get_multiple]
        tmp.collection("cinemas_location")
                .whereEqualTo("location", true)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                               //initMarker(document.getData());
                            }
                        } else {
                            System.out.print("erro");
                             }
                    }
                });
    }
    /*public void initMarker(Map gp){
        allMarkers.add(
                gp.put(markerOptions.position(latLng).title(gp['name']));
        );
    }*/

}
//todo -  add zoom buttons ; add markers from firebase