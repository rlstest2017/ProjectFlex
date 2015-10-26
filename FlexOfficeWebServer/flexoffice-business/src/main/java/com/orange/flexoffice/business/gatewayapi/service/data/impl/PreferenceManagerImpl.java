package com.orange.flexoffice.business.gatewayapi.service.data.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.orange.flexoffice.business.gatewayapi.exception.DataAlreadyExistsException;
import com.orange.flexoffice.business.gatewayapi.service.data.PreferenceManager;
import com.orange.flexoffice.dao.gateway.repository.data.jdbc.PreferenceRepository;
import com.orange.flexoffice.dao.gatewayapi.model.Stat;
import com.orange.flexoffice.dao.gatewayapi.model.data.Preference;
import com.orange.flexoffice.dao.gatewayapi.repository.stat.jdbc.PreferenceDescStatRepository;
import com.orange.flexoffice.dao.gatewayapi.repository.stat.jdbc.PreferenceUserStatRepository;

@Service
@Transactional
public class PreferenceManagerImpl implements PreferenceManager {

	@Autowired
	private PreferenceRepository preferenceRepository;
	@Autowired
	private PreferenceUserStatRepository preferenceUserStatRepository;
	@Autowired
	private PreferenceDescStatRepository preferenceDescStatRepository;
	
	
	@Transactional(readOnly=true)
	public Preference find(long id) {
		return preferenceRepository.findOne(id);
	}
	
	public Preference save(Preference pref) throws DataAlreadyExistsException {
		String userId = pref.getUserId();
		String descId = pref.getDescriptorId();
		Float rating = pref.getRating();
		
		Preference testPref = preferenceRepository.findByUserIdAndDescriptorId(userId, descId);
		if (testPref != null) {
			throw new DataAlreadyExistsException("Preference already saves.");
		}
		
		// Manages statistics
		Stat userStat = preferenceUserStatRepository.findByObjectId(userId);
		if (userStat == null) {
			userStat = new Stat();
			userStat.setObjectId(userId);
			userStat.addRating(rating);
			preferenceUserStatRepository.save(userStat);
		} else {
			userStat.addRating(rating);
			preferenceUserStatRepository.update(userStat);
		}
		
		Stat descStat = preferenceDescStatRepository.findByObjectId(descId);
		if (descStat == null) {
			descStat = new Stat();
			descStat.setObjectId(descId);
			descStat.addRating(rating);
			preferenceDescStatRepository.save(descStat);
		} else {
			descStat.addRating(rating);
			preferenceDescStatRepository.update(descStat);
		}
		
		// Saves preference
		return preferenceRepository.save(pref);
	}
	
	public Preference update(Preference pref) {
		String userId = pref.getUserId();
		String descId = pref.getDescriptorId();
		Float rating = pref.getRating();
		
		Preference oldPref = find(pref.getId());
		if (!rating.equals(oldPref.getRating())) {
			// Updates statistics
			Stat userStat = preferenceUserStatRepository.findByObjectId(userId);
			userStat.removeRating(oldPref.getRating());
			userStat.addRating(rating);
			preferenceUserStatRepository.update(userStat);
			
			Stat descStat = preferenceDescStatRepository.findByObjectId(descId);
			descStat.removeRating(oldPref.getRating());
			descStat.addRating(rating);
			preferenceDescStatRepository.update(descStat);
		}
		
		// Saves preference
		return preferenceRepository.update(pref);
	}
	
	public void delete(long id) {
		Preference pref = find(id);
		String userId = pref.getUserId();
		String descId = pref.getDescriptorId();
		Float rating = pref.getRating();
		
		// Manages statistics
		Stat userStat = preferenceUserStatRepository.findByObjectId(userId);
		userStat.removeRating(rating);
		if (userStat.getCount() > 0) {
			preferenceUserStatRepository.update(userStat);
		} else {
			preferenceUserStatRepository.delete(userStat.getId());
		}
		
		Stat descStat = preferenceDescStatRepository.findByObjectId(descId);
		descStat.removeRating(rating);
		if (descStat.getCount() > 0) {
			preferenceDescStatRepository.update(descStat);
		} else {
			preferenceDescStatRepository.delete(descStat.getId());
		}
		
		// Deletes preferences
		preferenceRepository.delete(id);
		
	}
}
