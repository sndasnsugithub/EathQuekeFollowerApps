package com.daslab.das.eathquekefollowerapps.Activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.daslab.das.eathquekefollowerapps.Model.EarthQUeke;
import com.daslab.das.eathquekefollowerapps.R;
import com.daslab.das.eathquekefollowerapps.UI.CustomInfoWindow;
import com.daslab.das.eathquekefollowerapps.Util.COnstants;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener
,GoogleMap.OnMapClickListener, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private LocationListener locationListener;
    private LocationManager locationManager;
    private RequestQueue queue;
    private AlertDialog.Builder dialogBiulder;
    private AlertDialog dialog;
    private BitmapDescriptor[] alliconColors;
    private Button showListbtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

      //  showListbtn =findViewById(R.id.showDitels);
//        showListbtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//              // startActivity(new Intent(MapsActivity.this,QuekesListViewActivity.class));
//               // startActivity(Intent.clas);
//
//              //  Intent intent = new Intent(MapsActivity.this,QuekesListViewActivity.class);
//              //  startActivity(intent);
//
//            }
//        });



        alliconColors= new BitmapDescriptor[]
                {
                        BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE),
                        BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN),
                        BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW),
                        BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN),
                        BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE),
                        BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA),
                     //   BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED),
                        BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE),
                        BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET),


                };

        queue = Volley.newRequestQueue(this);
        getEarthQuekes();
        // JSONObject parameters = new JSONObject(String.valueOf(this));
    }

    private void getEarthQuekes() {

        final EarthQUeke  earthQUeke = new EarthQUeke();


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, COnstants.URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //TODO: handle success
                try {
                    JSONArray features = response.getJSONArray("features");
                    for (int i = 0; i < COnstants.LIMIT; i++) {
                        JSONObject properties = features.getJSONObject(i).getJSONObject("properties");

                      //  Log.d("Properties",properties.getString("place"));

                        //GET geometry object

                        JSONObject geometry =features.getJSONObject(i).getJSONObject("geometry");
                        //get Coordinates Array

                        JSONArray coordinates = geometry.getJSONArray("coordinates");

                        double lon = coordinates.getDouble(0);
                        double lat = coordinates.getDouble(1);


                       // Log.d("Quake",lon+ " " +lat);

                        earthQUeke.setPlace(properties.getString("place"));
                        earthQUeke.setType(properties.getString("type"));
                        earthQUeke.setTime(properties.getString("time"));
                        earthQUeke.setLan(lat);
                        earthQUeke.setLon(lon);
                        earthQUeke.setMagnitude(properties.getDouble("mag"));
                        earthQUeke.setDetailLink(properties.getString("detail"));

                        java.text.DateFormat  dateFormat = java.text.DateFormat.getDateInstance();
                         String formattedDate= dateFormat.format(new Date(Long.valueOf(properties.getLong("time")))
                                            .getTime());


                      MarkerOptions markerOptions = new MarkerOptions();

                        markerOptions.icon(alliconColors[COnstants.randomInt(alliconColors.length,0)]);
                        markerOptions.title(earthQUeke.getPlace());
                        markerOptions.position(new LatLng(lat,lon));
                        markerOptions.snippet("Magnitude:"+earthQUeke.getMagnitude()+"\n" +"Date: "
                        +formattedDate);

                        //add cricle to markers that have mag > x
                        if (earthQUeke.getMagnitude()>=2.0)
                        {
                            CircleOptions circleOptions = new CircleOptions();
                            circleOptions.center(new LatLng(earthQUeke.getLan(),earthQUeke.getLon()));
                            circleOptions.radius(3000);
                            circleOptions.strokeWidth(3.6f);
                            circleOptions.fillColor(Color.RED);
                            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

                            mMap.addCircle(circleOptions);
                        }

                        Marker marker = mMap.addMarker(markerOptions);
                        marker.setTag(earthQUeke.getDetailLink());
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat,lon),1));




                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                //TODO: handle failure
            }
        });
        queue.add(jsonObjectRequest);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setInfoWindowAdapter(new CustomInfoWindow(getApplicationContext()));
        mMap.setOnInfoWindowClickListener(this);
        mMap.setOnMarkerClickListener(this);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
        if (Build.VERSION.SDK_INT < 23) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                // ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION});

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

            } else {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            //   LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
//
//
//                //  Add a marker in Sydney and move the camera
//
//                mMap.addMarker(new MarkerOptions()
//                        .position(latLng)
//                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
//                        .title("Hello"));
//
//                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 8));

            }
        }

//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                ;
            {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            }
        }
    }


    @Override
    public void onInfoWindowClick(Marker marker) {

        //Toast.makeText(getApplicationContext(),marker.getTag().toString(), Toast.LENGTH_LONG).show();
        getEarthQuekesDetails(marker.getTag().toString());


    }

    private void getEarthQuekesDetails(String url) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url,null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        String detailsUrl="";

                        try {





                            JSONObject propertics = response.getJSONObject("properties");
                            JSONObject products =propertics.getJSONObject("products");
                            JSONArray geoserve = products.getJSONArray("geoserve");

                            for (int i =0 ; i<geoserve.length();i++)
                            {
                                JSONObject geoservejsonObject = geoserve.getJSONObject(i);
                                JSONObject contenetjsonObject  = geoservejsonObject.getJSONObject("contents");
                                JSONObject geojsonObject = contenetjsonObject.getJSONObject("geoserve.json");
                                detailsUrl = geojsonObject.getString("url");


                            }
                           // Log.d("URL",detailsUrl.toString());

                            getMoreDetails(detailsUrl);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        queue.add(jsonObjectRequest);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void onMapClick(LatLng latLng) {

    }

    public void getMoreDetails(String url)
    {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {


                        dialogBiulder =  new AlertDialog.Builder(MapsActivity.this);
                        View view = getLayoutInflater().inflate(R.layout.popup,null);
                        Button dismissButton = view.findViewById(R.id.dismissPop);
                        Button dismissButtonTop = view.findViewById(R.id.dismissPopupTop);
                        TextView popList = view.findViewById(R.id.popList);
                        WebView html = view.findViewById(R.id.htmlAWeb);
                        StringBuilder stringBuilder = new StringBuilder();

                        try {

//                            if (response.has("tectonicSummary") && response.getString("tectonicSummary") != null)
//                            {
//                                JSONObject tectonicSummaryjsonObject =response.getJSONObject("tectonicSummary");
//                                if (tectonicSummaryjsonObject.has("text") && tectonicSummaryjsonObject.get("text") != null)
//                                {
//                                    String text = tectonicSummaryjsonObject.getString("text");
//
//                                    html.loadDataWithBaseURL(null,text,"text/html","UTF-8",null);
//                                }
//                            }
//

                            JSONArray cities = response.getJSONArray("cities");

                            for (int i =0 ; i<cities.length();i++)
                            {
                                JSONObject citiesjsonObject = cities.getJSONObject(i);

                                stringBuilder.append("Cities: " +citiesjsonObject.getString("name")+
                                        "\n"+ "Distance: "+citiesjsonObject.get("distance")
                                +"\n"+ "Population: "+citiesjsonObject.get("population"));

                                stringBuilder.append("\n\n");

                            }

                            dismissButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                }
                            });
                            dismissButtonTop.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                }
                            });
                            popList.setText(stringBuilder);
                            dialogBiulder.setView(view);
                            dialog = dialogBiulder.create();
                            dialog.show();


                        } catch (JSONException e) {


                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(jsonObjectRequest);

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }
}