package com.orange.flexoffice.dao.common.repository.data.jdbc;

import java.util.Date;
import java.util.List;

import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.orange.flexoffice.dao.common.model.data.MeetingRoomStatDao;
import com.orange.flexoffice.dao.common.repository.data.MeetingRoomStatDaoOperations;
import com.orange.flexoffice.dao.common.repository.data.jdbc.metadata.MeetingRoomStatDaoMetadata;

/*---------------------------------------------------------------------------------------
Manage meetingroom_stats table

Si réservation via agent/dashboard alors le champ reservation_date=now() & meetingroom_info=OCCUPIED
Si annulation via agent/dashboard alors meetingroom_info=CANCELED et les autres champs restent vides !!!

------------------------------------------------------------------------------------------*/

@Repository
public class MeetingRoomStatDaoRepository extends DataRepository<MeetingRoomStatDao> implements MeetingRoomStatDaoOperations {

	public MeetingRoomStatDaoRepository() {
		super(MeetingRoomStatDao.class);
	}
	
	
	@Override
	public List<MeetingRoomStatDao> findAllMeetingRoomStats() {
		SqlParameterSource paramMap = new MapSqlParameterSource();
		return jdbcTemplate.query(
				findAllQuery, 
				paramMap, 
				new BeanPropertyRowMapper<MeetingRoomStatDao>(MeetingRoomStatDao.class)
			);
	}
	
	@Override
	public List<MeetingRoomStatDao> findAllOccupiedDailyMeetingRoomStats(MeetingRoomStatDao data) {
		SqlParameterSource paramBean = new BeanPropertySqlParameterSource(data);
		return jdbcTemplate.query(
				findAllDailyQueryMeetingRoom, 
				paramBean, 
				new BeanPropertyRowMapper<MeetingRoomStatDao>(MeetingRoomStatDao.class)
			);
	}
	
	@Override
	public MeetingRoomStatDao findbyMeetingRoomId(MeetingRoomStatDao data) throws IncorrectResultSizeDataAccessException {
		SqlParameterSource paramBean = new BeanPropertySqlParameterSource(data);
		return jdbcTemplate.queryForObject(
				findMeetingRoomStatByMeetingRoomIdQuery, 
				paramBean, 
				new BeanPropertyRowMapper<MeetingRoomStatDao>(MeetingRoomStatDao.class)
			);
	}
	
	@Override
	public List<MeetingRoomStatDao> findbyMeetingRoomInfo(MeetingRoomStatDao data) throws IncorrectResultSizeDataAccessException {
		SqlParameterSource paramMap = new MapSqlParameterSource("meetingRoomInfo", data.getMeetingRoomInfo());
		return jdbcTemplate.query(
				findMeetingRoomStatByMeetingRoomInfoQuery, 
				paramMap, 
				new BeanPropertyRowMapper<MeetingRoomStatDao>(MeetingRoomStatDao.class)
			);
	}
	
	@Override
	public MeetingRoomStatDao saveOccupiedMeetingRoomStat(MeetingRoomStatDao data) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		SqlParameterSource paramBean = new BeanPropertySqlParameterSource(data);
		jdbcTemplate.update(saveOccupiedMeetingRoomStatQuery, paramBean, keyHolder);
		// Retrieves generated id of saved data.
		Integer id = (Integer)keyHolder.getKeys().get("id");
		data.setId(id.longValue());
		return data;
	}

	@Override
	public MeetingRoomStatDao updateMeetingRoomStatById(MeetingRoomStatDao data) {
	KeyHolder keyHolder = new GeneratedKeyHolder();
		SqlParameterSource paramBean = new BeanPropertySqlParameterSource(data);
		jdbcTemplate.update(updateMeetingRoomStatByIdQuery, paramBean, keyHolder);
		// Retrieves generated id of saved data.
		Integer id = (Integer)keyHolder.getKeys().get("id");
		data.setId(id.longValue());
		return data;	
	}
	
	@Override
	public MeetingRoomStatDao updateEndOccupancyDate(MeetingRoomStatDao data) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		SqlParameterSource paramBean = new BeanPropertySqlParameterSource(data);
		jdbcTemplate.update(updateUnOccupiedMeetingRoomStatQuery, paramBean, keyHolder);
		// Retrieves generated id of saved data.
		Integer id = (Integer)keyHolder.getKeys().get("id");
		data.setId(id.longValue());
		return data;	
	}
	
	@Override
	public void deleteByBeginOccupancyDate(Date date) {
		SqlParameterSource paramMap = new MapSqlParameterSource("date", date);
		jdbcTemplate.update(deleteByBeginOccupancyDateQueryMeetingRoom, paramMap);
	}
	
	@Override
	public void deleteByMeetingRoomId(Long meetingRoomId) {
		SqlParameterSource paramMap = new MapSqlParameterSource("meetingroomId", meetingRoomId);
		jdbcTemplate.update(deleteByMeetingRoomId, paramMap);
	}
	
	@Override
	protected String getTableName() {
		return MeetingRoomStatDaoMetadata.MEETINGROOM_STAT_TABLE_NAME;
	}

	@Override
	protected String getColumnColName() {
		return MeetingRoomStatDaoMetadata.MEETINGROOM_STAT_ID_COL;
	}

	@Override
	protected String getColName() {
		return MeetingRoomStatDaoMetadata.MEETINGROOM_STAT_ID_COL;
	}
	
}
