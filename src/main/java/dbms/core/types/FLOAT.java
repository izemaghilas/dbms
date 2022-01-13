package dbms.core.types;

import java.nio.ByteBuffer;

import dbms.core.Type;

public class FLOAT extends Type {

	public FLOAT() {
	}
	
	public int size() {
		return Float.BYTES;
	}

	public void write(String value, byte[] buffer) {
		ByteBuffer.wrap(buffer).putFloat(this.offset, Float.parseFloat(value));
		this.offset+=Float.BYTES;
	}

	public String read(byte[] buffer) {
		float value = ByteBuffer.wrap(buffer).getFloat(this.offset);
		this.offset+=Float.BYTES;
		return Float.toString(value);
	}
}
