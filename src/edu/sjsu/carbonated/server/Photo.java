package edu.sjsu.carbonated.server;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import edu.sjsu.carbonated.data.AlbumResource;
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
	 

}