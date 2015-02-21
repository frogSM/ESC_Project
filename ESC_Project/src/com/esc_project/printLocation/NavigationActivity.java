package com.esc_project.printLocation;

import java.util.ArrayList;
import java.util.Collection;

import com.esc_project.R;
import com.esc_project.StartActivity;
import com.esc_project.R.id;
import com.esc_project.R.layout;
import com.perples.recosdk.RECOBeacon;
import com.perples.recosdk.RECOBeaconManager;
import com.perples.recosdk.RECOBeaconRegion;
import com.perples.recosdk.RECORangingListener;
import com.perples.recosdk.RECOServiceConnectListener;

import android.R.anim;
import android.content.res.Resources.Theme;
import android.graphics.Movie;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View.MeasureSpec;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.AbsoluteLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class NavigationActivity extends StartActivity implements RECORangingListener, RECOServiceConnectListener {

	protected RECOBeaconManager mBeaconManager;
	protected ArrayList<RECOBeaconRegion> mRegions;

	private BeaconInfo mBeaconInfo;
	private androidToBeacon mAndroidToBeacon;

	// ���� �� �Ÿ� �迭 ���� [BeaconInfo.java]
	private float BeaconDistanceArr[];
	
	private AbsoluteLayout mapLayout;						//���� ���̾ƿ�
	private ImageView mMarker;								//��Ŀ �׸�
	
	private float screenWidth = 0, screenHeight = 0;	//mImage �� ����, ���� ����
	
	int cnt=0;
	TranslateAnimation animation;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_navigation);
		// TODO Auto-generated method stub
		
		actList.add(this);
		
		mBeaconManager = RECOBeaconManager.getInstance(getApplicationContext());
		mBeaconManager.setDiscontinuousScan(true); // �º� ���� ����
		
		mRegions = this.generateBeaconRegion();
		
		mBeaconManager.setRangingListener(this);
		mBeaconManager.bind(this);
		
		BeaconDistanceArr = new float[3];
		mAndroidToBeacon = new androidToBeacon();
		
		mapLayout = (AbsoluteLayout)findViewById(R.id.AbsoluteLayout1);
		mMarker = (ImageView)findViewById(R.id.imageView1);
		
		drawMaker();
		
	}
	

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		mBeaconInfo = new BeaconInfo(this);
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
		recoRegion = new RECOBeaconRegion("24DDF4118CF1440C87CDE368DAF9C93E",
				"RECO Sample Region");
		regions.add(recoRegion);

		return regions;
	}

	@Override
	public void didRangeBeaconsInRegion(Collection<RECOBeacon> beacons,
			RECOBeaconRegion regions) {
		double curPositionX = mAndroidToBeacon.getAndroidX();
		double curPositionY = mAndroidToBeacon.getAndroidY();
		
		// TODO Auto-generated method stub
		Log.i("NavigationActivity",
				"didRangeBeaconsInRegion() region: "
						+ regions.getUniqueIdentifier()
						+ ", number of beacons ranged: " + beacons.size());
		
		/** Į�� ���͸� ���� For�� **/
		for (int revisionCnt = 0; revisionCnt < 5 ; revisionCnt++) {
			mBeaconInfo.updateAllBeacons(beacons);
			mBeaconInfo.selectBeacons(); // ��� ���� Ž��
			BeaconDistanceArr = mBeaconInfo.getBeaconDistanceArr();
			mAndroidToBeacon.setAndroidPosition(BeaconDistanceArr);
			mAndroidToBeacon.setCalmanRevision(revisionCnt);
		}

		//mBeaconInfo.notifyDataSetChanged(); // baseAdapter ����Ҷ�, �� ������Ʈ�� ����ϴ� �޼ҵ�
		
		Log.i("NavigationActivity", "Accuracy[0] : " + BeaconDistanceArr[0]);
		Log.i("NavigationActivity", "Accuracy[1] : " + BeaconDistanceArr[1]);
		Log.i("NavigationActivity", "Accuracy[2] : " + BeaconDistanceArr[2]);
		
		double androidX = mAndroidToBeacon.getAndroidX();
		double androidY = mAndroidToBeacon.getAndroidY();
		double maxWidth = mAndroidToBeacon.getDistanceAB();
		double maxHeight = mAndroidToBeacon.getDistanceAC();
		
		TextView txtX = (TextView)findViewById(R.id.txtViewX);
		txtX.setText("[X] : " +androidX);
		TextView txtY = (TextView)findViewById(R.id.txtViewY);
		txtY.setText("[Y] : " + androidY);
		
		
		if ((androidX > 0 && androidX <= maxWidth)
				&& (androidY > 0 && androidY <= maxHeight)) {
			moveImage((float)(screenWidth / maxWidth * curPositionX), (float)(screenWidth / maxWidth * androidX),
					(float)(screenHeight / maxHeight * curPositionY), (float)(screenHeight / maxHeight * androidY));
		}
	}

	public void onServiceConnect() {
		// TODO Auto-generated method stub
		Log.i("NavigationActivity", "onServiceConnect()");
		this.start(mRegions);
	}

	private void unbind() {
		// TODO Auto-generated method stub
		try {
			mBeaconManager.unbind();
		} catch (RemoteException e) {
			Log.i("NavigationActivity", "Remote Exception");
			e.printStackTrace();
		}
	}

	private void start(ArrayList<RECOBeaconRegion> regions) {
		// TODO Auto-generated method stub
		for (RECOBeaconRegion region : regions) {
			try {
				mBeaconManager.startRangingBeaconsInRegion(region);
			} catch (RemoteException e) {
				Log.i("NavigationActivity", "Remote Exception");
				e.printStackTrace();
			} catch (NullPointerException e) {
				Log.i("NavigationActivity", "Null Pointer Exception");
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
				Log.i("NavigationActivity", "Remote Exception");
				e.printStackTrace();
			} catch (NullPointerException e) {
				Log.i("NavigationActivity", "Null Pointer Exception");
				e.printStackTrace();
			}
		}
	}
	
	public void drawMaker() {
		// ��ũ�� ������ ���ϱ�
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		
		mapLayout.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
		this.screenWidth = mapLayout.getMeasuredWidth() / dm.density;
		this.screenHeight = mapLayout.getMeasuredHeight() / dm.density;
		
		Log.e("NavigationActivity", "Width = " + screenWidth + ", Height = " + screenHeight);
		Log.e("NavigationActivity", "ScreenWidth = " + dm.widthPixels + ", ScreenHeight = " + dm.heightPixels);
	}
	
	public void moveImage(float fromX, final float toX, float fromY, final float toY) {
		// ���� ��Ŀ �̵�.
		animation = new TranslateAnimation(fromX, toX, fromY, toY);
		animation.setDuration(500);
		animation.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
			}
		});
		
		Log.e("NavigationActivity", "[Device]  X: " +mAndroidToBeacon.getAndroidX() + ", Y: " + mAndroidToBeacon.getAndroidY());
		animation.setFillEnabled(true);
		animation.setFillAfter(true);
		mMarker.startAnimation(animation);
	}

}
