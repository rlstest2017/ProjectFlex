package com.orange.flexoffice.dao.common.repository.data.jdbc;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.orange.flexoffice.dao.common.model.data.CountryDao;
import com.orange.flexoffice.dao.common.repository.data.CountryDaoOperations;
import com.orange.flexoffice.dao.common.repository.data.jdbc.metadata.CountryDaoMetadata;

@Repository
public class CountryDaoRepository extends DataRepository<CountryDao> implements CountryDaoOperations {

	
	public CountryDaoRepository() {
		super(CountryDao.class);
	}

	@Override
	public List<CountryDao> findAllCountries() {
		SqlParameterSource paramMap = new MapSqlParameterSource();
		return jdbcTemplate.query(
				findAllQuery, 
				paramMap, 
				new BeanPropertyRowMapper<CountryDao>(CountryDao.class)
			);
	}

	@Override
	public List<CountryDao> findCountriesHaveRooms() {
		SqlParameterSource paramMap = new MapSqlParameterSource();
		return jdbcTemplate.query(
				findCountriesHaveRoomsQuery, 
				paramMap, 
				new BeanPropertyRowMapper<CountryDao>(CountryDao.class)
			);
	}
	
	@Override
	public List<CountryDao> findCountriesHaveMeetingRooms() {
		SqlParameterSource paramMap = new MapSqlParameterSource();
		return jdbcTemplate.query(
				findCountriesHaveMeetingRoomsQuery, 
				paramMap, 
				new BeanPropertyRowMapper<CountryDao>(CountryDao.class)
			);
	}
	
	@Override
	public CountryDao saveCountry(CountryDao data) throws DataIntegrityViolationException {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		SqlParameterSource paramBean = new BeanPropertySqlParameterSource(data);
		jdbcTemplate.update(saveCountryQuery, paramBean, keyHolder);
		// Retrieves generated id of saved data.
		Integer id = (Integer)keyHolder.getKeys().get("id");
		data.setId(id.longValue());
		return data;
	}

	@Override
	public CountryDao updateCountry(CountryDao data) throws DataAccessException {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		SqlParameterSource paramBean = new BeanPropertySqlParameterSource(data);
		jdbcTemplate.update(updateCountryQuery, paramBean, keyHolder);
		// Retrieves generated id of saved data.
		Integer id = (Integer)keyHolder.getKeys().get("id");
		data.setId(id.longValue());	
		return data;
	}

	@Override
	protected String getTableName() {
		return CountryDaoMetadata.COUNTRY_TABLE_NAME;
	}

	@Override
	protected String getColumnColName() {
		return CountryDaoMetadata.COUNTRY_ID_COL;
	}

	@Override
	protected String getColName() {
		return CountryDaoMetadata.COUNTRY_NAME_COL;
	}
	
	
}
