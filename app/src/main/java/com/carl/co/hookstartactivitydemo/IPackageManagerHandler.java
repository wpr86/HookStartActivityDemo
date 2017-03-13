package com.carl.co.hookstartactivitydemo;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import android.util.Log;

public class IPackageManagerHandler implements InvocationHandler
{
	private Object mBase;
	public IPackageManagerHandler(Object base) 
	{
		mBase = base;
	}
	
	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable 
	{
		Log.i("wpr","pms hook : "+method.getName());
		return method.invoke(mBase, args);
	}

}
