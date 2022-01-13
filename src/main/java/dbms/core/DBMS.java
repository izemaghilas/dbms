package dbms.core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import dbms.core.databasemanager.Manager;
import dbms.core.databasemanager.RelationDefinition;
import dbms.core.diskmanager.DiskManagerException;
import dbms.core.filemanager.Record;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonValue;

public enum DBMS {
	INSTANCE;
	
	private DataBaseManager dataBaseManager;
	
	private DBMS() throws DBMSException {
		this.dataBaseManager = Manager.INSTANCE;
		try {
			this.dataBaseManager.init();
		} catch (ClassNotFoundException | IOException e) {
			throw new DBMSException("Error while initializing DBMS", e);
		}
	}
	
	public void createRelation(JsonObject query) throws DBMSException {
		try {
			JsonObject queryDefinition = query.getJsonObject("definition");
			String relationName = queryDefinition.getString("relation");
			int numberOfcolumns = queryDefinition.getInt("count");
			String[] types = new String[numberOfcolumns];
			for(JsonValue column: queryDefinition.getJsonArray("columns")) {
				JsonObject obj = column.asJsonObject();
				types[obj.getInt("index")-1] = obj.getString("type");
			}
			this.dataBaseManager.createRelation(relationName, numberOfcolumns, List.of(types));
		} catch (DiskManagerException e) {
			throw new DBMSException("Error while processing CREATE query", e.getCause());
		}
	}
	
	public void insertRecord(JsonObject query) {
		try {
			JsonObject queryDefinition = query.getJsonObject("definition");
			String relationName = queryDefinition.getString("relation");
			JsonArray recordValues = queryDefinition.getJsonObject("record").getJsonArray("values");
			String [] values = new String[recordValues.size()];
			for(JsonValue value : recordValues) {
				JsonObject obj = value.asJsonObject();
				values[obj.getInt("column")-1] = obj.getString("value");
			}
			this.dataBaseManager.insertRecord(relationName, List.of(values));
		} catch (DiskManagerException e) {
			throw new DBMSException("Error while processing INSERT query", e.getCause());
		}
	}
	
	public JsonArray getAllRecords(String relationName) {
		JsonArray recordsJson = null;
		List<JsonObject> objs = new ArrayList<JsonObject>();
		
		try {
			List<Record> records = this.dataBaseManager.readRecords(relationName);
			if(records.isEmpty()) {
				recordsJson = Json.createArrayBuilder().build();
			}
			else {
				RelationDefinition relation = this.dataBaseManager.getRelation(relationName);
				for(Record record : records) {
					List<String> values = record.getValues();
					List<JsonObject> valueObjs = new ArrayList<JsonObject>();
					for(int i=0; i<values.size(); i++) {
						valueObjs.add(
									Json.createObjectBuilder()
										.add("column", relation.getColumnTypes().get(i))
										.add("value", values.get(i))
										.build()
								);
					}
					objs.add( 
							Json.createObjectBuilder()
								.add("values", Json.createArrayBuilder(valueObjs).build())
								.build()
						);
				}
				recordsJson = Json.createArrayBuilder(objs).build();
			}
		} catch (DiskManagerException e) {
			throw new DBMSException("Error while processing SELECTALL query", e.getCause());
		}
		
		return recordsJson;
	}
}
