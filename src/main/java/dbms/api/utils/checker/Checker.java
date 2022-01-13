package dbms.api.utils.checker;

import dbms.api.utils.QueryCheckerException;
import jakarta.json.JsonObject;

public abstract class Checker {

	public abstract void checkQueryDefinition(JsonObject query) throws QueryCheckerException;
	
}
