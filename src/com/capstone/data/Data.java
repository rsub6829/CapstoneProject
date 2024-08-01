package com.capstone.data;

import java.util.HashMap;
import java.util.Map;

public class Data {

	Map<String,Object> records=new HashMap<String,Object>();
	
	public Object getRecord(String recordId)
	{
		return records.get(recordId);
	}
	public void addRecord(String recordId,Object record)
	{
		records.put(recordId, record);
	}
}
