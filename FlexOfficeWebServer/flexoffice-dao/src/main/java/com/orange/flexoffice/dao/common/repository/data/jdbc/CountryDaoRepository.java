package com.orange.flexoffice.dao.common.repository.data.jdbc;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.stereotype.Repository;

import com.orange.flexoffice.dao.common.model.data.CountryDao;
import com.orange.flexoffice.dao.common.repository.data.CountryDaoOperations;

@Repository
public class CountryDaoRepository extends DataRepository<CountryDao> implements CountryDaoOperations {

	
	public CountryDaoRepository() {
		super(CountryDao.class);
	}

	@Override
	public List<CountryDao> findAllCountries() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CountryDao findByCountryId(Long countryId) throws IncorrectResultSizeDataAccessException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CountryDao findByName(String name) throws IncorrectResultSizeDataAccessException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CountryDao saveCountry(CountryDao data) throws DataIntegrityViolationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CountryDao updateCountry(CountryDao data) throws DataAccessException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getTableName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getColumnColName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getColName() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}
