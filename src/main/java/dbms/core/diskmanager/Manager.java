package dbms.core.diskmanager;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import dbms.core.Constants;
import dbms.core.DiskManager;
import dbms.core.PageId;

public enum Manager implements DiskManager {
	INSTANCE;

	@Override
	public void createFile(long fileIndex) throws DiskManagerException {
		
		if(Files.notExists(Paths.get(Constants.DB_DIRECTORY))) {
			try {
				Files.createDirectory(Paths.get(Constants.DB_DIRECTORY));
			} catch (IOException e) {
				e.printStackTrace();
				throw new DiskManagerException("Error while creating directory", e);
			}
		}
		
		Path pathToDataFile = Path.of(Constants.DB_DIRECTORY + "/DATA_" + fileIndex + ".rf");
		
		try {
			
			Files.createFile(pathToDataFile);
		
		} catch (IOException e) {
			e.printStackTrace();
			throw new DiskManagerException("Error while creating data file", e);
		}
	}

	@Override
	public void addPage(long fileIndex, PageId pageId) throws DiskManagerException {
		
		Path pathToDataFile = Path.of(Constants.DB_DIRECTORY+"/DATA_"+fileIndex+".rf");
		
		try(RandomAccessFile raf = new RandomAccessFile(pathToDataFile.toFile(), "rw")){
			
			raf.seek(raf.length());
			raf.write(new byte[Constants.PAGE_SIZE]);
			pageId.setFileIndex(fileIndex);
			pageId.setPageIndex(raf.length() / Constants.PAGE_SIZE - 1);
			
			raf.close();
		
		} catch (IOException e) {
			e.printStackTrace();
			throw new DiskManagerException("Error while adding page", e);
		}
	}

	@Override
	public void readPage(PageId pageId, byte[] buffer) throws DiskManagerException {
		
		Path pathToDataFile = Path.of(Constants.DB_DIRECTORY+"/DATA_"+pageId.getFileIndex()+".rf");
		
		try(RandomAccessFile raf = new RandomAccessFile(pathToDataFile.toFile(), "r")){
			
			long pageIndex = pageId.getPageIndex() * Constants.PAGE_SIZE;
			raf.seek(pageIndex);
			raf.read(buffer, 0, Constants.PAGE_SIZE);
			
			raf.close();
		
		} catch(IOException e) {
			e.printStackTrace();
			throw new DiskManagerException("Error while reading page", e);
		}
	}

	@Override
	public void writePage(PageId pageId, byte[] buffer) throws DiskManagerException {
		
		Path pathToDataFile = Path.of(Constants.DB_DIRECTORY+"/DATA_"+pageId.getFileIndex()+".rf");
		
		try(RandomAccessFile raf = new RandomAccessFile(pathToDataFile.toFile(), "rw")){
			
			long pageIndex = pageId.getPageIndex() * Constants.PAGE_SIZE;
			raf.seek(pageIndex);
			raf.write(buffer);
			
			raf.close();
		
		} catch(IOException e) {
			e.printStackTrace();
			throw new DiskManagerException("Error while writing page", e);
		}
	}

	@Override
	public void clean() {
		if(Files.exists(Paths.get(Constants.DB_DIRECTORY))) {
			for( File file: Path.of(Constants.DB_DIRECTORY).toFile().listFiles() ) {
				file.delete();
			}			
		}
	}
	
}
