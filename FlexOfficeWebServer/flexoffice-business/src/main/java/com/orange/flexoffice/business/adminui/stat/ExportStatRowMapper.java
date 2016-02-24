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
		result.setRoomName(rs.getString("room_name"));
		result.setRoomType(rs.getString("room_type"));
		result.setBeginOccupancyDate(rs.getDate("begin_occupancy_date"));
		result.setEndOccupancyDate(rs.getDate("end_occupancy_date"));
			
		return result;
	} 

}
