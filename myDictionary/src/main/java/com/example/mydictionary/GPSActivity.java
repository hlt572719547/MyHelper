package com.example.mydictionary;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;
import android.widget.TextView;

@SuppressLint("HandlerLeak")
public class GPSActivity extends Activity {
    public LocationManager locationmanager;
    TextView t;
    Handler h;
    int i = 1;
    boolean b = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.gps_activity);
        h = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                // TODO Auto-generated method stub
                switch (msg.what) {
                case (1):
                    t.setText("正在定位.");
                    break;
                case (2):
                    t.setText("正在定位..");
                    break;
                case (3):
                    t.setText("正在定位...");
                    break;
                case (4):
                    t.setText("正在定位....");
                    break;
                case (5):
                    t.setText("正在定位.....");
                    break;
                case (6):
                    t.setText("正在定位......");
                    break;

                }
            }

        };

        final LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                updateWithNewLocation(location);
            }

            public void onProviderDisabled(String provider) {
                updateWithNewLocation(null);
            }

            public void onProviderEnabled(String provider) {
            }

            public void onStatusChanged(String provider, int status,
                    Bundle extras) {
            }
        };

        String serviceName = Context.LOCATION_SERVICE;
        locationmanager = (LocationManager) getSystemService(serviceName);
        // String provider = LocationManager.GPS_PROVIDER;

        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setCostAllowed(true);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        String provider = locationmanager.getBestProvider(criteria, true);

        Location location = locationmanager.getLastKnownLocation(provider);
        updateWithNewLocation(location);
        locationmanager.requestLocationUpdates(provider, 2000, 10,
                locationListener);

    }

    private void updateWithNewLocation(Location location1) {

        t = (TextView) findViewById(R.id.textViewgps);
        if (location1 != null) {
            double lat = location1.getLatitude();
            double lng = location1.getLongitude();
            t.setText("经度:" + lat + "\n" + "维度:" + lng);
            b = false;

        } else {
            new Thread(new Runnable() {

                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    while (b) {
                        Message m = new Message();
                        m.what = i;
                        if (i < 6) {
                            i++;
                        }
                        if (i == 6) {
                            i = 1;
                        }
                        h.sendMessage(m);
                        try {
                            Thread.sleep(400);
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }

            }).start();

        }

    }
}
