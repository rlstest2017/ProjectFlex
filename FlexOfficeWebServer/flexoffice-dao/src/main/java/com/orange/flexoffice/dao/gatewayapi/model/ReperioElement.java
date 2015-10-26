package com.orange.flexoffice.dao.gatewayapi.model;

public class ReperioElement implements Comparable<ReperioElement>{
	
	private String id;
	private float value;
	
	public ReperioElement(String id,float value){
		this.id=id;
		this.value=value;
	}
	
	public void setId(String id){
		this.id=id;
	}
	
	public void setValue(float value){
		this.value=value;
	}
	
	public String getId(){
		return this.id;
	}
	
	public float getValue(){
		return this.value;
	}

	public int compareTo(ReperioElement o) {
		float otherValue = ((ReperioElement)o).value;
		return (this.value==otherValue?0:(this.value>otherValue?-1:1));
	}
}
