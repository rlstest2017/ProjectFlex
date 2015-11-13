package com.orange.flexoffice.dao.common.repository.data.jdbc;

public final class DataSqlTemplate {
	
	public static final String FIND_ONE_TEMPLATE = 
			"select * from %s where id=:id";
	public static final String FIND_BY_COL_ID_TEMPLATE = 
			"select * from %s where %s=:columnId";
	public static final String FIND_BY_COL_GATEWAY_ID_TEMPLATE = 
			"select * from %s where gateway_id=:gatewayId";
	public static final String FIND_BY_COL_MAIL_TEMPLATE = 
			"select * from %s where email=:email";
	public static final String FIND_BY_COL_ROOM_ID_TEMPLATE = 
			"select * from %s where room_id=:roomId";
	public static final String FIND_ALL_TEMPLATE = 
			"select * from %s";
	public static final String REMOVE_TEMPLATE = 
			"delete from %s where id=:id";
	public static final String COUNT_TEMPLATE =
			"select count(id) from %s";
	public static final String CREATE_USER_TEMPLATE = 
			"insert into %s (first_name, last_name, email) values (:firstName, :lastName, :email)";
	public static final String UPDATE_USER_TEMPLATE =
			"update %s set first_name=:firstName, last_name=:lastName, email=:email WHERE %s=:id";
	public static final String UPDATE_GATEWAY_STATUS_TEMPLATE =
			"update %s set status=CAST(:status AS gatewayStatus) where id=:id";
	public static final String FIND_ALL_COL_IDS_WITH_ROW_ID_CONDITIONS_TEMPLATE = 
			"select %s from %s where %s in (:rowIds)";
	
	private DataSqlTemplate() {}

}
