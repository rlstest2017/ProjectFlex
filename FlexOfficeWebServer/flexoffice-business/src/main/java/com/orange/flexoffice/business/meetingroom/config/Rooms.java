package com.orange.flexoffice.business.meetingroom.config;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.namespace.QName;

/**
 * Rooms
 * @author oab
 *
 */ 
@XmlRootElement(name = "array")
@XmlSeeAlso(Numeric.class)
public class Rooms {

	private static int counter = 0;
	private ArrayList<Numeric> numeric = new ArrayList<Numeric>();
	
	@XmlAnyElement
	public List<JAXBElement<Numeric>> getNumeric() {
	      final  List<JAXBElement<Numeric>> output = new ArrayList<>();
	      for (final Numeric num : numeric){
	    	  if(!num.getName().isEmpty()){
	    		  output.add(new JAXBElement<Numeric>(new QName(num.getName()), Numeric.class, num));
	    	  } else {
	    		  output.add(new JAXBElement<Numeric>(new QName("numeric" + counter++), Numeric.class, num));
	    	  }
	      }
	      return output;
	}
	  
	 public void setNumeric(ArrayList<Numeric> numeric) {
			this.numeric = numeric;
	}
}
