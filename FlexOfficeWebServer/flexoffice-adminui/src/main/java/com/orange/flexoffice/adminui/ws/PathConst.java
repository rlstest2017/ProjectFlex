package com.orange.flexoffice.adminui.ws;

public final class PathConst {
	
	// Resources 1st level
	public static final String SYSTEM_PATH = "/system";  // 7 requests
	public static final String STATS_PATH = "/stats";    // 3 requests
	public static final String USERS_PATH = "/users";    // 5 requests
	public static final String SENSORS_PATH = "/sensors";    // 6 requests
	public static final String GATEWAYS_PATH = "/gateways";    // 5 requests
	public static final String ROOMS_PATH = "/rooms";    // 5 requests
	public static final String CONFIGURATION_PATH = "/configuration";    // 4 requests
	public static final String LOCATION_PATH = "/location";    // 4 requests
	
	// Resources ID
	public static final String USER_ID_PATH = "/{" + ParamsConst.USER_ID_PARAM + "}";
	public static final String SENSOR_IDENTIFIER_PATH = "/{" + ParamsConst.SENSOR_IDENTIFIER_PARAM + "}";
	public static final String SENSOR_UNPAIRED_PATH = "/unpaired";
	public static final String GATEWAY_MAC_ADDRESS_PATH = "/{" + ParamsConst.GATEWAY_MAC_ADDRESS_PARAM + "}";
	public static final String ROOM_ID_PATH = "/{" + ParamsConst.ROOM_ID_PARAM + "}";
	public static final String BUILDING_ID_PATH = "/{" + ParamsConst.BUILDING_ID_PARAM + "}";
	public static final String CITY_ID_PATH = "/{" + ParamsConst.CITY_ID_PARAM + "}";
	public static final String REGION_ID_PATH = "/{" + ParamsConst.REGION_ID_PARAM + "}";
	public static final String COUNTRY_ID_PATH = "/{" + ParamsConst.COUNTRY_ID_PARAM + "}";
	
	// Resources 2nd level
	public static final String LOGIN_PATH = "/login";  
	public static final String LOGOUT_PATH = "/logout";
	public static final String TEACHIN_PATH = "/teachin";
	public static final String UNPAIRED_PATH = "/unpaired";
	public static final String EXPORT_PATH = "/export";
	public static final String BUILDING_PATH = "/buildings";
	public static final String CITY_PATH = "/cities";
	public static final String REGION_PATH = "/regions";
	public static final String COUNTRY_PATH = "/countries";
	
	// Resources 3rd level
	public static final String INIT_PATH = "/init";
	public static final String CANCEL_PATH = "/cancel";
	public static final String SUBMIT_PATH = "/submit";
	public static final String POPULAR_PATH = "/popular";
	public static final String OCCUPANCY_PATH = "/occupancy";
	
	
	private PathConst() {
		
	}
}
