package com.orange.flexoffice.dao.gatewayapi.repository.support;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.springframework.jdbc.core.PreparedStatementCreator;


public class StreamingPreparedStatement implements PreparedStatementCreator {
	
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
			
		}
		return ps;
	}
}
