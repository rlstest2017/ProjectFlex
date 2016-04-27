package com.orange.flexoffice.business.meetingroom.config;

import java.io.IOException;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.orange.flexoffice.dao.common.model.data.RoomDao;


public class App {
	private static final String XML_FILE_NAME = "customer.xml";
	private static ClassPathXmlApplicationContext context;
	
	public static void main(String[] args) throws IOException {
		context = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-flexoffice-business.xml");
		XMLConverter converter = (XMLConverter) context.getBean("XMLConverter");
		
		try {
			RoomDao room = new RoomDao();
			room.setName("mkyong");
			
			System.out.println("Convert Object to XML!");
			//from object to XML file
			converter.convertFromObjectToXML(room, XML_FILE_NAME);
			System.out.println("Done \n");
			
			System.out.println("Convert XML back to Object!");
			//from XML to object
			RoomDao room2 = (RoomDao)converter.convertFromXMLToObject(XML_FILE_NAME);
			System.out.println(room2);
			System.out.println("Done");
			
		} finally {
			if (context != null)
                	context.close();  
		}
		
	}
}