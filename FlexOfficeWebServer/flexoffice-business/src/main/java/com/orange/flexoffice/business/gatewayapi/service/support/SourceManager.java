package com.orange.flexoffice.business.gatewayapi.service.support;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.orange.flexoffice.business.gatewayapi.source.Source;
import com.orange.flexoffice.business.gatewayapi.source.SourceType;
import com.orange.flexoffice.dao.gateway.repository.data.jdbc.CharacteristicRepository;
import com.orange.flexoffice.dao.gateway.repository.data.jdbc.LogRepository;
import com.orange.flexoffice.dao.gateway.repository.data.jdbc.PreferenceRepository;
import com.orange.flexoffice.dao.gateway.repository.data.jdbc.RelationshipRepository;
import com.orange.flexoffice.dao.gatewayapi.repository.stat.jdbc.CharacteristicDescStatRepository;
import com.orange.flexoffice.dao.gatewayapi.repository.stat.jdbc.CharacteristicItemStatRepository;
import com.orange.flexoffice.dao.gatewayapi.repository.stat.jdbc.LogItemStatRepository;
import com.orange.flexoffice.dao.gatewayapi.repository.stat.jdbc.LogUserStatRepository;
import com.orange.flexoffice.dao.gatewayapi.repository.stat.jdbc.PreferenceDescStatRepository;
import com.orange.flexoffice.dao.gatewayapi.repository.stat.jdbc.PreferenceUserStatRepository;
import com.orange.flexoffice.dao.gatewayapi.repository.stat.jdbc.RelationshipFriendStatRepository;
import com.orange.flexoffice.dao.gatewayapi.repository.stat.jdbc.RelationshipUserStatRepository;

@Service
public class SourceManager {
	
	@Autowired 
	private LogRepository logRepository;
	@Autowired
	private LogUserStatRepository logUserStatRepository;
	@Autowired
	private LogItemStatRepository logItemStatRepository;

	@Autowired
	private CharacteristicRepository characteristicRepository;
	@Autowired
	private CharacteristicItemStatRepository characteristicItemStatRepository;
	@Autowired
	private CharacteristicDescStatRepository characteristicDescStatRepository;

	@Autowired
	private PreferenceRepository preferenceRepository;
	@Autowired
	private PreferenceUserStatRepository preferenceUserStatRepository;
	@Autowired
	private PreferenceDescStatRepository preferenceDescStatRepository;

	@Autowired
	private RelationshipRepository relationshipRepository;
	@Autowired
	private RelationshipUserStatRepository relationshipUserStatRepository;
	@Autowired
	private RelationshipFriendStatRepository relationshipFriendStatRepository;
	
	private Map<SourceType, Source> sources;
	
	
	@PostConstruct
	public void init() {
		sources = new HashMap<SourceType, Source>();
		
		sources.put(SourceType.LOGS, new Source(5000,
				logRepository,
				logUserStatRepository,
				logItemStatRepository));
		sources.put(SourceType.CHARACTERISTICS, new Source(5000,
				characteristicRepository,
				characteristicItemStatRepository,
				characteristicDescStatRepository));
		sources.put(SourceType.PREFERENCES, new Source(5000,
				preferenceRepository,
				preferenceUserStatRepository,
				preferenceDescStatRepository));
		sources.put(SourceType.RELATIONSHIPS, new Source(5000,
				relationshipRepository,
				relationshipUserStatRepository,
				relationshipFriendStatRepository));
		
	}
	
	public Source findByType(SourceType type) {
		return sources.get(type);
	}
}
