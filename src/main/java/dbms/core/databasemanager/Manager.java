package dbms.core.databasemanager;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import dbms.core.BufferManager;
import dbms.core.Constants;
import dbms.core.DataBaseManager;
import dbms.core.DiskManager;
import dbms.core.FileManager;
import dbms.core.PageId;
import dbms.core.diskmanager.DiskManagerException;
import dbms.core.filemanager.Record;
import dbms.core.types.TypeFactory;

public enum Manager implements DataBaseManager {
	INSTANCE;
	
	private DiskManager diskManager;
	private BufferManager bufferManager;
	private FileManager fileManager;
	private DataBaseDefinition dataBaseDefinition;
	
	@Override
	public void init() throws FileNotFoundException, IOException, ClassNotFoundException {
		this.diskManager = dbms.core.diskmanager.Manager.INSTANCE;
		this.bufferManager = dbms.core.buffermanager.Manager.INSTANCE;
		this.fileManager = dbms.core.filemanager.Manager.INSTANCE;
		this.dataBaseDefinition = DataBaseDefinition.INSTANCE;
		
		this.dataBaseDefinition.init();
		this.fileManager.init(this.bufferManager, dataBaseDefinition);
		this.bufferManager.init(diskManager);
	}

	@Override
	public void clean() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void finish() throws IOException, DiskManagerException {
		this.dataBaseDefinition.finish();
		this.bufferManager.finish();
	}

	@Override
	public void createRelation(String name, int numberOfColumns, List<String> types) throws DiskManagerException {
		RelationDefinition relation = new RelationDefinition();
		int recordSize = this.calculateRecordSizeFrom(types);
		relation.setName(name);
		relation.setNumberOfColumns(numberOfColumns);
		relation.setColumnTypes(types);
		relation.setRecordSize(recordSize);
		relation.setSlotCount(Constants.PAGE_SIZE / recordSize);
		long fileIndex = this.dataBaseDefinition.getNumberOfRelation();
		relation.setFileIndex(fileIndex);
		
		this.diskManager.createFile(fileIndex);
		
		this.dataBaseDefinition.addRelation(relation);
		
		PageId pageId = new PageId();
		pageId.setFileIndex(fileIndex);
		pageId.setPageIndex(0);
		this.diskManager.addPage(fileIndex, pageId);
		
		this.fileManager.createHeapFile(relation);
	}
	private int calculateRecordSizeFrom(List<String> columnTypes) {
		int size = 0;
		for(String type: columnTypes) {
			size+=TypeFactory.of(type).size();
		}
		return size;
	}

	@Override
	public void insertRecord(String relationName, List<String> values) throws DiskManagerException {
		this.fileManager.insertRecord(relationName, values, this.bufferManager, this.diskManager);
	}

	@Override
	public List<Record> readRecords(String relationName) throws DiskManagerException {
		return this.fileManager.readRecords(relationName);
	}

	@Override
	public RelationDefinition getRelation(String name) {
		return this.dataBaseDefinition.getRelations()
				.stream()
				.filter(relation -> relation.getName().equals(name))
				.findFirst()
				.orElseThrow();
	}
	
}
