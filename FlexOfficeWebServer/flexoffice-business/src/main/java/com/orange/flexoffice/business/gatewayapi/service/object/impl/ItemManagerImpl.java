package com.orange.flexoffice.business.gatewayapi.service.object.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.orange.flexoffice.business.gatewayapi.service.object.ItemManager;
import com.orange.flexoffice.dao.gatewayapi.model.data.Characteristic;
import com.orange.flexoffice.dao.gatewayapi.model.data.Log;
import com.orange.flexoffice.dao.gatewayapi.model.object.Item;
import com.orange.flexoffice.dao.gatewayapi.repository.data.jdbc.CharacteristicRepository;
import com.orange.flexoffice.dao.gatewayapi.repository.data.jdbc.LogRepository;

@Service("itemManager")
@Transactional(readOnly=true)
public class ItemManagerImpl implements ItemManager {
	
	@Autowired
	private LogRepository logRepository;
	@Autowired
	private CharacteristicRepository characteristicRepository;
	
	public Item find(String itemId) {
		Item item = new Item();
		item.setId(itemId);
		item.setLogs(getItemLogs(itemId));
		item.setCharacteristics(getItemCharacteristics(itemId));
		
		return item;
	}
	
	public List<Log> getItemLogs(String itemId) {
		return logRepository.findByItemId(itemId);
	}
	
	public List<Characteristic> getItemCharacteristics(String itemId) {
		return characteristicRepository.findByItemId(itemId);
	}
	
}
