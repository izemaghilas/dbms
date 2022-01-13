package dbms.api.utils.checker;

import dbms.api.utils.QueryCheckerException;

public class CheckerFactory {

	private final static Checker CREATE = new Create();
	private final static Checker INSERT = new Insert();
	
	public static Checker of(String queryName) throws QueryCheckerException {
		queryName = queryName.toUpperCase();
		
		if(queryName.equals("CREATE")) {
			return CREATE;
		}
		
		if(queryName.equals("INSERT")) {
			return INSERT;
		}
		
		throw new QueryCheckerException("Unrecognized query *"+queryName+"*");
	}

}
