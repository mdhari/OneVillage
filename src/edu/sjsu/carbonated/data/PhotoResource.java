package edu.sjsu.carbonated.data;



import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class PhotoResource {
	/**
	 * MH: Apparently if you make these public, you don't need getters
	 * and setters because JAXB can set it correctly. But its usually 
	 * bad practice to expose all properties of a class publicly
	 */
	
	private String user_id;
	private String time_taken;
	private String latitude;
	private String longitude;
	private String album_id;
	private String created_by;
	private String description;
	
	public PhotoResource(){
		// JAXB needs this
	}
	
	public PhotoResource(String user_id,String time_taken, String latitude, String longitude,
			String album_id, String created_by, String description) {
		this.user_id = user_id;
		this.time_taken = time_taken;
		this.latitude = latitude;
		this.longitude = longitude;
		this.album_id = album_id;
		this.created_by = created_by;
		this.description = description;
	}
	
	/**
	 * @return the time_taken
	 */
	public String getTime_taken() {
		return time_taken;
	}
	/**
	 * @param time_taken the time_taken to set
	 */
	public void setTime_taken(String time_taken) {
		this.time_taken = time_taken;
	}
	/**
	 * @return the latitude
	 */
	public String getLatitude() {
		return latitude;
	}
	/**
	 * @param latitude the latitude to set
	 */
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	/**
	 * @return the longitude
	 */
	public String getLongitude() {
		return longitude;
	}
	/**
	 * @param longitude the longitude to set
	 */
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	/**
	 * @return the album_id
	 */
	public String getAlbum_id() {
		return album_id;
	}
	/**
	 * @param album_id the album_id to set
	 */
	public void setAlbum_id(String album_id) {
		this.album_id = album_id;
	}
	/**
	 * @return the created_by
	 */
	public String getCreated_by() {
		return created_by;
	}
	/**
	 * @param created_by the created_by to set
	 */
	public void setCreated_by(String created_by) {
		this.created_by = created_by;
	}
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getUser_id() {
		return user_id;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "PhotoResource [user_id=" + user_id + ", time_taken="
				+ time_taken + ", latitude=" + latitude + ", longitude="
				+ longitude + ", album_id=" + album_id + ", created_by="
				+ created_by + ", description=" + description + "]";
	}





	
	
	
}