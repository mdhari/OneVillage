package edu.sjsu.carbonated.data;

import javax.xml.bind.annotation.XmlRootElement;

 @XmlRootElement
   public class MyJaxbBean {



	public String name;
     public String description;
     public String user;
     public String createdDate;
         
     public MyJaxbBean() {} // JAXB needs this
   
 	public MyJaxbBean(String name, String description, String user,
			String createdDate) {
		
		this.name = name;
		this.description = description;
		this.user = user;
		this.createdDate = createdDate;
	}
 	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MyJaxbBean [name=" + name + ", description=" + description
				+ ", user=" + user + ", createdDate=" + createdDate + "]";
	}
  }