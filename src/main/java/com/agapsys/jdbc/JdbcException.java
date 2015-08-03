/*
 * Copyright 2015 Agapsys Tecnologia Ltda-ME.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.agapsys.jdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * SQLException wrapper
 * @author Leandro Oliveira (leandro@agapsys.com)
 */
public class JdbcException extends RuntimeException {
	private void closeConnection (Connection connection) throws IllegalArgumentException {
		if (connection == null)
			throw new IllegalArgumentException("connection == null");
		
		try {
			if (!connection.getAutoCommit()) {
				connection.rollback();
				connection.setAutoCommit(true);
			}

			connection.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	public JdbcException(String errorMsg, Connection connection) throws IllegalArgumentException {
		super(errorMsg);
		closeConnection(connection);
	}
	
	public JdbcException(SQLException cause, Connection connection) throws IllegalArgumentException{
		super(cause);
		closeConnection(connection);
	}
	
	public JdbcException(SQLException cause, ResultSet rs) throws IllegalArgumentException {
		super(cause);
		if (rs == null)
			throw new IllegalArgumentException("rs == null");
		
		try {
			closeConnection(rs.getStatement().getConnection());
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}
