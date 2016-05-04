package com.orange.flexoffice.business.meetingroom.config;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XMLConverter {

	private Marshaller marshaller;
	private Unmarshaller unmarshaller;

	public Marshaller getMarshaller() {
		return marshaller;
	}

	public void setMarshaller(Marshaller marshaller) {
		this.marshaller = marshaller;
	}

	public Unmarshaller getUnmarshaller() {
		return unmarshaller;
	}

	public void setUnmarshaller(Unmarshaller unmarshaller) {
		this.unmarshaller = unmarshaller;
	}

	public void convertFromObjectToXML(Object object, String filePath)
		throws IOException, JAXBException {
		
		FileOutputStream os = null;
		try {
			os = new FileOutputStream(filePath);
			JAXBContext jc = JAXBContext.newInstance(Array.class, Rooms.class, Numeric.class);
			setMarshaller(jc.createMarshaller());
			getMarshaller().setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			
			getMarshaller().marshal(object, new StreamResult(os));
		} finally {
			if (os != null) {
				os.close();
			}
		}
	}

	public Array convertFromXMLToObject(String filePath) throws IOException, JAXBException, ParserConfigurationException, SAXException {
		File inputFile = new File(filePath);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(inputFile);
        doc.getDocumentElement().normalize();
         
        Node rootNode = doc.getDocumentElement();
        NodeList nodes = rootNode.getChildNodes();
        Node nodeRooms = null;
         
        Array array = new Array();
        Rooms rooms = new Rooms();
        ArrayList<Numeric> numerics = new ArrayList<Numeric>();
        Numeric num = new Numeric();
         
        for (int temp = 0; temp < nodes.getLength(); temp++) {
        	Node nNode = nodes.item(temp);
	        if (nNode.getNodeType() == Node.ELEMENT_NODE) {
	        	if ("description".equals(nNode.getNodeName())){
	           		array.setDescription(nNode.getTextContent());
	           	}
	           	if("rooms".equals(nNode.getNodeName())){
	           		nodeRooms = nNode;
	           	}
	        }
	    }

        NodeList nList = nodeRooms.getChildNodes();

        for (int temp = 0; temp < nList.getLength(); temp++) {
        	Node nNode = nList.item(temp);
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
            	Element eElement = (Element) nNode;
            	num = new Numeric();
            	num.setName(nNode.getNodeName());
            	num.setRoomId(eElement.getElementsByTagName("RoomID").item(0).getTextContent());
            	numerics.add(num);
            }
        }
        rooms.setNumeric(numerics);
        array.setRooms(rooms);
         
        return array;
	}

}
