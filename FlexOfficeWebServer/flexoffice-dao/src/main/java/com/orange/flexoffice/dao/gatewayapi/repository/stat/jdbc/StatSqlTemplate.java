package com.orange.flexoffice.dao.gatewayapi.repository.stat.jdbc;

/**
 * Defines SQL templates to manage statistics.
 */
public final class StatSqlTemplate {
	
	public static final String FIND_ONE_TEMPLATE = "select * from %s where id=:id";
	
	public static final String FIND_BY_EXTERNAL_ID_TEMPLATE = "select * from %s where object_id=:objectId";
	
	public static final String FIND_ALL_TEMPLATE = "select * from %s";
	
	public static final String FIND_ALL_EXTERNAL_IDS_TEMPLATE = "select object_id from %s";
	
	public static final String FIND_BY_BEST_AVG_TEMPLATE = "select * from %s where count >= 20 order by average desc limit :nb";
	
	public static final String SAVE_TEMPLATE =
			"insert into %s (object_id, average, sum, count) values (:objectId, :average, :sum, :count)";
	
	public static final String UPDATE_TEMPLATE  =
			"update %s set average=:average, sum=:sum, count=:count where object_id=:objectId";
	
	public static final String DELETE_TEMPLATE =
			"delete from %s where id=:id";
	
	public static final String COUNT_TEMPLATE =
			"select count(id) from %s";
	
	private StatSqlTemplate() {}

}
