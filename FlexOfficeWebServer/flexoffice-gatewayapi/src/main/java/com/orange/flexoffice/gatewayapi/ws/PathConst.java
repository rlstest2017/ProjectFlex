package com.orange.flexoffice.gatewayapi.ws;

public final class PathConst {
	
	// Resources
	public static final String USERS_PATH = "/users";
	public static final String ITEMS_PATH = "/items";
	public static final String DESCRIPTORS_PATH ="/descriptors";
	public static final String LOGS_PATH = "/logs";
	public static final String PREFERENCES_PATH = "/preferences";
	public static final String RELATIONSHIPS_PATH = "/relationships";
	public static final String CHARACTERISTICS_PATH = "/characteristics";
	public static final String RECOMMENDATIONS_PATH = "/recommendations";
	public static final String SIMILARITIES_PATH = "/similarities";
	public static final String RANKINGS_PATH = "/rankings";
	
	// Resources ID
	public static final String DESCRIPTOR_ID_PATH ="/{" + ParamsConst.DESCRIPTOR_ID_PARAM + "}";
	public static final String USER_ID_PATH = "/{" + ParamsConst.USER_ID_PARAM + "}";
	public static final String ITEM_ID_PATH = "/{" + ParamsConst.ITEM_ID_PARAM + "}";
	public static final String LOG_ID_PATH = "/{" + ParamsConst.LOG_ID_PARAM + "}";
	public static final String PREFERENCE_ID_PATH = "/{" + ParamsConst.PREFERENCE_ID_PARAM + "}";
	public static final String RELATIONSHIP_ID_PATH = "/{" + ParamsConst.RELATIONSHIP_ID_PARAM + "}";
	public static final String CHARACTERISTIC_ID_PATH = "/{" + ParamsConst.CHARACTERISTIC_ID_PARAM + "}";
	
	public static final String FRIENDS_PATH = "/friends";
	public static final String FOLLOWERS_PATH = "/followers";
	public static final String THEMATIC_PATH = "/thematic";
	public static final String COLLABORATIVE_PATH = "/collaborative";
	public static final String GENERATE_PATH = "/generate";
	
	private PathConst() {}
}
