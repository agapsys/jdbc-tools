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

package com.agapsys.jdbc.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Load resources embedded in application JAR/WAR
 * @author Leandro Oliveira (leandro@agapsys.com)
 */
public class ResourceLoader {
	// CLASS SCOPE =============================================================
	public static class ResourceNotFoundException extends RuntimeException {
		private ResourceNotFoundException(String message) {
			super(message);
		}
	}
	
	/**
	 * Returns the URL of a resource.
	 * @param pkg package where resource is located
	 * @param  filename resource file name
	 * @throws ResourceNotFoundException if given resource was not found. 
	 * @throws IllegalArgumentExceptionif filename == null || filename.isEmpty()
	 */
	private static URL getResourceURL(String pkg, String filename) throws IllegalArgumentException, ResourceNotFoundException {
		if (pkg == null)
			pkg = "";
		
		String pkgPath = pkg.replace(".", "/");
		
		if (filename == null || filename.isEmpty())
			throw new IllegalArgumentException("Null/Empty filename");
		
		String urlStr = "/" + (pkgPath.isEmpty() ? "" : (pkgPath + "/")) + filename;
		URL url = ResourceLoader.class.getResource(urlStr);
		if (url == null)
			throw new ResourceNotFoundException(String.format("Resource '%s' not found in package '%s'", filename, pkg));
		else
			return url;
	}
		
	/** 
	 * Returns an InputStream associated with an embedded resource.
	 * @param pkg package where resource is located (null or empty for default package)
	 * @param  filename resource file name
	 * @throws ResourceNotFoundException if given resource was not found. 
	 * @throws IOException if there was an I/O error
	 * @throws IllegalArgumentException if filename == null || filename.isEmpty()
	 */
	public static InputStream getInputStream(String pkg, String filename) throws IllegalArgumentException, ResourceNotFoundException, IOException {
		URL url = getResourceURL(pkg, filename);
		return url.openStream();
	}
	// =========================================================================
	
	// INSTANCE SCOPE ==========================================================
	private ResourceLoader() {} // Prevents instantiation
	// =========================================================================

}
