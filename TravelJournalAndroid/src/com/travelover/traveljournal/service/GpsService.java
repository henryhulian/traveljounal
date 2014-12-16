package com.travelover.traveljournal.service;

import java.util.Iterator;

import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

public class GpsService {
	
	private  LocationManager lm;
	
	private final String TAG = "GpsService";

	public  void init( Context context) {
		
		lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		
		if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			Toast.makeText(context, "OPEN GPS PLEASE...", Toast.LENGTH_LONG).show();
			Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
			context.startActivity(intent);
			return;
		}
		
        String bestProvider = lm.getBestProvider(getCriteria(), true);
        Location location= lm.getLastKnownLocation(bestProvider);    
        ServiceDelegate.getCouchbaseService().recordNode( location );
        
        lm.addGpsStatusListener(listener);
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 1, locationListener);
	}
	
    private LocationListener locationListener=new LocationListener() {
        
        public void onLocationChanged(Location location) {
        	ServiceDelegate.getCouchbaseService().recordNode( location );
        }
        
        public void onStatusChanged(String provider, int status, Bundle extras) {
            switch (status) {

            case LocationProvider.AVAILABLE:
                Log.i(TAG, "GPS AVAILABLE");
                break;

            case LocationProvider.OUT_OF_SERVICE:
                Log.i(TAG, "GPS OUT_OF_SERVICE");
                break;

            case LocationProvider.TEMPORARILY_UNAVAILABLE:
                Log.i(TAG, "GPS TEMPORARILY_UNAVAILABLE");
                break;
            }
        }
    
        public void onProviderEnabled(String provider) {
            Location location=lm.getLastKnownLocation(provider);
            ServiceDelegate.getCouchbaseService().recordNode( location );
        }
    
        public void onProviderDisabled(String provider) {
        }

    
    };
    
    GpsStatus.Listener listener = new GpsStatus.Listener() {
        public void onGpsStatusChanged(int event) {
            switch (event) {

            case GpsStatus.GPS_EVENT_FIRST_FIX:
                Log.i(TAG, "GPS_EVENT_FIRST_FIX");
                break;

            case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
                Log.i(TAG, "GPS_EVENT_SATELLITE_STATUS");

                GpsStatus gpsStatus=lm.getGpsStatus(null);
                int maxSatellites = gpsStatus.getMaxSatellites();
                Iterator<GpsSatellite> iters = gpsStatus.getSatellites().iterator();
                int count = 0;     
                while (iters.hasNext() && count <= maxSatellites) {     
                    @SuppressWarnings("unused")
					GpsSatellite s = iters.next();     
                    count++;     
                }   
                Log.i(TAG,"FIND Satellite COUNT:"+count);
                break;

            case GpsStatus.GPS_EVENT_STARTED:
                Log.i(TAG, "GPS_EVENT_STARTED");
                break;

            case GpsStatus.GPS_EVENT_STOPPED:
                Log.i(TAG, "GPS_EVENT_STOPPED");
                break;
            }
        };
    };
    
    
    private Criteria getCriteria(){
        Criteria criteria=new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);    
        criteria.setSpeedRequired(false);
        criteria.setCostAllowed(false);
        criteria.setBearingRequired(false);
        criteria.setAltitudeRequired(false);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        return criteria;
    }
}
