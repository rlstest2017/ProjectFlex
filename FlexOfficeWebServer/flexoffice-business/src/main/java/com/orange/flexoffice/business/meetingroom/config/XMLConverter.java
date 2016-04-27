package com.orange.flexoffice.business.meetingroom.config;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

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

	public void convertFromObjectToXML(Object object, String filepath)
		throws IOException {

		FileOutputStream os = null;
		try {
			os = new FileOutputStream(filepath);
			try {
				getMarshaller().marshal(object, new StreamResult(os));
			} catch (JAXBException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} finally {
			if (os != null) {
				os.close();
			}
		}
	}

	public Object convertFromXMLToObject(String xmlfile) throws IOException {

		FileInputStream is = null;
		try {
			is = new FileInputStream(xmlfile);
			try {
				return getUnmarshaller().unmarshal(new StreamSource(is));
			} catch (JAXBException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} finally {
			if (is != null) {
				is.close();
			}
		}
		return is;
	}

}
