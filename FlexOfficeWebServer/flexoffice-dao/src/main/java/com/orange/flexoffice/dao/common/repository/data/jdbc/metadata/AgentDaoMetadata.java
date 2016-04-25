package com.orange.flexoffice.dao.common.repository.data.jdbc.metadata;

/**
 * AgentDaoMetadata
 * @author oab
 *
 */
public final class AgentDaoMetadata {
	
	public static final String AGENT_TABLE_NAME = "agents";
	public static final String AGENT_ID_COL = "id";
	public static final String AGENT_NAME_COL = "name";
	public static final String AGENT_MAC_ADDRESS_COL = "mac_address";
	public static final String AGENT_DESCRIPTION_COL = "description";
	public static final String AGENT_STATUS_COL = "status";
	public static final String AGENT_MEETINGROOM_ID_COL = "meetingroom_id";
	public static final String AGENT_LAST_MEASURE_DATE_COL = "last_measure_date";
	public static final String AGENT_COMMAND = "command";
	
	private AgentDaoMetadata(){
		
	}

}
