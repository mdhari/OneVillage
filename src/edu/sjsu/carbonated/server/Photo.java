package edu.sjsu.carbonated.server;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.BodyPartEntity;
import com.sun.jersey.multipart.FormDataParam;
import com.sun.jersey.multipart.MultiPart;

import edu.sjsu.carbonated.data.AlbumResource;
import edu.sjsu.carbonated.data.PhotoResource;
import edu.sjsu.carbonated.data.PhotoResponse;
import edu.sjsu.carbonated.mongodbaccessors.MongoDBAlbum;
import edu.sjsu.carbonated.mongodbaccessors.MongoDBPhoto;
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
	MongoDBPhoto mDBPhoto = new MongoDBPhoto();

	// XXX: TO REMOVE
	// This method is called if TEXT_PLAIN is request
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String sayPlainTextHello() {
		return "Hello, I'm a Rest Server, won't you please add methods to me?";
	}

	// XXX: TO REMOVE
	// This method is called if XML is request
	@GET
	@Produces(MediaType.TEXT_XML)
	public String sayXMLHello() {
		return "<?xml version=\"1.0\"?>" + "<hello> Hello Jersey" + "</hello>";
	}

	// XXX: TO REMOVE
	// This method is called if HTML is request
	@GET
	@Produces(MediaType.TEXT_HTML)
	public String sayHtmlHello() {
		return "<html> " + "<title>" + "Hello Jersey" + "</title>"
				+ "<body><h1>"
				+ "I'm a Rest Server, won't you please add methods to me?"
				+ "</body></h1>" + "</html> ";
	}

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
	@Path("/echo")
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public String echo(String str) {

		return str;
	}

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
	@Path("/album")
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public AlbumResource addAlbum(AlbumResource request) {

		System.out.println(ServiceTag + "[addAlbum] request");

		if (request.hasNull()) {
			System.out.println(ServiceTag
					+ "[addAlbum] request had null, sending 400");
			// TODO: Add a more detailed error response to indicate what
			// parameter(s) were missing
			throw new WebApplicationException(Response.status(400)
					.entity("Error in your request")
					.type(MediaType.APPLICATION_JSON_TYPE).build());
		}

		// Generate a unique id for this album creation
		request.setAlbum_id(UUID.randomUUID().toString());
		// Commit it to data store
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
	 * http://aruld.info/handling-multiparts-in-restful-applications-using-
	 * jersey/
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

		// {"Time Taken":
		// “<time_taken>","Latitude": "<latitude>", "Longitude":"<longitude>",”Album”:”<album_id>”,
		// “Photo”:”<actual_picture>”,“User” : “<created_by”,”Description”:
		// “<description>”}

		System.out.println(ServiceTag + "[addPhotoToAlbumReq] for Album: "
				+ album_id);

		System.out.println(multiPart.getBodyParts().get(0)
				.getEntityAs(String.class));
		System.out.println(multiPart.getBodyParts().get(1)
				.getEntityAs(String.class));
		System.out.println(multiPart.getBodyParts().get(2)
				.getEntityAs(String.class));
		System.out.println(multiPart.getBodyParts().get(3)
				.getEntityAs(String.class));
		System.out.println(multiPart.getBodyParts().get(4)
				.getEntityAs(String.class));
		System.out.println(multiPart.getBodyParts().get(5)
				.getEntityAs(String.class));

		String photo_id = UUID.randomUUID().toString();
		writeToFile2(
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

	@POST
	@Path("/album/{album_id}")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response updateAlbumOrPhoto(@PathParam("album_id") String album_id,
			AlbumResource request) {

		// {"Time Taken":
		// “<time_taken>","Latitude": "<latitude>", "Longitude":"<longitude>",”Album”:”<album_id>”,
		// “Photo”:”<actual_picture>”,“User” : “<created_by”,”Description”:
		// “<description>”}

		// its an update to an album if the json has no photo id in it
		if (Utils.isEmptyString(request.getPhoto_id())) {
			mDBAlbum.updateAlbum(album_id, request);
		} else {
			String photo_id = request.getPhoto_id();
			request.setPhoto_id(null);
			mDBAlbum.updatePhoto(album_id, photo_id, request);
		}

		System.out.println(ServiceTag + "[updateAlbumReq] for AlbumID: "
				+ album_id + " request: " + request);
		System.out.println(ServiceTag + "asdf" + request);

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

		// {"Time Taken":
		// “<time_taken>","Latitude": "<latitude>", "Longitude":"<longitude>",”Album”:”<album_id>”,
		// “Photo”:”<actual_picture>”,“User” : “<created_by”,”Description”:
		// “<description>”}

		// if the request is do delete an album
		if (Utils.isEmptyString(photo_id)) {
			mDBAlbum.removeAlbum(album_id, user_id);
		} else { // its a photo deletion request

		}
		System.out.println(ServiceTag + "[deleteAlbumReq] User: " + user_id
				+ " for Album: " + album_id);

		return Response.status(200).build();
	}

	@GET
	@Path("/album")
	@Produces({ MediaType.APPLICATION_JSON })
	public String getAlbum(@QueryParam("user_id") String user_id) {

		String toReturn;

		if (Utils.isEmptyString(user_id)) {
			toReturn = mDBAlbum.getAllAlbums();
		} else {
			toReturn = mDBAlbum.getAlbumsByUser(user_id);
		}

		// List<AlbumResource> list = new ArrayList<AlbumResource>();
		//
		// list.add(new AlbumResource("asdf", "asdf", "asdf", "asdf"));
		// list.add(new AlbumResource("fasdf", "fdasdf", "fdasf", "fadsdf"));

		return toReturn;
	}

	@GET
	@Path("/album/{album_id}")
	public Response getAllPhotosFromAlbum(@PathParam("album_id") int album_id) {

		// {"Time Taken":
		// “<time_taken>","Latitude": "<latitude>", "Longitude":"<longitude>",”Album”:”<album_id>”,
		// “Photo”:”<actual_picture>”,“User” : “<created_by”,”Description”:
		// “<description>”}

		System.out.println(ServiceTag
				+ "[getAllPhotosFromAlbumReq] for Album: " + album_id);

		return Response.status(200).build();
	}

	/**
	 * Original implementation:
	 * http://www.mkyong.com/tutorials/jax-rs-tutorials/
	 * 
	 * @param uploadedInputStream
	 * @param fileDetail
	 * @return
	 */
	@POST
	@Path("/upload")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response uploadFile(
			@FormDataParam("file") InputStream uploadedInputStream,
			@FormDataParam("file") FormDataContentDisposition fileDetail) {

		String uploadedFileLocation = "/Users/michael/uploaded/"
				+ fileDetail.getFileName();

		// save it
		writeToFile(uploadedInputStream, uploadedFileLocation);

		String output = "File uploaded to : " + uploadedFileLocation;

		return Response.status(200).entity(output).build();

	}

	/**
	 * http://aruld.info/handling-multiparts-in-restful-applications-using-
	 * jersey/
	 * 
	 * @param multiPart
	 * @return
	 */
	@POST
	@Path("/uploadTest")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response uploadTest(MultiPart multiPart) {

		// {"Time Taken":
		// “<time_taken>","Latitude": "<latitude>", "Longitude":"<longitude>",”Album”:”<album_id>”,
		// “Photo”:”<actual_picture>”,“User” : “<created_by”,”Description”:
		// “<description>”}

		System.out.println(multiPart.getBodyParts().get(0)
				.getEntityAs(String.class));
		System.out.println(multiPart.getBodyParts().get(1)
				.getEntityAs(String.class));
		System.out.println(multiPart.getBodyParts().get(2)
				.getEntityAs(String.class));
		System.out.println(multiPart.getBodyParts().get(3)
				.getEntityAs(String.class));
		System.out.println(multiPart.getBodyParts().get(4)
				.getEntityAs(String.class));

		String id = UUID.randomUUID().toString();
		writeToFile(
				multiPart.getBodyParts().get(6).getEntityAs(InputStream.class),
				"/Users/michael/uploaded/" + id + ".png");

		return Response.status(200).entity("received").build();
	}

	/**
	 * http://aruld.info/handling-multiparts-in-restful-applications-using-
	 * jersey/
	 * 
	 * @param multiPart
	 * @return
	 */
	@POST
	@Path("/uploadjson")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response uploadJson(MultiPart multiPart) {

		System.out.println(multiPart.getBodyParts().get(0)
				.getEntityAs(String.class));
		String id = UUID.randomUUID().toString();
		writeToFile(
				multiPart.getBodyParts().get(1).getEntityAs(InputStream.class),
				"/Users/michael/uploaded/" + id + ".png");

		return Response.status(200).entity("received").build();
	}

	// save uploaded file to new location
	private void writeToFile(InputStream uploadedInputStream,
			String uploadedFileLocation) {

		try {
			OutputStream out = new FileOutputStream(new File(
					uploadedFileLocation));
			int read = 0;
			byte[] bytes = new byte[1024];

			out = new FileOutputStream(new File(uploadedFileLocation));
			while ((read = uploadedInputStream.read(bytes)) != -1) {
				out.write(bytes, 0, read);
			}
			out.flush();
			out.close();
		} catch (IOException e) {

			e.printStackTrace();
		}

	}

	private void createDir(String uploadedFileLocation) {
		new File(uploadedFileLocation).mkdirs(); // since its not saved, no
													// worries about mem
	}

	private void writeToFile2(InputStream uploadedInputStream,
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