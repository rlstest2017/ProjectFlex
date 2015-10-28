package com.orange.flexoffice.business.gatewayapi.service.data.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.orange.flexoffice.business.gatewayapi.exception.DataAlreadyExistsException;
import com.orange.flexoffice.business.gatewayapi.service.data.CharacteristicManager;
import com.orange.flexoffice.dao.gatewayapi.model.Stat;
import com.orange.flexoffice.dao.gatewayapi.model.data.Characteristic;
import com.orange.flexoffice.dao.gatewayapi.repository.data.jdbc.CharacteristicRepository;
import com.orange.flexoffice.dao.gatewayapi.repository.stat.jdbc.CharacteristicDescStatRepository;
import com.orange.flexoffice.dao.gatewayapi.repository.stat.jdbc.CharacteristicItemStatRepository;

/**
 * Manages {@link Characteristic}
 * 
 * @author Guillaume Mouricou
 */
@Service
@Transactional
public class CharacteristicManagerImpl implements CharacteristicManager {

	@Autowired
	private CharacteristicRepository characteristicRepository;
	@Autowired
	private CharacteristicItemStatRepository characteristicItemStatRepository;
	@Autowired
	private CharacteristicDescStatRepository characteristicDescStatRepository;
	
	/**
	 * Finds a {@link Characteristic} by its ID.
	 * 
	 * @param id
	 * 		  the {@link Characteristic} ID
	 * @return a {@link Characteristic}
	 */
	@Transactional(readOnly=true)
	public Characteristic find(long id) {
		return characteristicRepository.findOne(id);
	}
	
	/**
	 * Saves a {@link Characteristic}.
	 * 
	 * @param charac
	 * 		  the new {@link Characteristic}
	 * @return the saved {@link Characteristic}
	 * @throws DataAlreadyExistsException 
	 */
	public Characteristic save(Characteristic charac) throws DataAlreadyExistsException {
		String itemId = charac.getItemId();
		String descId = charac.getDescriptorId();
		Float weight = charac.getWeight();
		
		Characteristic testCharac = characteristicRepository.findByItemIdAndDescriptorId(itemId, descId);
		if (testCharac != null) {
			throw new DataAlreadyExistsException("Characteristic already saves.");
		}
		
		// Manages statistics
		Stat itemStat = characteristicItemStatRepository.findByObjectId(itemId);
		if (itemStat == null) {
			itemStat = new Stat();
			itemStat.setObjectId(itemId);
			itemStat.addRating(weight);
			characteristicItemStatRepository.save(itemStat);
		} else {
			itemStat.addRating(weight);
			characteristicItemStatRepository.update(itemStat);	
		}
		
		
		Stat descStat = characteristicDescStatRepository.findByObjectId(descId);
		if (descStat == null) {
			descStat = new Stat();
			descStat.setObjectId(descId);
			descStat.addRating(weight);
			characteristicDescStatRepository.save(descStat);
		} else {
			descStat.addRating(weight);
			characteristicDescStatRepository.update(descStat);
		}
		
		// Saves characteristics
		return characteristicRepository.save(charac);
	}
	
	/**
	 * Updates a {@link Characteristic}.
	 * 
	 * @param charac
	 * 		  the {@link Characteristic}
	 * @return the saved {@link Characteristic}
	 */
	public Characteristic update(Characteristic charac) {
		String itemId = charac.getItemId();
		String descId = charac.getDescriptorId();
		Float weight = charac.getWeight();
		
		Characteristic oldCharac = characteristicRepository.findOne(charac.getId());

		if (!weight.equals(oldCharac.getWeight())) {
			// Manages statistics
			Stat itemStat = characteristicItemStatRepository.findByObjectId(itemId);
			itemStat.removeRating(oldCharac.getWeight());
			itemStat.addRating(weight);
			characteristicItemStatRepository.update(itemStat);
			
			Stat descStat = characteristicDescStatRepository.findByObjectId(descId);
			descStat.removeRating(oldCharac.getWeight());
			descStat.addRating(weight);
			characteristicDescStatRepository.update(descStat);
		}
		
		// Saves characteristics
		return characteristicRepository.update(charac);
	}
	
	/**
	 * Deletes a {@link Characteristic}.
	 * 
	 * @param id 
	 * 		  a {@link Characteristic} ID
	 */
	public void delete(long id) {
		Characteristic charac = characteristicRepository.findOne(id);
		String itemId = charac.getItemId();
		String descId = charac.getDescriptorId();
		Float weight = charac.getWeight();
		
		// Manages statistics
		Stat itemStat = characteristicItemStatRepository.findByObjectId(itemId);
		itemStat.removeRating(weight);
		if (itemStat.getCount() > 0) {
			characteristicItemStatRepository.update(itemStat);
		} else {
			characteristicItemStatRepository.delete(itemStat.getId());
		}
		
		Stat descStat = characteristicDescStatRepository.findByObjectId(descId);
		descStat.removeRating(weight);
		if (descStat.getCount() > 0) {
			characteristicDescStatRepository.update(descStat);
		} else {
			characteristicDescStatRepository.delete(descStat.getId());
		}
		
		// Deletes log
		characteristicRepository.delete(id);
	}
}
