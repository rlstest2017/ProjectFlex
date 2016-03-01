package com.orange.flexoffice.userui.ws;

public final class PathConst {
	
	// Resources 1st level
	public static final String SYSTEM_PATH = "/system";  // 3 requests
	public static final String ROOMS_PATH = "/rooms";
	public static final String LOCATION_PATH = "/location";    // 4 requests
	
	// Resources 2nd level
	public static final String USER_PATH = "/user";
	public static final String LOGIN_PATH = "/login";  
	public static final String LOGOUT_PATH = "/logout";
	public static final String CITY_ID_PATH = "/{" + ParamsConst.CITY_ID_PARAM + "}";
	public static final String REGION_ID_PATH = "/{" + ParamsConst.REGION_ID_PARAM + "}";
	public static final String COUNTRY_ID_PATH = "/{" + ParamsConst.COUNTRY_ID_PARAM + "}";
	
	// Resources ID
	public static final String ROOM_ID_PATH = "/{" + ParamsConst.ROOM_ID_PARAM + "}";
	
	// Resources 3rd level
	public static final String CURRENT_PATH = "/current";
	public static final String RESERVE_PATH = "/reserve";
	public static final String CANCEL_PATH = "/cancel";
	public static final String BUILDING_PATH = "/buildings";
	public static final String CITY_PATH = "/cities";
	public static final String REGION_PATH = "/regions";
	public static final String COUNTRY_PATH = "/countries";

		
	private PathConst() {		
	}
}
