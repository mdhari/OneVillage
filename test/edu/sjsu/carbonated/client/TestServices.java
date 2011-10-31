package edu.sjsu.carbonated.client;

import javax.ws.rs.core.MediaType;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

import edu.sjsu.carbonated.data.AlbumResource;

public class TestServices {


	 public TestServices(){
		 
	 }
	 
	 public void testAddAlbum(){
		 
//		 Form f = new Form();
//		   f.add("Name", "MyFirstPhotoAlbum");
//		   f.add("Description", "My Description");
//		   f.add("User", "michael");
//		   f.add("Created_Date", "10/11/12");
		   
		   AlbumResource newBean = new AlbumResource("MyFirstPhotoAlbum", "My Desc", "michael", "created");
		   
		   Client c = Client.create();
		   WebResource r = c.resource("http://localhost:8080/OneVillage/photo/album");
		   
		   String result = r.
		       type(MediaType.APPLICATION_JSON_TYPE)
		      .accept(MediaType.APPLICATION_JSON_TYPE)
		      .post(String.class, newBean);
		    
		   
		   System.out.println(result);
	 }
	 
	 public static void main(String[] args){
		
		 TestServices tSvcs = new TestServices();
		 tSvcs.testAddAlbum();
		 
	 }

}