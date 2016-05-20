package com.orange.flexoffice.dao.common.repository.data.jdbc;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.orange.flexoffice.dao.common.model.data.DashboardDao;
import com.orange.flexoffice.dao.common.model.data.MeetingRoomGroupsConfigurationDao;
import com.orange.flexoffice.dao.common.repository.data.MeetingRoomGroupsConfigurationDaoOperations;
import com.orange.flexoffice.dao.common.repository.data.jdbc.metadata.MeetingRoomGroupsConfigurationDaoMetadata;

/*---------------------------------------------------------------------------------------
Manage meetingroom_groups_configuration table
---------------------------------------------------------------------------------------*/

@Repository
public class MeetingRoomGroupsConfigurationDaoRepository extends DataRepository<MeetingRoomGroupsConfigurationDao> implements MeetingRoomGroupsConfigurationDaoOperations {
	
	public MeetingRoomGroupsConfigurationDaoRepository() {
		super(MeetingRoomGroupsConfigurationDao.class);
	}
	
	@Override
	public List<MeetingRoomGroupsConfigurationDao> findAllMeetingRoomGroupsConfigurations() {
		SqlParameterSource paramMap = new MapSqlParameterSource();
		return jdbcTemplate.query(
				findAllMeetingRoomGroupsConfigurationQuery, 
				paramMap, 
				new BeanPropertyRowMapper<MeetingRoomGroupsConfigurationDao>(MeetingRoomGroupsConfigurationDao.class)
			);
	}

	@Override
	public List<MeetingRoomGroupsConfigurationDao> findByBuildingId(Long buildingId) {
		SqlParameterSource paramMap = new MapSqlParameterSource("buildingId", buildingId);
		return jdbcTemplate.query(
				findByColumnBuildingIdQuery, 
				paramMap, 
				new BeanPropertyRowMapper<MeetingRoomGroupsConfigurationDao>(MeetingRoomGroupsConfigurationDao.class)
			);
	}
	
	@Override
	public MeetingRoomGroupsConfigurationDao findByBuildingIdAndFloor(DashboardDao data) {
		SqlParameterSource paramBean = new BeanPropertySqlParameterSource(data);
		return jdbcTemplate.queryForObject(
				findByColumnBuildingIdAndFloorQuery, 
				paramBean, 
				new BeanPropertyRowMapper<MeetingRoomGroupsConfigurationDao>(MeetingRoomGroupsConfigurationDao.class)
			);
	}
	
	@Override
	public MeetingRoomGroupsConfigurationDao saveMeetingRoomGroupsConfiguration(MeetingRoomGroupsConfigurationDao data) throws DataIntegrityViolationException {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		
		SqlParameterSource paramBean = new BeanPropertySqlParameterSource(data);
		jdbcTemplate.update(saveMeetingRoomGroupsConfigurationQuery, paramBean, keyHolder);
		
		// Retrieves generated id of saved data.
		Integer id = (Integer)keyHolder.getKeys().get("id");
		data.setId(id.longValue());
		
		return data;
	}
	
	@Override
	public MeetingRoomGroupsConfigurationDao updateMeetingRoomGroupsConfiguration(MeetingRoomGroupsConfigurationDao data) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		
		SqlParameterSource paramBean = new BeanPropertySqlParameterSource(data);
		jdbcTemplate.update(updateMeetingRoomGroupsConfigurationQuery, paramBean, keyHolder);
		
		// Retrieves generated id of saved data.
		Integer id = (Integer)keyHolder.getKeys().get("id");
		data.setId(id.longValue());
		
		return data;
	}
	
	@Override
	public void deleteByBuildingId(Long buildingId) {
		SqlParameterSource paramMap = new MapSqlParameterSource("buildingId", buildingId);
		jdbcTemplate.update(deleteByBuildingIdQuery, paramMap);
	}
	
	@Override
	public void deleteByBuildingIdAndFloor(MeetingRoomGroupsConfigurationDao data) {
		SqlParameterSource paramBean = new BeanPropertySqlParameterSource(data);
		jdbcTemplate.update(deleteByBuildingIdAndFloorQuery, paramBean);
	}
	
	@Override
	protected String getTableName() {
		return MeetingRoomGroupsConfigurationDaoMetadata.MEETINGROOM_GROUPS_CONFIGURATION_TABLE_NAME;
	}

	@Override
	protected String getColumnColName() {
		return MeetingRoomGroupsConfigurationDaoMetadata.MEETINGROOM_GROUPS_CONFIGURATION_ID_COL;
	}

	@Override
	protected String getColName() {
		// TODO Auto-generated method stub
		return null;
	}

}
