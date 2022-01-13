package dbms.api.utils.checker;

import dbms.api.utils.QueryCheckerException;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonValue;

public class Insert extends Checker {

	@Override
	public void checkQueryDefinition(JsonObject query) throws QueryCheckerException {
		JsonObject queryDefinition =  query.getJsonObject("definition");
		
		if(queryDefinition == null) {
			throw new QueryCheckerException("INSERT query, missing *definition* key");
		}
		
		if(!queryDefinition.containsKey("relation")) {
			throw new QueryCheckerException("INSERT query definition, missing *relation* key");
		}
		
		JsonObject record = queryDefinition.getJsonObject("record");
		if(record == null) {
			throw new QueryCheckerException("INSERT query definition, missing *record* key");
		}
		
		JsonArray values = record.getJsonArray("values");
		if(values == null) {
			throw new QueryCheckerException("INSERT query definition, missing record *values* key");
		}
		if(values.isEmpty()) {
			throw new QueryCheckerException("INSERT query definition, empty record values");
		}
		
		for(JsonValue value : values) {
			JsonObject obj = value.asJsonObject();
			if(!obj.containsKey("column")) {
				throw new QueryCheckerException("INSERT query definition, missing record values *column* key");
			}
			
			if(!obj.containsKey("value")) {
				throw new QueryCheckerException("INSERT query definition, missing record values *value* key");
			}
		}
	}
}
