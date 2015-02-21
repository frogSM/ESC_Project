package com.esc_project.Connection;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.*;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.esc_project.Constants;
import com.esc_project.productManager.Product;

import android.content.Context;
import android.database.Observable;

public class JsonHelper extends Observable {
	private static JsonHelper instance;
	private Context mContext;
	
	private String mMessage;
	
	private String mType;
	private Object mObject;
	
	public static synchronized JsonHelper getInstance(Context mContext) {
		if(instance == null) {
			instance = new JsonHelper(mContext);
		}
		return instance;
	}
	
	public JsonHelper(Context mContext) {
		this.mContext = mContext;
	}
	
	/** 명령타입과 데이터를 매개변수로 받아서 명령타입에 따른 JSON문장을 만든다. **/
	public String makeJsonMessage(String msgType, Object data) {

		JSONObject jsonObj = new JSONObject();

		switch (msgType) {
		case Constants.Uid_Info:
			jsonObj.put("type", "Uid_Info");
			
			ArrayList<String> castingData = new ArrayList<String>();
			castingData =(ArrayList<String>)data;

			jsonObj.put("uid", castingData);
			break;
		}

		return jsonObj.toString();
	}
	
	/** JSON형식을 이용하여 명령타입을 구분하고 그에 따른 객체를 반환한다. **/
	public Object parserJsonMessage(String json_msg) {
		
		mMessage = json_msg;
		
		Gson gson = new Gson();
		JsonParser parser = new JsonParser();
		JsonObject object = parser.parse(mMessage).getAsJsonObject();
		
		mType = gson.fromJson(object.get("type"), String.class);
		switch(mType) {
		case Constants.Uid_Info :
			// List<Product>로 반환된 객체를 오브젝트로 변환후 다시 반환(가독성 위해)
			List<Product> products = new ArrayList<Product>();
			products = gson.fromJson(object.get("Object"), new TypeToken<List<Product>>(){}.getType());
			mObject = products;
			break;
		}
		
		return mObject;
	}
	
	/** 명령 타입을 반환한다. **/
	public String getType() {
		return mType;
	}
	
	/** 데이터 객체를 반환한다. **/
	public Object getObject() {
		return mObject;
	}
}
