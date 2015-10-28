package com.orange.flexoffice.business.gatewayapi.service.object.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.orange.flexoffice.business.gatewayapi.service.object.DescriptorManager;
import com.orange.flexoffice.dao.gatewayapi.model.data.Characteristic;
import com.orange.flexoffice.dao.gatewayapi.model.data.Preference;
import com.orange.flexoffice.dao.gatewayapi.model.object.Descriptor;
import com.orange.flexoffice.dao.gatewayapi.repository.data.jdbc.CharacteristicRepository;
import com.orange.flexoffice.dao.gatewayapi.repository.data.jdbc.PreferenceRepository;

@Service("descriptorManager")
@Transactional(readOnly=true)
public class DescriptorManagerImpl implements DescriptorManager {

	@Autowired
	private CharacteristicRepository characteristicRepository;
	@Autowired
	private PreferenceRepository preferenceRepository;
	
	public Descriptor find(String descId) {
		Descriptor desc = new Descriptor();
		desc.setId(descId);
		desc.setCharacteristics(getDescriptorCharacteristic(descId));
		desc.setPreferences(getDescriptorPreference(descId));
		return desc;
	}
	
	public List<Characteristic> getDescriptorCharacteristic(String descId) {
		return characteristicRepository.findByDescriptorId(descId);
	}
	
	public List<Preference> getDescriptorPreference(String descId) {
		return preferenceRepository.findByDescriptorId(descId);
	}
	
	
}
