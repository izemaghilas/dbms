package dbms.core;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import dbms.core.databasemanager.RelationDefinition;
import dbms.core.diskmanager.DiskManagerException;

public interface DataBaseManager {
	
	public void init() throws FileNotFoundException, IOException, ClassNotFoundException;
	
	public void clean();
		
	public void finish() throws IOException, DiskManagerException;
	
	public void createRelation(String name, int numberOfColumns, List<String> types) throws DiskManagerException;
	
	public void insertRecord(String relationName, List<String> values) throws DiskManagerException;
	
	public List<dbms.core.filemanager.Record> readRecords(String relationName) throws DiskManagerException;
	
	public RelationDefinition getRelation(String name);
}
