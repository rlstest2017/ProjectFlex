package com.orange.flexoffice.dao.gatewayapi.repository.data.jdbc;

import static com.orange.flexoffice.dao.gatewayapi.repository.data.jdbc.metadata.CharacteristicMetadata.CHARACTERISTIC_DESC_ID_COL;
import static com.orange.flexoffice.dao.gatewayapi.repository.data.jdbc.metadata.CharacteristicMetadata.CHARACTERISTIC_ITEM_ID_COL;
import static com.orange.flexoffice.dao.gatewayapi.repository.data.jdbc.metadata.CharacteristicMetadata.CHARACTERISTIC_TABLE_NAME;
import static com.orange.flexoffice.dao.gatewayapi.repository.data.jdbc.metadata.CharacteristicMetadata.CHARACTERISTIC_WEIGHT_COL;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.orange.flexoffice.dao.gatewayapi.model.data.Characteristic;
import com.orange.flexoffice.dao.gatewayapi.model.data.Data;
import com.orange.flexoffice.dao.gatewayapi.repository.data.CharacteristicOperations;
import com.orange.flexoffice.dao.gatewayapi.repository.support.DataExtractor;
import com.orange.flexoffice.dao.gatewayapi.repository.support.StreamingPreparedStatement;


@Repository
public class CharacteristicRepository 
	extends DataRepository<Characteristic>
	implements CharacteristicOperations {
	
	public CharacteristicRepository() {
		super(Characteristic.class);
	}

	@Override
	protected String getTableName() {
		return CHARACTERISTIC_TABLE_NAME;
	}

	@Override
	protected String getColumnColName() {
		return CHARACTERISTIC_ITEM_ID_COL;
	}

	@Override
	protected String getRowColName() {
		return CHARACTERISTIC_DESC_ID_COL;
	}

	@Override
	protected String getRatingColName() {
		return CHARACTERISTIC_WEIGHT_COL;
	}

	@Override
	public Characteristic findByItemIdAndDescriptorId(String itemId,
			String descriptorId) {
		MapSqlParameterSource paramMap = new MapSqlParameterSource();
		paramMap.addValue("columnId", itemId);
		paramMap.addValue("rowId", descriptorId);
		
		Characteristic data = null;
		try {
			data = jdbcTemplate.queryForObject(
					findByColumnIdAndRowIdQuery, 
					paramMap, 
					new BeanPropertyRowMapper<Characteristic>(Characteristic.class)
				);
		} catch (EmptyResultDataAccessException erdae) {
			// If no data found just return null
		}
		return data;
	}

	@Override
	public List<Characteristic> findByItemId(String itemId) {
		SqlParameterSource paramMap = new MapSqlParameterSource("columnId", itemId);
		return jdbcTemplate.query(
				findByColumnIdQuery, 
				paramMap, 
				new BeanPropertyRowMapper<Characteristic>(Characteristic.class)
			);
	}

	@Override
	public List<Characteristic> findByDescriptorId(String descriptorId) {
		SqlParameterSource paramMap = new MapSqlParameterSource("rowId", descriptorId);
		return jdbcTemplate.query(
				findByRowIdQuery, 
				paramMap, 
				new BeanPropertyRowMapper<Characteristic>(Characteristic.class)
			);
	}
	
	@Override
	public void forEach(final DataExtractor dataExtractor) {
		jdbcTemplate.getJdbcOperations().query(new StreamingPreparedStatement(findAllQuery), new RowCallbackHandler() {
			
			@Override
			public void processRow(ResultSet rs) throws SQLException {
				Data data = new Characteristic();
				data.setColumnId(rs.getString(getColumnColName()));
				data.setRowId(rs.getString(getRowColName()));
				data.setComment(rs.getString("comment"));
				data.setTimestamp(rs.getDate("timestamp"));
				data.setRating(rs.getFloat(getRatingColName()));
				
				dataExtractor.extractData(data);
				
			}
		});		
	}

	@Override
	protected String getColumnMailName() {
		// TODO Auto-generated method stub
		return null;
	}

}
