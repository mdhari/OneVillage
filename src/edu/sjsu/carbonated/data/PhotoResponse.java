package edu.sjsu.carbonated.data;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class PhotoResponse{
	
	private String photo_id;
	
	public PhotoResponse(){
		// JAXB needs this
	}
	
	public PhotoResponse(String photo_id){
		this.photo_id=photo_id;
	}

	/**
	 * @return the photo_id
	 */
	public String getPhoto_id() {
		return photo_id;
	}

	/**
	 * @param photo_id the photo_id to set
	 */
	public void setPhoto_id(String photo_id) {
		this.photo_id = photo_id;
	}
	
	
	
	
}