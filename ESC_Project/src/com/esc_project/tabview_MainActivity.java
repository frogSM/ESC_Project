package com.esc_project;

import com.esc_project.R;
import com.esc_project.printLocation.NavigationActivity;
import com.esc_project.productManager.*;
import com.esc_project.test.TestActivity;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;


public class tabview_MainActivity extends TabActivity {

	private TabHost m_tabHost = null;
	
	View tabwidgetview;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_tapview_main);
	
	    // TODO Auto-generated method stub
	    m_tabHost = (TabHost) findViewById(android.R.id.tabhost);
	    
		TabSpec main_TabSpec = m_tabHost.newTabSpec("main");
		main_TabSpec.setIndicator("메인화면");
		main_TabSpec.setContent(new Intent(this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
		m_tabHost.addTab(main_TabSpec);
		
		TabSpec navi_TabSpec = m_tabHost.newTabSpec("navi");
		navi_TabSpec.setIndicator("네비게이션");
		navi_TabSpec.setContent(new Intent(this, NavigationActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
		m_tabHost.addTab(navi_TabSpec);
		
		TabSpec cal_TabSpec = m_tabHost.newTabSpec("cal");
		cal_TabSpec.setIndicator("계산");
		cal_TabSpec.setContent(new Intent(this, ProductManagerActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
		m_tabHost.addTab(cal_TabSpec);
		
		TabSpec exit_TabSpec = m_tabHost.newTabSpec("exit");
		exit_TabSpec.setIndicator("쇼핑종료");
		exit_TabSpec.setContent(new Intent(this, ExitActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
		m_tabHost.addTab(exit_TabSpec);
		
		TabSpec test_TabSpec = m_tabHost.newTabSpec("test");
		test_TabSpec.setIndicator("TEST");
		test_TabSpec.setContent(new Intent(this, TestActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
		m_tabHost.addTab(test_TabSpec);

	}

}
