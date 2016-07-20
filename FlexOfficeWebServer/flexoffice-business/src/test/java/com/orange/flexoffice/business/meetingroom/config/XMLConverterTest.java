package com.orange.flexoffice.business.meetingroom.config;

import static org.junit.Assert.assertEquals;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;

import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.util.Log4jConfigurer;
import org.xml.sax.SAXException;

import com.orange.flexoffice.business.common.exception.DataAlreadyExistsException;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@PropertySource("classpath:spring/flexoffice.properties.test.xml")
public class XMLConverterTest {

	static {
	    try {
	      Log4jConfigurer.initLogging( "classpath:spring/log4j-flexoffice-business-test.xml" );
	    }
	    catch( FileNotFoundException ex ) {
	      System.err.println( "Cannot Initialize log4j" );
	    }
	  }
	
	private static ClassPathXmlApplicationContext context;

	private static FileManager fileManager;
	
	private static XMLConverter xmlConverter;
	
	
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void initSpringContextAndDatabase() throws Exception {
		context = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-flexoffice-business-test.xml");
		fileManager = (FileManager)context.getBean("fileManager");
		xmlConverter = (XMLConverter)context.getBean("xmlConverter");
	}
	
	
	@Test
	public void TestA_createFile() {
		boolean ok = false;
		
		try {
			fileManager.createFile("test");
			ok = true;
		} catch (IOException e) {
			ok = false;
			e.printStackTrace();
		} catch (JAXBException e) {
			ok = false;
			e.printStackTrace();
		} finally{
			// Assert
			assertEquals(ok, true);
		}
	}

	@Test
	public void TestB_convertFromObjectToXML() throws IOException, JAXBException, DataAlreadyExistsException, ParserConfigurationException, SAXException {
		Array array = new Array();
		array.setDescription("TestA");
		
		Rooms rooms = new Rooms();
		ArrayList<Numeric> nums = new ArrayList<Numeric>();
		
		Numeric num = new Numeric();
		num.setName("numeric0");
		num.setRoomId("id@a");
		
		nums.add(num);
		
		rooms.setNumeric(nums);
		array.setRooms(rooms);
				
		xmlConverter.convertFromObjectToXML(array, ".\\test.xml");
		
		// Asserts
		assertEquals(1, xmlConverter.convertFromXMLToObject(".\\test.xml").getRooms().getNumeric().size());
	}
	
	@Test
	public void TestC_convertFromXMLToObject() throws IOException, JAXBException, DataAlreadyExistsException, ParserConfigurationException, SAXException {
		Array array = xmlConverter.convertFromXMLToObject(".\\test.xml");
		
		// Asserts
		assertEquals("TestA", array.getDescription());
		assertEquals("id@a", array.getRooms().getNumeric().get(0).getValue().getRoomId());
	}
	
	@Test
	public void TestD_modifyObject() throws IOException, JAXBException, DataAlreadyExistsException, ParserConfigurationException, SAXException {
		Array array = xmlConverter.convertFromXMLToObject(".\\test.xml");
		array.setDescription("Description modified");
		
		xmlConverter.convertFromObjectToXML(array, ".\\test.xml");
		
		// Asserts
		assertEquals("Description modified", xmlConverter.convertFromXMLToObject(".\\test.xml").getDescription());
	}
	
	@Test
	public void TestE_deleteFile() {
		boolean ok = false;
		
		try {
			fileManager.deleteFile("test");
			ok = true;
		} finally{
			// Assert
			assertEquals(ok, true);
		}
	}
}
