package com.esc_project.Connection;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.LinkedHashMap;

import com.esc_project.Constants;
import com.esc_project.printLocation.androidToBeacon;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

/** SocketHelper 클래스.**/
/** AsyncTask를 이용하여 구현한것을 Thread - Handler방식으로 변경 **/
public class SocketHelper {
	
	/** ServerAddress & ServerPort define **/
	private final String serverAddress = "58.231.74.83";
	private final int serverPort = 7218;
	
	private Context mContext;
	private Socket clientSocket;

	private static SocketHelper instance;
	private JsonHelper mJsonHelper; 

	private boolean connectCheck = false ;
	
	private String getSentence ;
	private Handler mHandler;
	
	/** Contex 가져오는 부분 **/
	public static synchronized SocketHelper getInstance(Context mContext) {
		if (instance == null) {
			instance = new SocketHelper(mContext);
		}
		return instance;
	}

	/** 생성자 **/
	public SocketHelper(Context mContext) {
		this.connectCheck = true;
		this.mJsonHelper = new JsonHelper(mContext);
		this.mContext =  mContext;
	}
	
	/** 소켓통신으로 서버에게 JSON양식문장 전송 **/
	public void sendMessage(Handler handler, final String msg_json) {
		
		this.mHandler = handler;
		
		Thread thread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					openSocket();
					
					DataOutputStream outToServer = new DataOutputStream(
							clientSocket.getOutputStream());
					
					BufferedReader inFromServer = new BufferedReader(
							new InputStreamReader(clientSocket.getInputStream()));
					
					outToServer.writeBytes(msg_json + '\n');
					
					getSentence = inFromServer.readLine();
					
					if(mHandler != null) {
						Message msg = new Message().obtain();
						msg.what = Constants.THREAD_MESSAGE;
						msg.obj = getSentence;
						mHandler.sendMessage(msg);
					}
					
					closeSocket();
					
					Thread.sleep(100);
					
				} catch (SocketException e) {
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
				
			}
		});
		thread.setDaemon(true);
		thread.start();
		thread.interrupt();
		
	}
	
	public String getMessage() {
		return getSentence;
	}
	
	/** 소켓 열기 **/
	private void openSocket() {
		
		if (connectCheck == true) {
			try {
				clientSocket = new Socket(serverAddress, serverPort);
				clientSocket.setSoTimeout(3000);
				Log.e("SocketHelper", "socket open success!");
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	/** 소켓 닫기 **/
	private void closeSocket() {
		if(connectCheck == true) {
			try {
				clientSocket.close();
				Log.e("SocketHelper", "socket close success!");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	
	/** 
	 * 	안드로이드 허니콤 버전 이후 부터는 synchronized를 이용하여 비동기식 스레드 처리하는 방법의 변화로 인해
	 * 	AsyncTask 를 이용하여 비동기식 스레드를 처리해야함. 이것을 이루기 위한 조건이 몇가지 있음.
	 * 	1. AsyncTask 클래스는 항상 서브클래싱하여서 사용하여야 한다.
	 * 	2. AsyncTask 인스턴스는 항상 UI 스레드에서 생성한다.
	 * 	3. AsyncTask:execute() 메소드는 항상 UI 스레드에서 호출한다.
	 * 	4. AsyncTask:execute() 메소드는 생성된 AsyncTask 인스턴스 별로 꼭 한번만 사용 가능(그래서 서브클래싱화 함)
	 * 		- 이를 어길경우 Exception이 발생한다. 그러므로 background 작업이 필요할 때마다 new연산자를 이용하여
	 * 			해당 작업에 대한 AsyncTask 인스턴스를 새로 생성하여야 한다. 
	 * 	5. AsyncTask 인스턴스는 항상 UI스레드에서 생성
	 * 	6. AsyncTask execute() 메소드는 항상 UI 스레드에서 호출
	 * 	name : Kim Sang Hyun 
	 * **/
//	private class subAsyncTask extends AsyncTask<String, Void, Void> {
//		
//		@Override
//		protected Void doInBackground(String... message) {
//			// TODO Auto-generated method stub
//			
//			// 서버로부터 받은 문장
//			String getSentence = "";
//
//			openSocket();
//			try {
//
//				DataOutputStream outToServer = new DataOutputStream(
//						clientSocket.getOutputStream());
//
//				BufferedReader inFromServer = new BufferedReader(
//						new InputStreamReader(clientSocket.getInputStream()));
//
//				outToServer.writeBytes(message[0] + '\n');
//
//				getSentence = inFromServer.readLine();
//				
//				//hendler 설정
//				Message msg = mHandler.obtainMessage();
////				msg.what = Constants.Uid_Info;
//				msg.obj = getSentence;
//				mHandler.sendMessage(msg);
//				
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//
//			closeSocket();
//
//			try {
//				
//				Thread.sleep(100);
//				
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//			
//			return null;
//		}
//		
//		@Override
//		protected void onProgressUpdate(Void... values) {
//			// TODO Auto-generated method stub
//			super.onProgressUpdate(values);
//		}
//		
//	}
	
}
