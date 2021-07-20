package com.example.wip_android.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import com.example.wip_android.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MapScreenshotActivity extends AppCompatActivity {


    private GoogleMap mMap;
    private FloatingActionButton fabTakeScreenshotButton;
    private Fragment switchFragment;
    FragmentTransaction transaction;
    SupportMapFragment supportMapFragment;
    FusedLocationProviderClient fusedClient;
    private final String TAG = this.getClass().getCanonicalName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_screenshot);


        // Action bar settings
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(Html.fromHtml("<font color='#ffffff'>Google Map</font>"));
        }


        // Back button
        assert getSupportActionBar() != null; // null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // show back button

        // Floating Button
        fabTakeScreenshotButton = (FloatingActionButton) findViewById(R.id.fabTakeScreenshotButton);
        fabTakeScreenshotButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takeMapScreenshot();
            }
        });

        //Assign values
        supportMapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.google_map);

        //Initialize fussed location
        fusedClient = LocationServices.getFusedLocationProviderClient(this);


        //check permission
        if(ActivityCompat.checkSelfPermission(MapScreenshotActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            //wHEN PERMISSION GRANTED

            // Call method
            getCurrentLocation();

        }else{
//        when permission denied
//            request permission
            ActivityCompat.requestPermissions(MapScreenshotActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},44);
        }

    }


    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }


    // Initializing Snapshot Method
    public void takeMapScreenshot() {
        GoogleMap.SnapshotReadyCallback callback = new GoogleMap.SnapshotReadyCallback() {
            Bitmap bitmap;

            @Override
            public void onSnapshotReady(Bitmap snapshot) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                bitmap = snapshot;

                try {
                    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                    String imageFileName = "JPEG_" + timeStamp + "_";
                    // File storageDir =
                    // Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                    // File file=new
                    // File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),"map.png");

                    saveImage(bitmap, imageFileName);

                    // File image = File.createTempFile(
                    // imageFileName,
                    // ".jpg",
                    // storageDir
                    // );
                    // String currentPhotoPath = image.getAbsolutePath();
                    // Log.d(TAG, "createImageFile: " + image.getAbsolutePath());
                    //
                    // Uri photoURI = FileProvider.getUriForFile(getContext(),
                    // "com.example.android.fileprovider",
                    // image);
                    // Log.d(TAG, "dispatchTakePictureIntent: " + photoURI);
                    // takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    //// startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
                    //
                    // FileOutputStream fout = new FileOutputStream (image);
                    //
                    // bitmap.compress (Bitmap.CompressFormat.PNG,90,fout);
                    Log.d(TAG, "onSnapshotReady: Screenshot captured");

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d(TAG, "onSnapshotReady: Not captured");
                }

            }
        };
        mMap.snapshot(callback);
    }


    private void saveImage(Bitmap bitmap, @NonNull String name) throws IOException {
        boolean saved;
        OutputStream fos;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ContentResolver resolver = this.getContentResolver();
            ContentValues contentValues = new ContentValues();
            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, name);
            contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/png");
            contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, "DCIM/" + "camera");
            Uri imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
            fos = resolver.openOutputStream(imageUri);
        } else {
            String imagesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString()
                    + File.separator + "camera";

            File file = new File(imagesDir);

            if (!file.exists()) {
                file.mkdir();
            }

            File image = new File(imagesDir, name + ".png");
            fos = new FileOutputStream(image);

        }

        saved = bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
        fos.flush();
        fos.close();

//        Dialog box

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!isFinishing()) {
                    new AlertDialog.Builder(MapScreenshotActivity.this).setTitle("Screenshot Captured")
                            .setMessage("Open gallery and choose an image").setCancelable(false)
                            .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            }).show();
                }
            }
        });





    }

    private void getCurrentLocation(){
//initialize task location

        @SuppressLint("MissingPermission") Task<Location> task = fusedClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
//                when success
                if(location != null){
//                    Sync map

                    supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(GoogleMap googleMap) {

                            mMap = googleMap;
                            googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                            setupGoogleMapScreenSetting(googleMap);

//                    Initialize lat lng

                            LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());

//                    Create marker option
                            MarkerOptions options = new MarkerOptions().position(latLng).title("I am Here").snippet("My Current Location");

//                    zoom map
                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,24));

//                            camera position
                            CameraPosition cPosition = CameraPosition.builder().target(latLng).zoom(24).bearing(0).tilt(45)
                                    .build();

//                    add marker on map

                            googleMap.addMarker(options);

                        }
                    });

                }
            }
        });

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 44) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                when permission granted
//                call method
                getCurrentLocation();
            }
        }


    }

    private void setupGoogleMapScreenSetting(GoogleMap gMap) {
        gMap.setBuildingsEnabled(true);
        gMap.setIndoorEnabled(true);
        gMap.setTrafficEnabled(false);

        UiSettings myUiSettings = gMap.getUiSettings();

        myUiSettings.setZoomControlsEnabled(true);
        myUiSettings.setZoomGesturesEnabled(true);
        myUiSettings.setMyLocationButtonEnabled(true);
        myUiSettings.setScrollGesturesEnabled(true);
        myUiSettings.setRotateGesturesEnabled(true);
    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event)
//    {
//        if ((keyCode == KeyEvent.KEYCODE_BACK))
//        {
//            finish();
//        }
//        return super.onKeyDown(keyCode, event);
//    }

}