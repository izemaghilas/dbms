package dbms.core.filemanager;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import dbms.core.BufferManager;
import dbms.core.DiskManager;
import dbms.core.PageId;
import dbms.core.Type;
import dbms.core.databasemanager.RelationDefinition;
import dbms.core.diskmanager.DiskManagerException;
import dbms.core.types.TypeFactory;

public class HeapFile {
	
	private RelationDefinition relation;
	
	public HeapFile() {
		
	}

	public RelationDefinition getRelation() {
		return relation;
	}

	public void setRelation(RelationDefinition relation) {
		this.relation = relation;
	}
	
	private byte[] initializeHeaderPage(HeaderPage headerPage, PageId headerPageId, BufferManager bufferManager) throws DiskManagerException {
		headerPageId.setFileIndex(this.relation.getFileIndex());
		headerPageId.setPageIndex(0);
		byte[] buffer = bufferManager.getPageBuffer(headerPageId);
		headerPage.readFromBuffer(buffer);
		
		return buffer; 
	}
	
	public void createHeaderPage(BufferManager bufferManager) throws DiskManagerException {
		HeaderPage headerPage = new HeaderPage();
		PageId headerPageId = new PageId();
		headerPageId.setFileIndex(relation.getFileIndex());
		headerPageId.setPageIndex(0);
		
		byte[] headerPageBuffer = bufferManager.getPageBuffer(headerPageId);
		headerPage.writeToBuffer(headerPageBuffer);
		bufferManager.freePage(headerPageId, true);
	}
	
	public void insertRecord(Record record, BufferManager bufferManager, DiskManager diskManager) throws DiskManagerException {
		
		HeaderPage headerPage = new HeaderPage();
		PageId headerPageId = new PageId();
		byte[] headerPageBuffer = this.initializeHeaderPage(headerPage, headerPageId, bufferManager);
		
		// insert record in page
		PageId pageId = new PageId();
		try {
			DataPage dataPage = headerPage.getDataPages().stream().filter(dataPageP -> dataPageP.getFreeSlots() > 0).findFirst().orElseThrow();
			pageId.setFileIndex(this.relation.getFileIndex());
			pageId.setPageIndex(dataPage.getDataPageIndex());
			byte[] pageBuffer = bufferManager.getPageBuffer(pageId);
			this.writeRecordToBuffer(record, pageBuffer);
			
			// update header page, as the data page had a slot filled with a new record
			int freeSlots = dataPage.getFreeSlots();
			dataPage.setFreeSlots(freeSlots - 1);
			headerPage.writeToBuffer(headerPageBuffer);
			
			bufferManager.freePage(headerPageId, true);
			bufferManager.freePage(pageId, true);
			
		} catch (NoSuchElementException e) {
			
			diskManager.addPage(relation.getFileIndex(), pageId);
			byte[] pageBuffer = bufferManager.getPageBuffer(pageId);
			this.writeRecordToBuffer(record, pageBuffer);
			
			DataPage dataPage = new DataPage();
			dataPage.setDataPageIndex(pageId.getPageIndex());

			// each data page has a bytemap.
			// bytemap size = number of slots in the relation - 1, each byte is either 0 if free and 1 if not
			dataPage.setFreeSlots( relation.getSlotCount() - 2 ); // minus the bytemap and the filled slot
			headerPage.addDataPage(dataPage);
			headerPage.writeToBuffer(headerPageBuffer);
			
			bufferManager.freePage(headerPageId, true);
			bufferManager.freePage(pageId, true);
		}
	}
	
	private void writeRecordToBuffer(Record record, byte[] buffer) {
		List<String> values = record.getValues();
		List<String> types = this.relation.getColumnTypes();
		
		int bytemapSlotIndex = this.getBytemapFreeSlotIndex(buffer);
		int bytemapSize = this.relation.getSlotCount() -1;
		// offset = starting index of record data
		int offset = ( bytemapSlotIndex * this.relation.getRecordSize() ) + bytemapSize;
		
		for(int i=0; i<types.size(); i++) {
			Type dataType = TypeFactory.of(types.get(i));
			dataType.setOfffset(offset);
			dataType.write(values.get(i), buffer);
			offset = dataType.getOffset();
		}
		ByteBuffer.wrap(buffer).put(bytemapSlotIndex, (byte) 1);
	}
	private Record readRecordFromBuffer(byte[] buffer, int offset) {
		Record record = new Record();
		List<String> values = new ArrayList<String>();
		List<String> types = this.relation.getColumnTypes();
		
		for(String type : types) {
			Type dataType = TypeFactory.of(type);
			dataType.setOfffset(offset);
			values.add(dataType.read(buffer));
			offset = dataType.getOffset();
		}
		record.setValues(values);
		
		return record;
	}
	
	private int getBytemapFreeSlotIndex(byte[] buffer) {
		// data page bytemap size
		int bytemapSize = this.relation.getSlotCount() -1;
		for(int i=0; i<bytemapSize; i++) {
			byte index = ByteBuffer.wrap(buffer).get(i);
			if(index == (byte)0) {
				return i;
			}
		}
		return -1;
	}

	public List<Record> readRecords(BufferManager bufferManager) throws DiskManagerException {
		List<Record> records = new ArrayList<Record>();
		
		HeaderPage headerPage = new HeaderPage();
		PageId headerPageId = new PageId();
		this.initializeHeaderPage(headerPage, headerPageId, bufferManager);
		bufferManager.freePage(headerPageId, false);
		
		List<PageId> dataPageIds = this.getDataPageIds(headerPage);
		
		for(PageId dataPageId : dataPageIds) {
			byte[] buffer = bufferManager.getPageBuffer(dataPageId);
			
			int bytemapSize = this.relation.getSlotCount() -1;
			for(int i = 0; i < bytemapSize; i++) {
				byte index = ByteBuffer.wrap(buffer).get(i);
				if(index == (byte)1) {
					int offset =  ( i * this.relation.getRecordSize() ) + bytemapSize;
					records.add( this.readRecordFromBuffer(buffer, offset) );
				}
			}
			
			bufferManager.freePage(dataPageId, false);
		}
		
		return records;
	}

	private List<PageId> getDataPageIds(HeaderPage headerPage){
		List<PageId> pageIds = new ArrayList<PageId>();
		for(DataPage dataPage : headerPage.getDataPages()) {
			PageId pageId = new PageId();
			pageId.setFileIndex(this.relation.getFileIndex());
			pageId.setPageIndex(dataPage.getDataPageIndex());
			
			pageIds.add(pageId);
		}
		return pageIds;
	}
	
}
