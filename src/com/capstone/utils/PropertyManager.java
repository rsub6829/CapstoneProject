package com.capstone.utils;

import java.util.Properties;
import java.io.FileInputStream;
import java.io.IOException;

public class PropertyManager {
	Properties prop;
	private final String propertyFilePath = "projectConfiguration.properties";

	public  PropertyManager() {
		loadProperties();
	}

//	private static ThreadLocal<PropertyManager> _threadLocal = new ThreadLocal<PropertyManager>() {
//		@Override
//		protected PropertyManager initialValue() {
//			return new PropertyManager();
//		}
//	};
//
//	
//	public static PropertyManager getInstance() {
//		return _threadLocal.get();
//	}

	private void loadProperties() {
		prop = new Properties();
		try {
			prop.load(this.getClass().getClassLoader().getResourceAsStream(propertyFilePath));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public String getProperty(String propertyName) {
		if(prop.containsKey(propertyName))
			return prop.getProperty(propertyName);
		{
			System.out.println(propertyName + " is not found");
			return null;
		}
	}
}
