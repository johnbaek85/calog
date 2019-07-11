package com.example.calog.Drinking;

import android.content.Context;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.calog.Drinking.driver.UsbSerialDriver;
import com.example.calog.Drinking.driver.UsbSerialPort;
import com.example.calog.Drinking.driver.UsbSerialProber;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

//실제 USB 연결 객체
public class SerialConnector {
	public static final String tag = "SerialConnector";

	private Context mContext;
	private DrinkingCheckActivity.SerialListener mListener;
	private Handler mHandler;
	
	private SerialMonitorThread mSerialThread;
	
	private UsbSerialDriver mDriver;
	private UsbSerialPort mPort;
	
	public static final int TARGET_VENDOR_ID = 9025;	// Arduino
	public static final int TARGET_VENDOR_ID2 = 1659;	// PL2303
	public static final int TARGET_VENDOR_ID3 = 1027;	// FT232R
	public static final int TARGET_VENDOR_ID4 = 6790;	// CH340G
	public static final int TARGET_VENDOR_ID5 = 4292;	// CP210x
	public static final int BAUD_RATE = 115200;
	
	
	/*****************************************************
	*	Constructor, Initialize
	******************************************************/
	public SerialConnector(Context c, DrinkingCheckActivity.SerialListener l, Handler h) {
		mContext = c;
		mListener = l;
		mHandler = h;
	}
	
	
	public void initialize() {

		//Android Device에서 USB를 통해, 외부 기기와 시리얼 통신을 할 수 있다.
		//UsbManager - 연결 된 USB를 찾아서 객체를 만들 수 있게한다.
		//출처: https://rgws.tistory.com/24
		UsbManager manager = (UsbManager) mContext.getSystemService(Context.USB_SERVICE); //컨텍스트에서 USB관련 서비스를 가져옴

		// 해당하는 driver를 찾아냄
		List<UsbSerialDriver> availableDrivers = UsbSerialProber.getDefaultProber().findAllDrivers(manager);

		//가능한 드라이버가 없다면 에러를 발생한다
		if (availableDrivers.isEmpty())
		{
			mListener.onReceive(Constants.MSG_SERIAL_ERROR, 0, 0, "Error: There is no available device. \n", null);
			return;
		}
		
		mDriver = availableDrivers.get(0); //드라이버를 꺼내옴
		if(mDriver == null) {
			mListener.onReceive(Constants.MSG_SERIAL_ERROR, 0, 0, "Error: Driver is Null \n", null);
			return;
		}
		
		// Report to UI
		StringBuilder sb = new StringBuilder();
		UsbDevice device = mDriver.getDevice();

		//로그를 남기기위한 작업
		sb.append(" DName : ").append(device.getDeviceName()).append("\n")
			.append(" DID : ").append(device.getDeviceId()).append("\n")
			.append(" VID : ").append(device.getVendorId()).append("\n")
			.append(" PID : ").append(device.getProductId()).append("\n")
			.append(" IF Count : ").append(device.getInterfaceCount()).append("\n");

		mListener.onReceive(Constants.MSG_DEVICD_INFO, 0, 0, sb.toString(), null);
		
		UsbDeviceConnection connection = manager.openDevice(device);
		if (connection == null) {
			mListener.onReceive(Constants.MSG_SERIAL_ERROR, 0, 0, "Error: Cannot connect to device. \n", null);
			return;
		}
		
		// Read some data! Most have just one port (port 0).
		mPort = mDriver.getPorts().get(0);
		if(mPort == null) {
			mListener.onReceive(Constants.MSG_SERIAL_ERROR, 0, 0, "Error: Cannot get port. \n", null);
			return;
		}
		
		try {
			mPort.open(connection);
			mPort.setParameters(9600, 8, 1, 0);		// baudrate:9600, dataBits:8, stopBits:1, parity:N
//			byte buffer[] = new byte[16];
//			int numBytesRead = mPort.read(buffer, 1000);
//			Log.d(TAG, "Read " + numBytesRead + " bytes.");
		} catch (IOException e) {
			// Deal with error.
			mListener.onReceive(Constants.MSG_SERIAL_ERROR, 0, 0, "Error: Cannot open port \n" + e.toString() + "\n", null);
		} finally {
		}
		
		// Everything is fine. Start serial monitoring thread.
		startThread();
	}	// End of initialize()
	
	public void finalize() {
		try {
			mDriver = null;
			stopThread();
			
			mPort.close();
			mPort = null;
		} catch(Exception ex) {
			mListener.onReceive(Constants.MSG_SERIAL_ERROR, 0, 0, "Error: Cannot finalize serial connector \n" + ex.toString() + "\n", null);
		}
	}
	
	
	
	/*****************************************************
	*	public methods
	******************************************************/
	// send string to remote
	public void sendCommand(String cmd) {
		
		if(mPort != null && cmd != null) {
			try {
				mPort.write(cmd.getBytes(), cmd.length());		// Send to remote device 
			}
			catch(IOException e) {
				mListener.onReceive(Constants.MSG_SERIAL_ERROR, 0, 0, "Failed in sending command. : IO Exception \n", null);
			}
		}
	}
	
	
	/*****************************************************
	*	private methods
	******************************************************/
	// start thread
	private void startThread() {
		Log.d(tag, "Start serial monitoring thread");
		mListener.onReceive(Constants.MSG_SERIAL_ERROR, 0, 0, "Start serial monitoring thread \n", null);
		if(mSerialThread == null) {
			mSerialThread = new SerialMonitorThread();
			mSerialThread.start();
		}	
	}
	// stop thread
	private void stopThread() {
		if(mSerialThread != null && mSerialThread.isAlive())
			mSerialThread.interrupt();
		if(mSerialThread != null) {
			mSerialThread.setKillSign(true);
			mSerialThread = null;
		}
	}
	
	
	
	
	
	/*****************************************************
	*	Sub classes, Handler, Listener
	******************************************************/
	
	public class SerialMonitorThread extends Thread {
		// Thread status
		private boolean mKillSign = false;
		private SerialCommand mCmd = new SerialCommand();
		
		
		private void initializeThread() {
			// This code will be executed only once.
		}
		
		private void finalizeThread() {
		}
		
		// stop this thread
		public void setKillSign(boolean isTrue) {
			mKillSign = isTrue;
		}
		
		/**
		*	Main loop
		**/
		@Override
		public void run() 
		{
			byte buffer[] = new byte[128];
			
			while(!Thread.interrupted())
			{
				if(mPort != null) {
					Arrays.fill(buffer, (byte)0x00);
					
					try {
						// Read received buffer
						int numBytesRead = mPort.read(buffer, 1000);
						if(numBytesRead > 0) {
							Log.d(tag, "run : read bytes = " + numBytesRead);
							
							// Print message length
							// 메시지전달
							Message msg = mHandler.obtainMessage(Constants.MSG_READ_DATA_COUNT, numBytesRead, 0, 
									new String(buffer));
							mHandler.sendMessage(msg);
							
							// Extract data from buffer
							//버퍼에담긴 데이터 전송
							for(int i=0; i<numBytesRead; i++) {
								char c = (char)buffer[i];
								if(c == 'z') {
									// This is end signal. Send collected result to UI
									if(mCmd.mStringBuffer != null && mCmd.mStringBuffer.length() < 20) {
										Message msg1 = mHandler.obtainMessage(Constants.MSG_READ_DATA, 0, 0, mCmd.toString());
										mHandler.sendMessage(msg1);
									}
								} else {
									mCmd.addChar(c);
								}
							}
						} // End of if(numBytesRead > 0)
					} 
					catch (IOException e) {
						Log.d(tag, "IOException - mDriver.read");
						Message msg = mHandler.obtainMessage(Constants.MSG_SERIAL_ERROR, 0, 0, "Error # run: " + e.toString() + "\n");
						mHandler.sendMessage(msg);
						mKillSign = true;
					}
				}

				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
					break;
				}
				
				if(mKillSign)
					break;
				
			}	// End of while() loop
			
			// Finalize
			finalizeThread();
			
		}	// End of run()
	}	// End of SerialMonitorThread
}