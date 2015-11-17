package com.orange.flexoffice.dao.common.repository.support;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.PreparedStatementCreator;

/**
 * StreamingPreparedStatement
 * @author oab
 *
 */
public class StreamingPreparedStatement implements PreparedStatementCreator {
	
	private static final Logger LOGGER = Logger.getLogger(StreamingPreparedStatement.class);
	private String query;
	
	public StreamingPreparedStatement(String query) {
		this.query = query;
	}
	
	@Override
	public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
		PreparedStatement ps = con.prepareStatement(query);
		try{
			con.setAutoCommit(false);
			ps.setFetchSize(5000);
		}
		catch (SQLException e) {
			LOGGER.debug("SQLException in createPreparedStatement of StreamingPreparedStatement class, as :" + e.getMessage());
		}
		return ps;
	}
}
