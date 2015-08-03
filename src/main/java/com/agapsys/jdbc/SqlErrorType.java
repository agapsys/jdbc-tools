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

import java.sql.SQLException;

/**
 * SQL error types according to 
 * <a href="http://www.contrib.andrew.cmu.edu/~shadow/sql/sql1992.txt">SQL92 standard</a>
 * @author Leandro Oliveira (leandro@agapsys.com)
 */
public enum SqlErrorType {	
	NO_DATA                        (0x02),
	CONNECTION_EXCEPTION           (0x08),
	DYNAMIC_SQL_ERROR              (0x07),
	FEATURE_NOT_SUPPORTED          (0x0A),
	CARDINALITY_VIOLATION          (0x21),
	DATA_EXCEPTION                 (0x22),
	INTEGRITY_CONSTRAINT_VIOLATION (0x23),
	INVALID_CURSOR_STATE           (0x24),
	INVALID_TRANSACTION_STATE      (0x25),
	INVALID_SQL_STATEMENT_NAME     (0x26),
	INVALID_AUTHORIZATION_SPEC     (0x28),
	DEPENDENT_PRIVILEGE_DESC_EXIST (0x2B),
	INVALID_CHARSET                (0x2C),
	INVALID_TRANSACTION_TERMINATION(0x2D),
	INVALID_CONNECTION_NAME        (0x2E),
	INVALID_SQL_DESCRIPTOR_NAME    (0x33),
	INVALID_CURSOR_NAME            (0x34),
	INVALID_CONDITION_NUMBER       (0x35),
	INVALID_CATALOG_NAME           (0x3D),
	AMBIGUOS_CURSOR_NAME           (0x3C),
	INVALID_SCHEMA_NAME            (0x3F);
	
	int val;
	private SqlErrorType(int val) {
		this.val = val;
	}
	
	/** Get error type from a SQLException. */
	public static SqlErrorType getInstance(SQLException ex) {
		if (ex == null)
			return null;
		
		int val = Integer.parseInt(ex.getSQLState().substring(0, 2), 16);
		
		switch (val) {
		case 0x02:
			return NO_DATA;
			
		case 0x08:
			return CONNECTION_EXCEPTION;
			
		case 0x07:
			return DYNAMIC_SQL_ERROR;
			
		case 0x0A:
			return FEATURE_NOT_SUPPORTED;
			
		case 0x21:
			return CARDINALITY_VIOLATION;
			
		case 0x22:
			return DATA_EXCEPTION;
			
		case 0x23:
			return INTEGRITY_CONSTRAINT_VIOLATION;
			
		case 0x24:
			return INVALID_CURSOR_STATE;
		
		case 0x25:
			return INVALID_TRANSACTION_STATE;
			
		case 0x26:
			return INVALID_SQL_STATEMENT_NAME;
			
		case 0x28:
			return INVALID_AUTHORIZATION_SPEC;
		
		case 0x2B:
			return DEPENDENT_PRIVILEGE_DESC_EXIST;
			
		case 0x2C:
			return INVALID_CHARSET;
			
		case 0x2D:
			return INVALID_TRANSACTION_TERMINATION;
			
		case 0x2E:
			return INVALID_CONNECTION_NAME;
			
		case 0x33:
			return INVALID_SQL_DESCRIPTOR_NAME;
			
		case 0x34:
			return INVALID_CURSOR_NAME;
			
		case 0x35:
			return INVALID_CONDITION_NUMBER;
			
		case 0x3D:
			return INVALID_CATALOG_NAME;
			
		case 0x3C:
			return AMBIGUOS_CURSOR_NAME;
		
		case 0x3F:
			return INVALID_SCHEMA_NAME;

		default:
			return null;
		}
	}
}