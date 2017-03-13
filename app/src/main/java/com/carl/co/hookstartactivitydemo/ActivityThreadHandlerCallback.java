package com.carl.co.hookstartactivitydemo;

import java.lang.reflect.Field;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class ActivityThreadHandlerCallback implements Handler.Callback
{
	private Handler mBase;
	
	public ActivityThreadHandlerCallback(Handler base) 
	{
		mBase = base;
	}
	
	@Override
	public boolean handleMessage(Message msg) 
	{
		Log.i("wpr","handler hook : "+msg.what);
		switch (msg.what) 
		{
		case 100:
			handleLaunchActivity(msg);
			break;

		default:
			break;
		}
		
		mBase.handleMessage(msg);
		return true;
	}
	
	private void handleLaunchActivity(Message msg)
	{
		Object obj = msg.obj;
		try {
			Field intent = obj.getClass().getDeclaredField("intent");
			intent.setAccessible(true);
			Intent rawIntent = (Intent) intent.get(obj);
			Intent target = rawIntent.getParcelableExtra(HookHelper.EXTRA_TARGET_INTENT);
			rawIntent.setComponent(target.getComponent());
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}

}
