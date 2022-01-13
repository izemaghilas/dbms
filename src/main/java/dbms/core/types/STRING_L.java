package dbms.core.types;

import java.nio.ByteBuffer;

import dbms.core.Type;

public class STRING_L extends Type {
	
	private int L; // string length
	
	public STRING_L(int L) {
		this.L = L;
	}

	public int size() {
		return this.L * Character.BYTES;
	}

	public void write(String value, byte[] buffer) {
		for(int i=0; i<L; i++) {
			if(i < value.length()) {
				ByteBuffer.wrap(buffer).putChar(this.offset, value.charAt(i));
			}
			else {
				ByteBuffer.wrap(buffer).putChar(this.offset, '\u0000');
			}
			this.offset+=Character.BYTES;
		}
		
	}

	public String read(byte[] buffer) {
		String value = "";
		for(int i=0; i<this.L; i++) {
			value+=ByteBuffer.wrap(buffer).getChar(this.offset);
			this.offset+=Character.BYTES;
		}
		return value.trim();
	}

}
