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

import com.orange.flexoffice.dao.common.model.data.RegionDao;
import com.orange.flexoffice.dao.common.model.object.RegionSummaryDto;
import com.orange.flexoffice.dao.common.repository.data.RegionDaoOperations;
import com.orange.flexoffice.dao.common.repository.data.jdbc.metadata.RegionDaoMetadata;

@Repository
public class RegionDaoRepository extends DataRepository<RegionDao> implements RegionDaoOperations {

	
	public RegionDaoRepository() {
		super(RegionDao.class);
	}

	@Override
	public List<RegionSummaryDto> findAllRegionsSummary() {
		SqlParameterSource paramMap = new MapSqlParameterSource();
		return jdbcTemplate.query(
				findAllRegionsSummaryQuery, 
				paramMap, 
				new BeanPropertyRowMapper<RegionSummaryDto>(RegionSummaryDto.class)
			);
	}

	@Override
	public List<RegionDao> findByCountryId(Long countryId) throws IncorrectResultSizeDataAccessException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RegionDao findByRegionId(Long regionId) throws IncorrectResultSizeDataAccessException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RegionDao findByName(String name) throws IncorrectResultSizeDataAccessException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RegionDao saveRegion(RegionDao data) throws DataIntegrityViolationException {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		SqlParameterSource paramBean = new BeanPropertySqlParameterSource(data);
		jdbcTemplate.update(saveRegionQuery, paramBean, keyHolder);
		// Retrieves generated id of saved data.
		Integer id = (Integer)keyHolder.getKeys().get("id");
		data.setId(id.longValue());
		return data;
	}

	@Override
	public RegionDao updateRegion(RegionDao data) throws DataAccessException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getTableName() {
		return RegionDaoMetadata.REGION_TABLE_NAME;
	}

	@Override
	protected String getColumnColName() {
		return RegionDaoMetadata.REGION_ID_COL;
	}

	@Override
	protected String getColName() {
		return RegionDaoMetadata.REGION_NAME_COL;
	}
		
	
}
