package com.carl.co.hookstartactivitydemo;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import android.content.ComponentName;
import android.content.Intent;
import android.util.Log;

public class IActivityManagerHandler implements InvocationHandler
{
	private Object mBase;
	public IActivityManagerHandler(Object base) 
	{
		mBase = base;
	}
	
	
	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable 
	{
		Log.i("wpr","ams hook : "+method.getName());
		if("startActivity".equals(method.getName()))
		{
			Intent raw;
		    int index = 0;

		    for (int i = 0; i < args.length; i++) {
		        if (args[i] instanceof Intent) {
		            index = i;
		            break;
		        }
		    }
		    raw = (Intent) args[index];
		    Intent newIntent = new Intent();
		    ComponentName componentName = new ComponentName("com.carl.co.hookstartactivitydemo", StubActivity.class.getCanonicalName());
		    newIntent.setComponent(componentName);
		    newIntent.putExtra(HookHelper.EXTRA_TARGET_INTENT, raw);
		    args[index] = newIntent;
		}
		return method.invoke(mBase, args);
	}

}
