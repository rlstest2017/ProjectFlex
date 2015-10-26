package com.orange.flexoffice.business.gatewayapi.service.data.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.orange.flexoffice.business.gatewayapi.exception.DataAlreadyExistsException;
import com.orange.flexoffice.business.gatewayapi.service.data.RelationshipManager;
import com.orange.flexoffice.dao.gateway.repository.data.jdbc.RelationshipRepository;
import com.orange.flexoffice.dao.gatewayapi.model.Stat;
import com.orange.flexoffice.dao.gatewayapi.model.data.Relationship;
import com.orange.flexoffice.dao.gatewayapi.repository.stat.jdbc.RelationshipFriendStatRepository;
import com.orange.flexoffice.dao.gatewayapi.repository.stat.jdbc.RelationshipUserStatRepository;

@Service
@Transactional
public class RelationshipManagerImpl implements RelationshipManager {
	
	@Autowired
	private RelationshipRepository relationshipRepository;
	@Autowired
	private RelationshipUserStatRepository relationshipUserStatRepository;
	@Autowired
	private RelationshipFriendStatRepository relationshipFriendStatRepository;
	
	@Transactional(readOnly=true)
	public Relationship find(long id) {
		return relationshipRepository.findOne(id);
	}
	
	public Relationship save(Relationship rel) throws DataAlreadyExistsException {
		String userId = rel.getUserId();
		String friendId = rel.getFriendId();
		Float rating = rel.getRating();
		
		Relationship testRel = relationshipRepository.findByUserIdAndFriendId(userId, friendId);
		if (testRel != null) {
			throw new DataAlreadyExistsException("Relationship already saves.");
		}
		
		// Manages statistics
		Stat userStat = relationshipUserStatRepository.findByObjectId(userId);
		if (userStat == null) {
			userStat = new Stat();
			userStat.setObjectId(userId);
			userStat.addRating(rating);
			relationshipUserStatRepository.save(userStat);
		} else {
			userStat.addRating(rating);
			relationshipUserStatRepository.update(userStat);
		}
		
		Stat friendStat = relationshipFriendStatRepository.findByObjectId(friendId);
		if (friendStat == null) {
			friendStat = new Stat();
			friendStat.setObjectId(friendId);
			friendStat.addRating(rating);
			relationshipFriendStatRepository.save(friendStat);
		} else {
			friendStat.addRating(rating);
			relationshipFriendStatRepository.update(friendStat);
		}
		
		// Saves preference
		return relationshipRepository.save(rel);
	}
	
	public Relationship update(Relationship rel) {
		String userId = rel.getUserId();
		String friendId = rel.getFriendId();
		Float rating = rel.getRating();
		
		Relationship oldRel = find(rel.getId());
		if (!rating.equals(oldRel.getRating())) {
			// Updates statistics
			Stat userStat = relationshipUserStatRepository.findByObjectId(userId);
			userStat.removeRating(oldRel.getRating());
			userStat.addRating(rating);
			relationshipUserStatRepository.update(userStat);
			
			Stat friendStat = relationshipFriendStatRepository.findByObjectId(friendId);
			friendStat.removeRating(oldRel.getRating());
			friendStat.addRating(rating);
			relationshipFriendStatRepository.update(friendStat);
		}
		
		// Saves preference
		return relationshipRepository.update(rel);
	}
	
	public void delete(long id) {
		Relationship rel = find(id);
		String userId = rel.getUserId();
		String friendId = rel.getFriendId();
		Float rating = rel.getRating();
		
		// Manages statistics
		Stat userStat = relationshipUserStatRepository.findByObjectId(userId);
		userStat.removeRating(rating);
		if (userStat.getCount() > 0) {
			relationshipUserStatRepository.update(userStat);
		} else {
			relationshipUserStatRepository.delete(userStat.getId());
		}
		
		Stat friendStat = relationshipFriendStatRepository.findByObjectId(friendId);
		friendStat.removeRating(rating);
		if (friendStat.getCount() > 0) {
			relationshipFriendStatRepository.update(friendStat);
		} else {
			relationshipFriendStatRepository.delete(friendStat.getId());
		}
		
		// Deletes preferences
		relationshipRepository.delete(id);
		
	}

}
