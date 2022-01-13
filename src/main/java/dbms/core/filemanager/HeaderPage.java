package dbms.core.filemanager;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HeaderPage {
	
	private List<DataPage> dataPages;

	public HeaderPage() {
		this.dataPages = new ArrayList<DataPage>();
	}
	
	public List<DataPage> getDataPages() {
		return dataPages;
	}

	public void setDataPages(List<DataPage> dataPages) {
		this.dataPages = dataPages;
	}
	
	public void addDataPage(DataPage dataPage) {
		this.dataPages.add(dataPage);
	}

	public void writeToBuffer(byte[] headerPageBuffer) {
		int offset = 0;
		ByteBuffer.wrap(headerPageBuffer).putInt(offset, this.dataPages.size());
		offset+=Integer.BYTES;
		for(DataPage dataPage: this.dataPages) {
			ByteBuffer.wrap(headerPageBuffer).putLong(offset, dataPage.getDataPageIndex());
			offset+=Long.BYTES;
			ByteBuffer.wrap(headerPageBuffer).putInt(offset, dataPage.getFreeSlots());
			offset+=Integer.BYTES;
		}
	}

	public void readFromBuffer(byte[] buffer) {
		int offset = 0;
		int numberOfDataPages = ByteBuffer.wrap(Arrays.copyOfRange(buffer, offset, offset+Integer.BYTES)).getInt();
		offset+=Integer.BYTES;
		for(int i=0; i < numberOfDataPages; i++) {
			DataPage dataPage = new DataPage();
			long dataPageIndex = ByteBuffer.wrap(Arrays.copyOfRange(buffer, offset, offset+Long.BYTES)).getLong();
			offset+=Long.BYTES;
			int freeSlots = ByteBuffer.wrap(Arrays.copyOfRange(buffer, offset, offset+Integer.BYTES)).getInt();
			offset+=Integer.BYTES;
			
			dataPage.setDataPageIndex(dataPageIndex);
			dataPage.setFreeSlots(freeSlots);
			this.dataPages.add(dataPage);
		}
	}
	
	
	
}
