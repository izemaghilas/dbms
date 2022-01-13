package dbms.api.ressource;

import dbms.api.utils.QueryChecker;
import dbms.api.utils.QueryCheckerException;
import dbms.core.DBMS;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

@Path("relations")
public class Relations {
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response create(JsonObject query) throws QueryCheckerException {
		
		QueryChecker.check(query);
		DBMS.INSTANCE.createRelation(query);
		
		JsonObject message = Json.createObjectBuilder()
				.add("message", "created relation *"+query.getJsonObject("definition").getString("relation")+"*")
				.build();
		
		return Response
				.status(Status.CREATED)
				.entity(message)
				.build();
	}
}
