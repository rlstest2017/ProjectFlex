package com.orange.flexoffice.dao.common.repository.data;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;

import com.orange.flexoffice.dao.common.model.data.CountryDao;

/**
 * CountryDaoOperations
 * @author oab
 *
 */
public interface CountryDaoOperations {
	
	List<CountryDao> findAllCountries();
	
	CountryDao findByCountryId(Long countryId) throws IncorrectResultSizeDataAccessException;
	
	CountryDao findByName(String name) throws IncorrectResultSizeDataAccessException;
	
	CountryDao saveCountry(CountryDao data) throws DataIntegrityViolationException;
	
	CountryDao updateCountry(CountryDao data) throws DataAccessException; 
	
}
