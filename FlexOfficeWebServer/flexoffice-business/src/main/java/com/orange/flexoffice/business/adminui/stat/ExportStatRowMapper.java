package com.orange.flexoffice.business.adminui.stat;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.orange.flexoffice.dao.common.model.object.ExportStatDto;

/**
 * ExportStatRowMapper
 * @author oab
 *
 */
public class ExportStatRowMapper implements RowMapper<ExportStatDto> {

	@Override
	public ExportStatDto mapRow(ResultSet rs, int rowNum) throws SQLException {

		ExportStatDto result = new ExportStatDto();
		result.setCountryName(rs.getString("country_name"));
		result.setRegionName(rs.getString("region_name"));
		result.setCityName(rs.getString("city_name"));
		result.setBuildingName(rs.getString("building_name"));
		result.setRoomName(rs.getString("room_name"));
		result.setRoomFloor(rs.getInt("room_floor"));
		result.setRoomType(rs.getString("room_type"));
		result.setBeginOccupancyDate(rs.getTimestamp("begin_occupancy_date"));
		result.setEndOccupancyDate(rs.getTimestamp("end_occupancy_date"));
			
		return result;
	} 

}
