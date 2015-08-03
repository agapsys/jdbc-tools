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
import java.sql.Driver;
import java.sql.SQLException;
import java.util.Properties;

/**
 * JDBC Runtime connection factory.
 * All connections will be established using a JDBC driver loaded at runtime
 * @author Leandro Oliveira (leandro@agapsys.com)
 */
public final class ConnectionFactory implements org.apache.commons.dbcp2.ConnectionFactory {
	private final Driver     driver;
	private final Properties connectionProperties;
	private final String     uri;
	
	public ConnectionFactory(String className, String uri, String username, String password) throws IllegalArgumentException {
		if (className == null || className.isEmpty())
			throw new IllegalArgumentException("Null/Empty className");
		
		try {
			Class<?> drvClass = Class.forName(className);
			driver = (Driver) drvClass.newInstance();
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
		
		connectionProperties = new Properties();
		if (username != null)
			connectionProperties.put("user", username);
		
		if (password != null)
			connectionProperties.put("password", password);
		
		if (uri == null || uri.isEmpty())
			throw new IllegalArgumentException("Null/Empty uri");
			
		this.uri = uri;
	}
	
	@Override
	public Connection createConnection() throws SQLException {
		return driver.connect(uri, connectionProperties);
	}
}
