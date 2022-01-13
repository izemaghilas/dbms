package dbms.core.buffermanager;

import java.util.ArrayList;
import java.util.List;

import dbms.core.Constants;
import dbms.core.PageId;

public enum BufferPool {
	INSTANCE;
	
	private List<Frame> frames;
	private int pointer; // pointer for clock replacement policy
	
	public void init() {
		this.frames = new ArrayList<>();
		this.setPointer(0);
	}
	
	public void clean() {
		this.frames.clear();
		this.setPointer(0);
	}
	
	public List<Frame> getFrames() {
		return this.frames;
	}
	
	public int size() {
		return this.frames.size();
	}
	
	public void addFrame(Frame frame) throws FullPoolException {
		if(this.frames.size() < Constants.POOL_SIZE) {
			Frame f = this.getFrameBy(frame.getPageId());
			if(f != null) {
				f.setUseBit(true);
			}
			else {
				this.frames.add(frame);
			}
		}
		else {
			throw new FullPoolException("Buffer Pool is full");
		}
	}
	
	public Frame getFrameBy(PageId pageId) {
		for(Frame frame: this.frames) {
			if(frame.getPageId().equals(pageId)) {
				return frame;
			}
		}
		return null;
	}

	public int getPointer() {
		return pointer;
	}

	public void setPointer(int pointer) {
		if(pointer >= this.frames.size()) {
			this.pointer = 0;
		}
		else {
			this.pointer = pointer;
		}
	}
}
