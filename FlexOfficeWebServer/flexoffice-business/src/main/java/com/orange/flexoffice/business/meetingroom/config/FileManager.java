package com.orange.flexoffice.business.meetingroom.config;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

import com.orange.flexoffice.business.common.exception.DataAlreadyExistsException;


@Service("FileManager")
public class FileManager {
	public String EXTENSION = ".xml";
	
	public String SEPARATOR = "\\";
	
	private Properties properties;
	
	private String NUMERIC = "numeric";
	
	@Autowired
    @Qualifier("appProperties")
    public void setProperties(Properties properties) {
        this.properties = properties;
    }
	
	XMLConverter converter = new XMLConverter();

	public void createFile(String fileName) throws IOException, JAXBException {
		String filePath = properties.getProperty("meetingroom.path");
		Array room = new Array();
		room.setDescription("Liste des meeting rooms pour le bâtiment " + fileName);
		Rooms rooms = new Rooms();
		ArrayList<Numeric> nums = new ArrayList<Numeric>();
		rooms.setNumeric(nums);
		room.setRooms(rooms);
	
		converter.convertFromObjectToXML(room, filePath + SEPARATOR + fileName + EXTENSION);
	}
	
	public void updateFile(String oldName, String newName) throws IOException, JAXBException, ParserConfigurationException, SAXException{		
		String filePath = properties.getProperty("meetingroom.path");
		Array array = converter.convertFromXMLToObject(filePath + SEPARATOR + oldName + EXTENSION);
		array.setDescription("Liste des meeting rooms pour le bâtiment et l'étage suivant: " + newName);
		
		converter.convertFromObjectToXML(array, filePath + SEPARATOR + newName + EXTENSION);
		deleteFile(oldName);
	}
	
	public void deleteFile(String fileName){
		String filePath = properties.getProperty("meetingroom.path");
		File file = new File(filePath + SEPARATOR + fileName + EXTENSION);
		file.delete();
	}
	
	public void addObjectToFile(String fileName, String roomId) throws DataAlreadyExistsException, IOException, JAXBException, ParserConfigurationException, SAXException{
		String filePath = properties.getProperty("meetingroom.path");
		Array array = converter.convertFromXMLToObject(filePath + SEPARATOR + fileName + EXTENSION);
		List<JAXBElement<Numeric>> JAXBnums = array.getRooms().getNumeric();
		ArrayList<Numeric> nums = new ArrayList<Numeric>();
		Rooms rooms = array.getRooms();
		Numeric numeric;
		
		for (JAXBElement<Numeric> num: JAXBnums){
			if(num.getValue().getRoomId().equals(roomId)){
				throw new DataAlreadyExistsException("FileManager.addObjectToFile : RoomId already exists");
			}
			numeric = new Numeric();
			numeric.setName(num.getName().toString());
			numeric.setRoomId(num.getValue().getRoomId());
			nums.add(numeric);
		}
		
		Numeric numToAdd = new Numeric();
		numToAdd.setRoomId(roomId);
		numToAdd.setName(NUMERIC + String.valueOf(JAXBnums.size()));

		nums.add(numToAdd);

		rooms.setNumeric(nums);
		
		converter.convertFromObjectToXML(array, filePath + SEPARATOR + fileName + EXTENSION);
	}
	
	public void updateObjectFromFile(String fileName, String oldRoomId, String newRoomId) throws DataAlreadyExistsException, IOException, JAXBException, ParserConfigurationException, SAXException{
		String filePath = properties.getProperty("meetingroom.path");
		Array array = converter.convertFromXMLToObject(filePath + SEPARATOR + fileName + EXTENSION);
		List<JAXBElement<Numeric>> JAXBnums = array.getRooms().getNumeric();
		ArrayList<Numeric> nums = new ArrayList<Numeric>();
		Rooms rooms = array.getRooms();
		Numeric numeric;
		
		for (JAXBElement<Numeric> num: JAXBnums){
			if(num.getValue().getRoomId().equals(newRoomId)){
				throw new DataAlreadyExistsException("FileManager.addObjectToFile : RoomId already exists");
			}
			numeric = new Numeric();
			numeric.setName(num.getName().toString());
			numeric.setRoomId(num.getValue().getRoomId());
			if(!numeric.getRoomId().equals(oldRoomId)){
				nums.add(numeric);
			} else {
				Numeric numToAdd = new Numeric();
				numToAdd.setRoomId(newRoomId);
				numToAdd.setName(num.getName().toString());

				nums.add(numToAdd);
			}
		}

		rooms.setNumeric(nums);
		
		converter.convertFromObjectToXML(array, filePath + SEPARATOR + fileName + EXTENSION);
	}
	
	public void removeObjectFromFile(String fileName, String roomId) throws IOException, JAXBException, ParserConfigurationException, SAXException{
		String filePath = properties.getProperty("meetingroom.path");
		Array array = converter.convertFromXMLToObject(filePath + SEPARATOR + fileName + EXTENSION);
		List<JAXBElement<Numeric>> JAXBnums = array.getRooms().getNumeric();
		ArrayList<Numeric> nums = new ArrayList<Numeric>();
		Rooms rooms = array.getRooms();
		Numeric numeric;
		int i = 0;
		
		for (JAXBElement<Numeric> num: JAXBnums){
			numeric = new Numeric();
			numeric.setRoomId(num.getValue().getRoomId());
			if(!numeric.getRoomId().equals(roomId)){
				numeric.setName(NUMERIC + i);
				nums.add(numeric);
				i++;
			}
		}

		rooms.setNumeric(nums);
		
		converter.convertFromObjectToXML(array, filePath + SEPARATOR + fileName + EXTENSION);
	}
	
}
