package com.orange.flexoffice.dao.common.repository.data.jdbc;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.orange.flexoffice.dao.common.model.data.ConfigurationDao;
import com.orange.flexoffice.dao.common.repository.data.ConfigurationDaoOperations;
import com.orange.flexoffice.dao.common.repository.data.jdbc.metadata.ConfigurationDaoMetadata;

@Repository
public class ConfigurationDaoRepository extends DataRepository<ConfigurationDao> implements ConfigurationDaoOperations {

	
	public ConfigurationDaoRepository() {
		super(ConfigurationDao.class);
	}

	@Override
	public ConfigurationDao findByKey(String key) {
		SqlParameterSource paramMap = new MapSqlParameterSource("key", key);
		ConfigurationDao data = null;
		data =  jdbcTemplate.queryForObject(
				findByKeyQuery, 
				paramMap, 
				new BeanPropertyRowMapper<ConfigurationDao>(ConfigurationDao.class)
			);
		return data;
	}

	@Override
	protected String getTableName() {
		return ConfigurationDaoMetadata.CONFIGURATION_TABLE_NAME;
	}

	@Override
	protected String getColumnColName() {
		return null;
	}

}
