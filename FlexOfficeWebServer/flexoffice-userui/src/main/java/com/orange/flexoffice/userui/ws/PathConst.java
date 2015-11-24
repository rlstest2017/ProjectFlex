package com.orange.flexoffice.userui.ws;

public final class PathConst {
	
	// Resources 1st level
	public static final String SYSTEM_PATH = "/system";  // 3 requests
	public static final String ROOMS_PATH = "/rooms";
	
	// Resources 2nd level
	public static final String USER_PATH = "/user";
	public static final String LOGIN_PATH = "/login";  
	public static final String LOGOUT_PATH = "/logout";
	
	// Resources 3rd level
	public static final String CURRENT_PATH = "/current";
		
	// Resources ID
	public static final String ROOM_ID_PATH = "/{" + ParamsConst.ROOM_ID_PARAM + "}";
	public static final String ROOM_ID_RESERVE_PATH = "/{" + ParamsConst.ROOM_ID_PARAM + "}/reserve";
	public static final String ROOM_ID_CANCEL_PATH = "/{" + ParamsConst.ROOM_ID_PARAM + "}/cancel";
	
	private PathConst() {		
	}
}
