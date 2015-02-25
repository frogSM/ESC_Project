package com.esc_project.printLocation;

import java.util.ArrayList;
import java.util.Collection;

import android.content.Context;
import android.os.RemoteException;
import android.util.Log;

import com.perples.recosdk.RECOBeacon;
import com.perples.recosdk.RECOBeaconManager;
import com.perples.recosdk.RECOBeaconRegion;
import com.perples.recosdk.RECOServiceConnectListener;

public class BeaconHelper implements RECOServiceConnectListener {
	
	private Context mContext;
	
	private RECOBeaconManager mBeaconManager;
	private ArrayList<RECOBeaconRegion> mRegions;
	private ArrayList<RECOBeacon> mRangedBeacons;
	
	private BeaconInfo GreenBeacon, YellowBeacon, RedBeacon;
	
	// 비콘 각 거리 정보
	private float BeaconDistanceArr[];
	
	public BeaconHelper(Context context) {
		// TODO Auto-generated constructor stub
		mContext = context;
		settingBeacon();
	}
	
	private void settingBeacon() {
		// 널 값 대비 초기화
		GreenBeacon = new BeaconInfo(18250, 18001, 0, 0);
		YellowBeacon = new BeaconInfo(18250, 18001, 0, 0);
		RedBeacon = new BeaconInfo(18250, 18001, 0, 0);
		
		mBeaconManager = RECOBeaconManager.getInstance(mContext);
		mBeaconManager.setDiscontinuousScan(true);

		mRegions = generateBeaconRegion();
		mBeaconManager.bind(this);
		
		Log.i("BeaconHelper", "settingBeacon");
	}
	
	public ArrayList<BeaconInfo> getBeaconInfo(Collection<RECOBeacon> beacons) {
		ArrayList<BeaconInfo> classifiedBeacons = new ArrayList<BeaconInfo>();
		
		updateAllBeacons(beacons);
		
		for (RECOBeacon recoBeacon : mRangedBeacons) {
			switch(recoBeacon.getMinor()) {
			case 18001 :
				GreenBeacon = new BeaconInfo(recoBeacon.getMajor(), recoBeacon.getMinor(), recoBeacon.getRssi(), revisionAccuracy(recoBeacon));
				break;
			case 18002 :
				YellowBeacon = new BeaconInfo(recoBeacon.getMajor(), recoBeacon.getMinor(), recoBeacon.getRssi(), revisionAccuracy(recoBeacon));
				break;
			case 18003 :
				RedBeacon = new BeaconInfo(recoBeacon.getMajor(), recoBeacon.getMinor(), recoBeacon.getRssi(), revisionAccuracy(recoBeacon));
				break;
			}
		}
		
		classifiedBeacons.add(0, GreenBeacon);
		classifiedBeacons.add(1, YellowBeacon);
		classifiedBeacons.add(2, RedBeacon);

		Log.i("BeaconHelper", "getBeaconInfo");
		
		return classifiedBeacons;
	}

	public RECOBeaconManager getBeaconManager() {
		return mBeaconManager;
	}
	
	public void stopAndUnbind() {
		this.stop(mRegions);
		this.unbind();
	}
	
	private ArrayList<RECOBeaconRegion> generateBeaconRegion() {
		ArrayList<RECOBeaconRegion> regions = new ArrayList<RECOBeaconRegion>();

		RECOBeaconRegion recoRegion;
		recoRegion = new RECOBeaconRegion("24DDF4118CF1440C87CDE368DAF9C93E",
				"RECO Sample Region");
		regions.add(recoRegion);

		return regions;
	}
	
	private void start(ArrayList<RECOBeaconRegion> regions) {
		// TODO Auto-generated method stub
		for (RECOBeaconRegion region : regions) {
			try {
				mBeaconManager.startRangingBeaconsInRegion(region);
			} catch (RemoteException e) {
				Log.i("BeaconHelper", "Remote Exception");
				e.printStackTrace();
			} catch (NullPointerException e) {
				Log.i("BeaconHelper", "Null Pointer Exception");
				e.printStackTrace();
			}
		}
	}

	private void stop(ArrayList<RECOBeaconRegion> regions) {
		// TODO Auto-generated method stub
		for (RECOBeaconRegion region : regions) {
			try {
				mBeaconManager.stopRangingBeaconsInRegion(region);
			} catch (RemoteException e) {
				Log.i("BeaconHelper", "Remote Exception");
				e.printStackTrace();
			} catch (NullPointerException e) {
				Log.i("BeaconHelper", "Null Pointer Exception");
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onServiceConnect() {
		// TODO Auto-generated method stub
		Log.i("BeaconHelper", "onServiceConnect()");
		mBeaconManager.setDiscontinuousScan(true);
		this.start(mRegions);
	}
	
	private void unbind() {
		// TODO Auto-generated method stub
		try {
			mBeaconManager.unbind();
			Log.i("BeaconHelper", "unbind");
		} catch (RemoteException e) {
			Log.i("BeaconHelper", "Remote Exception");
			e.printStackTrace();
		}
	}
	
	public void updateAllBeacons(Collection<RECOBeacon> beacons) {
		synchronized (beacons) {
			mRangedBeacons = new ArrayList<RECOBeacon>(beacons);
		}
	}
	
	private float revisionAccuracy(RECOBeacon recoBeacon) {
		String distance;
		float distanceValue = 0;
		
		distance = String.format("%.2f", recoBeacon.getAccuracy());

		if (Double.parseDouble(distance) != 0.0
				&& Double.parseDouble(distance) != -1.0)
			distanceValue = Float.parseFloat(distance);

		return distanceValue;
	}
}
