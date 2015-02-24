package com.esc_project.printLocation;

public class BeaconInfo {
	
	private int major;
	private int minor;
	private int rssi;
	private double accuracy;
	
	public BeaconInfo(int major, int minor, int rssi, int accuracy) {
		// TODO Auto-generated constructor stub
		this.major = major;
		this.minor = minor;
		this.rssi = rssi;
		this.accuracy = accuracy;
	}
	
	public int getMajor() {
		return major;
	}
	public int getMinor() {
		return minor;
	}
	public int getRssi() {
		return rssi;
	}
	public double getAccuracy() {
		return accuracy;
	}
}
