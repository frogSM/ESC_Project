package com.esc_project.test;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Random;

import android.content.Context;
import android.hardware.usb.UsbManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.esc_project.Constants;
import com.esc_project.R;
import com.esc_project.StartActivity;
import com.esc_project.Connection.JsonHelper;
import com.esc_project.Connection.SocketHelper;
import com.esc_project.productManager.Product;
import com.esc_project.productManager.ProductManager;

public class TestActivity extends StartActivity {

	TestListAdapter mTestListAdapter;
	
	SocketHelper mSocketHelper;
	subAsyncTask mSubAsyncTask;
	
	private List<String> mScanUid;
	
	ProductManager mProductManager;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_test);
	    
	    actList.add(this);
	    
//	    mRfidController = new RfidController(getApplicationContext());
	    
	    Log.e("TestActivity", "onCreate success!");
	    
//	    ListView listview = new ListView(this);
//	    mTestListAdapter = new TestListAdapter(this);
//	    listview.setAdapter(mTestListAdapter);
	    
	   mScanUid = new ArrayList<String>();
	   mSocketHelper = mSocketHelper.getInstance(getApplicationContext());
	   
	   mSubAsyncTask = new subAsyncTask();
	   mSubAsyncTask.execute();
	    
	}
	
	private class subAsyncTask extends AsyncTask<Void, Void, Void> {
		
		@Override
		protected Void doInBackground(Void... message) {
			// TODO Auto-generated method stub
			
//			TEST_Method();

			// String형 List = RFID UID
			ArrayList<String> sendData = new ArrayList<String>();
			for (int i = 0; i < mScanUid.size(); i++) {
				sendData.add(mScanUid.get(i));
			}

			/** 처리부분 **/
			JsonHelper jsonhelper = JsonHelper.getInstance(getApplicationContext());
			String str_json = jsonhelper.makeJsonMessage(Constants.Uid_Info, sendData);
			mSocketHelper.sendMessage(mHandler, str_json);
			/**처리부분 **/

//			TEST_Method_CLEAN();

			return null;
		}
		
	}
	
	Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			if(msg.what == 0) {
				ArrayList<Product> test = (ArrayList<Product>)msg.obj;
				// Product객체를 List로 받아온 것을 이용해서 baseAdapter이용해야함.
			}
		}
	};
}
