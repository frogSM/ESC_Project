package com.esc_project;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.esc_project.Connection.JsonHelper;
import com.esc_project.Connection.SocketHelper;
import com.esc_project.productManager.*;

public class ExitActivity extends StartActivity implements OnClickListener {
	
	SocketHelper mSocketHelper;
	JsonHelper mJsonHelper;
	
	TextView mTextViewTEST;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_exit);

	    // 액티비티 스택에 추가
	    actList.add(this);
	    
	    mTextViewTEST = (TextView)findViewById(R.id.txtViewTest);
	    Button mExit = (Button)findViewById(R.id.btnExit);
	    mExit.setOnClickListener(this);
	    Button mTest = (Button)findViewById(R.id.btnTest);
	    mTest.setOnClickListener(this);
	   
	    mSocketHelper = mSocketHelper.getInstance(getApplicationContext());
	    mJsonHelper = mJsonHelper.getInstance(getApplicationContext());
	    
	    // TODO Auto-generated method stub
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v.getId() == R.id.btnExit) {
			for(Activity stackActivity : actList) {
				stackActivity.finish();
			}
		}
		else if(v.getId() == R.id.btnTest) {
			ArrayList<String> test = new ArrayList<String>();
			test.add("AA-AA-AA-AA");
			test.add("BB-BB-BB-BB");
			
			String msg = mJsonHelper.makeJsonMessage(Constants.Uid_Info, test);
			mSocketHelper.sendMessage(mHandler, msg);
//			List<Product> product =(List<Product>)mJsonHelper.parserJsonMessage(mSocketHelper.getMessage());
			
			  
			
		}
	}
	
	
	/** 메인 Thread와 다른 Thread를 통신하는 핸들러 **/
	/** 메인 Thread가 아닌곳에서는 UI 변경이 불가능 **/
	private Handler mHandler = new Handler() {
		
		@Override
		public void handleMessage(android.os.Message msg) {
			if(msg.what == 0) {
				List<Product> product = (List<Product>) msg.obj;
				
//				Log.e("ExitActivity", "uid : " + product.get(0).getUid() + "\n name : " + product.get(0).getName() + "\n price : " + product.get(0).getPrice());
				mTextViewTEST.setText("uid : " + product.get(1).getUid() + "\n name : " + product.get(1).getName() + "\n price : " + product.get(1).getPrice());
			}
		};
	};
}
