package com.example.adolfo.mapas;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.OnStreetViewPanoramaReadyCallback;
import com.google.android.gms.maps.StreetViewPanorama;
import com.google.android.gms.maps.StreetViewPanoramaFragment;
import com.google.android.gms.maps.StreetViewPanoramaView;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Created by adolf on 17/01/2017.
 */

public class StreetView extends FragmentActivity implements OnStreetViewPanoramaReadyCallback {

    Bundle extra;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_street);
        StreetViewPanoramaFragment st = (StreetViewPanoramaFragment) getFragmentManager().findFragmentById(R.id.streetviewpanorama);
        st.getStreetViewPanoramaAsync(this);

    }

    @Override
    public void onStreetViewPanoramaReady(StreetViewPanorama Panorama) {
        extra = getIntent().getExtras();
        if(extra!=null){
            Panorama.setPosition(new LatLng(extra.getDouble("lat"),extra.getDouble("lon")));
        }
    }
}
