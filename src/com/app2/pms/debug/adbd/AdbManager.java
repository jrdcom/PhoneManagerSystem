package com.app2.pms.debug.adbd;

import java.io.IOException;

import android.util.Log;

public class AdbManager {
	private static final String TAG = "AdbManager";
	
	private static final String ADBD_TCP_SYSTEM_PROPERTY = "service.adb.tcp.port";
	//private static final String ADBD_TCP_SYSTEM_PROPERTY = "persist.adb.tcp.port";
	
	public static void startAdbd(){
        String result = null;
        try {
            String[] cmdx = { "/system/bin/start", "adbd" };
            
            int ret = ShellCommond.execCommand(cmdx);
            if (0 == ret) {
                result = ShellCommond.getOutput();
            } else {
                // result = "ERROR";
                result = ShellCommond.getOutput();
            }
        } catch (IOException e) {
            Log.i(TAG, e.toString());
            result = "ERR.JE";
        }
        Log.i(TAG, "startAdbd(): result="+result);
	}
	
	public static void stopAdbd(){
        String result = null;
        try {
            String[] cmdx = { "/system/bin/stop", "adbd" };
            int ret = ShellCommond.execCommand(cmdx);
            if (0 == ret) {
                result = ShellCommond.getOutput();
            } else {
                // result = "ERROR";
                result = ShellCommond.getOutput();
            }
        } catch (IOException e) {
            Log.i(TAG, e.toString());
            result = "ERR.JE";
        }
        Log.i(TAG, "stopAdbd(): result="+result);
	}
	
	public static void setAdbdTcpPort(String port){
    	setSystemProperty(ADBD_TCP_SYSTEM_PROPERTY, port);
	}
	
	public static String getAdbdTcpPort(){
		return getSystemProperty(ADBD_TCP_SYSTEM_PROPERTY);
	}
	
	
	private static String getSystemProperty(String property) {
        String value = null;
        try {
            value = SystemProperties.get(property, null);
        } catch (RuntimeException e) {
            Log.d(TAG, "getSystemProperty faied");
            e.printStackTrace();
        }
        Log.d(TAG, "getSystemProperty(): key=" + property + ", value = " + value);
        return value;
    }
    
    private static void setSystemProperty(String property, String value) {
        Log.d(TAG, "setSystemProperty key = " + property + ", value = " + value);
        try {
            SystemProperties.set(property, value);
        } catch (RuntimeException e) {
            Log.d(TAG, "setSystemProperty faied");
            e.printStackTrace();
        }
    }
    
	
}
