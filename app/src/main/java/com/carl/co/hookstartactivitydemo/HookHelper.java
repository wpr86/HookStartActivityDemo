package com.carl.co.hookstartactivitydemo;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Handler;

public class HookHelper 
{
	public static final String EXTRA_TARGET_INTENT = "extra_target_intent";
	
	public static void hookAMS()
	{
		try {
			Class<?> activityManagerNativeClass = Class.forName("android.app.ActivityManagerNative");
			Field gDefaultField = activityManagerNativeClass.getDeclaredField("gDefault");
			gDefaultField.setAccessible(true);
			Object gDefault = gDefaultField.get(null);
			
			Class<?> singleton = Class.forName("android.util.Singleton");
			Field mInstanceField = singleton.getDeclaredField("mInstance");
			mInstanceField.setAccessible(true);
			Object rawIActivityManager = mInstanceField.get(gDefault);
			
			Class<?> iActivityManagerInterface = Class.forName("android.app.IActivityManager");
			Object proxy = Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
					new Class<?>[] {iActivityManagerInterface} , new IActivityManagerHandler(rawIActivityManager));
			mInstanceField.set(gDefault, proxy);
		} 
		catch (Exception e) {
		}
	}
	
	public static void hookPMS(Context context)
	{
		try {
			Class<?> activityThreadClass = Class.forName("android.app.ActivityThread");
			Method currentActivityThreadMethod = activityThreadClass.getDeclaredMethod("currentActivityThread");
			Object currentActivityThread = currentActivityThreadMethod.invoke(null);
			
			Field sPackageManagerField = activityThreadClass.getDeclaredField("sPackageManager");
			sPackageManagerField.setAccessible(true);
			Object sPackageManager = sPackageManagerField.get(currentActivityThread);
			
			Class<?> iPackageManagerInterface = Class.forName("android.content.pm.IPackageManager");
			Object proxy = Proxy.newProxyInstance(iPackageManagerInterface.getClassLoader(),
					new Class<?>[]{iPackageManagerInterface}, new IPackageManagerHandler(sPackageManager));
			
			sPackageManagerField.set(currentActivityThread, proxy);
			
			PackageManager pm = context.getPackageManager();
			Field mPmField = pm.getClass().getDeclaredField("mPM");
			mPmField.setAccessible(true);
			mPmField.set(pm, proxy);
			
		} catch (Exception e) {
		}
	}
	
	public static void hookHandlerCallback()
	{
		try {
			Class<?> activityThreadClass = Class.forName("android.app.ActivityThread");
			Method currentActivityThreadMethod = activityThreadClass.getDeclaredMethod("currentActivityThread");
			Object currentActivityThread = currentActivityThreadMethod.invoke(null);
			
			Field mHField = activityThreadClass.getDeclaredField("mH");
			mHField.setAccessible(true);
			Handler mH = (Handler) mHField.get(currentActivityThread);
			
			Field mCallBackField = Handler.class.getDeclaredField("mCallback");
			mCallBackField.setAccessible(true);
			mCallBackField.set(mH, new ActivityThreadHandlerCallback(mH));
			
		} catch (Exception e) {
		}
	}
}
