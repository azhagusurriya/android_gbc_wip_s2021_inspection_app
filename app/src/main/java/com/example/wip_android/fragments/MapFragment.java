package com.example.wip_android.fragments;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.wip_android.R;
import com.example.wip_android.viewmodels.MapViewModel;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    MapView mMapView;
    View mView;
    private final String TAG = this.getClass().getCanonicalName();
    private Geocoder geocoder;
    private final Float DEFAULT_ZOOM = 15.0F;
    private LocationManager locationManager;
    private LocationCallback locationCallback;
    private LatLng currentLocation;


    private MapViewModel mViewModel;

    public static MapFragment newInstance() {
        return new MapFragment();
    }




    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,
                              Bundle savedInstanceState) {
        mView =  inflater.inflate(R.layout.fragment_map, container, false);

        return mView;
    }

    @Override
    public void onViewCreated(@NonNull View view,  Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mMapView = (MapView) mView.findViewById(R.id.map);
        if(mMapView != null){
            mMapView.onCreate(null);
            mMapView.onResume();
            mMapView.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady( GoogleMap googleMap) {
        MapsInitializer.initialize(getContext());
        mMap = googleMap;
        googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

        LatLng waterloo = new LatLng(43.4643, -80.516670);
        LatLng address = getLocationFromAddress(getActivity(),"43 melbourne crescent,waterloo,ontario,canada");
        googleMap.addMarker(new MarkerOptions()
                .position(address)
                .title("Marker in Address").snippet("My Home"));

        googleMap.addMarker(new MarkerOptions()
                .position(waterloo)
                .title("Marker in Waterloo").snippet("My Home"));

        CameraPosition cPosition = CameraPosition.builder().target(address).zoom(16).bearing(0).tilt(45).build();

        // Move the camera to the marker
        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cPosition));

        // Zoom in, animating the camera.
       // googleMap.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(MapViewModel.class);
        // TODO: Use the ViewModel

    }

    public LatLng getLocationFromAddress(FragmentActivity context, String strAddress) {
        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;

        try {
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }
            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();

            p1 = new LatLng(location.getLatitude(), location.getLongitude());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return p1;


    }
}
