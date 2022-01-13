package dbms.core.types;

import java.nio.ByteBuffer;

import dbms.core.Type;

public class INT extends Type {
	
	public INT() {
	}

	public int size() {
		return Integer.BYTES;
	}

	public void write(String value, byte[] buffer) {
		ByteBuffer.wrap(buffer).putInt(this.offset, Integer.parseInt(value));
		this.offset+=Integer.BYTES;
	}

	public String read(byte[] buffer) {
		int value = ByteBuffer.wrap(buffer).getInt(this.offset);
		this.offset+=Integer.BYTES;
		return Integer.toString(value);
	}
}
