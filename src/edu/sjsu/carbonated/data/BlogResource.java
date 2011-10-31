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
public class BlogResource {

	private String blog_name;
	private String blog_description;
	private String userid;
	private String timestamp;

	public BlogResource() {
	} // JAXB needs this
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map getMap(){
		
		Map map = new HashMap();
		
		map.put("blog_name", blog_name);
		map.put("blog_description", blog_description);
		map.put("userid", userid);
		map.put("timestamp", timestamp);
		
		return map;
		
	}
	
	public BlogResource(String blog_name, String blog_description,
			String userid, String timestamp) {
		this.blog_name = blog_name;
		this.blog_description = blog_description;
		this.userid = userid;
		this.timestamp = timestamp;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "BlogResource [blog_name=" + blog_name + ", blog_description="
				+ blog_description + ", userid=" + userid + ", timestamp="
				+ timestamp + "]";
	}
	
	/**
	 * @return the blog_name
	 */
	public String getBlog_name() {
		return blog_name;
	}

	/**
	 * @param blog_name the blog_name to set
	 */
	public void setBlog_name(String blog_name) {
		this.blog_name = blog_name;
	}

	/**
	 * @return the blog_description
	 */
	public String getBlog_description() {
		return blog_description;
	}

	/**
	 * @param blog_description the blog_description to set
	 */
	public void setBlog_description(String blog_description) {
		this.blog_description = blog_description;
	}

	/**
	 * @return the userid
	 */
	public String getUserid() {
		return userid;
	}

	/**
	 * @param userid the userid to set
	 */
	public void setUserid(String userid) {
		this.userid = userid;
	}

	/**
	 * @return the timestamp
	 */
	public String getTimestamp() {
		return timestamp;
	}

	/**
	 * @param timestamp the timestamp to set
	 */
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	
}