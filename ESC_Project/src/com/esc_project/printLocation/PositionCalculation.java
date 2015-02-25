package com.esc_project.printLocation;

import java.util.ArrayList;

import com.esc_project.printLocation.androidToBeacon.position;

import android.util.Log;

public class PositionCalculation {
	
	private Position GreenBeacon, YelloBeacon, RedBeacon;
	private Position AndroidDevice;
	
	// 보정 전 안드로이드 좌표
	private Position revisionBeforeAndroid[];
	
	private double distance_GY = 1.2;
	private double distance_GR = 0.6;
	private double distance_YR;
	
	private class Position {
		double X;
		double Y;
		double Z;
	}
	
	public PositionCalculation() {
		// TODO Auto-generated constructor stub
		GreenBeacon = new Position();
		YelloBeacon = new Position();
		RedBeacon = new Position();
		AndroidDevice = new Position();
		
		revisionBeforeAndroid = new Position[5];
		for(int i=0 ; i<5 ; i++) {
			revisionBeforeAndroid[i] = new Position();
		}
		Log.i("PositionCalculation", "PositionCalculation()");
		settingBeaconPosition();
	}
	
	private void settingBeaconPosition() {
		GreenBeacon.X = 0;
		GreenBeacon.Y = 0;
		GreenBeacon.Z = 0;
		
		YelloBeacon.X = distance_GY;
		YelloBeacon.Y = 0;
		YelloBeacon.Z = 0;
		
		RedBeacon.X = 0;
		RedBeacon.Y = distance_GR;
//		beaconC.X = (Math.pow(distanceAC, 2) - Math.pow(distanceBC, 2) + Math.pow(beaconB.X, 2)) / (2 * beaconB.X);
//		beaconC.Y = Math.sqrt(Math.abs(Math.pow(distanceAC, 2) - Math.pow(beaconC.X, 2)));
		RedBeacon.Z = 0;
		
		distance_YR = (float)Math.sqrt(Math.pow(distance_GY, 2) + Math.pow(distance_GR, 2));
		
		Log.e("PositionCalculation", "distance_YR : " + distance_YR);
		Log.e("PositionCalculation", "GreenBeacon : (" + GreenBeacon.X + ", " +GreenBeacon.Y + ", " + GreenBeacon.Z + ")");
		Log.e("PositionCalculation", "YellowBeacon : (" + YelloBeacon.X + ", " +YelloBeacon.Y + ", " + YelloBeacon.Z + ")");
		Log.e("PositionCalculation", "RedBeacon : (" + RedBeacon.X + ", " +RedBeacon.Y + ", " + RedBeacon.Z + ")");
	}
	
	public void setAndroidPosition(ArrayList<BeaconInfo> rangedbeacons) {
		float Android_GreenBeacon = rangedbeacons.get(0).getAccuracy();
		float Android_YellowBeacon = rangedbeacons.get(1).getAccuracy();
		float Android_RedBeacon = rangedbeacons.get(2).getAccuracy();
		
		
		AndroidDevice.X = ((Math.pow(Android_GreenBeacon, 2)
				- Math.pow(Android_YellowBeacon, 2) + Math.pow(YelloBeacon.X, 2))
				/ (2 * YelloBeacon.X));
		
		AndroidDevice.Y = ((Math.pow(RedBeacon.X, 2) + Math.pow(RedBeacon.Y, 2)
						+ Math.pow(Android_GreenBeacon, 2)
						- Math.pow(Android_RedBeacon, 2) - (2 * AndroidDevice.X * RedBeacon.X))
						/ (2 * RedBeacon.Y));
		
		AndroidDevice.Z = (Math.sqrt(Math.abs(Math.pow(
				Android_GreenBeacon, 2)
				- Math.pow(AndroidDevice.X, 2)
				- Math.pow(AndroidDevice.Y, 2))));
	}
	
	public void setCalmanRevision(int index) {

		revisionBeforeAndroid[index].X = AndroidDevice.X;
		revisionBeforeAndroid[index].Y = AndroidDevice.Y;
		revisionBeforeAndroid[index].Z = AndroidDevice.Z;

		if (index == 4) {
			Position temp = new Position();
			temp.X = 0;
			temp.Y = 0;
			temp.Z = 0;

			for (int i = 0; i < 5; i++) {
				switch (i) {
				case 0:
					temp.X += (revisionBeforeAndroid[i].X * 0.45);
					temp.Y += (revisionBeforeAndroid[i].Y * 0.45);
					temp.Z += (revisionBeforeAndroid[i].Z * 0.45);
					break;
				case 1:
					temp.X += (revisionBeforeAndroid[i].X * 0.25);
					temp.Y += (revisionBeforeAndroid[i].Y * 0.25);
					temp.Z += (revisionBeforeAndroid[i].Z * 0.25);
					break;
				case 2:
					temp.X += (revisionBeforeAndroid[i].X * 0.1);
					temp.Y += (revisionBeforeAndroid[i].Y * 0.1);
					temp.Z += (revisionBeforeAndroid[i].Z * 0.1);
					break;
				case 3:
					temp.X += (revisionBeforeAndroid[i].X * 0.05);
					temp.Y += (revisionBeforeAndroid[i].Y * 0.05);
					temp.Z += (revisionBeforeAndroid[i].Z * 0.05);
					break;
				case 4:
					temp.X += (revisionBeforeAndroid[i].X * 0);
					temp.Y += (revisionBeforeAndroid[i].Y * 0);
					temp.Z += (revisionBeforeAndroid[i].Z * 0);
					break;
				}
			}

			AndroidDevice.X = temp.X;
			AndroidDevice.Y = temp.Y;
			AndroidDevice.Z = temp.Z;
		}
		

	}
	
	public double getAndroidPosition_X() {
		return AndroidDevice.X;
	}
	
	public double getAndroidPosition_Y() {
		return AndroidDevice.Y;
	}
	
	public double getAndroidPosition_Z() {
		return AndroidDevice.Z;
	}
	
	public double getDistanceGY() {
		return distance_GY;
	}
	
	public double getDistanceGR() {
		return distance_GR;
	}
	
	public double getDistanceYR() {
		return distance_YR;
	}
	
}
