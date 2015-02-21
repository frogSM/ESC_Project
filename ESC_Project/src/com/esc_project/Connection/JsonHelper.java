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
	
	/** ���Ÿ�԰� �����͸� �Ű������� �޾Ƽ� ���Ÿ�Կ� ���� JSON������ �����. **/
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
	
	/** JSON������ �̿��Ͽ� ���Ÿ���� �����ϰ� �׿� ���� ��ü�� ��ȯ�Ѵ�. **/
	public Object parserJsonMessage(String json_msg) {
		
		mMessage = json_msg;
		
		Gson gson = new Gson();
		JsonParser parser = new JsonParser();
		JsonObject object = parser.parse(mMessage).getAsJsonObject();
		
		mType = gson.fromJson(object.get("type"), String.class);
		switch(mType) {
		case Constants.Uid_Info :
			// List<Product>�� ��ȯ�� ��ü�� ������Ʈ�� ��ȯ�� �ٽ� ��ȯ(������ ����)
			List<Product> products = new ArrayList<Product>();
			products = gson.fromJson(object.get("Object"), new TypeToken<List<Product>>(){}.getType());
			mObject = products;
			break;
		}
		
		return mObject;
	}
	
	/** ��� Ÿ���� ��ȯ�Ѵ�. **/
	public String getType() {
		return mType;
	}
	
	/** ������ ��ü�� ��ȯ�Ѵ�. **/
	public Object getObject() {
		return mObject;
	}
}
