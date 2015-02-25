package com.esc_project.printLocation;

import java.util.ArrayList;
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

	private RECOBeaconManager mBeaconManager;
	private BeaconHelper mBeaconHelper;
	private PositionCalculation mPositionCalculation;
	
	/** 이전 안드로이드 X, Y 값 **/
	private double NowPosition_X;
	private double NowPosition_Y;
	
	/** 갱신 후 보정된 안드로이드 X, Y 값 **/
	private double RevisionPosition_X;
	private double RevisionPosition_Y;
	private double RevisionPosition_Z;
	
	/** 최대 넓이와 높이 제한 **/
	private double maxWidth;
	private double maxHeight;

	/** 맵 관련 전역변수 **/
	private AbsoluteLayout mapLayout;		//지도 레이아웃
	private ImageView mMarker;				//마커 그림
	private float screenWidth = 0;			// mImage 뷰 가로 길이
	private float screenHeight = 0;			// mImage 뷰 세로 길이
	
	/** 애니메이션 전역변수 **/
	private TranslateAnimation animation;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_navigation);
		// TODO Auto-generated method stub
		
		actList.add(this);
		
		mBeaconHelper = new BeaconHelper(getApplicationContext());
		mBeaconManager = mBeaconHelper.getBeaconManager();
		mBeaconManager.setRangingListener(this);
		
		mPositionCalculation = new PositionCalculation();
		
		mapLayout = (AbsoluteLayout)findViewById(R.id.AbsoluteLayout1);
		mMarker = (ImageView)findViewById(R.id.imageView1);
		
		drawMaker();
		
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		mBeaconHelper = new BeaconHelper(getApplicationContext());
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		mBeaconHelper.stopAndUnbind();
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mBeaconHelper.stopAndUnbind();
	}
	
	@Override
	public void didRangeBeaconsInRegion(Collection<RECOBeacon> beacons,
			RECOBeaconRegion regions) {
		// TODO Auto-generated method stub
		Log.i("NavigationActivity",
				"didRangeBeaconsInRegion() region: "
						+ regions.getUniqueIdentifier()
						+ ", number of beacons ranged: " + beacons.size());
		
		NowPosition_X = mPositionCalculation.getAndroidPosition_X();
		NowPosition_Y = mPositionCalculation.getAndroidPosition_Y();
		
		/** 칼만 보정 값 **/
		for(int revisionCnt=0 ; revisionCnt<5 ; revisionCnt++) {
			mPositionCalculation.setAndroidPosition(mBeaconHelper.getBeaconInfo(beacons));
			mPositionCalculation.setCalmanRevision(revisionCnt);
		}
		
		RevisionPosition_X = mPositionCalculation.getAndroidPosition_X();
		RevisionPosition_Y = mPositionCalculation.getAndroidPosition_Y();
		RevisionPosition_Z = mPositionCalculation.getAndroidPosition_Z();
		maxWidth = mPositionCalculation.getDistanceGY();
		maxHeight = mPositionCalculation.getDistanceGR();
		
		if ((RevisionPosition_X > 0 && RevisionPosition_X <= maxWidth)
				&& (RevisionPosition_Y > 0 && RevisionPosition_Y <= maxHeight)) {
			moveImage((float)(screenWidth / maxWidth * NowPosition_X), (float)(screenWidth / maxWidth * RevisionPosition_X),
					(float)(screenHeight / maxHeight * NowPosition_Y), (float)(screenHeight / maxHeight * RevisionPosition_Y));
		}
		
		Log.i("PositionCalculation", "[RevisionAfter] X="+ RevisionPosition_X+" Y="+RevisionPosition_Y +" Z="+ RevisionPosition_Z);
		TextView txtX = (TextView)findViewById(R.id.txtViewX);
		txtX.setText("[X] : " +RevisionPosition_X);
		TextView txtY = (TextView)findViewById(R.id.txtViewY);
		txtY.setText("[Y] : " + RevisionPosition_Y);
	}

	/** 현재 Map을 그린 레이아웃 정보 받기 **/
	public void drawMaker() {
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		
		mapLayout.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
		this.screenWidth = mapLayout.getMeasuredWidth() / dm.density;
		this.screenHeight = mapLayout.getMeasuredHeight() / dm.density;
		
		Log.i("NavigationActivity", "Width = " + screenWidth + ", Height = " + screenHeight);
		Log.i("NavigationActivity", "ScreenWidth = " + dm.widthPixels + ", ScreenHeight = " + dm.heightPixels);
	}
	
	/** Map Marker 이동 함수 **/
	public void moveImage(float fromX, final float toX, float fromY, final float toY) {
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
		
//		Log.e("NavigationActivity", "[Device]  X: " +mAndroidToBeacon.getAndroidX() + ", Y: " + mAndroidToBeacon.getAndroidY());
		animation.setFillEnabled(true);
		animation.setFillAfter(true);
		mMarker.startAnimation(animation);
	}

}
