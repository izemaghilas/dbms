package dbms.core.buffermanager;

import java.util.List;

import dbms.core.BufferManager;
import dbms.core.Constants;
import dbms.core.DiskManager;
import dbms.core.PageId;
import dbms.core.diskmanager.DiskManagerException;

public enum Manager implements BufferManager {
	INSTANCE;

	private DiskManager diskManager;
	private BufferPool bufferPool;
	
	@Override
	public void init(DiskManager diskManager) {
		this.diskManager = diskManager;
		this.bufferPool = BufferPool.INSTANCE;
		this.bufferPool.init();
	}
	
	@Override
	public byte[] getPageBuffer(PageId pageId) throws DiskManagerException {
		
		Frame frame = this.bufferPool.getFrameBy(pageId);
		
		if(frame == null) {
			byte[] pageBuffer = new byte[Constants.PAGE_SIZE];
			
			this.diskManager.readPage(pageId, pageBuffer);
			frame = new Frame(pageId, 1, false, pageBuffer, true);
			
			try {
				this.bufferPool.addFrame(frame);
			} catch (FullPoolException e) {
				this.applyClock(frame);
			}
			
			return pageBuffer;
		}
		
		frame.incrementPinCount();
		return frame.getBuffer();
	}

	@Override
	public void freePage(PageId pageId, boolean isDirty) throws DiskManagerException {
		Frame frame = this.bufferPool.getFrameBy(pageId);
		frame.decrementPinCount();
		if(isDirty) {
			this.diskManager.writePage(pageId, frame.getBuffer());
		}
	}

	public void applyClock(Frame newFrame) throws DiskManagerException {
		List<Frame> frames = this.bufferPool.getFrames();
		while(true) {
			int pointer = this.bufferPool.getPointer();
			Frame frame = frames.get(pointer);
			
			if(frame.getPinCount() == 0 && frame.getUseBit() == true) {
				frame.setUseBit(false);
			}
			else if(frame.getPinCount() == 0 && frame.getUseBit() == false) {
				if(frame.isDirty()) {
					this.diskManager.writePage(frame.getPageId(), frame.getBuffer());
				}
				frames.set(pointer, newFrame);
				this.bufferPool.setPointer(pointer+1);
				break;
			}
			
			this.bufferPool.setPointer(pointer+1);
		}
	}

	@Override
	public void finish() throws DiskManagerException {
		List<Frame> frames = this.bufferPool.getFrames();
		
		for(Frame frame : frames) {
			if(frame.isDirty()) {
				this.diskManager.writePage(frame.getPageId(), frame.getBuffer());
			}
		}
	}
}
