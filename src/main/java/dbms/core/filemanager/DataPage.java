package dbms.core.filemanager;

public class DataPage {
	private long dataPageIndex;
	private int freeSlots;
	
	
	public DataPage() {
	
	}


	public long getDataPageIndex() {
		return dataPageIndex;
	}


	public void setDataPageIndex(long dataPageIndex) {
		this.dataPageIndex = dataPageIndex;
	}


	public int getFreeSlots() {
		return freeSlots;
	}


	public void setFreeSlots(int freeSlots) {
		this.freeSlots = freeSlots;
	}
	
	
}
