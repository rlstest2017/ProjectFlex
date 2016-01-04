package com.orange.flexoffice.dao.common.model.object;

/**
 * SensorDto
 * @author oab
 *
 */
public class SensorDto  {

	private String sensorIdentifier; // sensor back id on hexa 4 octets
	private String sensorStatus;
	
	/**
	 * @return the sensorIdentifier
	 */
	public String getSensorIdentifier() {
		return sensorIdentifier;
	}

	/**
	 * @param sensorIdentifier the sensorIdentifier to set
	 */
	public void setSensorIdentifier(String sensorIdentifier) {
		this.sensorIdentifier = sensorIdentifier;
	}

	/**
	 * @return the sensorStatus
	 */
	public String getSensorStatus() {
		return sensorStatus;
	}

	/**
	 * @param sensorStatus the sensorStatus to set
	 */
	public void setSensorStatus(String sensorStatus) {
		this.sensorStatus = sensorStatus;
	}

}
