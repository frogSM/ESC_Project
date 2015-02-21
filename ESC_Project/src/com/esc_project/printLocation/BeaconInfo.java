package com.esc_project.printLocation;

import java.util.ArrayList;
import java.util.Collection;

import com.esc_project.R;
import com.esc_project.R.id;
import com.esc_project.R.layout;
import com.perples.recosdk.RECOBeacon;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class BeaconInfo extends BaseAdapter {
	private ArrayList<RECOBeacon> mRangedBeacons;
	private LayoutInflater mLayoutInflater;
	
	// 비콘 각 거리 배열 정보 [BeaconInfo.java]
	private float BeaconDistanceArr[];
	
	public BeaconInfo(Context context) {
		super();
		
		BeaconDistanceArr = new float[3];
		mRangedBeacons = new ArrayList<RECOBeacon>();
		mLayoutInflater = LayoutInflater.from(context);
	}
	
	public float[] getBeaconDistanceArr() {
		return BeaconDistanceArr;
	}
	
	public void updateBeacon(RECOBeacon beacon) {
		synchronized (mRangedBeacons) {
			if(mRangedBeacons.contains(beacon)) {
				mRangedBeacons.remove(beacon);
			}
			mRangedBeacons.add(beacon);
		}
	}
	
	public void updateAllBeacons(Collection<RECOBeacon> beacons) {
		synchronized (beacons) {
			mRangedBeacons = new ArrayList<RECOBeacon>(beacons);
		}
	}

	public void selectBeacons() {
		for (RECOBeacon recoBeacon : mRangedBeacons) {
			
			switch (recoBeacon.getMinor()) {
			// Green
			case 18001:
				BeaconDistanceArr[0] = getDistance(recoBeacon);
				break;

			// Yellow
			case 18002:
				BeaconDistanceArr[1] = getDistance(recoBeacon);
				break;

			// Red
			case 18003:
				BeaconDistanceArr[2] = getDistance(recoBeacon);
				break;

			}
		}
	}
	
	private float getDistance(RECOBeacon recoBeacon) {
		String distance;
		float distanceValue = 0;
		
		distance = String.format("%.2f", recoBeacon.getAccuracy());

		if (Double.parseDouble(distance) != 0.0
				&& Double.parseDouble(distance) != -1.0)
			distanceValue = Float.parseFloat(distance);

		return distanceValue;
	}
	
	public void clear() {
		mRangedBeacons.clear();
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mRangedBeacons.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mRangedBeacons.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder;
		
		if(convertView == null) {
			convertView = mLayoutInflater.inflate(R.layout.beacon_distance, parent, false);
			viewHolder = new ViewHolder();
			viewHolder.beaconDistance = (TextView)convertView.findViewById(R.id.beacondistance);
			viewHolder.beaconRssi = (TextView)convertView.findViewById(R.id.beaconrssi);
			
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder)convertView.getTag();
		}
		
		RECOBeacon recoBeacon = mRangedBeacons.get(position);
		String distance;
		
		switch(recoBeacon.getMinor()) {
		// Green
		case 18001 :
			BeaconDistanceArr[0] = getDistance(recoBeacon);
			break;

		// Yellow
		case 18002 :
			BeaconDistanceArr[1] = getDistance(recoBeacon);
			break;
			
		// Red	
		case 18003 :
			BeaconDistanceArr[2] = getDistance(recoBeacon);
			break;
		}
		
		viewHolder.beaconDistance.setText("Beacon[" + recoBeacon.getMinor() + "] " +
				"거리 : " +String.format("%.2f", recoBeacon.getAccuracy()));
		viewHolder.beaconRssi.setText("Beacon[" + recoBeacon.getMinor() + "] " +
				"RSSI : " + recoBeacon.getRssi());
		
		return convertView;
	}
	
	static class ViewHolder {
		TextView beaconDistance;
		TextView beaconRssi;
	}

}
