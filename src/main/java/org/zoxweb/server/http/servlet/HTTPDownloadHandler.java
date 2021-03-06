/*
 * Copyright 2012 ZoxWeb.com LLC.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.zoxweb.server.http.servlet;

import java.io.IOException;





import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.zoxweb.server.http.HTTPRequestAttributes;
import org.zoxweb.server.io.FileInfoStreamSource;
import org.zoxweb.shared.data.FileInfoDAO;
import org.zoxweb.shared.security.AccessException;
import org.zoxweb.shared.security.AuthenticationHandler;


public interface HTTPDownloadHandler
extends AuthenticationHandler<HttpServletRequest, HttpServletResponse>
{
	
	/**
	 * 
	 * [Please state the purpose for this class or method because it will help the team for future maintenance ...].
	 * 
	 * @param request
	 * @param attributes
	 * @return
	 * @throws AccessException
	 * @throws IOException
	 */
	FileInfoStreamSource lookupFileInfoSource(HttpServletRequest request, HTTPRequestAttributes attributes)
					  throws AccessException, IOException;
	
	
	
	
	/**
	 * 
	 * @param session
	 * @param fid
	 */
	 void updatedFileInfoDAO( FileInfoDAO fileInfo) 
			 throws AccessException, IOException;
	
}
