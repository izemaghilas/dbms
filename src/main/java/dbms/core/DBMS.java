package dbms.core;

import java.io.IOException;
import java.util.List;

import dbms.core.databasemanager.Manager;
import dbms.core.databasemanager.RelationDefinition;
import dbms.core.diskmanager.DiskManagerException;
import dbms.core.filemanager.Record;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
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
		JsonArrayBuilder recordsBuilder = Json.createArrayBuilder();
		
		try {
			List<Record> records = this.dataBaseManager.readRecords(relationName);
			if(!records.isEmpty()) {
				RelationDefinition relation = this.dataBaseManager.getRelation(relationName);
				for(Record record : records) {
					JsonObjectBuilder recordBuilder = Json.createObjectBuilder();
					JsonArrayBuilder recordValuesBuilder = Json.createArrayBuilder();
					List<String> values = record.getValues();
					for(int i=0; i<values.size(); i++) {
						JsonObjectBuilder valueBuilder = Json.createObjectBuilder();
						valueBuilder.add("column", relation.getColumnTypes().get(i)).add("value", values.get(i));
						recordValuesBuilder.add(valueBuilder);
					}
					recordBuilder.add("values", recordValuesBuilder);
					recordsBuilder.add(recordBuilder);
				}
			}
		} catch (DiskManagerException e) {
			throw new DBMSException("Error while processing SELECTALL query", e.getCause());
		}
		
		return recordsBuilder.build();
	}
}
