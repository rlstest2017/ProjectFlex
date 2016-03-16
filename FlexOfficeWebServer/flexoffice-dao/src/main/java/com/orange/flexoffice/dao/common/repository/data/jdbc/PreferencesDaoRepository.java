package com.orange.flexoffice.dao.common.repository.data.jdbc;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.orange.flexoffice.dao.common.model.data.PreferencesDao;
import com.orange.flexoffice.dao.common.repository.data.PreferencesDaoOperations;
import com.orange.flexoffice.dao.common.repository.data.jdbc.metadata.PreferenceDaoMetadata;


@Repository
public class PreferencesDaoRepository extends DataRepository<PreferencesDao> implements PreferencesDaoOperations {

	public PreferencesDaoRepository() {
		super(PreferencesDao.class);
	}
	
	@Override
	public PreferencesDao findByUserId(Long userId) throws IncorrectResultSizeDataAccessException {
		SqlParameterSource paramMap = new MapSqlParameterSource("columnId", userId);
		return jdbcTemplate.queryForObject(
				findPreferenceByUserIdQuery, 
				paramMap, 
				new BeanPropertyRowMapper<PreferencesDao>(PreferencesDao.class)
			);
	}

	@Override
	public PreferencesDao savePreferences(PreferencesDao data) throws DataIntegrityViolationException {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		SqlParameterSource paramBean = new BeanPropertySqlParameterSource(data);
		jdbcTemplate.update(savePreferenceQuery, paramBean, keyHolder);
		// Retrieves generated id of saved data.
		Integer id = (Integer)keyHolder.getKeys().get("id");
		data.setId(id.longValue());
		return data;
	}

	@Override
	public PreferencesDao updatePreferences(PreferencesDao data) throws DataAccessException {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		SqlParameterSource paramBean = new BeanPropertySqlParameterSource(data);
		jdbcTemplate.update(updatePreferenceQuery, paramBean, keyHolder);
		// Retrieves generated id of saved data.
		Integer id = (Integer)keyHolder.getKeys().get("id");
		data.setId(id.longValue());	
		return data;
	}

	@Override
	public void deleteByCountryId(long countryId) {
		SqlParameterSource paramMap = new MapSqlParameterSource("countryId", countryId);
		jdbcTemplate.update(deletePrefByCountryIdQuery, paramMap);
	}

	@Override
	public void deleteByRegionId(long regionId) {
		SqlParameterSource paramMap = new MapSqlParameterSource("regionId", regionId);
		jdbcTemplate.update(deletePrefByRegionIdQuery, paramMap);
	}

	@Override
	public void deleteByCityId(long cityId) {
		SqlParameterSource paramMap = new MapSqlParameterSource("cityId", cityId);
		jdbcTemplate.update(deletePrefByCityIdQuery, paramMap);
	}

	@Override
	public void deleteByBuildingId(long buildingId) {
		SqlParameterSource paramMap = new MapSqlParameterSource("buildingId", buildingId);
		jdbcTemplate.update(deletePrefByBuildingIdQuery, paramMap);
	}

	@Override
	protected String getTableName() {
		return PreferenceDaoMetadata.PREFERENCE_TABLE_NAME;
	}

	@Override
	protected String getColumnColName() {
		return PreferenceDaoMetadata.PREFERENCE_ID_COL;
	}

	@Override
	protected String getColName() {
		return PreferenceDaoMetadata.PREFERENCE_USER_ID_COL;
	}
	
}
