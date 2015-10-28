package com.app2.pms.debug.adbd;

import java.lang.reflect.Method;

public class SystemProperties {
	public static final String TAG = "SystemProperties";
	
    public static void set(String key, String val) {		
		try {
			//get class object
			Class clazz = Class.forName("android.os.SystemProperties");

			//Match param type
			Class[] pareamType = new Class[2];
			pareamType[0] = String.class;
			pareamType[1] = String.class;
			Method method = clazz.getMethod("set", pareamType);
			
			//pass param to method
			Object[] params = new Object[2];
			params[0] = key;
			params[1] = val;
			            
            //call function,if static function, pass null
            method.invoke(null, params);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
    	//android.os.SystemProperties.set(key, val);
    }
    
	public static String get(String key, String def){
		String ret = null;
		try {
			//get class object
			Class clazz = Class.forName("android.os.SystemProperties");

			//Match param type
			Class[] pareamType = new Class[2];
			pareamType[0] = String.class;
			pareamType[1] = String.class;
			Method method = clazz.getMethod("get", pareamType);
			
			//Pass param to method
			Object[] params = new Object[2];
			params[0] = key;
			params[1] = def;
			            
            //Call function,if static function, pass null
            ret = (String)method.invoke(null, params);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}
	
	public static String get(String key){
		String ret = null;
		try {
			Class clazz = Class.forName("android.os.SystemProperties");
			Method method = clazz.getMethod("get", String.class);
            ret = (String)method.invoke(null, key);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}
}
