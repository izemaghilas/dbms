package dbms.core;

public abstract class Type {
	
	protected int offset;
	
	public Type() {
		
	}
	
	public int getOffset() {
		return this.offset;
	}
	
	public void setOfffset(int offset) {
		this.offset = offset;
	}
	
	public abstract int size();
	
	public abstract void write(String value, byte[] buffer);

	public abstract String read(byte[] buffer);
}
