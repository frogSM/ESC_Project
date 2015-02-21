package com.esc_project.test;

public class RfidModel {
	
	private String uid;
	private String name;
	private int price;
	private int sum;
	
	public RfidModel() {
		// TODO Auto-generated constructor stub
	}
	
	public void setUid(String uid) {
		this.uid = uid;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setPrice(String price) {
		this.price = Integer.parseInt(price);
	}
	
	public void setSum(String sum) {
		this.sum = Integer.parseInt(sum);
	}
	
	
}
