package dbms.core.databasemanager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RelationDefinition implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6400446349377258639L;
	
	private String name;
	private int numberOfColumns;
	private List<String> columnTypes;
	private int recordSize;
	private int slotCount;
	private long fileIndex;
	
	public RelationDefinition() {
		this.columnTypes = new ArrayList<String>();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getNumberOfColumns() {
		return numberOfColumns;
	}

	public void setNumberOfColumns(int numberOfColumns) {
		this.numberOfColumns = numberOfColumns;
	}

	public List<String> getColumnTypes() {
		return columnTypes;
	}

	public void setColumnTypes(List<String> columnTypes) {
		// as I used a sublist to get the types in Create executor
		// and the sublist is not serializable
		// so use addAll instead
		
		this.columnTypes.clear();
		this.columnTypes.addAll(columnTypes);
	}

	public int getRecordSize() {
		return recordSize;
	}

	public void setRecordSize(int recordSize) {
		this.recordSize = recordSize;
	}

	public int getSlotCount() {
		return slotCount;
	}

	public void setSlotCount(int slotCount) {
		this.slotCount = slotCount;
	}

	public long getFileIndex() {
		return fileIndex;
	}

	public void setFileIndex(long fileIndex) {
		this.fileIndex = fileIndex;
	}
	
	
}
