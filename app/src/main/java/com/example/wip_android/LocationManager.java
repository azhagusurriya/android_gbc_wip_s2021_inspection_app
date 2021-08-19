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
    // FusedLocationProviderClient is a service provided by Google Play services for
    // easier access to
    // APIs and methods that can provide us current location or any locations
    // updates that users have
    private FusedLocationProviderClient fusedLocationProviderClient = null;
    Boolean locationPermissionGranted = false;
    // an identifier for request send in to verify what kind the permission is
    // granted
    final int LOCATION_PERMISSION_REQUEST_CODE = 101;
    private final String[] permissionArray = new String[] { Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION }; // specify what permissions we need

    private LocationRequest locationRequest;
    // use MutableLiveData to keep track of location changes for further uses in
    // MainActivity.class
    MutableLiveData<Location> location = new MutableLiveData<>();

    // make the LocationManager class singleton
    private static final LocationManager ourInstance = new LocationManager();

    // make the LocationManager class singleton
    static LocationManager getInstance() {
        return ourInstance;
    }

    private LocationManager() {
        this.createLocationRequest();
    }

    private void createLocationRequest() {
        this.locationRequest = new LocationRequest();
        this.locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY); // set accuracy of the location,
                                                                                  // depending on what kind of
                                                                                  // permission asked for
        this.locationRequest.setInterval(5000); // refresh locations every 5 seconds
    }

    public void checkPermissions(Context context) {
        this.locationPermissionGranted = (ContextCompat.checkSelfPermission(context.getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED);

        Log.e(TAG, "LocationPermissionGranted " + this.locationPermissionGranted);

        if (!this.locationPermissionGranted) {
            this.requestLocationPermission(context);
        }
    }

    public void requestLocationPermission(Context context) {
        ActivityCompat.requestPermissions((Activity) context, this.permissionArray,
                this.LOCATION_PERMISSION_REQUEST_CODE);
    }

    public FusedLocationProviderClient getFusedLocationProviderClient(Context context) {
        if (fusedLocationProviderClient == null) {
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);
        }

        return fusedLocationProviderClient;
    }

    @SuppressLint("MissingPermission")
    public MutableLiveData<Location> getLastLocation(Context context) {
        if (this.locationPermissionGranted) {
            try {

                this.getFusedLocationProviderClient(context).getLastLocation()
                        .addOnSuccessListener(new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location loc) {
                                if (loc != null) {
                                    location.setValue(loc);
                                    Log.e(TAG, "Last Location ---- Lat : " + location.getValue().getLatitude()
                                            + " Lng : " + location.getValue().getLongitude());
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
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

            return this.location; // return the mutable live data

        } else {
            // explain the user why location is unavailable
            // request for permissions
        }

        return null;
    }

    @SuppressLint("MissingPermission")
    public void requestLocationUpdates(Context context, LocationCallback locationCallback) {
        if (this.locationPermissionGranted) {
            try {
                // suppress add @SUppressLint("Missing Permission") annotation
                this.getFusedLocationProviderClient(context).requestLocationUpdates(this.locationRequest,
                        locationCallback, Looper.getMainLooper());
            } catch (Exception ex) {
                Log.e(TAG, ex.toString());
                Log.e(TAG, ex.getLocalizedMessage());
            }
        }
    }

    // the location is automatically requested for updates every 5s and the call
    // back won't stop by its own.
    // so we need to create a method to stop requesting location updates when the
    // activity/app goes into the background or is paused
    public void stopLocationUpdates(Context context, LocationCallback locationCallback) {
        try {
            this.getFusedLocationProviderClient(context).removeLocationUpdates(locationCallback);
        } catch (Exception ex) {
            Log.e(TAG, ex.toString());
            Log.e(TAG, ex.getLocalizedMessage());
        }
    }
}
