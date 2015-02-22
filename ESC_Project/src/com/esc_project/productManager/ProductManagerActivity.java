package com.esc_project.productManager;

import java.util.ArrayList;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ListView;

import com.esc_project.Constants;
import com.esc_project.R;
import com.esc_project.StartActivity;
import com.esc_project.Connection.JsonHelper;
import com.esc_project.Connection.SocketHelper;

public class ProductManagerActivity extends StartActivity {

	// Git Hub TEST!!
	ProductListAdaptor productListAdapter;
	
	SocketHelper mSocketHelper;
	subAsyncTask mSubAsyncTask;
	
	ProductManager productManager;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_test);
	    
	    productManager = new ProductManager( this );
	    ArrayList<String> test = new ArrayList ( ); 
	    ListView listview = new ListView(this);
	    productListAdapter = new ProductListAdaptor(this);
	    listview.setAdapter(productListAdapter);
	    
	    productManager.OpenSerialPort();
	    
	    mSocketHelper = mSocketHelper.getInstance(getApplicationContext());
	   
	    mSubAsyncTask = new subAsyncTask();
	    mSubAsyncTask.execute();
	    
	    actList.add(this);
	}
	
	private class subAsyncTask extends AsyncTask<Void, Void, Void> {
		
		@Override
		protected Void doInBackground(Void... message) {
			// TODO Auto-generated method stub
			

			ArrayList<String> taggedUIDs = new ArrayList<String>();
			taggedUIDs = productManager.GetTaggedUIDs();

			/** 처리부분 **/
			JsonHelper jsonhelper = JsonHelper.getInstance(getApplicationContext());
			String str_json = jsonhelper.makeJsonMessage(Constants.Uid_Info, taggedUIDs);
			mSocketHelper.sendMessage(mHandler, str_json);
			/**처리부분 **/

			return null;
		}
		
	}
	
	@Override
	protected void onDestroy(){
		this.productManager.CloseSerialPort();
		super.onDestroy();
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
