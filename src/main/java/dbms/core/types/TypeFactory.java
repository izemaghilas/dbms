package dbms.core.types;

import dbms.core.Type;

public class TypeFactory {
	
	private TypeFactory() {
		
	}
	
	public static Type of(String name) {
		name = name.toUpperCase();
		
		if(name.matches("^INT$")) {
			return new INT();
		}
		
		if(name.matches("^FLOAT$")) {
			return new FLOAT();
		}
		
		if(name.matches("^STRING[0-9]+$")) {
			return new STRING_L( Integer.parseInt(name.substring(6)) );
		}
		
		return null;
	}
}
