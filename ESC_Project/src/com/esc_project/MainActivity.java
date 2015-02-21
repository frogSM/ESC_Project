package com.esc_project;

import java.util.ArrayList;
import java.util.Collection;

import com.esc_project.R;
import com.esc_project.printLocation.BeaconInfo;
import com.esc_project.printLocation.androidToBeacon;
import com.perples.recosdk.*;

import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.widget.ListView;

public class MainActivity extends StartActivity implements RECORangingListener, RECOServiceConnectListener{
	
	public static MainActivity mContext;
	
	protected ArrayList<RECOBeaconRegion> mRegions;
	protected RECOBeaconManager mBeaconManager;
	
	private BeaconInfo mBeaconInfo;
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
		
		mBeaconManager = RECOBeaconManager.getInstance(getApplicationContext());
		mBeaconManager.setDiscontinuousScan(true); // 태블릿 버그 수정
		
		mRegions = this.generateBeaconRegion();
		
		mBeaconManager.setRangingListener(this);
		mBeaconManager.bind(this);
		
		BeaconDistanceArr = new float[3];
		mAndroidToBeacon = new androidToBeacon();
		
		mRegionListView = (ListView)findViewById(R.id.listview);
		
		Log.i("MainActivity", "onCreate()");
	}	
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		mBeaconInfo = new BeaconInfo(this);
		mRegionListView.setAdapter(mBeaconInfo);
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	
	public void stopAndUnbind() {
		this.stop(mRegions);
		this.unbind();
	}

	private ArrayList<RECOBeaconRegion> generateBeaconRegion() {
		ArrayList<RECOBeaconRegion> regions = new ArrayList<RECOBeaconRegion>();
		
		RECOBeaconRegion recoRegion;
		recoRegion = new RECOBeaconRegion("24DDF4118CF1440C87CDE368DAF9C93E", "RECO Sample Region");
		regions.add(recoRegion);

		return regions;
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

	public void onServiceConnect() {
		// TODO Auto-generated method stub
		Log.i("MainActivity", "onServiceConnect()");
		this.start(mRegions);
	}

	private void unbind() {
		// TODO Auto-generated method stub
		try {
		mBeaconManager.unbind();
		} catch(RemoteException e) {
			Log.i("MainActivity", "Remote Exception");
			e.printStackTrace();
		}
	}
	
	private void start(ArrayList<RECOBeaconRegion> regions) {
		// TODO Auto-generated method stub
		for(RECOBeaconRegion region : regions) {
			try {
				mBeaconManager.startRangingBeaconsInRegion(region);
			} catch (RemoteException e) {
				Log.i("MainActivity", "Remote Exception");
				e.printStackTrace();
			} catch (NullPointerException e) {
				Log.i("MainActivity", "Null Pointer Exception");
				e.printStackTrace();
			}
		}
	}
	
	private void stop(ArrayList<RECOBeaconRegion> regions) {
		// TODO Auto-generated method stub
		for(RECOBeaconRegion region : regions) {
			try {
				mBeaconManager.stopRangingBeaconsInRegion(region);
			} catch (RemoteException e) {
				Log.i("MainActivity", "Remote Exception");
				e.printStackTrace();
			} catch (NullPointerException e) {
				Log.i("MainActivity", "Null Pointer Exception");
				e.printStackTrace();
			}
		}
	}

}
