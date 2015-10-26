package com.orange.flexoffice.business.gatewayapi.service.support;

import java.util.List;

public class Filter {
	
	private List<String> excludedList;
	private List<String> requiredList;
	
	public void addToExcludedList(String descId) {
		excludedList.add(descId);
	}
	
	public void removeFromExcludedList(String descId) {
		excludedList.remove(descId);
	}
	
	public void addToRequiredList(String descId) {
		requiredList.add(descId);
	}
	
	public void removeFromRequiredList(String descId) {
		requiredList.remove(descId);
	}

	public List<String> getExcludedList() {
		return excludedList;
	}

	public List<String> getRequiredList() {
		return requiredList;
	}
	
	
	

}
