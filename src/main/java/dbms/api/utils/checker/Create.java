package dbms.api.utils.checker;

import dbms.api.utils.QueryCheckerException;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonValue;

public class Create extends Checker {
	
	@Override
	public void checkQueryDefinition(JsonObject query) throws QueryCheckerException {
		JsonObject queryDefinition =  query.getJsonObject("definition");
		
		if(queryDefinition == null) {
			throw new QueryCheckerException("CREATE query, missing *definition* key");
		}
		
		if(!queryDefinition.containsKey("relation")) {
			throw new QueryCheckerException("CREATE query definition, missing *relation* key");
		}
		
		int count = -1;
		if(!queryDefinition.containsKey("count")) {
			throw new QueryCheckerException("CREATE query definition, missing *count* key");
		}
		else {
			count = queryDefinition.getInt("count");
		}
		
		JsonArray columns = queryDefinition.getJsonArray("columns");
		if(columns == null) {
			throw new QueryCheckerException("CREATE query definition, missing *columns* key");
		}
		
		if(count != columns.size()) {
			throw new QueryCheckerException("CREATE query definition, expected "+count+" columns "+"got "+columns.size());
		}
		
		for(JsonValue column : columns) {
			JsonObject obj = column.asJsonObject();
			if(!obj.containsKey("index")) {
				throw new QueryCheckerException("CREATE query definition, missing column *index* key");
			}
			
			if(!obj.containsKey("type")) {
				throw new QueryCheckerException("CREATE query definition, missing column *type* key");
			}
		}
	}

}
