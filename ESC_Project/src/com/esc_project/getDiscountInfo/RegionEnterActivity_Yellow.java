package com.esc_project.getDiscountInfo;

import com.esc_project.MainActivity;
import com.esc_project.R;
import com.esc_project.R.layout;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

public class RegionEnterActivity_Yellow extends MainActivity {

	/** Called when the activity is first created. */

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_region_yellow);
		// TODO Auto-generated method stub

		Intent intent = getIntent();
		String Enter = intent.getStringExtra("Enter");

		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub

			}
		});

		alert.setMessage("TEST메세지 : " + Enter
				+ System.getProperty("line.separator") + "Yellow Region!!");
		alert.show();
	}

}
