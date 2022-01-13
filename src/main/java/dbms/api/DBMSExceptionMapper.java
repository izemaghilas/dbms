package dbms.api;

import dbms.core.DBMSException;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class DBMSExceptionMapper implements ExceptionMapper<DBMSException> {

	@Override
	public Response toResponse(DBMSException exception) {
		exception.printStackTrace();
		JsonObject message = Json.createObjectBuilder()
				.add("message", exception.getMessage())
				.build();
		
		return Response
				.status(Status.INTERNAL_SERVER_ERROR)
				.entity(message)
				.build();
	}

}
