package com.esc_project.productManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;
import android.widget.Toast;
import driver.*;

public class ProductManager {
	
	UsbManager usbManager;
	List<UsbSerialDriver> availableDrivers;
    private UsbSerialPort port;
    UsbDeviceConnection connection;
    UsbSerialDriver driver;
    
	byte rxBuffer [ ] = new byte [64];
	byte txBuffer [ ] = new byte [64];
	
	Context context;
	
	ArrayList<Product> products;
	
	
	//Constructor
	public ProductManager( Context context ) {
		this.context = context;
	}
	
	//SericalCommunication
	public boolean SerialCommunication ( )  {
		
		boolean ret = false;

		// Find all available drivers from attached devices.
		usbManager = (UsbManager) this.context.getSystemService(Context.USB_SERVICE);
		
		availableDrivers = UsbSerialProber.getDefaultProber().findAllDrivers(usbManager);

		if (availableDrivers.isEmpty()) {
		  ret = false;
		}
		// Open a connection to the first available driver.
		driver = availableDrivers.get(0);
		
		connection = usbManager.openDevice(driver.getDevice());
		if (connection == null) {
			
			ret = false;
			
		}
		// Read some data! Most have just one port (port 0).

		port = driver.getPorts().get(0);

		//Port¿­
		try {
			port.open(connection);
			port.setParameters( 9600 , port.DATABITS_8, port.STOPBITS_1,port.PARITY_NONE);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if( ret == false ) {
			Toast.makeText(this.context.getApplicationContext(), "CONNECTION FAIL", Toast.LENGTH_SHORT).show();
		}
		else{
			Toast.makeText(this.context.getApplicationContext(), "CONNECTION SUCCESS", Toast.LENGTH_SHORT).show();
		}
		
		
		return ret;
	}
	
	//카트에 있는 상품들 저장.
	public int storeCartProducts( ) {
	
		//태그 전 ArrayList초기화
		this.products.clear();
		


		//Anticollision
		txBuffer[0] = 0x04;
		txBuffer[1] = 0x00;
		txBuffer[2] = 0x40;
		txBuffer[3] = (byte)0xff;
		
		//명령 요청.
		try {
			port.write(txBuffer, 1000);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return 0;
	
	}
}
