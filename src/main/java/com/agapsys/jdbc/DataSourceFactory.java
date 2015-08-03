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

import javax.sql.DataSource;
import org.apache.commons.dbcp2.PoolableConnection;
import org.apache.commons.dbcp2.PoolableConnectionFactory;
import org.apache.commons.dbcp2.PoolingDataSource;
import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

/**
 * JDBC connection pool factory.
 * @author Leandro Oliveira (leandro@agapsys.com)
 */
public class DataSourceFactory {
	// CLASS SCOPE =============================================================
    /**
	 * Returns a data source which uses a pool of connections.
	 * @param connectionFactory factory used to create connections
	 * @param maxConnections maximum number of connections in the pool
	 * @param maxIdleConnections maximum number of idle connections in the pool
	 * @param maxWaitMillis timeout (in milliseconds) in oder to obtain a connection from the pool before an error is raised
	 * @throws IllegalArgumentException if connectionFactory == null
	 */
	public static DataSource getDataSource(
		ConnectionFactory connectionFactory,
		int maxConnections, 
		int maxIdleConnections, 
		long maxWaitMillis
	) throws IllegalArgumentException { 
		if (connectionFactory == null)
			throw new IllegalArgumentException("Null connection factory");
		
		PoolableConnectionFactory poolableConnectionFactory = new PoolableConnectionFactory(connectionFactory, null);
		GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
			poolConfig.setMaxIdle(maxIdleConnections);
			poolConfig.setMaxWaitMillis(maxWaitMillis);
			poolConfig.setBlockWhenExhausted(true);
			poolConfig.setMaxTotal(maxIdleConnections);	
		ObjectPool<PoolableConnection> connectionPool = new GenericObjectPool<>(poolableConnectionFactory, poolConfig);
		poolableConnectionFactory.setPool(connectionPool);
		
        PoolingDataSource<PoolableConnection> dataSource = new PoolingDataSource<>(connectionPool);
        return dataSource;
	}
	
	/**
	 * Returns a pool of JDBC connections.
	 * @param driverClassName JDBC driver class name
	 * @param uri connection URI
	 * @param username connection username
	 * @param password connection password
	 * @param maxConnections maximum number of connections in the pool
	 * @param maxIdleConnections maximum number of idle connections in the pool
	 * @param maxWaitMillis timeout (in milliseconds) in oder to obtain a connection from the pool before an error is raised
	 * @throws ClassNotFoundException if a class with given class name was not found
	 */
	public static DataSource getDataSource(
		String driverClassName, 
		String uri, 
		String username, 
		String password,
		int maxConnections,
		int  maxIdleConnections,
		long maxWaitMillis
	) throws ClassNotFoundException {
		
		return getDataSource(
			new ConnectionFactory(driverClassName, uri, username, password), 
			maxConnections,
			maxIdleConnections, 
			maxWaitMillis
		);
    }
	
	public static DataSource getDataSource(PoolSettings dbSettings) throws ClassNotFoundException {
		return getDataSource(
			dbSettings.getDriverClass(), 
			dbSettings.getUri(),
			dbSettings.getUsername(),
			dbSettings.getPassword(), 
			dbSettings.getMaxConnections(), 
			dbSettings.getMaxIdleConnections(), 
			dbSettings.getMaxWaitMillis()
		);
	}
	
	// INSTANCE SCOPE ==========================================================
	private DataSourceFactory() {} // <-- Instances cannot be created outside class scope
}