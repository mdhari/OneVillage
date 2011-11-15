package edu.sjsu.carbonated.server;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.sun.jersey.multipart.MultiPart;

import edu.sjsu.carbonated.data.AlbumResource;
import edu.sjsu.carbonated.data.PhotoResponse;
import edu.sjsu.carbonated.mongodbaccessors.MongoDBAlbum;
import edu.sjsu.carbonated.util.Utils;

/**
 * 
 * @author Michael Hari
 * @version 1.0 10/30/11
 * @desc Implementation of the photo endpoint
 * @see http://jersey.java.net/nonav/documentation/latest/user-guide.html#d4e828
 * 
 */
@Path("/photo")
public class Photo {

	public static final String ServiceTag = "PHOTO: ";
	public static final String uploadLocation = "/opt/tomcat/webapps/ROOT/albums/";

	MongoDBAlbum mDBAlbum = new MongoDBAlbum();

	/**
	 * @param request
	 *            - JAXB will take a JSON or XML request and convert it
	 *            correctly into the POJO class defined
	 * @return Currently echos the client request, JAXB will also convert to
	 *         send across the wire
	 *         <p>
	 *         Standing HTTP error codes get handled without us having to throw
	 *         them (415 Unsupported Media Type, 500 Server Error)
	 */
	// XXX: TO REMOVE
	@Path("/echo")
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public String echo(String str) {

		return str;
	}

	/**
	 * Create an album through here. Note that a user can make multiple
	 * albums of the same name. This is by design since album_ids are unique
	 * 
	 * @param request in JSON format
	 * @return JSON (HTTP 200) or HTTP 400, 500 status messages
	 */
	@Path("/album")
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public AlbumResource addAlbum(AlbumResource request) {

		System.out.println(ServiceTag + "[addAlbum] request: " + request);

		if (request.albumCreationHasNull()) {
			System.out.println(ServiceTag
					+ "[addAlbum] request had null, sending 400");
			// TODO: Add a more detailed error response to indicate what
			// parameter(s) were missing
			throw new WebApplicationException(Response.status(400)
					.entity("Error in your request").build());
		}

		System.out.println(ServiceTag + "[addAlbum] request validation: "
				+ request);
		// currently checks if the date is valid
		// throws an error otherwise
		request.validationForAlbumCreation();

		// Generate a unique id for this album creation
		request.setAlbum_id(UUID.randomUUID().toString());
		// Commit metadata to data store
		mDBAlbum.addAlbum(request);

		createDir(uploadLocation + request.getAlbum_id()); // create the dir on
															// the backend

		// Current specification (2.1) says only send the album_id as a response
		// Pretty bad from an allocation point of view
		AlbumResource response = new AlbumResource();
		response.setAlbum_id(request.getAlbum_id());

		return response;

	}

	/**
	 * http://aruld.info/handling-multiparts-in-restful-applications-using-jersey/
	 * 
	 * @param multiPart
	 * @return
	 */
	@POST
	@Path("/album/{album_id}")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public PhotoResponse addPhotoToAlbum(
			@PathParam("album_id") String album_id, MultiPart multiPart) {


		System.out.println(ServiceTag + "[addPhotoToAlbumReq] for Album: "
				+ album_id);

		System.out.println(multiPart.getBodyParts().get(0) // user_id
				.getEntityAs(String.class));
		System.out.println(multiPart.getBodyParts().get(1) // time_taken
				.getEntityAs(String.class));
		System.out.println(multiPart.getBodyParts().get(2) // lat
				.getEntityAs(String.class));
		System.out.println(multiPart.getBodyParts().get(3) // long
				.getEntityAs(String.class));
		System.out.println(multiPart.getBodyParts().get(4) // created_by
				.getEntityAs(String.class));
		System.out.println(multiPart.getBodyParts().get(5) // description
				.getEntityAs(String.class));
		
		// validate the time_taken field at least
		try { //http://exampledepot.com/egs/java.text/ParseDate.html
		    DateFormat formatter = new SimpleDateFormat("MM/dd/yy");
		    formatter.parse(multiPart.getBodyParts().get(1) // time_taken
					.getEntityAs(String.class));
		} catch (ParseException e) {
			e.printStackTrace();
			throw new WebApplicationException(Response.status(400)
					.entity("Date format must be in the form of: MM/dd/yy")
					.type(MediaType.TEXT_PLAIN_TYPE).build());
			
		}

		String photo_id = UUID.randomUUID().toString();
		writeToFile(
				multiPart.getBodyParts().get(6).getEntityAs(InputStream.class),
				uploadLocation + album_id + "/", photo_id + ".png");

		mDBAlbum.addPhotoToAlbum(new AlbumResource(photo_id, album_id,
				multiPart.getBodyParts().get(0).getEntityAs(String.class),
				multiPart.getBodyParts().get(1).getEntityAs(String.class),
				multiPart.getBodyParts().get(2).getEntityAs(String.class),
				multiPart.getBodyParts().get(3).getEntityAs(String.class),
				multiPart.getBodyParts().get(4).getEntityAs(String.class),
				multiPart.getBodyParts().get(5).getEntityAs(String.class)));

		return new PhotoResponse(photo_id + ".png");
	}

	/**
	 * This endpoint is very flexible for updating Albums or Photos
	 * Based on the JSON request body received, an update can occur
	 * to either an Album or Photo
	 * 
	 * @param album_id
	 * @param request
	 * @return
	 */
	@POST
	@Path("/album/{album_id}")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response updateAlbumOrPhoto(@PathParam("album_id") String album_id,
			AlbumResource request) {

		// its an update to an album if the json has no photo id in it
		if (Utils.isEmptyString(request.getPhoto_id())) {
			mDBAlbum.updateAlbum(album_id, request);
		} else {
			String photo_id = request.getPhoto_id();
			request.setPhoto_id(null);
			mDBAlbum.updatePhoto(album_id, photo_id, request);
		}

		System.out.println(ServiceTag + "[updateAlbumOrPhoto] for AlbumID: "
				+ album_id + " request: " + request);

		return Response.status(200).build();
	}

	/**
	 * Will delete an album or photo based on query parameters passed
	 * 
	 * @param album_id
	 * @param user_id
	 * @return
	 */
	@DELETE
	@Path("/album/{album_id}")
	public Response deleteAlbumOrPhoto(@PathParam("album_id") String album_id,
			@QueryParam("user_id") String user_id,
			@QueryParam("photo_id") String photo_id) {

		// if the request is do delete an album
		if (Utils.isEmptyString(photo_id)) {
			mDBAlbum.removeAlbum(album_id, user_id);
			new File(uploadLocation + album_id).delete(); // remove the dir from file system
		} else { // its a photo deletion request
			mDBAlbum.removePhoto(user_id, photo_id);
			new File(uploadLocation + album_id + "/" + photo_id + ".png").delete(); // remove the pic from file system
		}
		System.out.println(ServiceTag + "[deleteAlbumReq] User: " + user_id
				+ " for Album: " + album_id);

		return Response.status(200).build();
	}

	/**
	 * Handles both the get Albums and get Albums by user
	 * @param user_id
	 * @return
	 */
	@GET
	@Path("/album")
	@Produces({ MediaType.APPLICATION_JSON })
	public String getAlbum(@QueryParam("user_id") String user_id) {

		String toReturn;

		// is the request for albums
		if (Utils.isEmptyString(user_id)) {
			toReturn = mDBAlbum.getAllAlbums();
		} else { // else its for a user
			toReturn = mDBAlbum.getAlbumsByUser(user_id);
		}

		return toReturn;
	}

	/**
	 * Handles get all photos in an album and get a single photo
	 * @param album_id
	 * @param photo_id
	 * @return
	 */
	@GET
	@Path("/album/{album_id}")
	public String getAllPhotosFromAlbum(@PathParam("album_id") String album_id, @QueryParam("photo_id") String photo_id) {

		System.out.println(ServiceTag
				+ "[getAllPhotosFromAlbumReq] for Album: " + album_id);
		
		// is this a get single photo request?
		if(!Utils.isEmptyString(photo_id)){
			return mDBAlbum.getPhoto(album_id, photo_id);
		}else{ // its a get all photos from album request
			return mDBAlbum.getAllPhotoInAlbum(album_id);
		}

		//return Response.status(200).build();
	}

	// /**
	// * Original implementation:
	// * http://www.mkyong.com/tutorials/jax-rs-tutorials/
	// *
	// * @param uploadedInputStream
	// * @param fileDetail
	// * @return
	// */
	// @POST
	// @Path("/upload")
	// @Consumes(MediaType.MULTIPART_FORM_DATA)
	// public Response uploadFile(
	// @FormDataParam("file") InputStream uploadedInputStream,
	// @FormDataParam("file") FormDataContentDisposition fileDetail) {
	//
	// String uploadedFileLocation = "/Users/michael/uploaded/"
	// + fileDetail.getFileName();
	//
	// // save it
	// writeToFile(uploadedInputStream, uploadedFileLocation);
	//
	// String output = "File uploaded to : " + uploadedFileLocation;
	//
	// return Response.status(200).entity(output).build();
	//
	// }

	// /**
	// * http://aruld.info/handling-multiparts-in-restful-applications-using-
	// * jersey/
	// *
	// * @param multiPart
	// * @return
	// */
	// @POST
	// @Path("/uploadTest")
	// @Consumes(MediaType.MULTIPART_FORM_DATA)
	// public Response uploadTest(MultiPart multiPart) {
	//
	// // {"Time Taken":
	// //
	// “<time_taken>","Latitude": "<latitude>", "Longitude":"<longitude>",”Album”:”<album_id>”,
	// // “Photo”:”<actual_picture>”,“User” : “<created_by”,”Description”:
	// // “<description>”}
	//
	// System.out.println(multiPart.getBodyParts().get(0)
	// .getEntityAs(String.class));
	// System.out.println(multiPart.getBodyParts().get(1)
	// .getEntityAs(String.class));
	// System.out.println(multiPart.getBodyParts().get(2)
	// .getEntityAs(String.class));
	// System.out.println(multiPart.getBodyParts().get(3)
	// .getEntityAs(String.class));
	// System.out.println(multiPart.getBodyParts().get(4)
	// .getEntityAs(String.class));
	//
	// String id = UUID.randomUUID().toString();
	// writeToFile(
	// multiPart.getBodyParts().get(6).getEntityAs(InputStream.class),
	// "/Users/michael/uploaded/" + id + ".png");
	//
	// return Response.status(200).entity("received").build();
	// }

	// /**
	// * http://aruld.info/handling-multiparts-in-restful-applications-using-
	// * jersey/
	// *
	// * @param multiPart
	// * @return
	// */
	// @POST
	// @Path("/uploadjson")
	// @Consumes(MediaType.APPLICATION_JSON)
	// public Response uploadJson(MultiPart multiPart) {
	//
	// System.out.println(multiPart.getBodyParts().get(0)
	// .getEntityAs(String.class));
	// String id = UUID.randomUUID().toString();
	// writeToFile(
	// multiPart.getBodyParts().get(1).getEntityAs(InputStream.class),
	// "/Users/michael/uploaded/" + id + ".png");
	//
	// return Response.status(200).entity("received").build();
	// }

	private void createDir(String uploadedFileLocation) {
		new File(uploadedFileLocation).mkdirs(); // since its not saved, no
													// worries about mem
	}

	private void writeToFile(InputStream uploadedInputStream,
			String uploadedFileLocation, String filename) {
		System.out.println(uploadedFileLocation);
		try {

			FileOutputStream fout = new FileOutputStream(new File(
					uploadedFileLocation + filename));

			int read = 0;
			byte[] bytes = new byte[1024];

			while ((read = uploadedInputStream.read(bytes)) != -1) {
				fout.write(bytes, 0, read);
			}

			fout.flush();
			fout.close();

		} catch (FileNotFoundException e) {
			throw new WebApplicationException(Response.status(404)
					.entity("Album not found")
					.type(MediaType.APPLICATION_JSON_TYPE).build());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}