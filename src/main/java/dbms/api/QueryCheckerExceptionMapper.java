package dbms.api;

import dbms.api.utils.QueryCheckerException;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class QueryCheckerExceptionMapper implements ExceptionMapper<QueryCheckerException> {

	@Override
	public Response toResponse(QueryCheckerException exception) {
		exception.printStackTrace();
		JsonObject message = Json.createObjectBuilder()
				.add("message", exception.getMessage())
				.build();
		
		return Response
				.status(Status.BAD_REQUEST)
				.entity(message)
				.build();
	}

}
