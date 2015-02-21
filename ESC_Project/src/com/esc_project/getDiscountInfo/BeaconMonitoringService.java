package com.esc_project.getDiscountInfo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import com.esc_project.R;
import com.esc_project.StartActivity;
import com.esc_project.R.drawable;
import com.perples.recosdk.RECOBeaconManager;
import com.perples.recosdk.RECOBeaconRegion;
import com.perples.recosdk.RECOBeaconRegionState;
import com.perples.recosdk.RECOMonitoringListener;
import com.perples.recosdk.RECOServiceConnectListener;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

public class BeaconMonitoringService extends Service implements
		RECOServiceConnectListener, RECOMonitoringListener {

	private long mScanDuration = 1 * 1000L;
	private long mSleepDuration = 1 * 1000L;
	private long mRegionExpirationTime = 3 * 1000L;
	private int mNotificationID = 9999;

	private RECOBeaconManager mRecoManager;
	private ArrayList<RECOBeaconRegion> mRegions;

	// 영역별 한번만 출력할 수 있게끔 하는 변수
	private int greenCnt = 0, yellowCnt = 0, redCnt = 0;

	public void onCreate() {
		Log.i("BeaconMonitoringService", "onCreate()");
		super.onCreate();
		
		mRecoManager = RECOBeaconManager.getInstance(getApplicationContext());
//		mRecoManager.setDiscontinuousScan(true);
	}

	public int onStartCommand(Intent intent, int flags, int startId) {
		this.bindService();
		Log.i("BeaconMonitoringService", "onStartCommand()");
		return START_STICKY;
	};

	public void onDestroy() {
		Log.i("BeaconMonitoringService", "onDestroy()");
		this.tearDown();
		super.onDestroy();
	}

	public void onResume() {
		Log.i("BeaconMonitoringService", "onResume()");
	}

	private void bindService() {
		mRegions = new ArrayList<RECOBeaconRegion>();
		this.generateBeaconRegion();

		mRecoManager.setMonitoringListener(this);
		Log.i("BeaconMonitoringService", "bindService()");

		if (mRecoManager.isBound() == true)
			Log.i("BeaconMonitoringService", "-ing");
		else {
			Log.i("BeaconMonitoringService", "no");
			mRecoManager.bind(this);
		}
	}

	private void generateBeaconRegion() {
		RECOBeaconRegion greenRegion;
		RECOBeaconRegion yellowRegion;
		RECOBeaconRegion redRegion;

		greenRegion = new RECOBeaconRegion(StartActivity.RECO_UUID, 18250,
				18001, "Green Region");
		greenRegion.setRegionExpirationTimeMillis(mRegionExpirationTime);
		yellowRegion = new RECOBeaconRegion(StartActivity.RECO_UUID, 18250,
				18002, "Yellow Region");
		yellowRegion.setRegionExpirationTimeMillis(mRegionExpirationTime);
		redRegion = new RECOBeaconRegion(StartActivity.RECO_UUID, 18250, 18003,
				"Red Region");
		redRegion.setRegionExpirationTimeMillis(mRegionExpirationTime);

		mRegions.add(greenRegion);
		mRegions.add(yellowRegion);
		mRegions.add(redRegion);
	}

	private void tearDown() {
		Log.i("BeaconMonitoringService", "tearDown()");
		this.stopMonitoring();

		try {
			mRecoManager.unbind();
		} catch (RemoteException e) {
			Log.e("BeaconMonitoringService",
					"RemoteException has occured while executing unbind()");
			e.printStackTrace();
		}
	}

	@Override
	public void didDetermineStateForRegion(RECOBeaconRegionState arg0,
			RECOBeaconRegion arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void didEnterRegion(RECOBeaconRegion region) {
		Intent intent;
		// TODO Auto-generated method stub
		// this.popupNotification("Inside of " + region.getUniqueIdentifier());

		switch (region.getUniqueIdentifier()) {
		case "Green Region":
			if (greenCnt != 1) { 
				intent = new Intent(this, RegionEnterActivity_Green.class);
				intent.putExtra("Enter", "비콘 영역에 들어왔습니다. ^0^");
				intent.addFlags(intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
				greenCnt++;
			}
			break;

		case "Yellow Region":
			if (yellowCnt != 1) {
				intent = new Intent(this, RegionEnterActivity_Yellow.class);
				intent.putExtra("Enter", "비콘 영역에 들어왔습니다. ^0^");
				intent.addFlags(intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
				yellowCnt++;
			}
			break;

		case "Red Region":
			if (redCnt != 1) {
				intent = new Intent(this, RegionEnterActivity_Red.class);
				intent.putExtra("Enter", "비콘 영역에 들어왔습니다. ^0^");
				intent.addFlags(intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
				redCnt++;
			}
			break;
		}
	}

	@Override
	public void didExitRegion(RECOBeaconRegion region) {
		// TODO Auto-generated method stub
		// this.popupNotification("Outside of " + region.getUniqueIdentifier());

		// Intent intent = new Intent(this, RegionEnterActivity.class);
		// intent.putExtra("Exit", "비콘 영역에서 나갔습니다.ㅠ.ㅠ");
		// intent.addFlags(intent.FLAG_ACTIVITY_NEW_TASK);
		// startActivity(intent);
	}

	@Override
	public void didStartMonitoringForRegion(RECOBeaconRegion arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onServiceConnect() {
		// TODO Auto-generated method stub
		Log.i("BeaconMonitoringService", "Beacon Connect success!!");
		this.startMonitoring();
	}

	private void startMonitoring() {
		// TODO Auto-generated method stub
		mRecoManager.setScanPeriod(mScanDuration);
		mRecoManager.setSleepPeriod(mSleepDuration);

		for (RECOBeaconRegion region : mRegions) {
			try {
				region.getMinor();
				mRecoManager.startMonitoringForRegion(region);
			} catch (RemoteException e) {
				Log.e("BeaconMonitoringService",
						"RemoteException has occured while executing RECOManager.startMonitoringForRegion()");
				e.printStackTrace();
			} catch (NullPointerException e) {
				Log.e("BeaconMonitoringService",
						"NullPointerException has occured while executing RECOManager.startMonitoringForRegion()");
				e.printStackTrace();
			}
		}
	}

	private void stopMonitoring() {
		Log.i("RECOBackgroundMonitoringService", "stopMonitoring()");

		for (RECOBeaconRegion region : mRegions) {
			try {
				mRecoManager.stopMonitoringForRegion(region);
			} catch (RemoteException e) {
				Log.e("BeaconMonitoringService",
						"RemoteException has occured while executing RECOManager.stopMonitoringForRegion()");
				e.printStackTrace();
			} catch (NullPointerException e) {
				Log.e("BeaconMonitoringService",
						"NullPointerException has occured while executing RECOManager.stopMonitoringForRegion()");
				e.printStackTrace();
			}
		}
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

}
