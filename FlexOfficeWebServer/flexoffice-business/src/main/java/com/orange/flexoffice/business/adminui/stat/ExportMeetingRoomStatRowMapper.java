package com.orange.flexoffice.business.adminui.stat;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.orange.flexoffice.dao.common.model.object.ExportMeetingRoomStatDto;

/**
 * ExportMeetingRoomStatRowMapper
 * @author oab
 *
 */
public class ExportMeetingRoomStatRowMapper implements RowMapper<ExportMeetingRoomStatDto> {

	@Override
	public ExportMeetingRoomStatDto mapRow(ResultSet rs, int rowNum) throws SQLException {

		ExportMeetingRoomStatDto result = new ExportMeetingRoomStatDto();
		result.setCountryName(rs.getString("country_name"));
		result.setRegionName(rs.getString("region_name"));
		result.setCityName(rs.getString("city_name"));
		result.setBuildingName(rs.getString("building_name"));
		result.setMeetingRoomName(rs.getString("meeting_room_name"));
		result.setMeetingRoomFloor(rs.getInt("meeting_room_floor"));
		result.setMeetingRoomType(rs.getString("meeting_room_type"));
		result.setBeginOccupancyDate(rs.getTimestamp("begin_occupancy_date"));
		result.setEndOccupancyDate(rs.getTimestamp("end_occupancy_date"));
			
		return result;
	} 

}
