package com.orange.flexoffice.dao.common.repository.data.jdbc;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.orange.flexoffice.dao.common.model.data.BuildingDao;
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
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public BuildingDao findByBuildingId(Long buildingId) throws IncorrectResultSizeDataAccessException {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public BuildingDao findByName(String name) throws IncorrectResultSizeDataAccessException {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public BuildingDao saveBuilding(BuildingDao data) throws DataIntegrityViolationException {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public BuildingDao updateBuilding(BuildingDao data) throws DataAccessException {
		// TODO Auto-generated method stub
		return null;
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
