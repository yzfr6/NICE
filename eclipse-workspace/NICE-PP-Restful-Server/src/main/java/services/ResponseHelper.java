package services;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.json.simple.JSONObject;

public class ResponseHelper {

	public static Response get500(String message) {
		// TODO handle the logger message
		JSONObject error = new JSONObject();
		error.put("error", message);
		MediaType mediaType = MediaType.APPLICATION_JSON_TYPE;
		return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).type(mediaType).header("Access-Control-Allow-Origin", "*").build();
	}

	public static Response get404(String message) {
		// TODO handle the logger message
		return Response.status(Status.NOT_FOUND).header("Access-Control-Allow-Origin", "*").build();
	}

	public static Response get200(Object o, MediaType mediaType) {

		return Response.status(Status.OK).entity(o).type(mediaType).header("Access-Control-Allow-Origin", "*").build();
	}

}