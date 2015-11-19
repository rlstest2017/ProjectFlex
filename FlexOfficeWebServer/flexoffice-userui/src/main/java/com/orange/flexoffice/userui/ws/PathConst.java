package com.orange.flexoffice.userui.ws;

public final class PathConst {
	
	// Resources
	public static final String USERS_PATH = "/users";
	public static final String ROOMS_PATH = "/rooms";
	
	// Resources ID
	public static final String USER_ID_PATH = "/{" + ParamsConst.USER_ID_PARAM + "}";
	public static final String ROOM_ID_PATH = "/{" + ParamsConst.ROOM_ID_PARAM + "}";
	public static final String ROOM_ID_RESERVE_PATH = "/{" + ParamsConst.ROOM_ID_PARAM + "}/reserve";
	public static final String ROOM_ID_CANCEL_PATH = "/{" + ParamsConst.ROOM_ID_PARAM + "}/cancel";
	
	public static final String GENERATE_PATH = "/generate";
	
	private PathConst() {}
}
