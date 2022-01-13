package dbms.api;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class GenericExceptionMapper implements ExceptionMapper<Throwable> {
	
	@Override
	public Response toResponse(Throwable exception) {
		exception.printStackTrace();
		JsonObject message = Json.createObjectBuilder()
				.add("message", "Error while processing query")
				.build();
		
		return Response
				.status(Status.INTERNAL_SERVER_ERROR)
				.entity(message)
				.build();
	}
}
