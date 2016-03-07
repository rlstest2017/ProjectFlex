package com.orange.flexoffice.dao.common.repository.data.jdbc;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.orange.flexoffice.dao.common.model.data.CityDao;
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
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public CityDao findByCityId(Long cityId) throws IncorrectResultSizeDataAccessException {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public CityDao findByName(String name) throws IncorrectResultSizeDataAccessException {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public CityDao saveCity(CityDao data) throws DataIntegrityViolationException {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public CityDao updateCity(CityDao data) throws DataAccessException {
		// TODO Auto-generated method stub
		return null;
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
