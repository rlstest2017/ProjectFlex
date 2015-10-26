package com.orange.flexoffice.dao.gatewayapi.repository.similarity;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.orange.flexoffice.dao.gatewayapi.model.Similarity;

public abstract class AbstractSimilarityRepository implements SimilarityRepository {

	private static final String SAVE_TEMPLATE = 
			"insert into %s (object_id, similar_object_id, value) values (:objectId, :similarObjectId, :value)";
	private static final String UPDATE_TEMPLATE =
			"update %s set value=:value where id=:id";
	private static final String DELETE_TEMPLATE =
			"delete from %s where id=:id";
	private static final String FIND_ONE_TEMPLATE =
			"select * from %s where id=:id";
	private static final String FIND_BY_OBJECT_ID_TEMPLATE =
			"select * from %s where object_id=:objectId";
	private static final String FIND_ALL_TEMPLATE =
			"select * from %s";
	private static final String FIND_ALL_SORTED_TEMPLATE =
			"select * from %s order by value desc";
	private static final String COUNT_TEMPLATE =
			"select count(*) from %s";
	
	private String saveQuery;
	private String updateQuery;
	private String deleteQuery;
	private String findOneQuery;
	private String findByObjectIdQuery;
	private String findAllQuery;
	private String findAllSortedQuery;
	private String countQuery;
	
	private NamedParameterJdbcTemplate jdbcTemplate;
	
	public AbstractSimilarityRepository(String tableName) {
		saveQuery = String.format(SAVE_TEMPLATE, tableName);
		updateQuery = String.format(UPDATE_TEMPLATE, tableName);
		deleteQuery = String.format(DELETE_TEMPLATE, tableName);
		findOneQuery = String.format(FIND_ONE_TEMPLATE, tableName);
		findByObjectIdQuery = String.format(FIND_BY_OBJECT_ID_TEMPLATE, tableName);
		findAllQuery = String.format(FIND_ALL_TEMPLATE, tableName);
		findAllSortedQuery = String.format(FIND_ALL_SORTED_TEMPLATE, tableName);
		countQuery = String.format(COUNT_TEMPLATE, tableName);
	}
	
	@Autowired
	public void setDataSource(DataSource dataSource) {
		jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
	}
	
	@Override
	public Similarity save(Similarity similarity) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		SqlParameterSource paramBean = new BeanPropertySqlParameterSource(similarity);
		jdbcTemplate.update(saveQuery, paramBean, keyHolder);
		
		// Retrieves generated id of saved data.
		Integer id = (Integer)keyHolder.getKeys().get("id");
		similarity.setId(id.longValue());
		
		return similarity;
	}

	@Override
	public Similarity update(Similarity similarity) {
		SqlParameterSource paramBean = new BeanPropertySqlParameterSource(similarity);
		jdbcTemplate.update(updateQuery, paramBean);
		return similarity;
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
	public Similarity findOne(Long id) {
		SqlParameterSource paramMap = new MapSqlParameterSource("id", id);
		return jdbcTemplate.queryForObject(findOneQuery, paramMap, new BeanPropertyRowMapper<Similarity>(Similarity.class));
	}
	
	@Override
	public Similarity findByObjectId(String objectId) {
		SqlParameterSource paramMap = new MapSqlParameterSource("objectId", objectId);
		return jdbcTemplate.queryForObject(findByObjectIdQuery, paramMap,  new BeanPropertyRowMapper<Similarity>(Similarity.class));
	}

	@Override
	public List<Similarity> findAll() {
		return jdbcTemplate.getJdbcOperations()
				.query(findAllQuery, new BeanPropertyRowMapper<Similarity>(Similarity.class));
	}

	@Override
	public List<Similarity> findAllSorted() {
		return jdbcTemplate.getJdbcOperations()
				.query(findAllSortedQuery, new BeanPropertyRowMapper<Similarity>(Similarity.class));
	}

}
