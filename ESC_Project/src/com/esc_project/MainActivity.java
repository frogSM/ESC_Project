package com.esc_project;

import java.util.Collection;

import com.esc_project.R;
import com.esc_project.printLocation.BeaconHelper;
import com.esc_project.printLocation.TempBeaconInfo;
import com.esc_project.printLocation.androidToBeacon;
import com.perples.recosdk.*;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

public class MainActivity extends StartActivity implements RECORangingListener {
	
	public static MainActivity mContext;
	
	private RECOBeaconManager mBeaconManager;
	private BeaconHelper mBeaconHelper2;
	
	private TempBeaconInfo mBeaconInfo;
	private androidToBeacon mAndroidToBeacon;
	
	private ListView mRegionListView;
	
	// 비콘 각 거리 배열 정보 [BeaconInfo.java]
	private float BeaconDistanceArr[];

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		actList.add(this);
		
		mContext = this;
		
		mBeaconHelper2 = new BeaconHelper(getApplicationContext());
		mBeaconManager = mBeaconHelper2.getBeaconManager();
	    mBeaconManager.setRangingListener(this);
		
		BeaconDistanceArr = new float[3];
		mAndroidToBeacon = new androidToBeacon();
		
		mRegionListView = (ListView)findViewById(R.id.listview);
		
		Log.i("MainActivity", "onCreate()");
	}	
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		mBeaconHelper2 = new BeaconHelper(getApplicationContext());
		mBeaconInfo = new TempBeaconInfo(this);
		mRegionListView.setAdapter(mBeaconInfo);
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		mBeaconHelper2.stopAndUnbind();
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mBeaconHelper2.stopAndUnbind();
	}
	

	@Override
	public void didRangeBeaconsInRegion(Collection<RECOBeacon> beacons,
			RECOBeaconRegion regions) {
		// TODO Auto-generated method stub
		
		Log.i("MainActivity", "didRangeBeaconsInRegion() region: " + regions.getUniqueIdentifier() + ", number of beacons ranged: " + beacons.size());
		mBeaconInfo.updateAllBeacons(beacons);
		
		BeaconDistanceArr = mBeaconInfo.getBeaconDistanceArr();
		mAndroidToBeacon.setAndroidPosition(BeaconDistanceArr);

		mBeaconInfo.notifyDataSetChanged();
		
	}

}
