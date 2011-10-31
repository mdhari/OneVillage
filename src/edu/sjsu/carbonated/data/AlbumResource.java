package edu.sjsu.carbonated.data;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlRootElement;
/**
 * 
 * @author Michael Hari
 * @version 1.0 10/30/11
 * @desc POJO that JAXB uses as a template for creating different MIME types
 * @see http://jersey.java.net/nonav/documentation/latest/user-guide.html#d4e828
 *
 */
@XmlRootElement
public class AlbumResource {

	/**
	 * MH: Apparently if you make these public, you don't need getters
	 * and setters because JAXB can set it correctly. But its usually 
	 * bad practice to expose all properties of a class publicly
	 */
	private String album_name;
	private String album_description;
	private String user;
	private String createdDate;

	public AlbumResource() {
	} // JAXB needs this
	
	/**
	 * Less maintenance for storing in the DB
	 * @return Map
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map getMap(){
		
		Map map = new HashMap();
		
		map.put("album_name", album_name);
		map.put("album_description", album_description);
		map.put("user", user);
		map.put("createdDate", createdDate);
		
		return map;
		
	}
	
	public AlbumResource(String album_name, String album_description,
			String user, String createdDate) {
		this.album_name = album_name;
		this.album_description = album_description;
		this.user = user;
		this.createdDate = createdDate;
	}

	/**
	 * @return the album_name
	 */
	public String getAlbum_name() {
		return album_name;
	}

	/**
	 * @param album_name the album_name to set
	 */
	public void setAlbum_name(String album_name) {
		this.album_name = album_name;
	}

	/**
	 * @return the album_description
	 */
	public String getAlbum_description() {
		return album_description;
	}

	/**
	 * @param album_description the album_description to set
	 */
	public void setAlbum_description(String album_description) {
		this.album_description = album_description;
	}

	/**
	 * @return the user
	 */
	public String getUser() {
		return user;
	}

	/**
	 * @param user the user to set
	 */
	public void setUser(String user) {
		this.user = user;
	}

	/**
	 * @return the createdDate
	 */
	public String getCreatedDate() {
		return createdDate;
	}

	/**
	 * @param createdDate the createdDate to set
	 */
	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "AlbumResource [album_name=" + album_name
				+ ", album_description=" + album_description + ", user=" + user
				+ ", createdDate=" + createdDate + "]";
	}

}