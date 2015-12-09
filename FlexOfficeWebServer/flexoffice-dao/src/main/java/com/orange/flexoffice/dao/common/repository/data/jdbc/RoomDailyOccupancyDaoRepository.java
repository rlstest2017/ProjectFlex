package com.orange.flexoffice.dao.common.repository.data.jdbc;

import org.springframework.stereotype.Repository;

import com.orange.flexoffice.dao.common.model.data.RoomDailyOccupancyDao;
import com.orange.flexoffice.dao.common.repository.data.RoomDailyOccupancyDaoOperations;
import com.orange.flexoffice.dao.common.repository.data.jdbc.metadata.RoomDailyOccupancyDaoMetadata;
import com.orange.flexoffice.dao.common.repository.support.DataExtractor;

@Repository
public class RoomDailyOccupancyDaoRepository extends DataRepository<RoomDailyOccupancyDao> implements RoomDailyOccupancyDaoOperations {

	public RoomDailyOccupancyDaoRepository() {
		super(RoomDailyOccupancyDao.class);
	}
	
	
		
	@Override
	protected String getTableName() {
		return RoomDailyOccupancyDaoMetadata.ROOM_DAILY_OCCUPANCY_TABLE_NAME;
	}

	@Override
	protected String getColumnColName() {
		return RoomDailyOccupancyDaoMetadata.ROOM_DAILY_OCCUPANCY_ROOM_ID_COL;
		
	}

	@Override
	public void forEach(DataExtractor dataExtractor) {
	}

	@Override
	protected String getRowColName() {
		return null;
	}

}
