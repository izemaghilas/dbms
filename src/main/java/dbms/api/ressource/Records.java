package dbms.api.ressource;

import dbms.api.utils.QueryChecker;
import dbms.api.utils.QueryCheckerException;
import dbms.core.DBMS;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

@Path("records")
public class Records {
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAll(@QueryParam("relation") String relation) {
		JsonArray records = DBMS.INSTANCE.getAllRecords(relation);
		
		return Response
				.status(Status.OK)
				.entity(records)
				.build();
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response insert(JsonObject query) throws QueryCheckerException {
		QueryChecker.check(query);
		DBMS.INSTANCE.insertRecord(query);
		
		JsonObject message = Json.createObjectBuilder()
				.add("message", "record inserted to relation *"+query.getJsonObject("definition").getString("relation")+"*")
				.build();
		
		return Response
				.status(Status.CREATED)
				.entity(message)
				.build();
	}
}
