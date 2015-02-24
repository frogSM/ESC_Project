package com.esc_project.printLocation;

import java.util.Collection;

import com.esc_project.R;
import com.esc_project.StartActivity;
import com.perples.recosdk.RECOBeacon;
import com.perples.recosdk.RECOBeaconManager;
import com.perples.recosdk.RECOBeaconRegion;
import com.perples.recosdk.RECORangingListener;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View.MeasureSpec;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.AbsoluteLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class NavigationActivity extends StartActivity implements RECORangingListener {

	protected RECOBeaconManager mBeaconManager;
	private BeaconHelper mBeaconHelper;

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
		
		mBeaconManager.setRangingListener(this);
		mBeaconHelper = new BeaconHelper(getApplicationContext());
		
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
