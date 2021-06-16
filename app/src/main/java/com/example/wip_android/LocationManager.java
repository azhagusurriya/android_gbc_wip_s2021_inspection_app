package com.example.wip_android;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

public class LocationManager {
    private final String TAG = this.getClass().getCanonicalName();
    private FusedLocationProviderClient fusedLocationProviderClient = null;
    public Boolean locationPermissionGranted = false;
    public final int LOCATION_PERMISSION_REQUEST_CODE = 101;
    private final String[] permissionArray = new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION};

    private LocationRequest locationRequest;
    MutableLiveData<Location> location = new MutableLiveData<>();

    private static final LocationManager ourInstance = new LocationManager();

    static LocationManager getInstance() {
        return ourInstance;
    }

    public LocationManager() {
        this.createLocationRequest();
    }

    //1. Initial location
    private void createLocationRequest(){
        this.locationRequest = new LocationRequest();
        this.locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY); // high accuracy
        this.locationRequest.setInterval(5000); //5 inteval for updating seconds location
    }

    //2. Check Permission
    public void checkPermissions(Context context){
        this.locationPermissionGranted = (ContextCompat.checkSelfPermission(context.getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED);

        Log.e(TAG, "LocationPermissionGranted " + this.locationPermissionGranted);

        if (!this.locationPermissionGranted) {
            this.requestLocationPermission(context);
        }
    }

    //3. request location permission -> MainActivity 4.
    public void requestLocationPermission(Context context) {
        ActivityCompat.requestPermissions((Activity) context, this.permissionArray, this.LOCATION_PERMISSION_REQUEST_CODE);
    }

    // 6. Call location provider for real time location information
    public FusedLocationProviderClient getFusedLocationProviderClient(Context context) {
        if (fusedLocationProviderClient == null) {
            //Singleton
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);
        }

        return fusedLocationProviderClient;
    }

    // 7. Get current location => MainActivity 8
    @SuppressLint("MissingPermission")
    public MutableLiveData<Location> getLastLocation(Context context) {
        if (this.locationPermissionGranted) {
            try {

                this.getFusedLocationProviderClient(context)
                        .getLastLocation()
                        .addOnSuccessListener(new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location loc) {
                                if (loc != null) {
                                    location.setValue(loc);
                                    Log.e(TAG, "Last Location ---- Lat : " + location.getValue().getLatitude() +
                                            " Lng : " + location.getValue().getLongitude());
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e(TAG, e.toString());
                                Log.e(TAG, e.getLocalizedMessage());
                            }
                        });

            } catch (Exception ex) {
                Log.e(TAG, ex.getLocalizedMessage());
                return null;
            }

            return this.location;

        } else {
            //explain the user why location is unavailable
            //request for permissions
        }

        return null;
    }

    //10. location update  (initial to last location for moving ) => MainActivity 11
    @SuppressLint("MissingPermission")
    public void requestLocationUpdates(Context context, LocationCallback locationCallback) {
        if (this.locationPermissionGranted) {
            try {
                this.getFusedLocationProviderClient(context).requestLocationUpdates(this.locationRequest, locationCallback, Looper.getMainLooper());
            } catch (Exception ex) {
                Log.e(TAG, ex.toString());
                Log.e(TAG, ex.getLocalizedMessage());
            }
        }
    }

    //15 stop location update
    public void stopLocationUpdates(Context context, LocationCallback locationCallback) {
        try {
            this.getFusedLocationProviderClient(context).removeLocationUpdates(locationCallback);
        } catch (Exception ex) {
            Log.e(TAG, ex.toString());
            Log.e(TAG, ex.getLocalizedMessage());
        }
    }
}
