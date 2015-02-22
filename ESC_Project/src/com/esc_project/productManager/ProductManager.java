package com.esc_project.productManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import util.HexDump;
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
	
	Context context;
	
	ArrayList<String> taggedUIDs;
	
	
	//Constructor
	public ProductManager( Context context ) {
		this.context = context;
	}
	
	//SericalCommunication
	public boolean OpenSerialPort ( )  {
		
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
		else{
			ret = true;
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
	
	public boolean CloseSerialPort( ) {
		boolean ret = false;
		if( this.port != null ) {
			try {
				port.close();
				ret = true;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				ret = false;
			}
		}
		
		return ret;
	}
	
	//카트에 있는 상품들 초기
	public ArrayList<String> GetTaggedUIDs( ) {
	
		byte txBuffer [ ] = new byte [4];
		byte rxBuffer [ ] = new byte [128];
		
		txBuffer[0] = 0x04;
		txBuffer[1] = 0x00;
		txBuffer[2] = 0x40;
		txBuffer[3] = (byte)0xff;
		
		int numBytesRead = 0;
		
		try { 
			port.write( txBuffer, 1000 );
			numBytesRead = port.read(rxBuffer, 1000) ;
		} catch ( IOException e ) {
			e.printStackTrace();
		}
		
		byte[ ] [ ] dividedByteBuffer = new byte [20][12];
		
		int tagCount = 0;
		int k = 0;
		
		for( int i = 0 ; i < numBytesRead ; i++ ) {
			dividedByteBuffer[ tagCount ][ k ] = rxBuffer [ i ];
			k++;
			if ( rxBuffer [ i ] == (byte) 0xff ) { 
				tagCount++;
				k = 0;
			}
		}
		
		for ( int i = 0 ; i < tagCount ; i ++ ) { 
			this.taggedUIDs.add( HexDump.toHexString( dividedByteBuffer[ i ] ) ) ;
		}
		
		return this.taggedUIDs;
	}
}
