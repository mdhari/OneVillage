package edu.sjsu.carbonated.server;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.BodyPartEntity;
import com.sun.jersey.multipart.FormDataParam;
import com.sun.jersey.multipart.MultiPart;

import edu.sjsu.carbonated.data.AlbumResource;
import edu.sjsu.carbonated.data.PhotoResource;
import edu.sjsu.carbonated.mongodbaccessors.MongoDBAlbum;


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

	MongoDBAlbum mDBAlbum = new MongoDBAlbum();
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
				+ "<body><h1>" + "I'm a Rest Server, won't you please add methods to me?" + "</body></h1>" + "</html> ";
	}
	
	/**
	 * @param request - JAXB will take a JSON or XML request and convert it 
	 * correctly into the POJO class defined
	 * @return Currently echos the client request, JAXB will also convert
	 * to send across the wire
	 * <p>
	 * Standing HTTP error codes get handled without us having
	 * to throw them (415 Unsupported Media Type, 500 Server Error)
	 */
	@Path("/echo")
	@POST
	@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public String echo(String str){

		return str;
	}
	

	/**
	 * @param request - JAXB will take a JSON or XML request and convert it 
	 * correctly into the POJO class defined
	 * @return Currently echos the client request, JAXB will also convert
	 * to send across the wire
	 * <p>
	 * Standing HTTP error codes get handled without us having
	 * to throw them (415 Unsupported Media Type, 500 Server Error)
	 */
	@Path("/album")
	@POST
	@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public AlbumResource addAlbum(AlbumResource request){
		//return "test";

		mDBAlbum.addEntry(request);

		System.out.println(request.toString());
		return request;
	}
	
	/**
	 * Original implementation:
	 * http://www.mkyong.com/tutorials/jax-rs-tutorials/
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
 
		String uploadedFileLocation = "/Users/michael/uploaded/" + fileDetail.getFileName();
 
		// save it
		writeToFile(uploadedInputStream, uploadedFileLocation);
 
		String output = "File uploaded to : " + uploadedFileLocation;
 
		return Response.status(200).entity(output).build();
 
	}
	
	/**
	 * http://aruld.info/handling-multiparts-in-restful-applications-using-jersey/
	 * @param multiPart
	 * @return
	 */
	@POST
	@Path("/uploadTest")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response uploadTest(MultiPart multiPart){
	
		//{"Time Taken": “<time_taken>","Latitude": "<latitude>", "Longitude":"<longitude>",”Album”:”<album_i d>”, “Photo”:”<actual_picture>”,“User” : “<created_by”,”Description”: “<description>”}
		
		System.out.println(multiPart.getBodyParts().get(0).getEntityAs(String.class));
		String id = UUID.randomUUID().toString();
		writeToFile(multiPart.getBodyParts().get(1).getEntityAs(InputStream.class), 
				"/Users/michael/uploaded/" + id + ".png");
		
		
		return Response.status(200).entity("received").build();
	}
	
	/**
	 * http://aruld.info/handling-multiparts-in-restful-applications-using-jersey/
	 * @param multiPart
	 * @return
	 */
	@POST
	@Path("/uploadjson")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response uploadJson(MultiPart multiPart){
	
		
		
		System.out.println(multiPart.getBodyParts().get(0).getEntityAs(String.class));
		String id = UUID.randomUUID().toString();
		writeToFile(multiPart.getBodyParts().get(1).getEntityAs(InputStream.class), 
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
	 

}