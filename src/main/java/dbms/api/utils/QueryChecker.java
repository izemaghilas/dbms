package dbms.api.utils;


import dbms.api.utils.checker.Checker;
import dbms.api.utils.checker.CheckerFactory;
import jakarta.json.JsonObject;

public class QueryChecker {
	
	public static void check(JsonObject query) throws QueryCheckerException {
		if(query == null || query.isEmpty()) {
			throw new QueryCheckerException("No query to process");
		}
		
		if(query.containsKey("name")) {
			String queryName = query.getString("name");
			Checker checker = CheckerFactory.of(queryName);
			checker.checkQueryDefinition(query);
		}
		else {
			throw new QueryCheckerException("Missing query *name* key");
		}
	}
}
