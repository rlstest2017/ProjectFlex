package com.orange.flexoffice.dao.gatewayapi.repository.stat.jdbc;

import static com.orange.flexoffice.dao.gatewayapi.repository.stat.jdbc.StatSqlTemplate.COUNT_TEMPLATE;
import static com.orange.flexoffice.dao.gatewayapi.repository.stat.jdbc.StatSqlTemplate.DELETE_TEMPLATE;
import static com.orange.flexoffice.dao.gatewayapi.repository.stat.jdbc.StatSqlTemplate.FIND_ALL_EXTERNAL_IDS_TEMPLATE;
import static com.orange.flexoffice.dao.gatewayapi.repository.stat.jdbc.StatSqlTemplate.FIND_ALL_TEMPLATE;
import static com.orange.flexoffice.dao.gatewayapi.repository.stat.jdbc.StatSqlTemplate.FIND_BY_BEST_AVG_TEMPLATE;
import static com.orange.flexoffice.dao.gatewayapi.repository.stat.jdbc.StatSqlTemplate.FIND_BY_EXTERNAL_ID_TEMPLATE;
import static com.orange.flexoffice.dao.gatewayapi.repository.stat.jdbc.StatSqlTemplate.FIND_ONE_TEMPLATE;
import static com.orange.flexoffice.dao.gatewayapi.repository.stat.jdbc.StatSqlTemplate.SAVE_TEMPLATE;
import static com.orange.flexoffice.dao.gatewayapi.repository.stat.jdbc.StatSqlTemplate.UPDATE_TEMPLATE;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.orange.flexoffice.dao.gatewayapi.model.Stat;
import com.orange.flexoffice.dao.gatewayapi.repository.stat.StatOperations;
import com.orange.flexoffice.dao.gatewayapi.repository.support.StatExtractor;
import com.orange.flexoffice.dao.gatewayapi.repository.support.StreamingPreparedStatement;

public abstract class AbstractStatRepository implements StatOperations {
	
	private final String findOneQuery;
	private final String findAllQuery;
	private final String findByBestAvgQuery;
	private final String findByExternalIdQuery;
	private final String findAllExternalIdsQuery;
	private final String saveQuery;
	private final String updateQuery;
	private final String deleteQuery;
	private final String countQuery;
	
	private NamedParameterJdbcTemplate jdbcTemplate;
	
	public AbstractStatRepository() {
		findOneQuery = String.format(FIND_ONE_TEMPLATE, getTableName());
		findAllQuery = String.format(FIND_ALL_TEMPLATE, getTableName());
		findByExternalIdQuery = String.format(FIND_BY_EXTERNAL_ID_TEMPLATE, getTableName());
		findAllExternalIdsQuery = String.format(FIND_ALL_EXTERNAL_IDS_TEMPLATE, getTableName());
		findByBestAvgQuery = String.format(FIND_BY_BEST_AVG_TEMPLATE, getTableName());
		saveQuery = String.format(SAVE_TEMPLATE, getTableName());
		updateQuery = String.format(UPDATE_TEMPLATE, getTableName());
		deleteQuery = String.format(DELETE_TEMPLATE, getTableName());
		countQuery = String.format(COUNT_TEMPLATE, getTableName());
	}
	
	@Autowired
	public void setDataSource(DataSource dataSource) {
		jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
	}
	
	protected NamedParameterJdbcTemplate getNamedParameterJdbcTemplate() {
		return jdbcTemplate;
	}

	@Override
	public Stat findOne(Long id) {
		SqlParameterSource paramMap = new MapSqlParameterSource("id", id);
		
		Stat stat = null;
		try {
			stat = jdbcTemplate.queryForObject(
					findOneQuery, 
					paramMap, 
					new BeanPropertyRowMapper<Stat>(Stat.class)
				);
		} catch (EmptyResultDataAccessException erdae) {
			// If no data found just return null.
		}
		return stat;
	}

	@Override
	public Stat findByObjectId(String objectId) {
		SqlParameterSource paramMap = new MapSqlParameterSource("objectId", objectId);
		
		Stat stat = null;
		try {
			stat = getNamedParameterJdbcTemplate().queryForObject(
					findByExternalIdQuery, 
					paramMap, 
					new BeanPropertyRowMapper<Stat>(Stat.class)
				);
		} catch (EmptyResultDataAccessException erdae) {
			// If no data found just return null.
		}
		
		return stat;
	}

	@Override
	public List<Stat> findAll() {
		return jdbcTemplate.getJdbcOperations().query(
				findAllQuery, 
				new BeanPropertyRowMapper<Stat>(Stat.class)
			);
	}
	
	@Override
	public List<Stat> findByBestAvg(int nb) {
		SqlParameterSource paramMap = new MapSqlParameterSource("nb", nb);
		return jdbcTemplate.query(
				findByBestAvgQuery, 
				paramMap, 
				new BeanPropertyRowMapper<Stat>(Stat.class)
			);
	}

	@Override
	public List<String> findAllObjectIds() {
		return getNamedParameterJdbcTemplate().getJdbcOperations().query(
				findAllExternalIdsQuery, 
				new BeanPropertyRowMapper<String>(String.class)
			);
	}
	
	@Override
	public Stat save(Stat stat) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		
		SqlParameterSource paramBean = new BeanPropertySqlParameterSource(stat);
		jdbcTemplate.update(saveQuery, paramBean, keyHolder);
		
		// Retrieves generated id of saved data.
		Integer id = (Integer)keyHolder.getKeys().get("id");
		stat.setId(id.longValue());
		
		return stat;
	}

	@Override
	public Stat update(Stat stat) {
		SqlParameterSource paramBean = new BeanPropertySqlParameterSource(stat);
		jdbcTemplate.update(updateQuery, paramBean);
		
		return stat;
	}

	@Override
	public void delete(Long id) {
		SqlParameterSource paramMap = new MapSqlParameterSource("id", id);
		jdbcTemplate.update(deleteQuery, paramMap);
	}
	
	@Override
	public Long count() {
		return jdbcTemplate.getJdbcOperations().queryForLong(countQuery);
	}
	
	@Override
	public void forEach(final StatExtractor se) {
		jdbcTemplate.getJdbcOperations().query(new StreamingPreparedStatement(findAllQuery), new RowCallbackHandler() {
			
			@Override
			public void processRow(ResultSet rs) throws SQLException {
				Stat stat = new Stat();
				stat.setId(rs.getLong("id"));
				stat.setObjectId(rs.getString("object_id"));
				stat.setAverage(rs.getFloat("average"));
				stat.setCount(rs.getInt("count"));
				stat.setSum(rs.getFloat("sum"));
				
				se.extractStat(stat);
				
			}
		});
	}
	
	protected abstract String getTableName();

}
