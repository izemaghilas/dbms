package dbms.api.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;

import static org.assertj.core.api.Assertions.*;

import java.util.stream.Stream;

public class QueryCheckerTest {
	
	
	// **************** Query Definition Providers ***************************************************************************
	
	private static Stream<Arguments> provideCREATEqueryDefinitions() {
		JsonObject queryDefinitionNoRelationKey = Json.createObjectBuilder()
				.add("count", 1)
				.add("columns", Json.createArrayBuilder()
						.add(Json.createObjectBuilder()
								.add("index", 1)
								.add("type", "INT")
								.build())
						.build())
				.build();
		
		JsonObject queryDefinitionNoCountKey = Json.createObjectBuilder()
				.add("relation", "book")
				.add("columns", Json.createArrayBuilder()
						.add(Json.createObjectBuilder()
								.add("index", 1)
								.add("type", "STRING24")
								.build())
						.build())
				.build();
		
		JsonObject queryDefinitionNoColumnsKey = Json.createObjectBuilder()
				.add("relation", "book")
				.add("count", 3)
				.build();
		
		JsonObject queryDefinitionNoColumnIndexKey = Json.createObjectBuilder()
				.add("relation", "book")
				.add("count", 1)
				.add("columns", Json.createArrayBuilder()
						.add(Json.createObjectBuilder()
								.add("type", "FLOAT")
								.build())
						.build())
				.build();
		
		JsonObject queryDefinitionNoColumnTypeKey = Json.createObjectBuilder()
				.add("relation", "book")
				.add("count", 1)
				.add("columns", Json.createArrayBuilder()
						.add(Json.createObjectBuilder()
								.add("index", 1)
								.build())
						.build())
				.build();
		
		JsonObject queryDefinitionNotSameCountColumnsSize = Json.createObjectBuilder()
				.add("relation", "book")
				.add("count", 2)
				.add("columns", Json.createArrayBuilder()
						.add(Json.createObjectBuilder()
								.add("index", 1)
								.add("type", "INT")
								.build())
						.build())
				.build();
		
		return Stream.of(
				Arguments.of(queryDefinitionNoRelationKey, "CREATE query definition, missing *relation* key"),
				Arguments.of(queryDefinitionNoCountKey, "CREATE query definition, missing *count* key"),
				Arguments.of(queryDefinitionNoColumnsKey, "CREATE query definition, missing *columns* key"),
				Arguments.of(queryDefinitionNoColumnIndexKey, "CREATE query definition, missing column *index* key"),
				Arguments.of(queryDefinitionNoColumnTypeKey, "CREATE query definition, missing column *type* key"),
				Arguments.of(queryDefinitionNotSameCountColumnsSize, "CREATE query definition, expected 2 columns got 1")
				);
	}
	
	private static Stream<Arguments> provideINSERTqueryDefinitions() {
		JsonObject queryDefinitionNoRelationKey = Json.createObjectBuilder()
				.add("record", Json.createObjectBuilder()
						.add("values", Json.createArrayBuilder()
								.add(Json.createObjectBuilder()
										.add("column", 1)
										.add("value", 25)
										.build())
								.build())
						.build())
				.build();
		
		JsonObject queryDefinitionNoRecordKey = Json.createObjectBuilder()
				.add("relation", "book")
				.build();
		
		JsonObject queryDefinitionNoRecordValuesKey = Json.createObjectBuilder()
				.add("relation", "book")
				.add("record", Json.createObjectBuilder().build())
				.build();
		
		JsonObject queryDefinitionNoRecordValuesColumnKey = Json.createObjectBuilder()
				.add("relation", "book")
				.add("record", Json.createObjectBuilder()
						.add("values", Json.createArrayBuilder()
								.add(Json.createObjectBuilder()
										.add("value", 25)
										.build())
								.build())
						.build())
				.build();
		
		JsonObject queryDefinitionNoRecordValuesValueKey = Json.createObjectBuilder()
				.add("relation", "book")
				.add("record", Json.createObjectBuilder()
						.add("values", Json.createArrayBuilder()
								.add(Json.createObjectBuilder()
										.add("column", 1)
										.build())
								.build())
						.build())
				.build();
		
		JsonObject queryDefinitionEmptyRecordValues = Json.createObjectBuilder()
				.add("relation", "book")
				.add("record", Json.createObjectBuilder()
						.add("values", Json.createArrayBuilder()
								.build())
						.build())
				.build();
		
		
		return Stream.of(
				Arguments.of(queryDefinitionNoRelationKey, "INSERT query definition, missing *relation* key"),
				Arguments.of(queryDefinitionNoRecordKey, "INSERT query definition, missing *record* key"),
				Arguments.of(queryDefinitionNoRecordValuesKey, "INSERT query definition, missing record *values* key"),
				Arguments.of(queryDefinitionNoRecordValuesColumnKey, "INSERT query definition, missing record values *column* key"),
				Arguments.of(queryDefinitionNoRecordValuesValueKey, "INSERT query definition, missing record values *value* key"),
				Arguments.of(queryDefinitionEmptyRecordValues, "INSERT query definition, empty record values")
				);
	}
	
	//*********************************************************************************************************
	
	
	@Test
	public void testQueryCREATE() {
		JsonArray columns = Json.createArrayBuilder()
				.add(Json.createObjectBuilder()
						.add("index", 1)
						.add("type", "INT")
						.build()
					)
				.add(Json.createObjectBuilder()
						.add("index", 2)
						.add("type", "STRING24")
						.build()
					)
				.add(Json.createObjectBuilder()
						.add("index", 3)
						.add("type", "FLOAT")
						.build()
						)
				.build();
		
		JsonObject queryDefinition = Json.createObjectBuilder()
				.add("relation", "book")
				.add("count", 3)
				.add("columns", columns)
				.build();
		
		JsonObject query = Json.createObjectBuilder()
				.add("name", "CREATE")
				.add("definition", queryDefinition)
				.build();
		
		assertThatNoException().isThrownBy(()->{ QueryChecker.check(query); });
	}
	
	@Test
	public void testQueryINSERT() {
		JsonArray recordValues = Json.createArrayBuilder()
				.add(Json.createObjectBuilder()
						.add("column", 1)
						.add("value", 25)
						.build())
				.add(Json.createObjectBuilder()
						.add("column", 2)
						.add("value", "JAVA")
						.build())
				.build();
		
		JsonObject queryDefinition = Json.createObjectBuilder()
				.add("relation", "book")
				.add("record", Json.createObjectBuilder().add("values", recordValues).build())
				.build();
		
		JsonObject query = Json.createObjectBuilder()
				.add("name", "INSERT")
				.add("definition", queryDefinition)
				.build();

		assertThatNoException().isThrownBy(()->{ QueryChecker.check(query); });
	}
	
	@Test
	public void testNull() {
		assertThatThrownBy(()->{ QueryChecker.check(null); }).hasMessage("No query to process");
	}
	
	@Test
	public void testEmptyQuery() {
		JsonObject query = Json.createObjectBuilder().build();
		
		assertThatThrownBy(()->{ QueryChecker.check(query); }).hasMessage("No query to process");
	}
	
	@Test
	public void testUnrecognizedQuery() {
		JsonObject query = Json.createObjectBuilder()
				.add("name", "SELECT")
				.build();
		assertThatThrownBy(()->{ QueryChecker.check(query); }).hasMessage("Unrecognized query *SELECT*");
	}
	
	@Test
	public void testMissingQueryName() {
		JsonObject query = Json.createObjectBuilder()
				.add("relation", "")
				.build();
		
		assertThatThrownBy(()->{ QueryChecker.check(query); }).hasMessage("Missing query *name* key");
	}
	
	@Test
	public void testMissingQueryDefinition() {
		JsonObject queryCREATE = Json.createObjectBuilder()
				.add("name", "CREATE")
				.build();
		
		JsonObject queryINSERT = Json.createObjectBuilder()
				.add("name", "INSERT")
				.build();
		
		assertThatThrownBy(()->{ QueryChecker.check(queryCREATE); }).hasMessage("CREATE query, missing *definition* key");
		assertThatThrownBy(()->{ QueryChecker.check(queryINSERT); }).hasMessage("INSERT query, missing *definition* key");
	}
	
	@ParameterizedTest
	@MethodSource("provideCREATEqueryDefinitions")
	public void testCREATEqueryDefinition(JsonObject queryDefinition, String exceptionMessage) {
		JsonObject query = Json.createObjectBuilder()
				.add("name", "CREATE")
				.add("definition", queryDefinition)
				.build();
		
		assertThatThrownBy(()->{ QueryChecker.check(query); }).hasMessage(exceptionMessage);
	}
	
	@ParameterizedTest
	@MethodSource("provideINSERTqueryDefinitions")
	public void testINSERTqueryDefinition(JsonObject queryDefinition, String exceptionMessage) {
		JsonObject query = Json.createObjectBuilder()
				.add("name", "INSERT")
				.add("definition", queryDefinition)
				.build();
		
		assertThatThrownBy(()->{ QueryChecker.check(query); }).hasMessage(exceptionMessage);
	}
	
}
