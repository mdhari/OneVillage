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
public class GroupResource {

	public String group_name;
	public String group_description;

	public GroupResource() {
	} // JAXB needs this

	public GroupResource(String group_name, String group_description) {
		this.group_name = group_name;
		this.group_description = group_description;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map getMap() {

		Map map = new HashMap();

		map.put("group_name", group_name);
		map.put("group_description", group_description);

		return map;

	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "GroupResource [group_name=" + group_name
				+ ", group_description=" + group_description + "]";
	}

}