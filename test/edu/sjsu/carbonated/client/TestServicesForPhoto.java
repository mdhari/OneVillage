package edu.sjsu.carbonated.client;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.ws.rs.core.MediaType;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.representation.Form;
import com.sun.jersey.multipart.BodyPart;
import com.sun.jersey.multipart.MultiPart;

import edu.sjsu.carbonated.data.AlbumResource;

public class TestServicesForPhoto {

	public TestServicesForPhoto() {

	}

	public void testAddAlbum() {

		AlbumResource newBean = new AlbumResource("MyFirstPhotoAlbum",
				"My Desc", "renis", "01/23/19");

		Client c = Client.create();
		WebResource r = c
				.resource("http://localhost:8080/OneVillage/photo/album");

		String result = r.type(MediaType.APPLICATION_JSON_TYPE)
				.accept(MediaType.APPLICATION_JSON_TYPE)
				.post(String.class, newBean);

		System.out.println(result);
	}

	public void testUpdateAlbum() {
		AlbumResource newBean = new AlbumResource(null, "my hope 2", "michael",
				null);

		Client c = Client.create();
		WebResource r = c
				.resource("http://localhost:8080/OneVillage/photo/album/b5fbac9e-82f6-4bb0-a435-2d7fe2f11373");

		String result = r.type(MediaType.APPLICATION_JSON_TYPE)
				.accept(MediaType.APPLICATION_JSON_TYPE)
				.post(String.class, newBean);

		System.out.println(result);

	}

	public void testDeleteAlbum() {

		Client c = Client.create();
		WebResource r = c
				.resource("http://localhost:8080/OneVillage/photo/album/f07c625f-27a6-4c22-9e6e-5ae15f7e5a61?user_id=michael");

		String result = r.type(MediaType.APPLICATION_JSON_TYPE)
				.accept(MediaType.APPLICATION_JSON_TYPE).delete(String.class);

		System.out.println(result);

	}

	public void getAllPhotosInAlbum() {

		Client c = Client.create();
		WebResource r = c
				.resource("http://localhost:8080/OneVillage/photo/album/f07c625f-27a6-4c22-9e6e-5ae15f7e5a61");

		String result = r.get(String.class);

		System.out.println(result);

	}

	public void testGetAllAlbum() {

		Client c = Client.create();
		WebResource r = c
				.resource("http://localhost:8080/OneVillage/photo/album");

		String result = r.get(String.class);

		System.out.println(result);

	}

	public void testGetAllAlbumByUser() {

		Client c = Client.create();
		WebResource r = c
				.resource("http://localhost:8080/OneVillage/photo/album?user_id=michael");

		String result = r.get(String.class);

		System.out.println(result);

	}

	public void addPhotoToAlbum() {

		Client c = Client.create();
		WebResource r = c
				.resource("http://localhost:8080/OneVillage/photo/album/f07c625f-27a6-4c22-9e6e-5ae15f7e5a61");

		ByteArrayOutputStream bas = new ByteArrayOutputStream();
		File file = new File(
				"/Users/michael/Desktop/905474329885272_1290460728000_comedycentralonedemand_108x156.jpg");
		BufferedImage bi;
		try {
			bi = ImageIO.read(file);
			ImageIO.write(bi, "png", bas);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		byte[] logo = bas.toByteArray();

		// Construct a MultiPart with two body parts
		MultiPart multiPart = new MultiPart()
				.bodyPart(new BodyPart("michael", MediaType.TEXT_PLAIN_TYPE)) // user_id
				.bodyPart(new BodyPart("12/12/12", MediaType.TEXT_PLAIN_TYPE)) // time_taken
				.bodyPart(new BodyPart("30.2", MediaType.TEXT_PLAIN_TYPE)) // lat
				.bodyPart(new BodyPart("20.1", MediaType.TEXT_PLAIN_TYPE)) // long
				.bodyPart(new BodyPart("michael", MediaType.TEXT_PLAIN_TYPE))
				// created_by
				.bodyPart(
						new BodyPart("this is the description",
								MediaType.TEXT_PLAIN_TYPE)) // description
				.bodyPart(
						new BodyPart(logo,
								MediaType.APPLICATION_OCTET_STREAM_TYPE));

		String response = r.type(MediaType.MULTIPART_FORM_DATA_TYPE)
				.accept(MediaType.APPLICATION_JSON_TYPE)
				.post(String.class, multiPart);

		System.out.println(response);

	}

	public void updatePhotoInAlbum() {
		AlbumResource newBean = new AlbumResource(
				"0efca100-3149-4880-954e-0d7a3bd356ff", null, "michael", null,
				null, null, null, "this is hopefully a one time thing");

		Client c = Client.create();
		WebResource r = c
				.resource("http://localhost:8080/OneVillage/photo/album/f07c625f-27a6-4c22-9e6e-5ae15f7e5a61");

		String result = r.type(MediaType.APPLICATION_JSON_TYPE).post(
				String.class, newBean);

		System.out.println(result);
	}

	public void testDeletePhoto() {

		Client c = Client.create();
		WebResource r = c
				.resource("http://localhost:8080/OneVillage/photo/album/b21975eb-291e-4b22-8546-7b5f592fc427?user_id=michael&photo_id=0efca100-3149-4880-954e-0d7a3bd356ff");

		String result = r.type(MediaType.APPLICATION_JSON_TYPE)
				.accept(MediaType.APPLICATION_JSON_TYPE).delete(String.class);

		System.out.println(result);

	}

	public void getPhoto() {

		Client c = Client.create();
		WebResource r = c
				.resource("http://localhost:8080/OneVillage/photo/album/f07c625f-27a6-4c22-9e6e-5ae15f7e5a61?photo_id=ec2c382e-fdcb-436d-99ed-b17ff659c94a");

		String result = r.get(String.class);

		System.out.println(result);

	}

	public static void main(String[] args) {

		TestServicesForPhoto tSvcs = new TestServicesForPhoto();
		// tSvcs.testAddAlbum();
		// tSvcs.testUpdateAlbum();
		// tSvcs.testDeleteAlbum();
		// tSvcs.testGetAllAlbum();
		// tSvcs.testGetAllAlbumByUser();
		// tSvcs.addPhotoToAlbum();
		// tSvcs.updatePhotoInAlbum();
		// tSvcs.testDeletePhoto();
		// tSvcs.getPhoto();
		tSvcs.getAllPhotosInAlbum();
	}

}