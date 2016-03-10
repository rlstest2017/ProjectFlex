package com.orange.flexoffice.dao.common.repository.data;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import com.orange.flexoffice.dao.common.model.data.CountryDao;

/**
 * CountryDaoOperations
 * @author oab
 *
 */
public interface CountryDaoOperations {
	
	List<CountryDao> findAllCountries();
	
	List<CountryDao> findCountriesHaveRooms();
	
	CountryDao saveCountry(CountryDao data) throws DataIntegrityViolationException;
	
	CountryDao updateCountry(CountryDao data) throws DataAccessException; 
	
}
