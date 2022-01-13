package dbms.core.buffermanager;

import java.util.Objects;

import dbms.core.PageId;

public class Frame {
	private PageId pageId;
	private int pinCount;
	private boolean dirty;
	private byte[] buffer;
	private boolean useBit; // for clock replacement policy
	
	public Frame(PageId pageId, int pinCount, boolean dirty, byte[] buffer, boolean useBit) {
		this.pageId = pageId;
		this.pinCount = pinCount;
		this.dirty = dirty;
		this.buffer = buffer;
		this.setUseBit(useBit);
	}

	public PageId getPageId() {
		return pageId;
	}

	public void setPageId(PageId pageId) {
		this.pageId = pageId;
	}

	public byte[] getBuffer() {
		return buffer;
	}

	public void setBuffer(byte[] buffer) {
		this.buffer = buffer;
	}
	
	public boolean isDirty() {
		return this.dirty;
	}
	public void setFlagDirty(boolean bool) {
		this.dirty = bool;
	}
	
	public int getPinCount() {
		return pinCount;
	}

	public void incrementPinCount() {
		this.pinCount+=1;
	}
	public void decrementPinCount() {
		if(this.pinCount > 0) {
			this.pinCount-=1;
		}
	}
	
	public boolean getUseBit() {
		return useBit;
	}
	
	public void setUseBit(boolean bool) {
		this.useBit = bool;
	}

	@Override
	public int hashCode() {
		return Objects.hash(pageId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Frame other = (Frame) obj;
		return Objects.equals(pageId, other.pageId);
	}

	
}
