package dbms.core;

import dbms.core.diskmanager.DiskManagerException;

public interface DiskManager {
	
	public void createFile(long fileIndex) throws DiskManagerException;
	
	public void addPage(long fileIndex, PageId pageId ) throws DiskManagerException;

	public void readPage(PageId pageId, byte[] buffer) throws DiskManagerException;

	public void writePage(PageId pageId, byte[] buffer) throws DiskManagerException;
	
	public void clean();
}
