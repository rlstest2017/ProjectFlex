package com.orange.flexoffice.gatewayapi.ws;

public final class PathConst {
	
	// Resources 1st level
	public static final String SENSORS_PATH = "/sensor";    // 2 requests
	public static final String GATEWAYS_PATH = "/gateways";    // 3 requests
		
	// Resources ID
	public static final String SENSOR_ID_PATH = "/{" + ParamsConst.SENSOR_ID_PARAM + "}";
	public static final String GATEWAY_ID_PATH = "/{" + ParamsConst.GATEWAY_ID_PARAM + "}";
	
	// Resources 2nd level
	public static final String INFO_PATH = "/info";  
	
	private PathConst() {}
}
