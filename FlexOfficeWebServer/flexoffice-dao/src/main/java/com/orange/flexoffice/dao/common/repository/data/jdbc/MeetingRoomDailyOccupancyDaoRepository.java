package com.orange.flexoffice.dao.common.repository.data.jdbc;

import java.util.Date;
import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.orange.flexoffice.dao.common.model.data.MeetingRoomDailyOccupancyDao;
import com.orange.flexoffice.dao.common.model.object.MeetingRoomDailyOccupancyDto;
import com.orange.flexoffice.dao.common.model.object.MeetingRoomDailyTypeDto;
import com.orange.flexoffice.dao.common.repository.data.MeetingRoomDailyOccupancyDaoOperations;
import com.orange.flexoffice.dao.common.repository.data.jdbc.metadata.MeetingRoomDailyOccupancyDaoMetadata;

@Repository
public class MeetingRoomDailyOccupancyDaoRepository extends DataRepository<MeetingRoomDailyOccupancyDao> implements MeetingRoomDailyOccupancyDaoOperations {

	public MeetingRoomDailyOccupancyDaoRepository() {
		super(MeetingRoomDailyOccupancyDao.class);
	}
	
	@Override
	public List<MeetingRoomDailyOccupancyDao> findAllMeetingRoomsDailyOccupancy() {
		SqlParameterSource paramMap = new MapSqlParameterSource();
		return jdbcTemplate.query(
				findAllMeetingRoomDailyQuery, 
				paramMap, 
				new BeanPropertyRowMapper<MeetingRoomDailyOccupancyDao>(MeetingRoomDailyOccupancyDao.class)
			);
	}
	
	@Override
	public List<MeetingRoomDailyOccupancyDao> findRequestedMeetingRoomsDailyOccupancy(MeetingRoomDailyOccupancyDto data) {
		SqlParameterSource paramBean = new BeanPropertySqlParameterSource(data);
		return jdbcTemplate.query(
				findRequestedDailyQueryMeetingRoom, 
				paramBean, 
				new BeanPropertyRowMapper<MeetingRoomDailyOccupancyDao>(MeetingRoomDailyOccupancyDao.class)
			);
	}

	@Override
	public List<MeetingRoomDailyTypeDto> findMeetingRoomsDailyAndType(MeetingRoomDailyOccupancyDto data) {
		SqlParameterSource paramBean = new BeanPropertySqlParameterSource(data);
		return jdbcTemplate.query(
				findRequestedDailyAndTypesQueryMeetingRoom, 
				paramBean, 
				new BeanPropertyRowMapper<MeetingRoomDailyTypeDto>(MeetingRoomDailyTypeDto.class)
			);
	}
	
	@Override
	public MeetingRoomDailyOccupancyDao saveMeetingRoomDaily(MeetingRoomDailyOccupancyDao data) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		SqlParameterSource paramBean = new BeanPropertySqlParameterSource(data);
		jdbcTemplate.update(saveMeetingRoomDailyQuery, paramBean, keyHolder);
		// Retrieves generated id of saved data.
		Integer id = (Integer)keyHolder.getKeys().get("id");
		data.setId(id.longValue());
		return data;
	}

	@Override
	public void deleteByDay(Date day) {
		SqlParameterSource paramMap = new MapSqlParameterSource("day", day);
		jdbcTemplate.update(deleteByDayQueryMeetingRoom, paramMap);
	}
	
	@Override
	public void deleteByMeetingRoomId(Long meetingRoomId) {
		SqlParameterSource paramMap = new MapSqlParameterSource("meetingroomId", meetingRoomId);
		jdbcTemplate.update(deleteByMeetingRoomId, paramMap);
	}
	
	@Override
	protected String getTableName() {
		return MeetingRoomDailyOccupancyDaoMetadata.MEETINGROOM_DAILY_OCCUPANCY_TABLE_NAME;
	}

	@Override
	protected String getColumnColName() {
		return MeetingRoomDailyOccupancyDaoMetadata.MEETINGROOM_DAILY_OCCUPANCY_MEETINGROOM_ID_COL;
		
	}

	@Override
	protected String getColName() {
		return null;
	}


}
