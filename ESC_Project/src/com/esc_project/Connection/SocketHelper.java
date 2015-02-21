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

/** SocketHelper Ŭ����.**/
/** AsyncTask�� �̿��Ͽ� �����Ѱ��� Thread - Handler������� ���� **/
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
	
	/** Contex �������� �κ� **/
	public static synchronized SocketHelper getInstance(Context mContext) {
		if (instance == null) {
			instance = new SocketHelper(mContext);
		}
		return instance;
	}

	/** ������ **/
	public SocketHelper(Context mContext) {
		this.connectCheck = true;
		this.mJsonHelper = new JsonHelper(mContext);
		this.mContext =  mContext;
	}
	
	/** ����������� �������� JSON��Ĺ��� ���� **/
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
	
	/** ���� ���� **/
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
	
	/** ���� �ݱ� **/
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
	 * 	�ȵ���̵� ����� ���� ���� ���ʹ� synchronized�� �̿��Ͽ� �񵿱�� ������ ó���ϴ� ����� ��ȭ�� ����
	 * 	AsyncTask �� �̿��Ͽ� �񵿱�� �����带 ó���ؾ���. �̰��� �̷�� ���� ������ ��� ����.
	 * 	1. AsyncTask Ŭ������ �׻� ����Ŭ�����Ͽ��� ����Ͽ��� �Ѵ�.
	 * 	2. AsyncTask �ν��Ͻ��� �׻� UI �����忡�� �����Ѵ�.
	 * 	3. AsyncTask:execute() �޼ҵ�� �׻� UI �����忡�� ȣ���Ѵ�.
	 * 	4. AsyncTask:execute() �޼ҵ�� ������ AsyncTask �ν��Ͻ� ���� �� �ѹ��� ��� ����(�׷��� ����Ŭ����ȭ ��)
	 * 		- �̸� ����� Exception�� �߻��Ѵ�. �׷��Ƿ� background �۾��� �ʿ��� ������ new�����ڸ� �̿��Ͽ�
	 * 			�ش� �۾��� ���� AsyncTask �ν��Ͻ��� ���� �����Ͽ��� �Ѵ�. 
	 * 	5. AsyncTask �ν��Ͻ��� �׻� UI�����忡�� ����
	 * 	6. AsyncTask execute() �޼ҵ�� �׻� UI �����忡�� ȣ��
	 * 	name : Kim Sang Hyun 
	 * **/
//	private class subAsyncTask extends AsyncTask<String, Void, Void> {
//		
//		@Override
//		protected Void doInBackground(String... message) {
//			// TODO Auto-generated method stub
//			
//			// �����κ��� ���� ����
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
//				//hendler ����
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
