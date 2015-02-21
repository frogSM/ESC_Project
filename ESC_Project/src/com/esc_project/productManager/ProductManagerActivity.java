package com.esc_project.productManager;


import com.esc_project.R;
import com.esc_project.StartActivity;
import com.esc_project.Connection.SocketHelper;

import android.os.Bundle;

public class ProductManagerActivity extends StartActivity {
	
	ProductManager productManager;
	
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_calculation);
	    // TODO Auto-generated method stub
	    
	    actList.add(this);
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

}
