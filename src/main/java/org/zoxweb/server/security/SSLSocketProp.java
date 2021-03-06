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

package org.zoxweb.server.security;

import java.net.URLConnection;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSocketFactory;

public interface SSLSocketProp 
{
	
	/**
	 * Return the SSLFactory
	 * 
	 * @return
	 */
	public SSLSocketFactory getSSLFactory();
	
	
	/**
	 * Return the HostnameVerifier
	 * 
	 * @return
	 */
	public HostnameVerifier getHostnameVerifier();
	
	
	/**
	 * Update the URLConnection if it is a secure connection with getSSLFactory
	 *  
	 * @param con
	 */
	public void updateURLConnection(URLConnection con);
}
