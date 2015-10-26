package com.orange.flexoffice.business.gatewayapi.service.support;

public interface Constraints {
	
	Constraints setMaxSeeds(int max);
	
	Constraints setDiversity(boolean diversity);
	
	Constraints setFilter(Filter filter);

}
