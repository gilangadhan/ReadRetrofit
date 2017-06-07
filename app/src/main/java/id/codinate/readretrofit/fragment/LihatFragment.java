/*
 * Copyright (c) 2017. Gilang Ramadhan (gilangramadhan96.gr@gmail.com)
 */

package id.codinate.readretrofit.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;

import id.codinate.readretrofit.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class LihatFragment extends Fragment  implements OnMapReadyCallback {

    private GoogleMap mMap;
    MapView mapView;
    public LihatFragment() {
        // Required empty public constructor
    }

    String id, uuid, jenis_pohon, usia_pohon, kondisi_pohon, latitude, longitude, foto_pohon, tgl, keterangan;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        HashMap<String, String> data = new HashMap<String, String>();
        data = (HashMap<String, String>) getArguments().getSerializable("hashmap");

        id = data.get("id");
        uuid = data.get("uuid");
        jenis_pohon = data.get("jenis_pohon");
        usia_pohon = data.get("usia_pohon");
        kondisi_pohon = data.get("kondisi_pohon");
        latitude = data.get("latitude");
        longitude = data.get("longitude");
        foto_pohon = data.get("foto_pohon");
        tgl = data.get("tgl");
        keterangan = data.get("keterangan");

        Toast.makeText(getActivity(), usia_pohon, Toast.LENGTH_SHORT).show();
        View view = inflater.inflate(R.layout.fragment_lihat, container, false);
        mapView = (MapView)   view.findViewById(R.id.maps);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(this);

        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));

//        LatLng sydney = new LatLng(-7.0511601,110.4321671);

        mMap.addMarker(new MarkerOptions().position(sydney).title(jenis_pohon));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 15.0f));
    }
}
