package com.orange.flexoffice.dao.common.repository.data.jdbc.metadata;

/**
 * GatewayDaoMetadata
 * @author oab
 *
 */
public final class GatewayDaoMetadata {
	
	public static final String GATEWAY_TABLE_NAME = "gateways";
	public static final String GATEWAY_ID_COL = "id";
	public static final String GATEWAY_NAME_COL = "name";
	public static final String GATEWAY_MAC_ADDRESS_COL = "mac_address";
	public static final String GATEWAY_DESCRIPTION_COL = "description";
	public static final String GATEWAY_STATUS_COL = "status";
	public static final String GATEWAY_IS_ACTIVATED_COL = "is_activated";
	public static final String GATEWAY_ROOMS_ID_COL = "rooms_id";
	public static final String GATEWAY_LAST_POLLING_DATE_COL = "last_polling_date";
	
	private GatewayDaoMetadata(){
		
	}

}
