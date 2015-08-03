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

import java.util.Properties;

/**
 * Base class for JDBC settings
 * @author Leandro Oliveira (leandro@agapsys.com)
 */
public abstract class PoolSettings {
	// CLASS SCOPE =============================================================
	public static final String KEY_DRIVER_CLASS         = "com.agapsys.jdbc.driver";
	public static final String KEY_URI                  = "com.agapsys.jdbc.uri";
	public static final String KEY_USERNAME             = "com.agapsys.jdbc.username";
	public static final String KEY_PASSWORD             = "com.agapsys.jdbc.password";
	public static final String KEY_MAX_CONNECTIONS      = "com.agapsys.jdbc.poolMaxConnections";
	public static final String KEY_MAX_IDLE_CONNECTIONS = "com.agapsys.jdbc.poolMaxIdleConnections";
	public static final String KEY_MAX_WAIT_MILLIS      = "com.agapsys.jdbc.poolMaxWaitMillis";	
	
	public static final int DEFAULT_MAX_CONNECTIONS       = 10;
	public static final int DEFAULT_MAX_IDDLE_CONNECTIONS = 5;
	public static final int DEFAULT_MAX_WAIT_MILLLIS      = 5000;
	
	// INSTANCE SCOPE ==========================================================
	private final Properties properties;
	
	public PoolSettings() {
		this(new Properties());
	}
	
	public PoolSettings(Properties properties) {
		if (properties == null)
			throw new IllegalArgumentException("properties == null");
		
		this.properties = properties;
	}

	public Properties getProperties() {
		return properties;
	}
	
	private void setProperty(String key, Object value) throws IllegalArgumentException {
		if (key == null)
			throw new IllegalArgumentException("key == null");
		if (value == null) {
			properties.remove(key);
		} else {
			properties.setProperty(key, value.toString());
		}
	}
	
	/** Returns the JDBC driver class name. */
	public String getDriverClass() {
		return properties.getProperty(KEY_DRIVER_CLASS, null);
	}	
	/** Sets the JDBC driver class name. */
	public void setDriverClass(String drvClass) {
		setProperty(KEY_DRIVER_CLASS, drvClass);
	}

	/** Returns JDBC connection URI. */
	public String getUri() {
		return properties.getProperty(KEY_URI, null);
	}
	/** Sets JDBC connection URI. */
	public void setUri(String uri) {
		setProperty(KEY_URI, uri);
	}
		
	/** Returns the username to be used by JDBC connections. */
	public String getUsername() {
		return properties.getProperty(KEY_USERNAME, null);
	}
	/** Sets the username to be used by JDBC connections. */
	public void setUsername(String username) {
		setProperty(KEY_USERNAME, username);
	}
	
	/** Returns the password used by JDBC connections. */
	public String getPassword() {
		return properties.getProperty(KEY_PASSWORD, null);
	}
	/** Sets the password used by JDBC connections. */
	public void setPassword(String password) {
		setProperty(KEY_PASSWORD, password);
	}
	
	/** 
	 * Returns the maximum number of active connections in the connection pool. 
	 * If this setting is not defined, return {@link AbstractDbSettings#DEFAULT_MAX_CONNECTIONS}
	 */
	public int getMaxConnections() {
		return Integer.parseInt(properties.getProperty(KEY_MAX_CONNECTIONS, "" + DEFAULT_MAX_CONNECTIONS));
	}
	/** Sets the maximum number of active connections in the connection pool. */
	public void setMaxConnections(int connections) {
		setProperty(KEY_MAX_CONNECTIONS, connections);
	}
	
	/** 
	 * Returns the maximum number of idle connections in the connection pool. 
	 * If this setting is not defined, return {@linkplain AbstractDbSettings#DEFAULT_MAX_IDDLE}
	 */
	public int getMaxIdleConnections() {
		return Integer.parseInt(properties.getProperty(KEY_MAX_IDLE_CONNECTIONS, "" + DEFAULT_MAX_IDDLE_CONNECTIONS));
	}
	/** Returns the maximum number of idle connections in the connection pool. */
	public void setMaxIdleConnections(int connections) {
		setProperty(KEY_MAX_IDLE_CONNECTIONS, connections);
	}
	
	/** 
	 * Returns the maximum waiting time (in milliseconds) in order to obtain an JDBC connection in the connection pool before an error is raised. 
	 * If this setting is not defined, return {@linkplain AbstractDbSettings#DEFAULT_MAX_WAIT_MILLLIS}
	 */
	public int getMaxWaitMillis() {
		return Integer.parseInt(properties.getProperty(KEY_MAX_WAIT_MILLIS, "" + DEFAULT_MAX_WAIT_MILLLIS));
	}
	/** sets the maximum waiting time (in milliseconds) in order to obtain an JDBC connection in the connection pool before an error is raised. */
	public void setMaxWaitMillis(int waitMillis) {
		setProperty(KEY_MAX_WAIT_MILLIS, waitMillis);
	}
}