/*
 * Copyright (c) 2012-2014 ZoxWeb.com LLC.
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
package org.zoxweb.server.http;


import java.util.List;
import org.zoxweb.server.io.FileInfoStreamSource;
import org.zoxweb.shared.http.HTTPAuthentication;
import org.zoxweb.shared.http.HTTPAuthorizationType;
import org.zoxweb.shared.http.HTTPHeaderName;
import org.zoxweb.shared.util.GetNameValue;
import org.zoxweb.shared.util.SharedUtil;

/**
 * [Please state the purpose for this class or method because it will help the team for future maintenance ...].
 * 
 */
public class HTTPRequestAttributes 
{
	private final List<GetNameValue<String>> headers;
	private final List<GetNameValue<String>> parameters;
	
	/**
	 * The caller must close the streams
	 */
	private final List<FileInfoStreamSource> streamList;
	private final boolean isMultiPart;
	private final String content;
	private final String contentType;
	private final HTTPAuthentication httpAuthentication;
	private final String pathInfo;
	
	@SuppressWarnings("unchecked")
	public HTTPRequestAttributes(String pathInfo, 
								 String contentType,
								 boolean isMultiPart,
								 List<GetNameValue<String>> headers, 
								 List<GetNameValue<String>> parameters, 
								 List<FileInfoStreamSource> streamList,
								 String content)
	{
		this.pathInfo = pathInfo;
		this.isMultiPart = isMultiPart;
		this.headers = headers;
		this.parameters = parameters;
		this.streamList = streamList;
		this.content = content;
		this.contentType = contentType;
		if (headers != null)
			httpAuthentication = HTTPAuthorizationType.parse((GetNameValue<String>) SharedUtil.lookup(headers, HTTPHeaderName.AUTHORIZATION));
		else
			httpAuthentication = null;
	}
	
	
	/**
	 * Get the incoming request headers
	 * 
	 * @return
	 */
	public final List<GetNameValue<String>> getHeaders()
	{
		return headers;
	}
	
	/**
	 * Get the incoming request parameters
	 * 
	 * @return
	 */
	public final List<GetNameValue<String>> getParameters()
	{
		return parameters;
	}
	
	/**
	 * 
	 * @return true is the  request is a multipart request
	 */
	public final boolean isMultiPart()
	{
		return isMultiPart;
	}
	
	/**
	 * 
	 * @return the HTTPAuthentication 
	 */
	public final HTTPAuthentication getHTTPAuthentication()
	{
		return httpAuthentication;
	}
	
	/**
	 * Return the list of file information and stream information if the request has embedded multipart files
	 * 
	 * @return
	 */
	public final List<FileInfoStreamSource> getFileInfoStreamSources()
	{
		return streamList;
	}

	public String getContent()
	{
		return content;
	}
	
	/**
	 * 
	 * @return the content type of the request
	 */
	public String getContentType()
	{
		return contentType;
	}
	
	/**
	 * Returns any extra path information associated with the URL the client sent when it made this request. The extra path information follows the servlet path but precedes the query string and will start with a "/" character.
	 * This method returns null if there was no extra path information. 
	 * (Optional)
	 * @return
	 */
	public String getPathInfo()
	{
		return pathInfo;
	}
	
}