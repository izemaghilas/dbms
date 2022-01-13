package dbms.core;

import java.util.Objects;

public class PageId {
	private long fileIndex;
	private long pageIndex;
	
	public PageId() {
	}
	
	public long getFileIndex() {
		return fileIndex;
	}
	public void setFileIndex(long fileIndex) {
		this.fileIndex = fileIndex;
	}
	
	public long getPageIndex() {
		return pageIndex;
	}
	public void setPageIndex(long pageIndex) {
		this.pageIndex = pageIndex;
	}

	@Override
	public int hashCode() {
		return Objects.hash(fileIndex, pageIndex);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PageId other = (PageId) obj;
		return fileIndex == other.fileIndex && pageIndex == other.pageIndex;
	}
	
	
}
