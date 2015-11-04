package com.orange.flexoffice.dao.userui.repository.data.jdbc;

public final class DataSqlTemplate {
	
	public static final String FIND_ONE_TEMPLATE = 
			"select * from %s where id=:id";
	public static final String FIND_BY_COL_ID_TEMPLATE = 
			"select * from %s where %s=:columnId";
	public static final String FIND_BY_COL_MAIL_TEMPLATE = 
			"select * from %s where %s=:columnEmail";
	public static final String FIND_ALL_TEMPLATE = 
			"select * from %s";
	public static final String REMOVE_TEMPLATE = 
			"delete from %s where id=:id";
	public static final String COUNT_TEMPLATE =
			"select count(id) from %s";
	public static final String CREATE_USER_TEMPLATE = 
			"insert into %s (%s, %s, %s) values (:firstName, :lastName, :email)";
	public static final String UPDATE_USER_TEMPLATE =
			"update %s set %s=:firstName, %s=:lastName, %s=:email WHERE %s=:id";
	
	
	
	
	public static final String FIND_BY_COL_ID_AND_ROW_ID_TEMPLATE = 
			"select * from %s where %s=:columnId and %s=:rowId";
	public static final String FIND_BY_ROW_ID_TEMPLATE = 
			"select * from %s where %s=:rowId";
	public static final String FIND_ALL_COL_IDS_WITH_ROW_ID_CONDITIONS_TEMPLATE = 
			"select %s from %s where %s in (:rowIds)";
	public static final String SAVE_TEMPLATE = 
			"insert into %s (%s, %s, %s, comment) values (:columnId, :rowId, :rating, :comment)";
	public static final String UPDATE_TEMPLATE =
			"update %s set %s=:rating, comment=:comment WHERE %s=:columnId AND %s=:rowId";
	
	
	private DataSqlTemplate() {}

}
