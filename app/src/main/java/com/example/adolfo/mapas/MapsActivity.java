package com.example.adolfo.mapas;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.OnStreetViewPanoramaReadyCallback;
import com.google.android.gms.maps.StreetViewPanorama;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.Geocoder;
import android.widget.Toast;

import java.util.ArrayList;

import static android.location.LocationManager.NETWORK_PROVIDER;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    // Posicionamiento
    private LocationManager locManager;
    private LocationListener locListener;
    Intent it;

    // Posicion en el mapa
    private Location posicionactual;

    // zoom : va desde 2 ( nivel mas alto) hasta 21 ( a nivel de calle )
    private int zoom = 2;

    // tipo de mapa
    private int tipomapa = 0;

    SupportMapFragment mapFragment;

    ArrayList <LatLng> pos;
    Polyline pol;
    PolylineOptions poli;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        pos = new ArrayList<LatLng>();
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // CONFIGURACION DE GOOGLE MAPS
// Tipo de Mapa
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

// Aparece el botón para situarnos en el mapa mediante un circulo azul y hacer zoo sobre nuestra posición
        mMap.setMyLocationEnabled(true);

// Controles de zoom
        mMap.getUiSettings().setZoomControlsEnabled(true);

// Aparece la brujula cuando giramos el mapa
        mMap.getUiSettings().setCompassEnabled(true);

        mapFragment.getMap().setOnMapClickListener(new OnMapClickListener()
        {
            @Override
            public void onMapClick(LatLng posicion)
            {
                mMap.addMarker(new MarkerOptions()
                        .position(posicion)
                        .title("Marcador creado con onMapClick"));
                pos.add(posicion);
                ruta();
            }
        });


        mMap.setOnMapLongClickListener(new OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng posicion) {
                mMap.addMarker(new MarkerOptions()
                        .position(posicion)
                        .title("Marcador creado con onMapLongClick")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.
                                HUE_BLUE)));
              calle(posicion);
            }
        });



        // Add a marker in Sydney and move the camera
        LatLng granada = new LatLng(37.224929394916394, -3.6339855194091797 );
        mMap.addMarker(new MarkerOptions().position(granada).title("Marker in Maracena"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(granada));


    }

    public void ruta(){
        if(pos.size()==3){
            PolylineOptions poli = new PolylineOptions();
            for(int i=0; i<pos.size();i++){
                poli.add(pos.get(i));
            }
            mMap.addPolyline(poli);
            pos.clear();

        }
    }

    public void calle(LatLng posicion){
        it = new Intent(getApplicationContext(), StreetView.class);
        Toast.makeText(getApplicationContext(),"Posicion: "+posicion,Toast.LENGTH_LONG).show();
        it.putExtra("lat", posicion.latitude);
        it.putExtra("lon", posicion.longitude);
        startActivity(it);
    }

    private void obtenerPosicion() {
        locManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
//Nos registramos para recibir actualizaciones de la posición
        locListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                posicionactual = location;
                Toast.makeText(getApplicationContext(), "Nueva Posicion :" + posicionactual.getLatitude()+" , "+posicionactual.getLongitude(),Toast.LENGTH_LONG).show();
            }
            public void onProviderDisabled(String provider){
            }
            public void onProviderEnabled(String provider){
            }
            public void onStatusChanged(String provider, int status, Bundle extras){
            }
        };


    }

    private void addMarcador(LatLng position, String titulo, String info) {
// Comprueb si hemos obtenido el mapa correctamente
        if (mMap != null) {
            mMap.addMarker(new MarkerOptions()
                    .position(position) // posicion clase LatLon
                    .title(titulo) // titulo
                    .snippet(info) // subtitulo
            );
        }
    }
    public void bt_localizame(View v) {
// Comprobamos si hemos obtenido el MAPA correctamente
        if (mMap != null) {
// Comprobamos si hemos obtenido NUESTRA POSICION ACTUAL ( ULTIMA OBTENIDA )correctamente
            if (posicionactual != null) {
                addMarcador(new LatLng(posicionactual.getLatitude(),
                        posicionactual.getLongitude()),"Titulo : Aqui estamos", "Snippe : Anexo al titulo");
            }
        }
    }

    public void bt_zoom(View v) {
// Comprobamos si hemos obtenido el MAPA correctamente
        if (mMap != null) {
// Obtenemos la posicion de la camara ( donde estamos enfocando actualmente )
            CameraPosition cp = mMap.getCameraPosition();
// Obtenemos su posicion en Latitud , Longitud
            LatLng posicion = cp.target;
// Obtenemos el zoom
            zoom = (int) cp.zoom;
// Aumentamos el Zoom
            if (zoom < 21) {zoom++;};
// Nos situamos en una posicion y le asignamos un zoom
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(posicion, zoom));
        }
    }

    public void bt_tipomapa(View v) {
// Comprobamos si hemos obtenido el MAPA correctamente
        if (mMap != null) {
            tipomapa = (tipomapa + 1) % 4;
            switch(tipomapa)
            {
                case 0:
                    mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                    break;
                case 1:
                    mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                    break;
                case 2:
                    mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                    break;
                case 3:
                    mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                    break;
            }
        }
    }

    public void bt_animateCamera(View v) {
// Comprobamos si hemos obtenido el MAPA correctamente
        if (mMap != null) {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(37.173127,3.6065), 15));
            addMarcador(new LatLng(37.173127, 3.6065), "titulo", "snippe");
        }
    }




}



