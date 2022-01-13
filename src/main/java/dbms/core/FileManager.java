package dbms.core;

import java.util.List;

import dbms.core.databasemanager.DataBaseDefinition;
import dbms.core.databasemanager.RelationDefinition;
import dbms.core.diskmanager.DiskManagerException;

public interface FileManager {
	
	public void init(BufferManager bufferManager, DataBaseDefinition dataBaseDefinition);
	
	public void clean();
	
	public void createHeapFile(RelationDefinition relation) throws DiskManagerException;
	
	public void insertRecord(String relationName, List<String> values, BufferManager bufferManager, DiskManager diskManager) throws DiskManagerException;
	
	public List<dbms.core.filemanager.Record> readRecords(String relationName) throws DiskManagerException;
	
}
