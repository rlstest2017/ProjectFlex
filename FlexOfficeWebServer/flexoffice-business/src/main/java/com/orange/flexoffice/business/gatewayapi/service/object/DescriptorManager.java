package com.orange.flexoffice.business.gatewayapi.service.object;

import java.util.List;

import com.orange.flexoffice.dao.gatewayapi.model.data.Characteristic;
import com.orange.flexoffice.dao.gatewayapi.model.data.Preference;
import com.orange.flexoffice.dao.gatewayapi.model.object.Descriptor;

public interface DescriptorManager {

	Descriptor find(String descId);

	List<Characteristic> getDescriptorCharacteristic(String descId);

	List<Preference> getDescriptorPreference(String descId);

}