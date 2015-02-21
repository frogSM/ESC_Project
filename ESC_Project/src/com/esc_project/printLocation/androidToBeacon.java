package com.esc_project.printLocation;

import android.util.Log;

public class androidToBeacon {
	// A = Green 비콘
	// B = Yellow 비콘
	// C = Red 비콘
	private double distanceAB = 2.40;
	private double distanceAC = 1.20;
	private double distanceBC;
	private float distanceAtoAndroid, distanceBtoAndroid, distanceCtoAndroid;
	
	// 보정전 안드로이드 X,Y,Z값
	private position revisionBeforeAndroid[];
	
	private position beaconA, beaconB, beaconC, androidDevice;
	
	static class position {
		double X;
		double Y;
		double Z;
	}
	
	public androidToBeacon() {
		beaconA = new position();
		beaconB = new position();
		beaconC = new position();
		androidDevice = new position();
		
		revisionBeforeAndroid = new position[5];
		for(int i=0 ; i<5 ; i++) {
			revisionBeforeAndroid[i] = new position();
		}
		
		distanceBC = (float)Math.sqrt(Math.pow(distanceAB, 2) + Math.pow(distanceAC, 2));
		
		this.setBeaconPosition();
		
		Log.i("androidToBeacon", "[BeaconA] x="+ beaconA.X+" y="+beaconA.Y+" z="+beaconA.Z);
		Log.i("androidToBeacon", "[BeaconB] x="+ beaconB.X+" y="+beaconB.Y+" z="+beaconB.Z);
		Log.i("androidToBeacon", "[BeaconC] x="+ beaconC.X+" y="+beaconC.Y+" z="+beaconC.Z);
	}
	
	private void setBeaconPosition() {
		beaconA.X = 0;
		beaconA.Y = 0;
		beaconA.Z = 0;
		
		beaconB.X = distanceAB;
		beaconB.Y = 0;
		beaconB.Z = 0;
		
		beaconC.X = 0;
		beaconC.Y = distanceAC;
//		beaconC.X = (Math.pow(distanceAC, 2) - Math.pow(distanceBC, 2) + Math.pow(beaconB.X, 2)) / (2 * beaconB.X);
//		beaconC.Y = Math.sqrt(Math.abs(Math.pow(distanceAC, 2) - Math.pow(beaconC.X, 2)));
		beaconC.Z = 0;
		
		Log.e("beaconPosition", "distanceBC : " + distanceBC);
		Log.e("beaconPosition", "BeaconA : (" + beaconA.X + ", " +beaconA.Y + ", " + beaconA.Z + ")");
		Log.e("beaconPosition", "BeaconB : (" + beaconB.X + ", " +beaconB.Y + ", " + beaconB.Z + ")");
		Log.e("beaconPosition", "BeaconC : (" + beaconC.X + ", " +beaconC.Y + ", " + beaconC.Z + ")");
	}
	
	public void setAndroidPosition(float[] BeaconDistanceArr) {

		this.setDistanceAndroidToBeacon(BeaconDistanceArr);
		
			androidDevice.X = ((Math.pow(distanceAtoAndroid, 2)
					- Math.pow(distanceBtoAndroid, 2) + Math.pow(beaconB.X, 2))
					/ (2 * beaconB.X));
			
			androidDevice.Y = ((Math.pow(beaconC.X, 2) + Math.pow(beaconC.Y, 2)
							+ Math.pow(distanceAtoAndroid, 2)
							- Math.pow(distanceCtoAndroid, 2) - (2 * androidDevice.X * beaconC.X))
							/ (2 * beaconC.Y));
			
			androidDevice.Z = (Math.sqrt(Math.abs(Math.pow(
					distanceAtoAndroid, 2)
					- Math.pow(androidDevice.X, 2)
					- Math.pow(androidDevice.Y, 2))));
			
		Log.i("androidToBeacon", "[AndroidDevice] x="+ androidDevice.X+" y="+androidDevice.Y+" z="+androidDevice.Z);
	}

	private void setDistanceAndroidToBeacon(float[] BeaconDistanceArr) {
		distanceAtoAndroid = BeaconDistanceArr[0];
		distanceBtoAndroid = BeaconDistanceArr[1];
		distanceCtoAndroid = BeaconDistanceArr[2];
	}
	
	public void setCalmanRevision(int index) {
		
		revisionBeforeAndroid[index].X = this.androidDevice.X;
		revisionBeforeAndroid[index].Y = this.androidDevice.Y;
		revisionBeforeAndroid[index].Z = this.androidDevice.Z;
		
		if(index == 4) {
			position temp = new position();
			temp.X = 0;
			temp.Y = 0;
			temp.Z = 0;
			
			for(int i=0 ; i<5 ; i++) {
				switch(i) {
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
			
			androidDevice.X = temp.X;
			androidDevice.Y = temp.Y;
			androidDevice.Z = temp.Z;
			
		}
		
	}
	
	public double getAndroidX() {
		return androidDevice.X;
	}
	
	public double getAndroidY() {
		return androidDevice.Y;
	}
	
	public double getDistanceAB() {
		return distanceAB;
	}
	public double getDistanceAC() {
		return distanceAC;
	}
	public double getDistanceBC() {
		return distanceBC;
	}
}
