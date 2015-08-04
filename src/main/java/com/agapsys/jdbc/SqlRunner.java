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

import com.agapsys.jdbc.util.ResourceLoader;
import com.agapsys.jdbc.util.ResourceLoader.ResourceNotFoundException;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * SQL script runner.
 * SQL statements shall be delimited by semicolons
 * Only '--' are supported for comments
 * @author Leandro Oliveira (leandro@agapsys.com)
 */
public class SqlRunner {
	// CLASS SCOPE =============================================================
	private static synchronized void run(Connection connection, InputStream sqlFileInputStream, Charset charset) throws SQLException, IOException {
		// Check/adjust paramenters...
		if (connection == null)
			throw new IllegalArgumentException("Null connection");

		if (connection.isClosed())
			throw new SQLException("Connection is closed");
		
		if (charset == null)
			throw new IllegalArgumentException("Missing charset");
		
		boolean autoCommitWasEnabled = connection.getAutoCommit();
		
		if (autoCommitWasEnabled)
			connection.setAutoCommit(false);
		
		// Let's go...
		try (
			Statement stmt = connection.createStatement(); 
			BufferedReader reader = new BufferedReader(new InputStreamReader(sqlFileInputStream, charset));
		) {
			StringBuilder sb = new StringBuilder();
			String line;
			
			while ((line = reader.readLine()) != null) {
				line = line.trim();

				if (line.startsWith("--") || line.length() == 0) {
					// ignore
				} else {
					if (line.endsWith(";")) {
						line = line.substring(0, line.length()- 1).trim();
						sb.append(line); 
						sb.append(" ");
						stmt.execute(sb.toString().trim());
						sb = new StringBuilder();
					} else {
						sb.append(line);
						sb.append(" ");
					}
				}
			}

			line = sb.toString().trim();
			if (!line.isEmpty())
				stmt.execute(line);
		}
		
		connection.commit();
		if (autoCommitWasEnabled)
			connection.setAutoCommit(true);
	}
	
	/**
	 * Runs a SQL script from a string
	 * @param connection database connection
	 * @param sql SQL script
	 * @throws SQLException when there is error while processing script
	 */
	public static void run(Connection connection, String sql) throws SQLException {
		if (sql == null)
			throw new IllegalArgumentException("Null sql");
		
		
		InputStream stream = new ByteArrayInputStream(sql.getBytes());
		try {
			run(connection, stream, Charset.defaultCharset());
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
	}
	
	/**
	 * Runs a SQL script from an embedded file
	 * @param connection database connection
	 * @param pkg package which contains the file
	 * @param sqlFilename name of the embedded file
	 * @param charset file charset
	 * @throws SQLException when there is error while processing script
	 * @throws IOException when there is an I/O error
	 * @throws ResourceNotFoundException if given file was not found
	 */
	public static void run(Connection connection, String pkg, String sqlFilename, Charset charset) throws SQLException, IOException, ResourceNotFoundException {
		try (InputStream in = ResourceLoader.getInputStream(pkg, sqlFilename)) {
			run(connection, in, charset);
		}
	}
	
	/**
	 * Runs a SQL script from a disk file
	 * @param connection database connection
	 * @param sqlFile file to read
	 * @param charset file charset
	 * @throws SQLException when there is error while processing script
	 * @throws IOException when there is an I/O error
	 * @throws FileNotFoundException if given file was not found
	 */
	public static void run(Connection connection, File sqlFile, Charset charset) throws FileNotFoundException, SQLException, IOException {
		if (!sqlFile.exists())
			throw new FileNotFoundException(String.format("File not found: %s", sqlFile.getAbsolutePath()));
		
		try (FileInputStream fis = new FileInputStream(sqlFile)) {
			run(connection, fis, charset);
		}	
	}
	// =========================================================================
	
	// PRIVATE SCOPE ===========================================================
	private SqlRunner() {}
	// =========================================================================
}
