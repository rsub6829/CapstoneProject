package com.capstone.data;

import java.util.List;

public interface DataProvider {
	public void addData( DataProvider dP );

	
	public Data getRecord( String recordType );
	
	
	public void putRecord( Data pageData );
	
	public void readData();
	
	
	public Data[] getRecords( String recordType );
	
	
	public Data getRecord( String recordType, String recordId );
	
	public void addRecordType( String typeName, boolean lockRecords );
	public void addRecord( Data pageData );

	
	public List<String> getRecordTypes();

}
