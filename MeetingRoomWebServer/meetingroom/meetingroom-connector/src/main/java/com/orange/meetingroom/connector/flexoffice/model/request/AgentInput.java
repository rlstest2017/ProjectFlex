package com.orange.meetingroom.connector.flexoffice.model.request;

import com.orange.meetingroom.connector.flexoffice.enums.EnumAgentStatus;

/**
 * AgentInput
 * @author oab
 *
 */
public class AgentInput {
	
	String agentMacAddress;
	EnumAgentStatus agentStatus;
	/**
	 * @return the agentMacAddress
	 */
	public String getAgentMacAddress() {
		return agentMacAddress;
	}
	/**
	 * @param agentMacAddress the agentMacAddress to set
	 */
	public void setAgentMacAddress(String agentMacAddress) {
		this.agentMacAddress = agentMacAddress;
	}
	/**
	 * @return the agentStatus
	 */
	public EnumAgentStatus getAgentStatus() {
		return agentStatus;
	}
	/**
	 * @param agentStatus the agentStatus to set
	 */
	public void setAgentStatus(EnumAgentStatus agentStatus) {
		this.agentStatus = agentStatus;
	}

	
}
