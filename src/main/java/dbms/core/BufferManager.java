package dbms.core;

import dbms.core.diskmanager.DiskManagerException;

public interface BufferManager {
	
	public void init(DiskManager diskManager);
	
	public byte[] getPageBuffer(PageId pageId) throws DiskManagerException;
	
	public void freePage(PageId pageId, boolean isDirty) throws DiskManagerException;

	public void finish() throws DiskManagerException;
}
