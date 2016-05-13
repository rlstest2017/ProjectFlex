package com.orange.flexoffice.meetingroomapi.ws;

public final class PathConst {
	
	// Resources 1st level
	public static final String DASHBOARDS_PATH = "/dashboards";    // 2 requests
	public static final String AGENTS_PATH = "/agents";    // 1 request
	public static final String MEETINGROOMS_PATH = "/meetingrooms";    // 2 requests
	public static final String SYSTEM_PATH = "/system";    // 1 request
		
	// Resources ID
	public static final String DASHBOARD_ID_PATH = "/{" + ParamsConst.DASHBOARD_MAC_ADDRESS_PARAM + "}";
	public static final String AGENT_ID_PATH = "/{" + ParamsConst.AGENT_MAC_ADDRESS_PARAM + "}";
	public static final String MEETINGROOM_EXTERNAL_ID_PATH = "/{" + ParamsConst.MEETINGROOM_EXTERNAL_ID_PARAM + "}";
	
	// Resources 2nd level
	public static final String CONFIG_PATH = "/config";  
	public static final String TIMEOUT_PATH = "/timeout"; 
	
	private PathConst() {
		
	}
}
