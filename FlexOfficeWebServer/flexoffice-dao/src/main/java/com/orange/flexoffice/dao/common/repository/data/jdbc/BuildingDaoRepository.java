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

import com.orange.flexoffice.dao.common.model.data.BuildingDao;
import com.orange.flexoffice.dao.common.model.object.BuildingDto;
import com.orange.flexoffice.dao.common.model.object.BuildingSummaryDto;
import com.orange.flexoffice.dao.common.repository.data.BuildingDaoOperations;
import com.orange.flexoffice.dao.common.repository.data.jdbc.metadata.BuildingDaoMetadata;

@Repository
public class BuildingDaoRepository extends DataRepository<BuildingDao> implements BuildingDaoOperations {

	
	public BuildingDaoRepository() {
		super(BuildingDao.class);
	}

	@Override
	public List<BuildingSummaryDto> findAllBuildingsSummary() {
		SqlParameterSource paramMap = new MapSqlParameterSource();
		return jdbcTemplate.query(
				findAllBuildingsSummaryQuery, 
				paramMap, 
				new BeanPropertyRowMapper<BuildingSummaryDto>(BuildingSummaryDto.class)
			);
	}

	@Override
	public List<BuildingDao> findByCityId(Long cityId) throws IncorrectResultSizeDataAccessException {
		SqlParameterSource paramMap = new MapSqlParameterSource("cityId", cityId);
		return jdbcTemplate.query(
				findByColumnCityIdQuery, 
				paramMap, 
				new BeanPropertyRowMapper<BuildingDao>(BuildingDao.class)
			);
	}


	@Override
	public BuildingDto findByBuildingId(Long buildingId) throws IncorrectResultSizeDataAccessException {
		SqlParameterSource paramMap = new MapSqlParameterSource("columnId", buildingId);
		return jdbcTemplate.queryForObject(
				findByBuildingIdQuery, 
				paramMap, 
				new BeanPropertyRowMapper<BuildingDto>(BuildingDto.class)
			);
	}

	@Override
	public BuildingDao saveBuilding(BuildingDao data) throws DataIntegrityViolationException {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		SqlParameterSource paramBean = new BeanPropertySqlParameterSource(data);
		jdbcTemplate.update(saveBuildingQuery, paramBean, keyHolder);
		// Retrieves generated id of saved data.
		Integer id = (Integer)keyHolder.getKeys().get("id");
		data.setId(id.longValue());
		return data;	
	}

	@Override
	public BuildingDao updateBuilding(BuildingDao data) throws DataAccessException {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		SqlParameterSource paramBean = new BeanPropertySqlParameterSource(data);
		jdbcTemplate.update(updateBuildingQuery, paramBean, keyHolder);
		// Retrieves generated id of saved data.
		Integer id = (Integer)keyHolder.getKeys().get("id");
		data.setId(id.longValue());	
		return data;
	}

	@Override
	protected String getTableName() {
		return BuildingDaoMetadata.BUILDING_TABLE_NAME;
	}

	@Override
	protected String getColumnColName() {
		return BuildingDaoMetadata.BUILDING_ID_COL;
	}

	@Override
	protected String getColName() {
		return BuildingDaoMetadata.BUILDING_NAME_COL;
	}

		
}
