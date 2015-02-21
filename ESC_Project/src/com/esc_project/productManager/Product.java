package com.esc_project.productManager;

public class Product {

	String uid ;
	String name;
	String price;
	
	public Product(String uid, String name, String price) {
		// TODO Auto-generated constructor stub
		this.uid = uid;
		this.name = name;
		this.price = price;
	}
	
	public void setUid(String uid) {
		this.uid = uid;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setPrice(String price) {
		this.price = price;
	}
	
	public String getUid() {
		return uid;
	}
	
	public String getName() {
		return name;
	}
	
	public String getPrice() {
		return price;
	}
}
