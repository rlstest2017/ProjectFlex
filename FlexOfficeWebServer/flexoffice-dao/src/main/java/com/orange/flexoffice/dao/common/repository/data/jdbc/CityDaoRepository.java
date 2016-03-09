package com.orange.flexoffice.dao.common.repository.data.jdbc;

import java.util.List;

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

import com.orange.flexoffice.dao.common.model.data.CityDao;
import com.orange.flexoffice.dao.common.model.object.CityDto;
import com.orange.flexoffice.dao.common.model.object.CitySummaryDto;
import com.orange.flexoffice.dao.common.repository.data.CityDaoOperations;
import com.orange.flexoffice.dao.common.repository.data.jdbc.metadata.CityDaoMetadata;

@Repository
public class CityDaoRepository extends DataRepository<CityDao> implements CityDaoOperations {

	
	public CityDaoRepository() {
		super(CityDao.class);
	}

	@Override
	public List<CitySummaryDto> findAllCitiesSummary() {
		SqlParameterSource paramMap = new MapSqlParameterSource();
		return jdbcTemplate.query(
				findAllCitiesSummaryQuery, 
				paramMap, 
				new BeanPropertyRowMapper<CitySummaryDto>(CitySummaryDto.class)
			);
	}

	@Override
	public List<CityDao> findByRegionId(Long regionId) throws IncorrectResultSizeDataAccessException {
		SqlParameterSource paramMap = new MapSqlParameterSource("regionId", regionId);
		return jdbcTemplate.query(
				findByColumnRegionIdQuery, 
				paramMap, 
				new BeanPropertyRowMapper<CityDao>(CityDao.class)
			);
	}

	@Override
	public CityDto findByCityId(Long cityId) throws IncorrectResultSizeDataAccessException {
		SqlParameterSource paramMap = new MapSqlParameterSource("columnId", cityId);
		return jdbcTemplate.queryForObject(
				findByCityIdQuery, 
				paramMap, 
				new BeanPropertyRowMapper<CityDto>(CityDto.class)
			);
	}

	@Override
	public CityDao saveCity(CityDao data) throws DataIntegrityViolationException {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		SqlParameterSource paramBean = new BeanPropertySqlParameterSource(data);
		jdbcTemplate.update(saveCityQuery, paramBean, keyHolder);
		// Retrieves generated id of saved data.
		Integer id = (Integer)keyHolder.getKeys().get("id");
		data.setId(id.longValue());
		return data;	
	}

	@Override
	public CityDao updateCity(CityDao data) throws DataAccessException {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		SqlParameterSource paramBean = new BeanPropertySqlParameterSource(data);
		jdbcTemplate.update(updateCityQuery, paramBean, keyHolder);
		// Retrieves generated id of saved data.
		Integer id = (Integer)keyHolder.getKeys().get("id");
		data.setId(id.longValue());	
		return data;
	}
		
	@Override
	protected String getTableName() {
		return CityDaoMetadata.CITY_TABLE_NAME;
	}

	@Override
	protected String getColumnColName() {
		return CityDaoMetadata.CITY_ID_COL;
	}

	@Override
	protected String getColName() {
		return CityDaoMetadata.CITY_NAME_COL;
	}

	
}
