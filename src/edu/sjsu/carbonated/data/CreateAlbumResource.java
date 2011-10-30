package edu.sjsu.carbonated.data;

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
public class CreateAlbumResource {

	public String name;
	public String description;
	public String user;
	public String createdDate;

	public CreateAlbumResource() {
	} // JAXB needs this

	public CreateAlbumResource(String name, String description, String user,
			String createdDate) {

		this.name = name;
		this.description = description;
		this.user = user;
		this.createdDate = createdDate;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MyJaxbBean [name=" + name + ", description=" + description
				+ ", user=" + user + ", createdDate=" + createdDate + "]";
	}
}