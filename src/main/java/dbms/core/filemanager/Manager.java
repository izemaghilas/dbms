package dbms.core.filemanager;

import java.util.ArrayList;
import java.util.List;

import dbms.core.BufferManager;
import dbms.core.DiskManager;
import dbms.core.FileManager;
import dbms.core.RelationNotFoundException;
import dbms.core.databasemanager.DataBaseDefinition;
import dbms.core.databasemanager.RelationDefinition;
import dbms.core.diskmanager.DiskManagerException;

public enum Manager implements FileManager {
	INSTANCE;

	
	private BufferManager bufferManager;
	private List<HeapFile> heapFiles;
	
	@Override
	public void init(BufferManager bufferManager, DataBaseDefinition dataBaseDefinition) {
		this.bufferManager = bufferManager;
		this.heapFiles = new ArrayList<>();
		
		dataBaseDefinition.getRelations().forEach(relation->{
			HeapFile heapFile = new HeapFile();
			heapFile.setRelation(relation);
			this.heapFiles.add(heapFile);
		});
	}

	@Override
	public void clean() {
		this.heapFiles.clear();
	}

	@Override
	public void createHeapFile(RelationDefinition relation) throws DiskManagerException {
		HeapFile heapFile = new HeapFile();
		heapFile.setRelation(relation);
		heapFile.createHeaderPage(bufferManager);
		this.heapFiles.add(heapFile);
	}

	@Override
	public void insertRecord(String relationName, List<String> values, BufferManager bufferManager, DiskManager diskManager) throws DiskManagerException {
		Record record = new Record();
		record.setValues(values);
		
		HeapFile heapFile = this.heapFiles.stream().filter(heapfile -> heapfile.getRelation().getName().equals(relationName))
				.findFirst()
				.orElseThrow(()->new RelationNotFoundException("relation *"+relationName+"*, should be created first!"));
				
		heapFile.insertRecord(record, bufferManager, diskManager);		
	}

	@Override
	public List<Record> readRecords(String relationName) throws DiskManagerException {
		HeapFile heapFile = this.heapFiles.stream().filter(heapfile -> heapfile.getRelation().getName().equals(relationName))
				.findAny()
				.orElseThrow(()->new RelationNotFoundException("relation *"+relationName+"*, should be created first!"));
		
		return heapFile.readRecords(bufferManager);
		
	}

}
