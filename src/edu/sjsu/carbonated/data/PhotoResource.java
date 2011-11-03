package edu.sjsu.carbonated.data;

import java.io.InputStream;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class PhotoResource {

	/**
	 * MH: Apparently if you make these public, you don't need getters
	 * and setters because JAXB can set it correctly. But its usually 
	 * bad practice to expose all properties of a class publicly
	 */
	private String time_taken;
	private InputStream file;
	
	public PhotoResource(String time_taken, InputStream file) {
		this.time_taken = time_taken;
		this.file = file;
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
	 * @return the file
	 */
	public InputStream getFile() {
		return file;
	}
	/**
	 * @param file the file to set
	 */
	public void setFile(InputStream file) {
		this.file = file;
	}


	
	
	
}