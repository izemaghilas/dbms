package dbms.api;

import java.io.IOException;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.ext.Provider;

@Provider
public class CORSFilter implements ContainerResponseFilter {

	@Override
	public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext)
			throws IOException {
		responseContext.getHeaders().add("Access-Control-Allow-Origin", "https://izemaghilas.github.io/dbms-ui");
		responseContext.getHeaders().add("Access-Control-Allow-Headers", "content-type");
		responseContext.getHeaders().add("Access-Control-Allow-Methods", "GET, POST");
	}

}
