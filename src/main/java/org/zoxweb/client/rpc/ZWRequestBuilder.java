/*
 * Copyright (c) 2012-2015 ZoxWeb.com LLC.
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
package org.zoxweb.client.rpc;

import org.zoxweb.shared.http.HTTPMethod;

import com.google.gwt.http.client.RequestBuilder;

/**
 * [Please state the purpose for this class or method because it will help the team for future maintenance ...].
 * 
 */
public class ZWRequestBuilder
	extends RequestBuilder
{
	/**
	 * @param httpMethod
	 * @param url
	 */
	public ZWRequestBuilder(HTTPMethod method, String url)
	{
		this(method.getName(), url);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param httpMethod
	 * @param url
	 */
	public ZWRequestBuilder(String httpMethod, String url)
	{
		super(httpMethod, url);
		// TODO Auto-generated constructor stub
	}

}
