package com.app.wearapp;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.wearapp.R;
import com.app.wearapp.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;

public class SendMessage extends WearableActivity implements LocationListener {
    private static final String TAG = SendMessage.class.getName();
    private Location mLocation;
    private LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);

        // Enables Always-on
        setAmbientEnabled();
        sendMessage();
    }

    public boolean wifiIsActive()
    {
        ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        return mWifi.isConnected();

    }

    public boolean connectivityVerification(){
        // Determine whether you have an internet connection
        ConnectivityManager cm1 =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm1.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        // Determine the type of internet connection
        ConnectivityManager cm2 =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean isMetered = cm2.isActiveNetworkMetered();

        return isConnected;
    }

    public boolean locationAccessIsGranted()
    {
        return
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED &&
                        ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) ==
                                PackageManager.PERMISSION_GRANTED;
    }

    public void sendMessage()
    {
        if (!wifiIsActive()) {
            return;
        }

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        try {
            if (locationAccessIsGranted()) {
                locationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER, 0, 0, this
                );

                mLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            } else {
                ActivityCompat.requestPermissions(this, new String[] {
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION },
                        1337);
            }

        } catch (SecurityException e) {
            Log.e(TAG, e.getMessage());
        }

        if (!locationAccessIsGranted()) {
            return;
        }

        try {
            RequestQueue queue = Volley.newRequestQueue(this);
            String message = "message ...";
            String lorem = "...";

            DecimalFormat df = new DecimalFormat("0.00");

            double latitude = Double.parseDouble(df.format(mLocation.getLatitude()));
            double longitude = Double.parseDouble(df.format(mLocation.getLongitude()));

            JSONObject jsonBody = new JSONObject();
            jsonBody.put("student_id", 20192020);
            jsonBody.put("gps_lat", latitude);
            jsonBody.put("gps_long", longitude);
            jsonBody.put("student_message", lorem);

            final String requestBody = jsonBody.toString();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.SERVER_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Toast.makeText(SendMessage.this,
                                    "Message sent successfully",
                                    Toast.LENGTH_LONG
                            ).show();
//                            if (response.code() == 200) {
//                                // Do awesome stuff
//                            } else if(response.code() == 500){
//                                Toast.makeText(this, "Error: internal server error", Toast.LENGTH_SHORT).show();
//                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    NetworkResponse response = error.networkResponse;
                    if (error instanceof ServerError && response != null) {
                        try {
                            String res = new String(response.data,
                                    HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                            // Now you can use any deserializer to make sense of data
                            JSONObject obj = new JSONObject(res);
                        } catch (UnsupportedEncodingException e1) {
                            // Couldn't properly decode data to string
                            e1.printStackTrace();
                        } catch (JSONException e2) {
                            // returned data is not JSONObject?
                            e2.printStackTrace();
                        }
                    }
                }
            }) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return requestBody == null ? null : requestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                        return null;
                    }
                }

                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    String responseString = "";
                    if (response != null) {
                        responseString = String.valueOf(response.statusCode);
                        // can get more details such as response.headers
                    }
                    return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
                }
            };

            queue.add(stringRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onProviderEnabled(String provider)
    {
        Log.d("Latitude","enable");
    }

    @Override
    public void onProviderDisabled(String provider)
    {
        Log.d("Latitude","disable");
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras)
    {
        Log.d("Latitude","status");
    }

    @Override
    public void onLocationChanged(Location location)
    {
        mLocation = location;
    }
}
