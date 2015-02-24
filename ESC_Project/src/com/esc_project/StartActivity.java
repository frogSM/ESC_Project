package com.esc_project;

import java.util.ArrayList;

import com.esc_project.R;
import com.esc_project.getDiscountInfo.BeaconMonitoringService;

import android.app.Activity;
import android.app.ActivityManager;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class StartActivity extends Activity {
	
	public static ArrayList<Activity> actList = new ArrayList<Activity>();
	
	public static final String RECO_UUID = "24DDF4118CF1440C87CDE368DAF9C93E";
	private static final int REQUEST_ENABLE_BT = 1;

	private BluetoothManager mBluetoothManager;
	private BluetoothAdapter mBluetoothAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start);
		
		actList.add(this);

		mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
		mBluetoothAdapter = mBluetoothManager.getAdapter();

		if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
			Intent enableBTIntent = new Intent(
					BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableBTIntent, REQUEST_ENABLE_BT);
		}

		Intent test = new Intent(this, BeaconMonitoringService.class);
		startService(test);
	}

	@Override
	protected void onResume() {
		super.onResume();
		ActivityManager am = (ActivityManager) this
				.getSystemService(Context.ACTIVITY_SERVICE);
	};

	@Override
	protected void onDestroy() {
		super.onDestroy();

		Intent test = new Intent(this, BeaconMonitoringService.class);
		stopService(test);
		
	};

	public void onButtonClicked(View v) {
		Button btn = (Button) v;
		if (btn.getId() == R.id.btnStartESC) {
			Log.i("StartActivity", "btnClick!!");
			
			Intent ma = new Intent(this, tabview_MainActivity.class);
			startActivity(ma);
		}
	}


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.start, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
