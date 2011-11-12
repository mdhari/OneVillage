package edu.sjsu.carbonated.data;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.xml.bind.annotation.XmlRootElement;

import edu.sjsu.carbonated.util.Utils;

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
	 * MH: Apparently if you make these public, you don't need getters and
	 * setters because JAXB can set it correctly. But its usually bad practice
	 * to expose all properties of a class publicly
	 * <p>
	 * Make sure you DO make getters and setters, JAXB needs them
	 */
	
	private String photo_id;
	
	private String album_id;
	private String album_name;
	private String album_description;
	private String user_id;
	private String created_date;

	private String time_taken;
	private String latitude;
	private String longitude;

	private String created_by;
	private String description;

	public AlbumResource() {
	} // JAXB needs this

	/**
	 * Less maintenance for storing in the DB
	 * 
	 * @return Map
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map getMap() {

		Map map = new HashMap();

		if (!Utils.isEmptyString(album_id))
			map.put("album_id", album_id);

		if (!Utils.isEmptyString(album_name))
			map.put("album_name", album_name);

		if (!Utils.isEmptyString(album_description))
			map.put("album_description", album_description);

		if (!Utils.isEmptyString(user_id))
			map.put("user_id", user_id);

		if (!Utils.isEmptyString(created_date))
			map.put("createdDate", created_date);
		
		if (!Utils.isEmptyString(photo_id))
			map.put("photo_id", photo_id);		
		
		if (!Utils.isEmptyString(time_taken))
			map.put("time_taken", time_taken);
		
		if (!Utils.isEmptyString(latitude))
			map.put("latitude", latitude);
		
		if (!Utils.isEmptyString(longitude))
			map.put("longitude", longitude);
		
		if (!Utils.isEmptyString(created_by))
			map.put("created_by", created_by);
		
		if (!Utils.isEmptyString(description))
			map.put("description", description);
		

		return map;

	}

	public AlbumResource(String album_name, String album_description,
			String user_id, String created_date) {

		this.album_name = album_name;
		this.album_description = album_description;
		this.user_id = user_id;
		this.created_date = created_date;
	}
	
	/** 
	 * for photo addition to db
	 * @param photo_id
	 * @param album_id
	 * @param user_id
	 * @param time_taken
	 * @param latitude
	 * @param longitude
	 * @param created_by
	 * @param description
	 */
	public AlbumResource(String photo_id, String album_id, String user_id,
			String time_taken, String latitude, String longitude,
			String created_by, String description) {
		
		this.photo_id = photo_id;
		this.album_id = album_id;
		this.user_id = user_id;
		this.time_taken = time_taken;
		this.latitude = latitude;
		this.longitude = longitude;
		this.created_by = created_by;
		this.description = description;
	}

	public boolean hasNull() {
		if (Utils.isEmptyString(album_name)
				|| Utils.isEmptyString(album_description)
				|| Utils.isEmptyString(user_id)
				|| Utils.isEmptyString(created_date)) {
			return true;
		}
		return false;
	}

	/**
	 * @return the album_name
	 */
	public String getAlbum_name() {
		return album_name;
	}

	/**
	 * @param album_name
	 *            the album_name to set
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
	 * @param album_description
	 *            the album_description to set
	 */
	public void setAlbum_description(String album_description) {
		this.album_description = album_description;
	}

	/**
	 * @return the user_id
	 */
	public String getUser_id() {
		return user_id;
	}

	/**
	 * @param user_id
	 *            the user_id to set
	 */
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	/**
	 * @return the created_date
	 */
	public String getCreated_date() {
		return created_date;
	}

	/**
	 * @param created_date
	 *            the created_date to set
	 */
	public void setCreated_date(String created_date) {
		this.created_date = created_date;
	}


	/**
	 * @return the album_id
	 */
	public String getAlbum_id() {
		return album_id;
	}

	/**
	 * @param album_id
	 *            the album_id to set
	 */
	public void setAlbum_id(String album_id) {
		this.album_id = album_id;
	}

	public void setTime_taken(String time_taken) {
		this.time_taken = time_taken;
	}

	public String getTime_taken() {
		return time_taken;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setCreated_by(String created_by) {
		this.created_by = created_by;
	}

	public String getCreated_by() {
		return created_by;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public void setPhoto_id(String photo_id) {
		this.photo_id = photo_id;
	}

	public String getPhoto_id() {
		return photo_id;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "AlbumResource [photo_id=" + photo_id + ", album_id=" + album_id
				+ ", album_name=" + album_name + ", album_description="
				+ album_description + ", user_id=" + user_id
				+ ", created_date=" + created_date + ", time_taken="
				+ time_taken + ", latitude=" + latitude + ", longitude="
				+ longitude + ", created_by=" + created_by + ", description="
				+ description + "]";
	}

}